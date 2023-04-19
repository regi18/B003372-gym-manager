package controllers;

import models.Course;
import models.Customer;
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
    private BookingsController b;
    private CoursesController m;
    private int testCourse1Id;
    private int testCourse2Id;
    private Customer testCustomer;
    private Membership mockedMembership;

    @BeforeEach
    public void init() {
        TrainersController t = new TrainersController();
        t.addPerson("testTrainer", "testTrainer", "testTrainer", 50);
        m = new CoursesController(t);
        b = new BookingsController(m);
        testCourse1Id = m.addCourse("test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");
        testCourse2Id = m.addCourse("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "testTrainer");

        mockedMembership = Mockito.mock(EmptyMembership.class);
        when(mockedMembership.isValidForInterval(any(), any())).thenReturn(true);
        when(mockedMembership.isExpired()).thenReturn(false);
        testCustomer = new Customer("A", "A", "A", mockedMembership);
    }

    @Test
    public void When_BookingExistingCourse_Expect_Success() {
        b.bookCourse(testCustomer, testCourse1Id);
    }

    @Test
    public void When_BookingNonExistingCourse_Expect_RuntimeException() {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> b.bookCourse(testCustomer, testCourse1Id + 10),
            "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_DeletingNonExistingCourseBooking_Expect_ToReturnFalse() {
        // Test for non-existing courseId
        Assertions.assertFalse(b.deleteCourseBooking(testCustomer, -1));
        // Test for existing courseId, but user has not booked that course
        Assertions.assertFalse(b.deleteCourseBooking(Mockito.mock(Customer.class), testCourse1Id));
    }

    @Test
    public void When_DeletingExistingCourseBooking_Expect_ToReturnTrue() {
        b.bookCourse(testCustomer, testCourse1Id);
        b.bookCourse(testCustomer, testCourse2Id);
        Assertions.assertTrue(b.deleteCourseBooking(testCustomer, testCourse1Id));
    }

    @Test
    public void When_GettingBookedCourseForCustomer_Expect_ToReturnTheCourses() {
        b.bookCourse(testCustomer, testCourse1Id);
        b.bookCourse(testCustomer, testCourse2Id);
        b.bookCourse(new Customer("B", "A", "A", mockedMembership), testCourse1Id);

        List<Course> l = b.getBookingsForCustomer(testCustomer.getFiscalCode());
        Assertions.assertEquals(l, Arrays.asList(m.getCourse(testCourse1Id), m.getCourse(testCourse2Id)));
    }
}