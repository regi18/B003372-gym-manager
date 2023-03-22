package controllers;

import controllers.BookerService;
import controllers.CoursesController;
import models.Course;
import models.Customer;
import models.Trainer;
import models.membership.FullMembership;
import models.membership.Membership;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class BookerServiceTest {
    private BookerService b;
    private Course testCourse1;
    private Course testCourse2;
    private Customer testCustomer;
    private Membership mockedMembership;

    @BeforeEach
    public void init() {
        CoursesManager m = new CoursesManager();
        b = new BookerService(m);
        testCourse1 = new Course("test1", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
        testCourse2 = new Course("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
        m.addCourse(testCourse1);
        m.addCourse(testCourse2);

        mockedMembership = Mockito.mock(FullMembership.class);
        when(mockedMembership.isValidForInterval(any(), any())).thenReturn(true);
        when(mockedMembership.isExpired()).thenReturn(false);
        testCustomer = new Customer("A", "A", "A", mockedMembership);
    }

    @Test
    public void When_BookingExistingCourse_Expect_Success() {
        b.bookCourse(testCustomer, testCourse1.getId());
    }

    @Test
    public void When_BookingNonExistingCourse_Expect_RuntimeException() {
        Assertions.assertThrows(
            RuntimeException.class,
            () -> b.bookCourse(testCustomer, testCourse1.getId() + 10),
            "Expected bookCourse() to throw, but it didn't"
        );
    }

    @Test
    public void When_DeletingNonExistingCourseBooking_Expect_ToReturnFalse() {
        // Test for non-existing courseId
        Assertions.assertFalse(b.deleteCourseBooking(testCustomer, -1));
        // Test for existing courseId, but user has not booked that course
        Assertions.assertFalse(b.deleteCourseBooking(Mockito.mock(Customer.class), testCourse1.getId()));
    }

    @Test
    public void When_DeletingExistingCourseBooking_Expect_ToReturnTrue() {
        b.bookCourse(testCustomer, testCourse1.getId());
        b.bookCourse(testCustomer, testCourse2.getId());
        Assertions.assertTrue(b.deleteCourseBooking(testCustomer, testCourse1.getId()));
    }

    @Test
    public void When_GettingBookedCourseForCustomer_Expect_ToReturnTheCourses() {
        b.bookCourse(testCustomer, testCourse1.getId());
        b.bookCourse(testCustomer, testCourse2.getId());
        b.bookCourse(new Customer("B", "A", "A", mockedMembership), testCourse1.getId());

        List<Course> l = b.getBookingsForCustomer(testCustomer.getFiscalCode());
        Assertions.assertEquals(l, Arrays.asList(testCourse1, testCourse2));
    }
}