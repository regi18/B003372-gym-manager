package controllers;

import dao.BookingsDAO;
import dao.BookingsDAOsqlite;
import models.Course;
import models.Customer;

import java.util.List;


public class BookingsController {
    private final CoursesController coursesController;
    private final BookingsDAO bookingsDAO = new BookingsDAOsqlite();

    public BookingsController(CoursesController coursesController) {
        this.coursesController = coursesController;
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
        bookingsDAO.addBooking(customer.getFiscalCode(), courseId);
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
        bookingsDAO.deleteBooking(customer.getFiscalCode(), courseId);
        return c.removeAttendee(customer.getFiscalCode());
    }

    /**
     * Returns a read-only list of the courses that the given user booked
     *
     * @param fiscalCode The fiscal code of the customer
     */
    public List<Course> getBookingsForCustomer(String fiscalCode) {
        return bookingsDAO.getCoursesOfCustomer(fiscalCode);
    }
}
