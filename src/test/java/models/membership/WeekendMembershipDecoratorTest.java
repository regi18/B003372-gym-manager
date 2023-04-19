package models.membership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;


class WeekendMembershipDecoratorTest {
    @Test
    public void when_checkingIsValidForInterval_With_goodInterval_Expect_toReturnTrue() {
        Membership m = new WeekendMembershipDecorator(new EmptyMembership(LocalDate.now(), LocalDate.now().plusYears(1)));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Assertions.assertTrue(m.isValidForInterval(start, start.plusHours(1)));
    }

    @Test
    public void when_checkingIsValidForInterval_With_badInterval_Expect_toReturnFalse() {
        Membership m = new WeekendMembershipDecorator(new EmptyMembership(LocalDate.now(), LocalDate.now().plusYears(1)));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Assertions.assertFalse(m.isValidForInterval(start, start.plusHours(1)));

    }
}