package dao;

import models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOsqlite implements CourseDAO {

    @Override
    public Course get(Integer id) throws SQLException {
        Connection con = Database.getConnection();
        Course course = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM courses WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            course = new Course(    // TODO: Add id from db, and remove static nextId from Course
                    rs.getString("name"),
                    // TODO
                    trainerDAO.get(rs.getInt("id"))
            );
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);

        // TODO: Fetch bookings for course and add them to course
        // Use BookingsDAO?

        Database.closeConnection(con);
        return course;
    }

    @Override
    public List<Course> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        List<Course> courses = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM courses");

        while (rs.next()) {
            courses.add(new Course(
                    // TODO
            );

            // TODO: Fetch bookings for course and add them to course
        }

        return courses;
    }

    @Override
    public int insert(Course course) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO courses (id, name, max_capacity, start_date, end_date, trainer_fiscal_code) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setInt(1, course.getId());
        ps.setString(2, course.getName());
        ps.setInt(3, course.getMaxCapacity());
        ps.setDate(4, Date.valueOf(course.getStartDate().toLocalDate()));
        ps.setDate(5, Date.valueOf(course.getEndDate().toLocalDate()));
        ps.setString(6, course.getTrainer().getFiscalCode());
        int rows = ps.executeUpdate();

        // TODO: Add bookings for course in the bookings table

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int update(Course course) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE courses SET name = ?, max_capacity = ?, start_date = ?, end_date = ?, trainer_fiscal_code = ? WHERE id = ?");
        ps.setString(1, course.getName());
        ps.setInt(2, course.getMaxCapacity());
        ps.setDate(3, Date.valueOf(course.getStartDate().toLocalDate()));
        ps.setDate(4, Date.valueOf(course.getEndDate().toLocalDate()));
        ps.setString(5, course.getTrainer().getFiscalCode());
        ps.setInt(6, course.getId());
        int rows = ps.executeUpdate();

        // TODO: Update bookings for course in the bookings table
        // Use BookingsDAO?

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int delete(Course course) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
        ps.setInt(1, course.getId());
        int rows = ps.executeUpdate();

        // TODO: Delete bookings for course in the bookings table
        // Use BookingsDAO?

        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }
}
