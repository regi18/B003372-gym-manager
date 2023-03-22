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
        coursesController.addCourse(new Course("Spinning", 10, LocalDateTime.parse("2023-03-20T13:00:00"), LocalDateTime.parse("2023-03-20T14:00:00"), trainer1));
        coursesController.addCourse(new Course("Calisthenics", 10, LocalDateTime.parse("2023-03-15T20:00:00"), LocalDateTime.parse("2023-03-15T21:30:00"), trainer1));
        coursesController.addCourse(new Course("Yoga", 10, LocalDateTime.parse("2023-03-18T09:00:00"), LocalDateTime.parse("2023-03-18T10:00:00"), trainer1));



        CustomerView customerView = new CustomerView(customersController, bookingsController);
        customerView.login("VRDLGI111");
        customerView.prompt();
//
//
//
//        // USE CASE: "Prenota Corso"
//        System.out.println("Booking course...");
//        Customer c1 = gymTest.getCustomersManager().getPerson("VRDLGI111");
//        Customer c2 = gymTest.getCustomersManager().getPerson("RSSMRA000");
//        gymTest.getBookerService().bookCourse(c1, 2);
////        gymTest.getBookerService().bookCourse(c1, 0);
//        gymTest.getBookerService().bookCourse(c2, 0);
//        gymTest.getBookerService().bookCourse(c2, 2);
//
//        gymTest.getCoursesController().removeCourse(2);
    }
}