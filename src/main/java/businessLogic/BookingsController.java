package businessLogic;

import dao.CourseDAO;
import domainModel.Course;
import domainModel.Customer;
import java.util.List;


public class BookingsController {
    private final CoursesController coursesController;
    private final CustomersController customersController;
    private final CourseDAO coursesDAO;

    public BookingsController(CoursesController coursesController, CustomersController customersController, CourseDAO bookingsDAO) {
        this.coursesController = coursesController;
        this.customersController = customersController;
        this.coursesDAO = bookingsDAO;
    }

    /**
     * Books a course for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to book the course for
     * @param courseId The course id to book
     *
     * @throws RuntimeException when courseId doesn't exist and bubbles up exceptions of Course::addAttendee()
     */
    public void bookCourse(String customerFiscalCode, int courseId) throws RuntimeException {
        Course c = coursesController.getCourse(courseId);
        Customer customer = customersController.getPerson(customerFiscalCode);
        if (c == null) throw new RuntimeException("The given course id does not exist");
        if (customer == null) throw new RuntimeException("The given customer does not exist");

        List<Customer> attendees = coursesDAO.getAttendees(courseId);

        if (attendees.size() == c.getMaxCapacity())
            throw new RuntimeException("This course if full, can't book");
        if (attendees.contains(customer))
            throw new RuntimeException("The given customer is already booked for this course");
        if (customer.getMembership().isExpired())
            throw new RuntimeException("The membership of the given user is expired");
        if (!customer.getMembership().isValidForInterval(c.getStartDate(), c.getEndDate()))
            throw new RuntimeException("The membership of the given user is not valid for this course");

        coursesDAO.addBooking(customer.getFiscalCode(), courseId);
    }

    /**
     * Deletes a booking for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to remove the booking
     * @param courseId The course id to remove
     *
     * @return true if successful, false otherwise (i.e. customer not found in course or courseId not exiting)
     */
    public boolean deleteCourseBooking(String customerFiscalCode, int courseId) {
        return coursesDAO.deleteBooking(customerFiscalCode, courseId);
    }

    /**
     * Returns a list of the courses that the given user booked
     *
     * @param customerFiscalCode The fiscal code of the customer
     */
    public List<Course> getBookingsForCustomer(String customerFiscalCode) {
        return coursesDAO.getCoursesForCustomer(customerFiscalCode);
    }
}
