package dao;

import domainModel.Course;
import domainModel.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCourseDAO implements CourseDAO {

    private final TrainerDAO trainerDAO;
    private final CustomerDAO customerDAO;

    public SQLiteCourseDAO(TrainerDAO trainerDAO, CustomerDAO customerDAO) {
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
                        id,
                        rs.getString("name"),
                        rs.getInt("max_capacity"),
                        LocalDateTime.parse(rs.getString("start_date")),
                        LocalDateTime.parse(rs.getString("end_date")),
                        trainerDAO.get(rs.getString("trainer"))
                );
            }

            rs.close();
            ps.close();

            Database.closeConnection(connection);
            return course;
        } catch (SQLException e) {
            System.out.println("Unable to get course: " + e.getMessage());
            return null;
        }
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
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("max_capacity"),
                        LocalDateTime.parse(rs.getString("start_date")),
                        LocalDateTime.parse(rs.getString("end_date")),
                        trainerDAO.get(rs.getString("trainer"))
                );

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
            ps.setString(3, course.getStartDate().toString());
            ps.setString(4, course.getEndDate().toString());
            ps.setString(5, course.getTrainer().getFiscalCode());
            ps.executeUpdate();

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
            ps.setString(3, course.getStartDate().toString());
            ps.setString(4, course.getEndDate().toString());
            ps.setString(5, course.getTrainer().getFiscalCode());
            ps.setInt(6, course.getId());
            ps.executeUpdate();

            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to update course: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        try {
            Course course = get(id);
            if (course == null) return false;

            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            ps.close();
            Database.closeConnection(connection);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Unable to delete course: " + e.getMessage());
        }
        return false;
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
    public List<Customer> getAttendees(Integer courseId) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bookings WHERE course = ?");
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) customers.add(customerDAO.get(rs.getString("customer")));

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
    public List<Course> getCoursesForCustomer(String fiscalCode) {
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
    public boolean deleteBooking(String fiscalCode, Integer courseId) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM bookings WHERE customer = ? AND course = ?");
            ps.setString(1, fiscalCode);
            ps.setInt(2, courseId);
            int rows = ps.executeUpdate();

            ps.close();
            Database.closeConnection(connection);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Unable to delete booking: " + e.getMessage());
        }
        return false;
    }
}
