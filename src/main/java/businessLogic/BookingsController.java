package businessLogic;

import dao.CourseDAO;
import dao.MembershipDAO;
import dao.SQLiteMembershipDAO;
import domainModel.Course;
import domainModel.Customer;
import domainModel.membership.Membership;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class BookingsController {
    private final CoursesController coursesController;
    private final CustomersController customersController;
    private final CourseDAO coursesDAO;
    private final MembershipDAO membershipDAO;

    public BookingsController(CoursesController coursesController, CustomersController customersController, CourseDAO bookingsDAO, MembershipDAO membershipDAO) {
        this.coursesController = coursesController;
        this.customersController = customersController;
        this.coursesDAO = bookingsDAO;
        this.membershipDAO = membershipDAO;
    }

    /**
     * Books a course for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to book the course for
     * @param courseId           The course id to book
     *
     * @throws Exception when courseId doesn't exist and bubbles up exceptions of Customer::addBooking(), CourseDAO::getAttendees()
     */
    public void bookCourse(String customerFiscalCode, int courseId) throws Exception {
        Course c = coursesController.getCourse(courseId);
        Customer customer = customersController.getPerson(customerFiscalCode);
        if (c == null) throw new RuntimeException("The given course id does not exist");
        if (customer == null) throw new RuntimeException("The given customer does not exist");

        List<Customer> attendees = coursesDAO.getAttendees(courseId);
        if (attendees.size() == c.getMaxCapacity())
            throw new RuntimeException("This course if full, can't book");
        if (attendees.contains(customer))
            throw new RuntimeException("The given customer is already booked for this course");

        getBookingsForCustomer(customerFiscalCode).forEach(course -> {
            LocalDateTime c1 = course.getStartDate().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime c2 = c.getStartDate().truncatedTo(ChronoUnit.HOURS);
            if (c1.equals(c2))
                throw new RuntimeException("The given customer is already booked for a course at the same time");
        });

        if (customer.getMembership().isExpired())
            throw new RuntimeException("The membership of the given user is expired");
        if (!customer.getMembership().isValidForInterval(c.getStartDate(), c.getEndDate()))
            throw new RuntimeException("The membership of the given user is not valid for this course");

        // Update membership (update uses field)
        membershipDAO.updateOfCustomer(customer.getFiscalCode(), customer.getMembership());

        coursesDAO.addBooking(customer.getFiscalCode(), courseId);
    }

    /**
     * Deletes a booking for the given customer
     *
     * @param customerFiscalCode The fiscal code of the customer for whom to remove the booking
     * @param courseId           The course id to remove
     *
     * @return true if successful, false otherwise (i.e. customer not found in course or courseId not exiting)
     *
     * @throws Exception bubbles up exceptions of CourseDAO::deleteBooking()
     */
    public boolean deleteCourseBooking(String customerFiscalCode, int courseId) throws Exception {
        return coursesDAO.deleteBooking(customerFiscalCode, courseId);
    }

    /**
     * Returns a list of the courses that the given user booked
     *
     * @param customerFiscalCode The fiscal code of the customer
     *
     * @return A list of courses that the given user booked
     *
     * @throws Exception bubbles up exceptions of CourseDAO::getCoursesForCustomer()
     */
    public List<Course> getBookingsForCustomer(String customerFiscalCode) throws Exception {
        return coursesDAO.getCoursesForCustomer(customerFiscalCode);
    }
}
