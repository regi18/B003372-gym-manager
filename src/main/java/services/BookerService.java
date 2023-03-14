package services;

import managers.CoursesManager;
import models.Course;
import models.Customer;


public class BookerService {
    private final CoursesManager coursesManager;

    public BookerService(CoursesManager coursesManager) {
        this.coursesManager = coursesManager;
    }

    /**
     * Books a course for the given customer
     * @param customer The customer for whom to book the course for
     * @param courseId The course id to book
     * @throws RuntimeException when courseId doesn't exist and bubbles up exceptions of Customer::addAttendee()
     */
    public void bookCourse(Customer customer, int courseId) throws RuntimeException {
        Course c = coursesManager.getCourse(courseId);
        if (c == null) throw new RuntimeException("The given course id does not exist");
        c.addAttendee(customer);
        customer.addBookedCourse(c);
    }

    /**
     * Deletes a booking for the given customer
     * @param customer The customer for whom to remove the booking
     * @param courseId The course id to remove
     * @return true if successful, false otherwise (i.e. customer not found in course or courseId not exiting)
     */
    public boolean deleteCourseBooking(Customer customer, int courseId) {
        Course c = coursesManager.getCourse(courseId);
        if (c == null) return false;
        boolean res = c.removeAttendee(customer.getFiscalCode());
        if (res) return customer.removeBookedCourse(c);
        else return false;
    }
}
