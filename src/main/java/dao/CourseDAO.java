package dao;

import domainModel.Course;
import domainModel.Customer;

import java.util.List;

public interface CourseDAO extends DAO<Course, Integer> {
    public int getNextID() throws Exception;

    /**
     * Returns the list of customers that are attending the course with the given id.
     *
     * @param courseId The id of the course to get the attendees for
     * @return The list of customers that are attending the course with the given id
     * @throws Exception when the course doesn't exist or if there are problems to access the data source
     */
    public List<Customer> getAttendees(Integer courseId) throws Exception;

    /**
     * Returns the list of courses that the given customer booked
     *
     * @param fiscalCode The fiscal code of the customer to get the courses for
     * @return The list of courses that the given customer booked
     * @throws Exception when the customer doesn't exist or if there are problems to access the data source
     */
    public List<Course> getCoursesForCustomer(String fiscalCode) throws Exception;

    /**
     * Adds a booking for the given customer to the course with the given id
     *
     * @param fiscalCode The fiscal code of the customer to book the course for
     * @param courseId The id of the course to book
     * @throws Exception when the course or the customer doesn't exist or if there are problems to access the data source
     */
    public void addBooking(String fiscalCode, Integer courseId) throws Exception;

    /**
     * Removes a booking for the given customer from the course with the given id
     *
     * @param fiscalCode The fiscal code of the customer to remove the booking for
     * @param courseId The id of the course to remove the booking from
     * @return true if successful, false otherwise (i.e. customer not found in course or courseId not exiting)
     * @throws Exception if there are problems to access the data source
     */
    public boolean deleteBooking(String fiscalCode, Integer courseId) throws Exception;
}
