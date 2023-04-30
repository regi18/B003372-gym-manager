package controllers;

import dao.CourseDAO;
import dao.TrainerDAO;
import models.Course;
import models.Customer;
import models.Trainer;
import models.membership.EmptyMembership;
import models.membership.Membership;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class BookingsControllerTest {
    private BookingsController bookingsController;
    private CoursesController coursesController;
    private final int testCourse1Id = 1;
    private final int testCourse2Id = 2;
    private Customer testCustomer;
    private Membership mockedMembership;

    @BeforeEach
    public void init() {
        Trainer trainer = new Trainer("testTrainer", "testTrainer", "testTrainer", 50);
        Course course1 = new Course(testCourse1Id, "test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), trainer);
        Course course2 = new Course(testCourse2Id, "test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), trainer);

        TrainerDAO mockedTrainerDAO = Mockito.mock(TrainerDAO.class);
        when(mockedTrainerDAO.get("testTrainer")).thenReturn(trainer);
        when(mockedTrainerDAO.get(any())).thenThrow(RuntimeException.class);
        when(mockedTrainerDAO.getAll()).thenReturn(List.of(trainer));


        CourseDAO mockedCourseDAO = Mockito.mock(CourseDAO.class);
        when(mockedCourseDAO.getAll()).thenReturn(Arrays.asList(course1, course2));
        when(mockedCourseDAO.get(any())).thenReturn(course1);

        TrainersController trainersController = new TrainersController(mockedTrainerDAO);
        coursesController = new CoursesController(trainersController, mockedCourseDAO);
        bookingsController = new BookingsController(coursesController, mockedCourseDAO);

        mockedMembership = Mockito.mock(EmptyMembership.class);
        when(mockedMembership.isValidForInterval(any(), any())).thenReturn(true);
        when(mockedMembership.isExpired()).thenReturn(false);
        testCustomer = new Customer("A", "A", "A", mockedMembership);
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
        bookingsController.bookCourse(new Customer("B", "B", "B", mockedMembership), testCourse1Id);

        List<Course> l = bookingsController.getBookingsForCustomer(testCustomer.getFiscalCode());
        Assertions.assertEquals(l, Arrays.asList(coursesController.getCourse(testCourse1Id), coursesController.getCourse(testCourse2Id)));
    }
}