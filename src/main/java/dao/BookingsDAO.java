package dao;

import models.Course;
import models.Customer;

import java.sql.SQLException;
import java.util.List;

public interface BookingsDAO {

    public List<Customer> getCustomersOfCourse(Integer courseId);

    public List<Course> getCoursesOfCustomer(String fiscalCode);

    public void addBooking(String fiscalCode, Integer courseId);

    public void deleteBooking(String fiscalCode, Integer courseId);
}
