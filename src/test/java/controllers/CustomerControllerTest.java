package controllers;

import dao.CustomerDAO;
import dao.Database;
import dao.SQLiteCustomerDAO;
import dao.SQLiteMembershipDAO;
import models.Customer;
import models.membership.EmptyMembership;
import models.membership.Membership;
import models.membership.WeekendMembershipDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


class CustomerControllerTest {
    private CustomersController c;
    private Customer testCustomer;


    @BeforeEach
    public void init() throws SQLException {
        // Set up database
        Database.setDatabase("test.db");
        resetDatabase();

        // Create Customer DAO & controller
        CustomerDAO customerDAO = new SQLiteCustomerDAO(new SQLiteMembershipDAO());
        c = new CustomersController(customerDAO);

        // Create test data (insert a customer)
        Membership membership = new WeekendMembershipDecorator(new EmptyMembership(LocalDate.from(LocalDateTime.now()), LocalDate.parse("9999-01-01")));
        testCustomer = new Customer("RSSMRA", "Mario", "Rossi", membership);
        customerDAO.insert(testCustomer);
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
    public void when_AddingNewPerson_Expect_Success() {
        String[] tmp = {"weekend"};
        c.addPerson("LGIVRD", "Luigi", "Verdi", tmp, LocalDate.parse("9999-01-01"));
        testCustomer = c.getPerson("LGIVRD");
        Assertions.assertEquals("LGIVRD", testCustomer.getFiscalCode());
    }

    @Test
    public void when_AddingAlreadyExistingPerson_Expect_IllegalArgumentException() {
        String[] tmp = {"weekend"};
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> c.addPerson("RSSMRA", "Mario", "Rossi", tmp, LocalDate.parse("9999-01-01")),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_gettingExistingPerson_Expect_toReturnThatPerson() {
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
        c.addPerson("PLUTO", "Pluto", "Giallo", tmp, LocalDate.parse("9999-01-01"));
        testCustomer = c.getPerson("PLUTO");

        // Test if the membership is valid on next thursday
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); // Thursday of the current week
        LocalDateTime start = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertFalse(testCustomer.getMembership().isValidForInterval(start, start.plusDays(1)));

        // Test if the membership is valid on next saturday
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // Saturday of the current week
        // I add 7 days to get the next saturday, because otherwise if today is sunday this test will fail (because the membership is valid from today)
        c.add(Calendar.DATE, 7);
        LocalDateTime start2 = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Assertions.assertTrue(testCustomer.getMembership().isValidForInterval(start2, start2.plusDays(1)));
    }
}