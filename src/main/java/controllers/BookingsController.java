package controllers;

import dao.CourseDAO;
import models.Course;
import models.Customer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BookingsController {
    private final CoursesController coursesController;
    private final CourseDAO coursesDAO;

    public BookingsController(CoursesController coursesController, CourseDAO bookingsDAO) {
        this.coursesController = coursesController;
        this.coursesDAO = bookingsDAO;
    }

    /**
     * Books a course for the given customer
     *
     * @param customer The customer for whom to book the course for
     * @param courseId The course id to book
     *
     * @throws RuntimeException when courseId doesn't exist and bubbles up exceptions of Course::addAttendee()
     */
    public void bookCourse(Customer customer, int courseId) throws RuntimeException {
        Course c = coursesController.getCourse(courseId);
        if (c == null) throw new RuntimeException("The given course id does not exist");
        c.addAttendee(customer);
        coursesDAO.addBooking(customer.getFiscalCode(), courseId);
    }

    /**
     * Deletes a booking for the given customer
     *
     * @param customer The customer for whom to remove the booking
     * @param courseId The course id to remove
     *
     * @return true if successful, false otherwise (i.e. customer not found in course or courseId not exiting)
     */
    public boolean deleteCourseBooking(Customer customer, int courseId) {
        Course c = coursesController.getCourse(courseId);
        if (c == null) return false;
        coursesDAO.deleteBooking(customer.getFiscalCode(), courseId);
        return c.removeAttendee(customer.getFiscalCode());
    }

    /**
     * Returns a read-only list of the courses that the given user booked
     *
     * @param fiscalCode The fiscal code of the customer
     */
    public List<Course> getBookingsForCustomer(String fiscalCode) {
        Stream<Course> s = this.coursesController.getAll().stream().filter(c -> c.getAttendees().stream().anyMatch(customer -> customer.getFiscalCode().equals(fiscalCode)));
        return s.collect(Collectors.toUnmodifiableList());
    }

}
