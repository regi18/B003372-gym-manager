package dao;

import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOsqlite implements CustomerDAO {

    private MembershipDAOsqlite membershipDAO = new MembershipDAOsqlite;

    @Override
    public Customer get(String fiscalCode) throws SQLException {
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
                    membershipDAO.getOfCustomer(rs.getInt("id"))
            );
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return customer;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
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
    public int insert(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO customers (fiscal_code, name, surname) VALUES (?, ?, ?)");
        ps.setString(1, customer.getFiscalCode());
        ps.setString(2, customer.getName());
        ps.setString(3, customer.getSurname());
        int rows = ps.executeUpdate();

        membershipDAO.insert(customer.getMembership());

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int update(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();

        PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name = ?, surname = ? WHERE fiscal_code = ?");
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getSurname());
        ps.setString(3, customer.getFiscalCode());
        int rows = ps.executeUpdate();

        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int delete(Customer customer) throws SQLException {
        Connection connection = Database.getConnection();

        PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE fiscal_code = ?");
        ps.setString(1, customer.getFiscalCode());
        int rows = ps.executeUpdate();

        membershipDAO.deleteOfCustomer(customer.getFiscalCode());

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }
}
