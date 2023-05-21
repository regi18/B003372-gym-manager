package businessLogic;

import dao.CourseDAO;
import domainModel.Course;
import domainModel.Trainer;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.unmodifiableList;


public class CoursesController {
    private final PeopleController<Trainer> trainersController;
    private final CourseDAO courseDAO;

    public CoursesController(PeopleController<Trainer> trainersController, CourseDAO courseDAO) {
        this.trainersController = trainersController;
        this.courseDAO = courseDAO;
    }

    /**
     * Adds a new course to the list
     *
     * @param name              The name of the course
     * @param maxCapacity       The maximum attendees for the course
     * @param startDate         The start date of the course
     * @param endDate           The end date of the course
     * @param trainerFiscalCode The fiscal code of the trainer for the course
     *
     * @return The id of the newly created course
     *
     * @throws IllegalArgumentException If the trainer is not found
     */
    public int addCourse(String name, int maxCapacity, LocalDateTime startDate, LocalDateTime endDate, String trainerFiscalCode) throws IllegalArgumentException {
        Trainer trainer = trainersController.getPerson(trainerFiscalCode);
        if (trainer == null)
            throw new IllegalArgumentException("Trainer not found");

        Course c = new Course(courseDAO.getNextID(), name, maxCapacity, startDate, endDate, trainer);
        courseDAO.insert(c); // TODO if insert fails?? No way of knowing
        return c.getId();
    }

    /**
     * Deletes the given course from the list
     *
     * @param id The id of the course to delete
     *
     * @return true if successful, false otherwise
     */
    public boolean removeCourse(int id) {
        return courseDAO.delete(id);
    }

    /**
     * Returns the given course
     *
     * @param id The course id to fetch
     *
     * @return The course
     */
    public Course getCourse(int id) {
        return courseDAO.get(id);
    }

    /**
     * Returns a read-only list of courses
     *
     * @return The list of courses
     */
    public List<Course> getAll() {
        return unmodifiableList(this.courseDAO.getAll());
    }
}
