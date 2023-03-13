package models;
import models.membership.Membership;
import java.util.ArrayList;


public class Customer extends Person {
    private Membership membership;
    private final ArrayList<Course> bookedCourses = new ArrayList<>();

    public Customer(String fiscalCode, String name, String surname, Membership membership) {
        super(fiscalCode, name, surname);
        this.membership = membership;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public void addBookedCourse(Course c) {
        this.bookedCourses.add(c);
    }

    public boolean removeBookedCourse(Course c) {
        return this.bookedCourses.remove(c);
    }
}
