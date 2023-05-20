package dao;

import domainModel.Course;
import domainModel.Customer;

import java.util.List;

public interface CourseDAO extends DAO<Course, Integer> {
    public int getNextID();

    public List<Customer> getCustomersOfCourse(Integer courseId); // getAttendees

    public List<Course> getCoursesOfCustomer(String fiscalCode);

    public void addBooking(String fiscalCode, Integer courseId);

    public boolean deleteBooking(String fiscalCode, Integer courseId);
}
