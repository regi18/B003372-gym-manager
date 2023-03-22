package utils.observer.events;

import models.Course;


public class CourseDeletedEvent implements Event {
    private final Course removedCourse;

    public CourseDeletedEvent(Course removedCourse) {
        this.removedCourse = removedCourse;
    }

    @Override
    public String getName() {
        return "CourseDeletedEvent";
    }

    @Override
    public String getDescription() {
        return "The course " + removedCourse.getName() + " has been canceled";
    }

    public Course getRemovedCourse() {
        return removedCourse;
    }
}
