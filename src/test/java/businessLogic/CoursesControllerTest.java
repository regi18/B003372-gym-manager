package businessLogic;

import dao.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


class CoursesControllerTest {

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @Test
    void When_AddingCourse_With_OccupiedTrainer_Expect_IllegalArgumentException() throws Exception {
        // Create DAOs
        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO(new SQLiteMembershipDAO());
        TrainerDAO trainerDAO = new SQLiteTrainerDAO();
        CourseDAO courseDAO = new SQLiteCourseDAO(trainerDAO, customerDAO);

        // Create controllers
        CoursesController coursesController = new CoursesController(new TrainersController(trainerDAO), courseDAO);
        TrainersController trainersController = new TrainersController(trainerDAO);
        // Insert trainer
        trainersController.addPerson("testTrainer", "testTrainer", "testTrainer", 50);

        // Add course
        coursesController.addCourse("test1", 10, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), "testTrainer");
        // Add non overlapping course with same trainer
        coursesController.addCourse("test2", 10, LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6), "testTrainer");

        // Add overlapping course with same trainer
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> coursesController.addCourse("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(4), "testTrainer"),
                "Expected addCourse() to throw, but it didn't"
        );
    }
}