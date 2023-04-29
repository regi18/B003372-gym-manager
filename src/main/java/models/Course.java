package models;

import models.membership.Membership;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;


public class Course {
    private Integer id = null;
    private final String name;
    private final int maxCapacity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final ArrayList<Customer> attendees = new ArrayList<>();
    private final Trainer trainer;

    public Course(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, Trainer trainer) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainer = trainer;

        if (endDate.equals(startDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("endDate should be greater than startDate");
    }

    public Course(Integer id, String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, Trainer trainer) {
        this(name, maxCapacity, startDate, endDate, trainer);   // Delegate the other constructor
        this.id = id;
    }

    /**
     * Adds a new customer to this course
     *
     * @param c The custom to add
     *
     * @throws RuntimeException when course if full or membership is not valid
     */
    public void addAttendee(Customer c) throws RuntimeException {
        if (this.attendees.size() == maxCapacity)
            throw new RuntimeException("This course if full, can't book");
        if (this.attendees.contains(c))
            throw new RuntimeException("The given customer is already booked for this course");
        if (c.getMembership().isExpired())
            throw new RuntimeException("The membership of the given user is expired");
        if (!isMembershipValidForThisCourse(c.getMembership()))
            throw new RuntimeException("The membership of the given user is not valid for this course");

        this.attendees.add(c);
    }

    /**
     * Removes an attendee
     *
     * @param fiscalCode The fiscal code of the attendee to remove
     *
     * @return true if removed, false otherwise
     */
    public boolean removeAttendee(String fiscalCode) {
        return this.attendees.removeIf(a -> a.getFiscalCode().equals(fiscalCode));
    }

    /**
     * Returns a read-only list of attendees
     */
    public List<Customer> getAttendees() {
        return unmodifiableList(this.attendees);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
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
