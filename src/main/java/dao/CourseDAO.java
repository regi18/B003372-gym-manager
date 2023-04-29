package dao;

import models.Course;

public interface CourseDAO extends DAO<Course, Integer> {
    public int getNextID() throws Exception;
}
