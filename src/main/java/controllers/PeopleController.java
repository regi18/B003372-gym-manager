package controllers;

import dao.DAO;
import models.Person;

import java.util.List;

import static java.util.Collections.unmodifiableList;


public abstract class PeopleController<T extends Person> {
    private final DAO<T, String> dao;

    PeopleController(DAO<T, String> dao) {
        this.dao = dao;
    }

    /**
     * Add a new person
     *
     * @param newPerson The new person
     */
    protected void addPerson(T newPerson) throws IllegalArgumentException {
        if (getPerson(newPerson.getFiscalCode()) != null)
            throw new IllegalArgumentException("Someone with this fiscalCode exists already");

        this.dao.insert(newPerson);
    }

    /** Removes the person with the corresponding fiscalCode */
    public boolean removePerson(String fiscalCode) {
        T toRemove = getPerson(fiscalCode);
        if (toRemove == null) return false;
        else return this.dao.delete(fiscalCode);
    }

    /** Returns the person with the corresponding fiscalCode */
    public T getPerson(String fiscalCode) {
        return this.dao.get(fiscalCode);
    }

    /**
     * Returns a read-only list of people
     *
     * @return The list of people
     */
    public List<T> getAll() {
        return unmodifiableList(this.dao.getAll());
    }
}
