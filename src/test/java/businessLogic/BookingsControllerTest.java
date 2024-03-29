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
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


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
    public void init() throws Exception {
        resetDatabase();

        // Create DAOs
        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO(new SQLiteMembershipDAO());
        TrainerDAO trainerDAO = new SQLiteTrainerDAO();
        CourseDAO courseDAO = new SQLiteCourseDAO(trainerDAO, customerDAO);
        MembershipDAO membershipDAO = new SQLiteMembershipDAO();

        // Create controllers
        coursesController = new CoursesController(new TrainersController(trainerDAO), courseDAO);
        customersController = new CustomersController(customerDAO);
        bookingsController = new BookingsController(coursesController, customersController, courseDAO, membershipDAO);
        TrainersController trainersController = new TrainersController(trainerDAO);

        // Insert trainer, customer and two courses into the database
        trainersController.addPerson("testTrainer", "testTrainer", "testTrainer", 50);
        trainersController.addPerson("testTrainer2", "testTrainer2", "testTrainer2", 50);

        testCustomerFiscalCode = customersController.addPerson("A", "A", "A", new String[]{"weekdays", "weekend"}, LocalDate.now().plusYears(9999));
        testCourse1Id = coursesController.addCourse("test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");
        testCourse2Id = coursesController.addCourse("test2", 10, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4), "testTrainer");
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
    public void When_BookingButAlreadyBooked_Expected_Exception() throws Exception {
        bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_BookingButAlreadyBookedInSameTime_Expected_Exception() throws Exception {
        int id = coursesController.addCourse("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer2");
        bookingsController.bookCourse(testCustomerFiscalCode, testCourse1Id);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bookingsController.bookCourse(testCustomerFiscalCode, id),
                "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_BookingButCourseFull_Expected_RuntimeException() throws Exception {
        int courseId = coursesController.addCourse("tmp", 0, LocalDateTime.now().plusHours(9), LocalDateTime.now().plusHours(10), "testTrainer");

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

    @Test
    public void When_BookingForCustomer_Expect_UsesToIncreaseCorrectly() throws Exception {
        // Test if the membership is valid on next thursday
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); // Thursday of the current week
        LocalDateTime weekDay = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Test if the membership is valid on next saturday
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // Saturday of the current week
        // I add 7 days to get the next saturday, because otherwise if today is sunday this test will fail (because the membership is valid from today)
        c.add(Calendar.DATE, 7);
        LocalDateTime weekendDay = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        int weekC = coursesController.addCourse("testWeek", 10, weekDay, weekDay.plusHours(1), "testTrainer");
        int weekC2 = coursesController.addCourse("testWeek2", 10, weekDay.plusHours(2), weekDay.plusHours(3), "testTrainer");
        int weekendC = coursesController.addCourse("testWeekend", 10, weekendDay, weekendDay.plusHours(1), "testTrainer");

        // Book the courses
        bookingsController.bookCourse(testCustomerFiscalCode, weekC);
        bookingsController.bookCourse(testCustomerFiscalCode, weekC2);
        bookingsController.bookCourse(testCustomerFiscalCode, weekendC);

        HashMap<String, Integer> uses = customersController.getPerson(testCustomerFiscalCode).getMembership().getUses();
        Assertions.assertEquals(uses.get("weekdays"), 2);
        Assertions.assertEquals(uses.get("weekend"), 1);
    }
}