package dao;

import models.Course;
import models.Customer;

import java.util.List;

public interface CourseDAO extends DAO<Course, Integer> {
    public int getNextID();

    public List<Customer> getCustomersOfCourse(Integer courseId);

    public List<Course> getCoursesOfCustomer(String fiscalCode);

    public void addBooking(String fiscalCode, Integer courseId);

    public void deleteBooking(String fiscalCode, Integer courseId);
}
