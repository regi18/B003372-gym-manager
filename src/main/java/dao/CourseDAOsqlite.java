package dao;

import models.Course;
import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOsqlite implements CourseDAO {

    private final TrainerDAOsqlite trainerDAO = new TrainerDAOsqlite();
    private final BookingsDAOsqlite bookingsDAO = new BookingsDAOsqlite();

    @Override
    public Course get(Integer id) {
        try {
            Connection connection = Database.getConnection();
            Course course = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM courses WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                course = new Course(    // TODO: Add id from db, and remove static nextId from Course
                        rs.getString("name"),
                        rs.getInt("max_capacity"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        trainerDAO.get(rs.getString("trainer"))
                );
            }

            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);

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
        List<Customer> bookedCustomers = bookingsDAO.getCustomersOfCourse(course.getId());
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

            Database.closeResultSet(rs);
            Database.closeStatement(stmt);
            Database.closeConnection(connection);
            return courses;
        } catch (SQLException e) {
            System.out.println("Unable to get courses: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int insert(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO courses (name, max_capacity, start_date, end_date, trainer) VALUES (?, ?, ?, ?, ?)");
            // id is auto-incremented, so it's not needed
            ps.setString(1, course.getName());
            ps.setInt(2, course.getMaxCapacity());
            ps.setTimestamp(3, Timestamp.valueOf(course.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(course.getEndDate()));
            ps.setString(5, course.getTrainer().getFiscalCode());
            int rows = ps.executeUpdate();

            // Add bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                bookingsDAO.addBooking(customer.getFiscalCode(), course.getId());

            Database.closePreparedStatement(ps);
            Database.closeConnection(connection);
            return rows;
        } catch (SQLException e) {
            System.out.println("Unable to insert course: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE courses SET name = ?, max_capacity = ?, start_date = ?, end_date = ?, trainer = ? WHERE id = ?");
            ps.setString(1, course.getName());
            ps.setInt(2, course.getMaxCapacity());
            ps.setTimestamp(3, Timestamp.valueOf(course.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(course.getEndDate()));
            ps.setString(5, course.getTrainer().getFiscalCode());
            ps.setInt(6, course.getId());
            int rows = ps.executeUpdate();

            // Update bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                bookingsDAO.addBooking(customer.getFiscalCode(), course.getId());

            Database.closePreparedStatement(ps);
            Database.closeConnection(connection);
            return rows;
        } catch (SQLException e) {
            System.out.println("Unable to update course: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int delete(Course course) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
            ps.setInt(1, course.getId());
            int rows = ps.executeUpdate();

            // Delete bookings for course in the bookings table
            for (Customer customer : course.getAttendees())
                bookingsDAO.deleteBooking(customer.getFiscalCode(), course.getId());

            Database.closePreparedStatement(ps);
            Database.closeConnection(connection);
            return rows;
        } catch (SQLException e) {
            System.out.println("Unable to delete course: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int getNextID() {
        try {
            Connection connection = Database.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM courses");
            int id = rs.getInt(1) + 1;

            Database.closeResultSet(rs);
            Database.closeStatement(stmt);
            Database.closeConnection(connection);
            return id;
        } catch (SQLException e) {
            System.out.println("Unable to get next course id: " + e.getMessage());
            return 0;
        }
    }
}
