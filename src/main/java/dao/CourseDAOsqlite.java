package dao;

import models.Course;
import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOsqlite implements CourseDAO {

    private final TrainerDAO trainerDAO;
    private final CustomerDAO customerDAO;

    public CourseDAOsqlite(TrainerDAO trainerDAO, CustomerDAO customerDAO) {
        this.trainerDAO = trainerDAO;
        this.customerDAO = customerDAO;
    }

    @Override
    public Course get(Integer id) {
        try {
            Connection connection = Database.getConnection();
            Course course = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM courses WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                course = new Course(
                        rs.getString("name"),
                        rs.getInt("max_capacity"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        trainerDAO.get(rs.getString("trainer"))
                );
            }

            rs.close();
            ps.close();

            // Fetch bookings for course and add them to course
            if (course != null) addBookingsToCourse(course);

            Database.closeConnection(connection);
            return course;
        } catch (SQLException e) {
            System.out.println("Unable to get course: " + e.getMessage());
            return null;
        }
    }

    private void addBookingsToCourse(Course course) throws SQLException {
        List<Customer> bookedCustomers = getCustomersOfCourse(course.getId());
        for (Customer customer : bookedCustomers) course.addAttendee(customer);
    }

    @Override
    public List<Course> getAll() {
        try {
            Connection connection = Database.getConnection();
            List<Course> courses = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");

            while (rs.next()) {
                Course c = new Course(
                        rs.getString("name"),
                        rs.getInt("max_capacity"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        trainerDAO.get(rs.getString("trainer"))
                );

                addBookingsToCourse(c);    // Fetch bookings for course and add them to course
                courses.add(c);
            }

            rs.close();
            stmt.close();
            Database.closeConnection(connection);
            return courses;
        } catch (SQLException e) {
            System.out.println("Unable to get courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void insert(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO courses (name, max_capacity, start_date, end_date, trainer) VALUES (?, ?, ?, ?, ?)");
            // id is auto-incremented, so it's not needed
            ps.setString(1, course.getName());
            ps.setInt(2, course.getMaxCapacity());
            ps.setTimestamp(3, Timestamp.valueOf(course.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(course.getEndDate()));
            ps.setString(5, course.getTrainer().getFiscalCode());
            ps.executeUpdate();

            // Add bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                addBooking(customer.getFiscalCode(), course.getId());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to insert course: " + e.getMessage());
        }
    }

    @Override
    public void update(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE courses SET name = ?, max_capacity = ?, start_date = ?, end_date = ?, trainer = ? WHERE id = ?");
            ps.setString(1, course.getName());
            ps.setInt(2, course.getMaxCapacity());
            ps.setTimestamp(3, Timestamp.valueOf(course.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(course.getEndDate()));
            ps.setString(5, course.getTrainer().getFiscalCode());
            ps.setInt(6, course.getId());
            ps.executeUpdate();

            // Update bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                addBooking(customer.getFiscalCode(), course.getId());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to update course: " + e.getMessage());
        }
    }

    @Override
    public void delete(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
            ps.setInt(1, course.getId());
            ps.executeUpdate();

            // Delete bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                deleteBooking(customer.getFiscalCode(), course.getId());

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to delete course: " + e.getMessage());
        }
    }

    @Override
    public int getNextID() {
        try {
            Connection connection = Database.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM courses");
            int id = rs.getInt(1) + 1;

            rs.close();
            stmt.close();
            Database.closeConnection(connection);
            return id;
        } catch (SQLException e) {
            System.out.println("Unable to get next course id: " + e.getMessage());
            return 0;
        }
    }

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
            return new ArrayList<>();
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
            while (rs.next()) courses.add(this.get(rs.getInt("course")));

            rs.close();
            ps.close();
            Database.closeConnection(connection);
            return courses;
        } catch (SQLException e) {
            System.out.println("Unable to get courses of customer: " + e.getMessage());
            return new ArrayList<>();
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
