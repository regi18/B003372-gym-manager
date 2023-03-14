package models.membership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;


class EveningMembershipDecoratorTest {
    @Test
    public void when_checkingIsValidForInterval_With_goodInterval_Expect_toReturnTrue() {
        Membership m = new EveningMembershipDecorator(new FullMembership(1, LocalDate.now(), LocalDate.now().plusYears(1)));
        LocalDateTime start = LocalDate.now().atTime(20, 0);
        Assertions.assertTrue(m.isValidForInterval(start, start.plusHours(1)));
    }

    @Test
    public void when_checkingIsValidForInterval_With_badInterval_Expect_toReturnFalse() {
        Membership m = new EveningMembershipDecorator(new FullMembership(1, LocalDate.now(), LocalDate.now().plusYears(1)));
        LocalDateTime start = LocalDate.now().atTime(19, 0);
        Assertions.assertFalse(m.isValidForInterval(start, start.plusHours(1)));
    }
}