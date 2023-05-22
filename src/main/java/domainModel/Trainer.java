package domainModel;

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

    @Override
    public String toString() {
        return "Trainer{" +
                "fiscalCode='" + getFiscalCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", salary=" + salary +
                '}';
    }
}
