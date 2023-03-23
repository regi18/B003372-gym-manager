import controllers.BookingsController;
import controllers.CoursesController;
import controllers.PeopleController;
import models.Course;
import models.Customer;
import models.Trainer;
import models.membership.FullMembership;
import models.membership.Membership;
import models.membership.WeekendMembershipDecorator;
import view.CustomerView;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        Trainer trainer1 = new Trainer("A", "B", "C", 10);

        // create sample memberships
        Membership yearlyFullMembership = new FullMembership(300, LocalDate.now(), LocalDate.now().plusYears(1));
        Membership yearlyWeekendMembership = new WeekendMembershipDecorator(yearlyFullMembership);

        CoursesController coursesController = new CoursesController();
        BookingsController bookingsController = new BookingsController(coursesController);
        PeopleController<Customer> customersController = new PeopleController<>();
        PeopleController<Trainer> trainersController = new PeopleController<>();

        // Add sample trainers
        trainersController.addPerson(trainer1);

        // Add sample customers
        customersController.addPerson(new Customer("RSSMRA000", "Mario", "Rossi", yearlyFullMembership));
        customersController.addPerson(new Customer("VRDLGI111", "Luigi", "Verdi", yearlyWeekendMembership));

        // Add sample courses
        coursesController.addCourse(new Course("Spinning", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), trainer1));
        coursesController.addCourse(new Course("Calisthenics", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), trainer1));
        coursesController.addCourse(new Course("Yoga", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), trainer1));


        CustomerView customerView = new CustomerView(customersController, bookingsController, coursesController);
        customerView.open();
    }
}