package managers;

import models.Course;
import java.util.ArrayList;


public class CoursesManager {
    private final ArrayList<Course> courses = new ArrayList<>();

    public void addCourse(Course c) throws IllegalArgumentException {
        if (getCourse(c.getId()) != null)
            throw new IllegalArgumentException("A course with this id exists already");

        this.courses.add(c);
    }

    public boolean deleteCourse(int id) {
        Course toRemove = getCourse(id);
        if (toRemove == null) return false;
        else return this.courses.remove(toRemove);
    }

    public Course getCourse(int id) {
        for (Course c : courses) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void printCoursesList() {
        System.out.println(this.courses);
    }
}
