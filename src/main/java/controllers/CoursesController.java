package controllers;

import utils.observer.events.CourseDeletedEvent;
import models.Course;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;


public class CoursesController {
    private final ArrayList<Course> courses = new ArrayList<>();

    /**
     * Adds a new course to the list
     * @param c The course to add
     * @throws IllegalArgumentException In case the given course is already present in the list
     */
    public void addCourse(Course c) throws IllegalArgumentException {
        if (getCourse(c.getId()) != null)
            throw new IllegalArgumentException("A course with this id exists already");

        this.courses.add(c);
    }

    /**
     * Deletes the given course from the list
     * @param id The id of the course to delete
     * @return true if successful, false otherwise
     */
    public boolean removeCourse(int id) {
        Course toRemove = getCourse(id);
        if (toRemove == null) return false;

        boolean res = this.courses.remove(toRemove);
        if (res) toRemove.notifyAll(new CourseDeletedEvent(toRemove));
        return res;
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
