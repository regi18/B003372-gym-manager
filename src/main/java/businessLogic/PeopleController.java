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
     * @return The fiscal code of the newly created person
     */
    protected String addPerson(T newPerson) throws Exception {
        this.dao.insert(newPerson);
        return newPerson.getFiscalCode();
    }

    /** Removes the person with the corresponding fiscalCode */
    public boolean removePerson(String fiscalCode) throws Exception {
        return this.dao.delete(fiscalCode);
    }

    /** Returns the person with the corresponding fiscalCode */
    public T getPerson(String fiscalCode) throws Exception {
        return this.dao.get(fiscalCode);
    }

    /**
     * Returns a read-only list of people
     *
     * @return The list of people
     */
    public List<T> getAll() throws Exception {
        return unmodifiableList(this.dao.getAll());
    }
}
