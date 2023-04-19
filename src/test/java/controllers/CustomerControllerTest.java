package controllers;

import models.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;


class CustomerControllerTest {
    private CustomersController c;
    private Customer testCustomer;

    @Test
    @BeforeEach
    public void when_AddingNewPerson_Expect_Success() {
        c = new CustomersController();
        String[] tmp = {"weekend"};
        c.addPerson("RSSMRA", "Mario", "Rossi", tmp, "9999-01-01");
        testCustomer = c.getPerson("RSSMRA");
    }

    @Test
    public void when_AddingNewPerson_With_BadDate_Expect_Failure() {
        String[] tmp = {"test"};
        Assertions.assertThrows(
                DateTimeParseException.class,
                () -> c.addPerson("RSSMRA", "Mario", "Rossi", tmp, ""),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_AddingAlreadyExistingPerson_Expect_IllegalArgumentException() {
        String[] tmp = {"weekend"};
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> c.addPerson("RSSMRA", "Mario", "Rossi", tmp, "9999-01-01"),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_gettingExistingPerson_Expect_toReturnThatPerson() {
        String[] tmp = {"weekend"};
        c.addPerson("AAAAA", "", "", tmp, "9999-01-01");
        Assertions.assertEquals(testCustomer, c.getPerson(testCustomer.getFiscalCode()));
    }

    @Test
    public void when_gettingNonExistingPerson_Expect_toReturnNull() {
        Assertions.assertNull(c.getPerson(testCustomer.getFiscalCode() + 'Z'));
    }

    @Test
    public void when_removingExistingPerson_Expect_toReturnTrue() {
        Assertions.assertTrue(c.removePerson(testCustomer.getFiscalCode()));
    }

    @Test
    public void when_removingNonExistingPerson_Expect_toReturnFalse() {
        Assertions.assertFalse(c.removePerson(testCustomer.getFiscalCode() + 'Z'));
    }

    @Test
    public void when_AddingNewPerson_WithSpecificMembership_Expect_ThatMembershipToWork() {
        String[] tmp = {"weekend"};
        c.addPerson("EEEEEE", "Mario", "Rossi", tmp, "9999-01-01");
        testCustomer = c.getPerson("RSSMRA");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertFalse(testCustomer.getMembership().isValidForInterval(start, start.plusDays(1)));

        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        LocalDateTime start2 = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertTrue(testCustomer.getMembership().isValidForInterval(start2, start2.plusDays(1)));
    }
}