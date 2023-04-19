package controllers;

import models.Course;
import models.Trainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;


public class CoursesController {
    private final ArrayList<Course> courses = new ArrayList<>();
    private final PeopleController<Trainer> trainersController;

    public CoursesController(PeopleController<Trainer> trainersController) {
        this.trainersController = trainersController;
    }

    /**
     * Adds a new course to the list
     * @param name The name of the course
     * @param maxCapacity The maximum attendees for the course
     * @param startDate The start date of the course
     * @param endDate The end date of the course
     * @param trainerFiscalCode The fiscal code of the trainer for the course
     * @throws IllegalArgumentException If the trainer is not found
     * @return The id of the newly created course
     */
    public int addCourse(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, String trainerFiscalCode) throws IllegalArgumentException {
        Trainer trainer = trainersController.getPerson(trainerFiscalCode);
        if (trainer == null)
            throw new IllegalArgumentException("Trainer not found");

        Course c = new Course(name, maxCapacity, startDate, endDate, trainer);
        this.courses.add(c);
        return c.getId();
    }

    /**
     * Deletes the given course from the list
     * @param id The id of the course to delete
     * @return true if successful, false otherwise
     */
    public boolean removeCourse(int id) {
        Course toRemove = getCourse(id);
        if (toRemove == null) return false;

        return this.courses.remove(toRemove);
    }

    /**
     * Returns the given course
     * @param id The course id to fetch
     * @return The course
     */
    public Course getCourse(int id) {
        for (Course c : courses) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    /**
     * Returns a read-only list of courses
     * @return The list of courses
     */
    public List<Course> getAll() {
        return unmodifiableList(this.courses);
    }
}
