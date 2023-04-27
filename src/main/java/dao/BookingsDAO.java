package dao;

import models.Course;
import models.Customer;

import java.sql.SQLException;
import java.util.List;

public interface BookingsDAO {

    public List<Customer> getCustomersOfCourse(Integer courseId) throws SQLException;

    public List<Course> getCoursesOfCustomer(String fiscalCode) throws SQLException;

    public boolean addBooking(String fiscalCode, Integer courseId) throws SQLException;

    public boolean deleteBooking(String fiscalCode, Integer courseId) throws SQLException;
}
