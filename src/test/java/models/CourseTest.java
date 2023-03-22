package models;

import models.membership.EveningMembershipDecorator;
import models.membership.FullMembership;
import models.membership.Membership;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CourseTest {
    @Test
    public void When_CreatingNewCourse_With_InvalidDates_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Course("A", 10, LocalDateTime.now(), LocalDateTime.now().minusMinutes(1), Mockito.mock(Trainer.class)),
            "Expected constructor to throw, but it didn't"
        );

        LocalDateTime d = LocalDateTime.now();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Course("A", 10, d, d, Mockito.mock(Trainer.class)),
            "Expected constructor to throw, but it didn't"
        );
    }


    @Test
    public void When_AddingNewAttendeeButCourseFull_Expected_RuntimeException() {
        Course c = new Course("Test", 2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));

        Membership mockedMembership = Mockito.mock(FullMembership.class);
        when(mockedMembership.isValidForInterval(any(), any())).thenReturn(true);
        when(mockedMembership.isExpired()).thenReturn(false);

        c.addAttendee(new Customer("A", "A", "A", mockedMembership));
        c.addAttendee(new Customer("B", "A", "A", mockedMembership));
        Assertions.assertThrows(
            RuntimeException.class,
            () -> c.addAttendee(new Customer("C", "A", "A", mockedMembership)),
            "Expected addAttendee() to throw, but it didn't"
        );
    }

    @Test
    public void When_AddingNewAttendeeButMembershipInvalid_Expected_RuntimeException() {
        LocalDateTime start = LocalDate.now().atTime(10, 0);
        Course c = new Course("Test", 2, start, start.plusHours(1), Mockito.mock(Trainer.class));

        Membership m = new EveningMembershipDecorator(new FullMembership(1, LocalDate.now(), LocalDate.now()));

        Assertions.assertThrows(
            RuntimeException.class,
            () -> c.addAttendee(new Customer("A", "A", "A", m)),
            "Expected addAttendee() to throw, but it didn't"
        );
    }

    @Test
    public void When_AddingNewAttendeeButMembershipExpired_Expected_RuntimeException() {
        LocalDateTime start = LocalDate.now().atTime(10, 0);
        Course c = new Course("Test", 2, start, start.plusHours(1), Mockito.mock(Trainer.class));

        Membership m = new EveningMembershipDecorator(new FullMembership(1, LocalDate.now().plusDays(10), LocalDate.now().plusDays(20)));

        Assertions.assertThrows(
            RuntimeException.class,
            () -> c.addAttendee(new Customer("A", "A", "A", m)),
            "Expected addAttendee() to throw, but it didn't"
        );
    }

    @Test
    public void When_RemovingExistingAttendee_Expect_ToReturnTrue() {
        Course c = new Course("Test", 2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
        c.addAttendee(new Customer("A", "B", "C", new FullMembership(1, LocalDate.now(), LocalDate.now())));
        Assertions.assertTrue(c.removeAttendee("A"));
    }

    @Test
    public void When_RemovingNonExistingAttendee_Expect_ToReturnFalse() {
        Course c = new Course("Test", 2, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
        c.addAttendee(new Customer("A", "B", "C", new FullMembership(1, LocalDate.now(), LocalDate.now())));
        Assertions.assertFalse(c.removeAttendee("B"));
    }
}