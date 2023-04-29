package dao;

import models.Course;
import models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingsDAOsqlite implements BookingsDAO {

    private final CustomerDAOsqlite customerDAO = new CustomerDAOsqlite();
    private final CourseDAOsqlite courseDAO = new CourseDAOsqlite();

    @Override
    public List<Customer> getCustomersOfCourse(Integer courseId) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE course = ?");
        ps.setInt(1, courseId);
        ResultSet rs = ps.executeQuery();

        List<Customer> customers = new ArrayList<>();
        while (rs.next()) customers.add(customerDAO.get(rs.getString("fiscal_code")));

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return customers;
    }

    @Override
    public List<Course> getCoursesOfCustomer(String fiscalCode) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE customer = ?");
        ps.setString(1, fiscalCode);
        ResultSet rs = ps.executeQuery();

        List<Course> courses = new ArrayList<>();
        while (rs.next()) courses.add(courseDAO.get(rs.getInt("course")));

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return courses;
    }

    /**
     * Adds a booking to the database.
     * If the customer is already booked for the course, the booking is not added.
     *
     * @param fiscalCode Fiscal code of the customer
     * @param courseId   id of the course
     */
    @Override
    public void addBooking(String fiscalCode, Integer courseId) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO bookings (customer, course) VALUES (?, ?)");
        ps.setString(1, fiscalCode);
        ps.setInt(2, courseId);
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
    }

    @Override
    public void deleteBooking(String fiscalCode, Integer courseId) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM bookings WHERE customer = ? AND course = ?");
        ps.setString(1, fiscalCode);
        ps.setInt(2, courseId);
        int result = ps.executeUpdate();

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
    }
}
