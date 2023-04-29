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
    public List<Customer> getCustomersOfCourse(Integer courseId) {
        try {

            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE course = ?");
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) customers.add(customerDAO.get(rs.getString("fiscal_code")));

            rs.close();
            ps.close();
            Database.closeConnection(connection);
            return customers;
        } catch (SQLException e) {
            System.out.println("Unable to get customers of course: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Course> getCoursesOfCustomer(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE customer = ?");
            ps.setString(1, fiscalCode);
            ResultSet rs = ps.executeQuery();

            List<Course> courses = new ArrayList<>();
            while (rs.next()) courses.add(courseDAO.get(rs.getInt("course")));

            rs.close();
            ps.close();
            Database.closeConnection(connection);
            return courses;
        } catch (SQLException e) {
            System.out.println("Unable to get courses of customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Adds a booking to the database.
     * If the customer is already booked for the course, the booking is not added.
     *
     * @param fiscalCode Fiscal code of the customer
     * @param courseId   id of the course
     */
    @Override
    public void addBooking(String fiscalCode, Integer courseId) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO bookings (customer, course) VALUES (?, ?)");
            ps.setString(1, fiscalCode);
            ps.setInt(2, courseId);
            ps.executeUpdate();

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to add booking: " + e.getMessage());
        }
    }

    @Override
    public void deleteBooking(String fiscalCode, Integer courseId) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM bookings WHERE customer = ? AND course = ?");
            ps.setString(1, fiscalCode);
            ps.setInt(2, courseId);
            ps.executeUpdate();

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to delete booking: " + e.getMessage());
        }
    }
}
