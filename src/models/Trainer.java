package models;

public class Trainer extends Person {
    private float salary;

    public Trainer(String fiscalCode, String name, String surname, float salary) {
        super(fiscalCode, name, surname);
        this.salary = salary;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
}
