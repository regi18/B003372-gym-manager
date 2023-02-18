package managers;

import models.Person;
import java.util.ArrayList;


public class PeopleManager<T extends Person> {
    private final ArrayList<T> people = new ArrayList<T>();

    /**
     * Add a new person
     * @param newPerson The new person
     */
    public void addPerson(T newPerson) throws IllegalArgumentException {
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
}
