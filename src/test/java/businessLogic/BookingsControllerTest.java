package businessLogic;

import dao.*;
import domainModel.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;


class BookingsControllerTest {
    private BookingsController bookingsController;
    private CoursesController coursesController;
    private CustomersController customersController;
    private int testCourse1Id;
    private int testCourse2Id;
    private String testCustomerFiscalCode;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException, IOException {
        resetDatabase();

        // Create DAOs
        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO(new SQLiteMembershipDAO());
        TrainerDAO trainerDAO = new SQLiteTrainerDAO();
        CourseDAO courseDAO = new SQLiteCourseDAO(trainerDAO, customerDAO);

        // Create controllers
        coursesController = new CoursesController(new TrainersController(trainerDAO), courseDAO);
        customersController = new CustomersController(customerDAO);
        bookingsController = new BookingsController(coursesController, customersController, courseDAO);
        TrainersController trainersController = new TrainersController(trainerDAO);

        // Insert trainer, customer and two courses into the database
        trainersController.addPerson("testTrainer", "testTrainer", "testTrainer", 50);
        testCustomerFiscalCode = customersController.addPerson("A", "A", "A", new String[]{"weekdays", "weekend"}, LocalDate.now().plusDays(1));
        testCourse1Id = coursesController.addCourse("test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");
        testCourse2Id = coursesController.addCourse("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");
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
    public void When_BookingButCourseFull_Expected_RuntimeException() throws Exception {
        int courseId = coursesController.addCourse("tmp", 0, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");

        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookCourse(testCustomerFiscalCode, courseId),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_BookingWithNonExistingCustomer_Expected_RuntimeException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookCourse("nonExistingFiscalCode", testCourse1Id),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_BookingNonExistingCourse_Expected_RuntimeException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookCourse(testCustomerFiscalCode, -1),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_BookingExistingCourse_Expect_Success() {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id));
    }

    @Test
    public void When_DeletingNonExistingCourseBooking_Expect_ToReturnFalse() throws Exception {
        // Test for non-existing courseId
        Assertions.assertFalse(bookingsController.deleteCourseBooking(testCustomerFiscalCode, -1));
        // Test for existing courseId, but user has not booked that course
        customersController.addPerson("B", "B", "B", new String[]{"weekdays", "weekend"}, LocalDate.now().plusDays(1));
        Assertions.assertFalse(bookingsController.deleteCourseBooking("B", testCourse1Id));
    }

    @Test
    public void When_DeletingExistingCourseBooking_Expect_ToReturnTrue() throws Exception {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id));
        Assertions.assertDoesNotThrow(() -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse2Id));
        Assertions.assertTrue(bookingsController.deleteCourseBooking(testCustomerFiscalCode, testCourse1Id));
    }

    @Test
    public void When_GettingBookedCourseForCustomer_Expect_ToReturnTheCourses() throws Exception {
        Assertions.assertDoesNotThrow(() -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id));
        Assertions.assertDoesNotThrow(() -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse2Id));

        List<Course> l = bookingsController.getBookingsForCustomer(testCustomerFiscalCode);
        Assertions.assertEquals(l, Arrays.asList(coursesController.getCourse(testCourse1Id), coursesController.getCourse(testCourse2Id)));
    }
}