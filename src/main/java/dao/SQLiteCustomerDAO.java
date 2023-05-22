package dao;

import domainModel.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCustomerDAO implements CustomerDAO {

    private final MembershipDAO membershipDAO;

    public SQLiteCustomerDAO(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    @Override
    public Customer get(String fiscalCode) throws Exception {
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
    }

    @Override
    public List<Customer> getAll() throws Exception {
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
    }

    @Override
    public void insert(Customer customer) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO customers (fiscal_code, name, surname) VALUES (?, ?, ?)");
        ps.setString(1, customer.getFiscalCode());
        ps.setString(2, customer.getName());
        ps.setString(3, customer.getSurname());
        ps.executeUpdate();

        membershipDAO.insertOfCustomer(customer.getFiscalCode(), customer.getMembership());

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public void update(Customer customer) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name = ?, surname = ? WHERE fiscal_code = ?");
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getSurname());
        ps.setString(3, customer.getFiscalCode());
        ps.executeUpdate();

        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());
        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public boolean delete(String fiscalCode) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        int rows = ps.executeUpdate();

        // Not needed because of the ON DELETE CASCADE constraint
        // membershipDAO.deleteOfCustomer(customer.getFiscalCode());

        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }
}
