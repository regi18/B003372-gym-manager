package dao;

import domainModel.membership.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

import static java.util.Map.entry;

public class SQLiteMembershipDAO implements MembershipDAO {

    private final Map<String, Class<? extends Membership>> membershipStringToType = Map.ofEntries(
            entry("weekdays", WeekdaysMembershipDecorator.class),
            entry("weekends", WeekendMembershipDecorator.class)
    );

    private final Map<Class<? extends Membership>, String> membershipTypeToString = Map.ofEntries(
            entry(WeekdaysMembershipDecorator.class, "weekdays"),
            entry(WeekendMembershipDecorator.class, "weekends")
    );


    @Override
    public Membership getOfCustomer(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            Membership membership = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM memberships WHERE customer = ?");
            ps.setString(1, fiscalCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                membership = new EmptyMembership(LocalDate.parse(rs.getString("valid_from")), LocalDate.parse(rs.getString("valid_until")));

                // Add extensions to membership
                PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM membership_extensions WHERE customer = ?");
                ps2.setString(1, fiscalCode);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    try {
                        // Decorate the membership with the decorator of the correct type, based on the "type" column and the membershipTypes map
                        // The constructor of the decorator takes the membership to decorate and the number of uses
                        membership = membershipStringToType.get(rs2.getString("type")).getConstructor(Membership.class, int.class).newInstance(membership, rs2.getInt("uses"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                rs2.close();
                ps2.close();
            }

            rs.close();
            ps.close();
            Database.closeConnection(connection);
            return membership;
        } catch (SQLException e) {
            System.out.println("Unable to get membership of customer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void insertOfCustomer(String fiscalCode, Membership membership) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement insertMembership = connection.prepareStatement("INSERT INTO memberships (customer, valid_from, valid_until) VALUES (?, ?, ?)");
            insertMembership.setString(1, fiscalCode);
            insertMembership.setString(2, membership.getValidFrom().toString());
            insertMembership.setString(3, membership.getValidUntil().toString());
            insertMembership.executeUpdate();
            insertMembership.close();

            // Insert extensions to database
            insertExtensionsOfCustomer(fiscalCode, membership);

            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to insert membership of customer: " + e.getMessage());
        }
    }

    @Override
    public void updateOfCustomer(String fiscalCode, Membership membership) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement updateMembership = connection.prepareStatement("UPDATE memberships SET valid_from = ?, valid_until = ? WHERE customer = ?");
            updateMembership.setString(1, membership.getValidFrom().toString());
            updateMembership.setString(2, membership.getValidUntil().toString());
            updateMembership.setString(3, fiscalCode);
            updateMembership.executeUpdate();
            updateMembership.close();

            // Update extensions (delete all and reinsert)
            deleteExtensionsOfCustomer(fiscalCode);
            insertExtensionsOfCustomer(fiscalCode, membership);

            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to update membership of customer: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteOfCustomer(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement deleteMembership = connection.prepareStatement("DELETE FROM memberships WHERE customer = ?");
            deleteMembership.setString(1, fiscalCode);
            int rows = deleteMembership.executeUpdate();
            deleteMembership.close();

            // No need to delete extensions, they will be deleted automatically by the database (ON DELETE CASCADE)
            // deleteExtensionsOfCustomer(fiscalCode);

            Database.closeConnection(connection);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Unable to delete membership of customer: " + e.getMessage());
        }
        return false;
    }

    private void insertExtensionsOfCustomer(String fiscalCode, Membership membership) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement insertExtension = connection.prepareStatement("INSERT INTO membership_extensions (customer, type, uses) VALUES (?, ?, ?)");
            while (membership instanceof MembershipDecorator) {
                // Cast to MembershipDecorator to access the getMembership() method
                MembershipDecorator membershipDecorator = (MembershipDecorator) membership;

                // Insert extension
                insertExtension.setString(1, fiscalCode);
                insertExtension.setString(2, membershipTypeToString.get(membership.getClass()));
                insertExtension.setInt(3, membershipDecorator.getUses());
                insertExtension.executeUpdate();
                membership = membershipDecorator.getMembership();
            }

            insertExtension.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to insert extensions of customer: " + e.getMessage());
        }
    }

    private void deleteExtensionsOfCustomer(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement deleteExtensions = connection.prepareStatement("DELETE FROM membership_extensions WHERE customer = ?");
            deleteExtensions.setString(1, fiscalCode);
            deleteExtensions.executeUpdate();
            deleteExtensions.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to delete extensions of customer: " + e.getMessage());
        }
    }
}
