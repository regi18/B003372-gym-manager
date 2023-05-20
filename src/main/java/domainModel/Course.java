package domainModel;

import java.time.LocalDateTime;
import java.util.Objects;


public class Course {
    private final Integer id;
    private final String name;
    private final int maxCapacity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Trainer trainer;

    public Course(int id, String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, Trainer trainer) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainer = trainer;
        this.id = id;

        if (endDate.equals(startDate) || endDate.isBefore(startDate))
            throw new IllegalArgumentException("endDate should be greater than startDate");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
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
