package controllers;

import models.Course;
import models.Person;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;


public abstract class PeopleController<T extends Person> {
    private final ArrayList<T> people = new ArrayList<>();

    /**
     * Add a new person
     * @param newPerson The new person
     */
    protected void addPerson(T newPerson) throws IllegalArgumentException {
        if (getPerson(newPerson.getFiscalCode()) != null)
            throw new IllegalArgumentException("Someone with this fiscalCode exists already");

        this.people.add(newPerson);
    }

    /**
     * Removes the person with the corresponding fiscalCode
     */
    public boolean removePerson(String fiscalCode) {
        T toRemove = getPerson(fiscalCode);
        if (toRemove == null) return false;
        else return this.people.remove(toRemove);
    }

    /**
     * Returns the person with the corresponding fiscalCode
     */
    public T getPerson(String fiscalCode) {
        for (T p : people) {
            if (p.getFiscalCode().equals(fiscalCode)) return p;
        }
        return null;
    }

    /**
     * Returns a read-only list of people
     * @return The list of people
     */
    public List<T> getAll() {
        return unmodifiableList(this.people);
    }
}
