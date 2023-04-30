import controllers.*;
import dao.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        // DAOs
        TrainerDAO trainerDAO = new TrainerDAOsqlite();
        MembershipDAO membershipDAO = new MembershipDAOsqlite();
        CustomerDAO customerDAO = new CustomerDAOsqlite(membershipDAO);
        CourseDAO courseDAO = new CourseDAOsqlite(trainerDAO, customerDAO);

        // Controllers
        TrainersController trainersController = new TrainersController(trainerDAO);
        CustomersController customersController = new CustomersController(customerDAO);
        CoursesController coursesController = new CoursesController(trainersController, courseDAO);

        // Add sample trainers
        trainersController.addPerson("A", "B", "C", 10);

        // Add sample customers
        customersController.addPerson("RSSMRA000", "Mario", "Rossi", new String[]{"weekend", "weekdays"}, LocalDate.now().plusYears(1));
        customersController.addPerson("VRDLGI111", "Luigi", "Verdi", new String[]{"weekend"}, LocalDate.now().plusYears(1));

        // Add sample courses
        coursesController.addCourse("Spinning", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "A");
        coursesController.addCourse("Calisthenics", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "A");
        coursesController.addCourse("Yoga", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), "A");
    }
}