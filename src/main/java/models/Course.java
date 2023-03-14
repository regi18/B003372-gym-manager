package models;
import models.membership.Membership;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Course extends Subject {
    private static int nextId = 0;
    private final int id;
    private final String name;
    private final int maxCapacity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final ArrayList<Customer> attendees = new ArrayList<>();
    private final Trainer trainer;

    public Course(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, Trainer trainer) {
        this.id = nextId++;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainer = trainer;

        if (endDate.equals(startDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("endDate should be greater than startDate");
    }

    /**
     * Adds a new customer to this course
     * @param c The custom to add
     * @throws RuntimeException when course if full or membership is not valid
     */
    public void addAttendee(Customer c) throws RuntimeException {
        if (this.attendees.size() == maxCapacity)
            throw new RuntimeException("This course if full, can't book");
        if (c.getMembership().isExpired())
            throw new RuntimeException("The membership of the given user is expired");
        if (!isMembershipValidForThisCourse(c.getMembership()))
            throw new RuntimeException("The membership of the given user is not valid for this course");

        this.attendees.add(c);
        this.subscribe(c);
    }

    public boolean removeAttendee(String fiscalCode) {
        Customer res = this.attendees.stream().filter(a -> a.getFiscalCode().equals(fiscalCode)).findAny().orElse(null);
        if (res != null) {
            this.unsubscribe(res);
            return true;
        }
        else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    // ----- PRIVATE METHODS -----

    private boolean isMembershipValidForThisCourse(Membership m) {
        return m.isValidForInterval(this.startDate, this.endDate);
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

    public Trainer getTrainer() {
        return trainer;
    }
}
