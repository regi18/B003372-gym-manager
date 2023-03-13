package managers;

import events.CourseDeletedEvent;
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

        boolean res = this.courses.remove(toRemove);
        if (res) {
            System.out.println("[CourseManager] Course '" + toRemove.getName() + "' canceled");
            toRemove.notifyAll(new CourseDeletedEvent(toRemove));
        }
        return res;
    }

    public Course getCourse(int id) {
        for (Course c : courses) {
            if (c.getId() == id) return c;
        }
        return null;
    }
}
