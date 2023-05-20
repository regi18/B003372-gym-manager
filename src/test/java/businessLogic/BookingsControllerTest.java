package businessLogic;

import dao.*;
import domainModel.Course;
import domainModel.Customer;
import domainModel.Trainer;
import domainModel.membership.EmptyMembership;
import domainModel.membership.Membership;
import domainModel.membership.WeekdaysMembershipDecorator;
import domainModel.membership.WeekendMembershipDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


class BookingsControllerTest {
    private BookingsController bookingsController;
    private CoursesController coursesController;
    private final int testCourse1Id = 1;
    private final int testCourse2Id = 2;
    private Customer testCustomer;

    @BeforeEach
    public void init() throws SQLException {
        // Set up database
        Database.setDatabase("test.db");
        resetDatabase();

        // Create DAOs
        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO(new SQLiteMembershipDAO());
        TrainerDAO trainerDAO = new SQLiteTrainerDAO();
        CourseDAO courseDAO = new SQLiteCourseDAO(trainerDAO, customerDAO);

        // Create controllers
        coursesController = new CoursesController(new TrainersController(trainerDAO), courseDAO);
        bookingsController = new BookingsController(coursesController, courseDAO);

        // Create customer, trainer, membership
        Membership membership = new WeekendMembershipDecorator(new WeekdaysMembershipDecorator(new EmptyMembership(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))));
        testCustomer = new Customer("A", "A", "A", membership);
        Trainer trainer = new Trainer("testTrainer", "testTrainer", "testTrainer", 50);

        // Insert trainer, customer and two courses into the database
        trainerDAO.insert(trainer);
        customerDAO.insert(testCustomer);
        courseDAO.insert(new Course(testCourse1Id, "test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), trainer));
        courseDAO.insert(new Course(testCourse2Id, "test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), trainer));
    }

    private void resetDatabase() throws SQLException {
        Connection connection = Database.getConnection();

        // Delete data from all tables
        List<String> tables = Arrays.asList("trainers", "courses", "customers", "memberships", "bookings", "membership_extensions");
        for (String table : tables) connection.prepareStatement("DELETE FROM " + table).executeUpdate();

        // Reset autoincrement counters
        connection.prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();
        Database.closeConnection(connection);
    }

    @Test
    public void When_BookingExistingCourse_Expect_Success() {
        bookingsController.bookCourse(testCustomer, testCourse1Id);
    }

    @Test
    public void When_BookingNonExistingCourse_Expect_Exception() {
        Assertions.assertThrows(
                Exception.class,
                () -> bookingsController.bookCourse(testCustomer, testCourse1Id + 10),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_DeletingNonExistingCourseBooking_Expect_ToReturnFalse() {
        // Test for non-existing courseId
        Assertions.assertFalse(bookingsController.deleteCourseBooking(testCustomer, -1));
        // Test for existing courseId, but user has not booked that course
        Assertions.assertFalse(bookingsController.deleteCourseBooking(Mockito.mock(Customer.class), testCourse1Id));
    }

    @Test
    public void When_DeletingExistingCourseBooking_Expect_ToReturnTrue() {
        bookingsController.bookCourse(testCustomer, testCourse1Id);
        bookingsController.bookCourse(testCustomer, testCourse2Id);
        Assertions.assertTrue(bookingsController.deleteCourseBooking(testCustomer, testCourse1Id));
    }

    @Test
    public void When_GettingBookedCourseForCustomer_Expect_ToReturnTheCourses() {
        bookingsController.bookCourse(testCustomer, testCourse1Id);
        bookingsController.bookCourse(testCustomer, testCourse2Id);

        List<Course> l = bookingsController.getBookingsForCustomer(testCustomer.getFiscalCode());
        Assertions.assertEquals(l, Arrays.asList(coursesController.getCourse(testCourse1Id), coursesController.getCourse(testCourse2Id)));
    }
}