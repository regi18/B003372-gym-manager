package models;

import java.util.Objects;

public abstract class Person {
    private final String fiscalCode;
    private final String name;
    private final String surname;

    public Person(String fiscalCode, String name, String surname) {
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.surname = surname;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(fiscalCode, person.fiscalCode);
    }
}
