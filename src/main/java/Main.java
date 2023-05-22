import businessLogic.*;
import dao.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        // DAOs
        TrainerDAO trainerDAO = new SQLiteTrainerDAO();
        MembershipDAO membershipDAO = new SQLiteMembershipDAO();
        CustomerDAO customerDAO = new SQLiteCustomerDAO(membershipDAO);
        CourseDAO courseDAO = new SQLiteCourseDAO(trainerDAO, customerDAO);

        // Controllers
        TrainersController trainersController = new TrainersController(trainerDAO);
        CustomersController customersController = new CustomersController(customerDAO);
        CoursesController coursesController = new CoursesController(trainersController, courseDAO);
        BookingsController bookingsController = new BookingsController(coursesController, customersController, courseDAO, membershipDAO);

        // Add sample trainers
        trainersController.addPerson("A", "B", "C", 10);

        // Add sample customers
        customersController.addPerson("RSSMRA000", "Mario", "Rossi", new String[]{"weekend", "weekdays"}, LocalDate.now().plusYears(1));
        customersController.addPerson("VRDLGI111", "Luigi", "Verdi", new String[]{"weekend"}, LocalDate.now().plusYears(1));

        // Add sample courses
        int spinning = coursesController.addCourse("Spinning", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "A");
        int cali = coursesController.addCourse("Calisthenics", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "A");
        int yoga = coursesController.addCourse("Yoga", 10, LocalDateTime.now().plusHours(1).plusDays(5), LocalDateTime.now().plusHours(3).plusDays(5), "A");

        bookingsController.bookCourse("RSSMRA000", spinning);
        bookingsController.bookCourse("RSSMRA000", cali);
        bookingsController.bookCourse("RSSMRA000", yoga);

        // Try getUses()
        System.out.println(customersController.getPerson("RSSMRA000").getMembership().getUses());
        System.out.println(customersController.getPerson("RSSMRA000").getMembership().getUsesDescription());
        System.out.println(customersController.getPerson("RSSMRA000"));
    }
}