package models;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Course {
    private final int id;
    private final String name;
    private final int maxCapacity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final ArrayList<Customer> attendees = new ArrayList<>();

    public Course(int id, String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addAttendee(Customer c) {

    }

    public void removeAttendee(String fiscalCode) {

    }

    private boolean isMembershipValidForThisCourse() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    // ----- GETTERS -----

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
