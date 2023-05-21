package businessLogic;

import dao.DAO;
import domainModel.Person;

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
    protected String addPerson(T newPerson) throws IllegalArgumentException {
//        if (getPerson(newPerson.getFiscalCode()) != null)
//            throw new IllegalArgumentException("Someone with this fiscalCode exists already");
        // TODO throw IllegalArgumentException if person already exists, use sql exception instead of manual check like above
        this.dao.insert(newPerson);
        return newPerson.getFiscalCode();
    }

    /** Removes the person with the corresponding fiscalCode */
    public boolean removePerson(String fiscalCode) {
        return this.dao.delete(fiscalCode);
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
