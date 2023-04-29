package dao;

import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOsqlite implements CustomerDAO {

    private final MembershipDAOsqlite membershipDAO = new MembershipDAOsqlite();

    @Override
    public Customer get(String fiscalCode) {
        try {
            Connection con = Database.getConnection();
            Customer customer = null;
            PreparedStatement ps = con.prepareStatement("SELECT * FROM customers WHERE fiscal_code = ?");
            ps.setString(1, fiscalCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                customer = new Customer(
                        rs.getString("fiscal_code"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        membershipDAO.getOfCustomer(rs.getString("fiscal_code"))
                );
            }

            rs.close();
            ps.close();
            Database.closeConnection(con);
            return customer;
        } catch (SQLException e) {
            System.out.println("Unable to get customer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Customer> getAll() {
        try {
            Connection connection = Database.getConnection();
            List<Customer> customers = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("fiscal_code"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        membershipDAO.getOfCustomer(rs.getString("fiscal_code"))
                ));
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("Unable to get all customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void insert(Customer customer) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers (fiscal_code, name, surname) VALUES (?, ?, ?)");
            ps.setString(1, customer.getFiscalCode());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getSurname());
            ps.executeUpdate();

            membershipDAO.insertOfCustomer(customer.getFiscalCode(), customer.getMembership());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to insert customer: " + e.getMessage());
        }
    }

    @Override
    public void update(Customer customer) {
        try {
            Connection connection = Database.getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name = ?, surname = ? WHERE fiscal_code = ?");
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getSurname());
            ps.setString(3, customer.getFiscalCode());
            ps.executeUpdate();

            membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to update customer: " + e.getMessage());
        }
    }

    @Override
    public void delete(Customer customer) {
        try {
            Connection connection = Database.getConnection();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE fiscal_code = ?");
            ps.setString(1, customer.getFiscalCode());
            ps.executeUpdate();

            // Not needed because of the ON DELETE CASCADE constraint
            // membershipDAO.deleteOfCustomer(customer.getFiscalCode());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to delete customer: " + e.getMessage());
        }
    }
}
