package controllers;

import models.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;


class PeopleControllerTest {
    private PeopleController<Person> m;
    private Person testPerson;

    @Test
    @BeforeEach
    public void when_AddingNewPerson_Expect_Success() {
        m = new PeopleController<>();
        testPerson = Mockito.mock(Person.class, Mockito.withSettings().useConstructor("RSSMRA", "Mario", "Rossi").defaultAnswer(Mockito.CALLS_REAL_METHODS));
        m.addPerson(testPerson);
    }

    @Test
    public void when_AddingAlreadyExistingPerson_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> m.addPerson(testPerson),
            "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_gettingExistingPerson_Expect_toReturnThatPerson() {
        Person p = Mockito.mock(Person.class, Answers.CALLS_REAL_METHODS); 
        m.addPerson(p);
        Assertions.assertEquals(testPerson, m.getPerson(testPerson.getFiscalCode()));
    }

    @Test
    public void when_gettingNonExistingPerson_Expect_toReturnNull() {
        Assertions.assertNull(m.getPerson(testPerson.getFiscalCode() + 'Z'));
    }

    @Test
    public void when_removingExistingPerson_Expect_toReturnTrue() {
        Assertions.assertTrue(m.removePerson(testPerson.getFiscalCode()));
    }

    @Test
    public void when_removingNonExistingPerson_Expect_toReturnFalse() {
        Assertions.assertFalse(m.removePerson(testPerson.getFiscalCode() + 'Z'));
    }
}