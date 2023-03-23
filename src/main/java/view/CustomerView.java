package view;

import controllers.BookingsController;
import controllers.CoursesController;
import controllers.PeopleController;
import models.Course;
import models.Customer;

import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private final PeopleController<Customer> customerController;
    private final BookingsController bookingsController;
    private Customer currentCustomer;
    private final CoursesController coursesController;
    Scanner reader = new Scanner(System.in);

    public CustomerView(PeopleController<Customer> customerController, BookingsController bookingsController, CoursesController coursesController) {
        this.customerController = customerController;
        this.bookingsController = bookingsController;
        this.coursesController = coursesController;
    }

    public void open() {
        this.login();
        while (true) {
            this.prompt();
        }
//        this.reader.close();
    }

    public void login() {
        while (this.currentCustomer == null) {
            System.out.print("\nPlease, insert your fiscal code: ");
            String res = reader.nextLine();
            this.currentCustomer = customerController.getPerson(res);
            if (this.currentCustomer == null) {
                System.out.print("Invalid fiscal code\n");
            }
        }

        showCustomerDetails();
        prompt();
    }

    public void showCustomerDetails() {
        System.out.println("--------------------");
        System.out.println("Welcome, ");
        System.out.println("Customer: " + currentCustomer.getName() + " " + currentCustomer.getSurname());
        System.out.println("Membership: " + currentCustomer.getMembership().toString());
        System.out.print("Booked courses: [");
        for (Course course : bookingsController.getBookingsForCustomer(currentCustomer.getFiscalCode())) {
            System.out.print(course.getName());
            System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("--------------------");
    }

    public void prompt() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("1. Book a course");
        System.out.println("2. Cancel a booking");
        System.out.println("3. List courses");
        System.out.println("9. Logout");
        System.out.print("> ");

        int res = reader.nextInt();
        reader.nextLine();
        System.out.println();

        switch (res) {
            case 1:
                bookCourse();
                break;
            case 2:
                deleteCourseBooking();
                break;
            case 3:
                listCourses();
                break;
            case 9:
                System.out.print("\n\n\n\n");
                this.currentCustomer = null;
                this.login();
                break;
            default:
                System.out.println("Invalid response");
        }
    }


    public void bookCourse() {
        System.out.print("Which course do you want to book (insert id)? ");
        int id = reader.nextInt();
        reader.nextLine();
        try {
            this.bookingsController.bookCourse(this.currentCustomer, id);
            System.out.println("Course booked successfully");
        }
        catch (Exception e) {
            System.out.println("Error while booking course: " + e);
        }
    }

    public void deleteCourseBooking() {
        List<Course> l = this.bookingsController.getBookingsForCustomer(this.currentCustomer.getFiscalCode());
        if (l.size() == 0) {
            System.out.println("You have no bookings");
            return;
        }

        System.out.println("Which course do you want to cancel? ");
        for (Course c : l) {
            System.out.println("  - " + c.getId() + " " + c.getName());
        }
        System.out.print("> ");
        int id = reader.nextInt();
        reader.nextLine();
        if (this.bookingsController.deleteCourseBooking(this.currentCustomer, id)) {
            System.out.println("Course booking cancelled successfully");
        }
        else {
            System.out.println("Error while cancelling course booking");
        }
    }

    public void listCourses() {
        System.out.println("List of courses:");
        for (Course c : this.coursesController.getAll()) {
            System.out.println("  - " + c.getId() + " " + c.getName());
        }
        System.out.println();
    }
}
