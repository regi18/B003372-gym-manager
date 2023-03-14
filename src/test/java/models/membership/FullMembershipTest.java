package models.membership;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FullMembershipTest {
    @Test
    public void when_creatingNewMembership_With_negativePrice_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new FullMembership(-10, LocalDate.now(), LocalDate.now().plusDays(1)),
            "Expected constructor to throw, but it didn't"
        );
    }

    @Test
    public void when_creatingNewMembership_With_validInterval_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new FullMembership(1, LocalDate.now(), LocalDate.now().minusDays(10)),
                "Expected constructor to throw, but it didn't"
        );
    }

    @Test
    public void when_creatingNewMembership_With_invalidInterval_Expect_Success() {
        new FullMembership(1, LocalDate.now(), LocalDate.now());
        new FullMembership(1, LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    public void when_checkingIsValidForInterval_With_goodInterval_Expect_toReturnTrue() {
        Membership m = new FullMembership(1, LocalDate.now(), LocalDate.now().plusYears(1));
        Assertions.assertTrue(m.isValidForInterval(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
        Assertions.assertTrue(m.isValidForInterval(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(1)));
        Assertions.assertTrue(m.isValidForInterval(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2)));

        m = new FullMembership(1, LocalDate.now(), LocalDate.now());
        Assertions.assertTrue(m.isValidForInterval(LocalDateTime.now(), LocalDateTime.now().plusHours(3)));
    }

    @Test
    public void when_checkingIsValidForInterval_With_badInterval_Expect_toReturnFalse() {
        Membership m = new FullMembership(1, LocalDate.now(), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isValidForInterval(LocalDateTime.now().minusDays(1), LocalDateTime.now()));
        Assertions.assertFalse(m.isValidForInterval(LocalDateTime.now(), LocalDateTime.now().plusYears(2)));
        Assertions.assertFalse(m.isValidForInterval(LocalDateTime.now().plusYears(2), LocalDateTime.now().plusYears(3)));
    }

    @Test
    public void when_checkingIsExpired_With_validMembership_Expect_toReturnFalse() {
        Membership m = new FullMembership(1, LocalDate.now(), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isExpired());

        m = new FullMembership(1, LocalDate.now().minusDays(10), LocalDate.now());
        Assertions.assertFalse(m.isExpired());

        m = new FullMembership(1, LocalDate.now().plusDays(10), LocalDate.now().plusYears(1));
        Assertions.assertFalse(m.isExpired());

        m = new FullMembership(1, LocalDate.now(), LocalDate.now());
        Assertions.assertFalse(m.isExpired());
    }

    @Test
    public void when_checkingIsExpired_With_invalidMembership_Expect_toReturnTrue() {
        Membership m = new FullMembership(1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1));
        Assertions.assertTrue(m.isExpired());
    }
}