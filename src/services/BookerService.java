package services;

import managers.CoursesManager;
import models.Course;
import models.Customer;

import java.util.ArrayList;

public class BookerService {
    ArrayList<Course> bookedCourses = new ArrayList<>();
    Customer customer;
    CoursesManager coursesManager;

    public BookerService(Customer customer) {
        this.customer = customer;
    }

    public bookCourse(int id) {
        Course c = coursesManager.getCourse(id);
        c.addAttendee(customer);
        bookedCourses.add(c);
    }

    public deleteCourseBooking(int id) {

    }
}
