package dao;

import domainModel.Trainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class SQLiteTrainerDAOTest {
    private SQLiteTrainerDAO trainerDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException, IOException {
        Connection connection = Database.getConnection();
        trainerDAO = new SQLiteTrainerDAO();

        // Clear the "trainers" table
        connection.prepareStatement("DELETE FROM trainers").executeUpdate();

        // Insert some test data
        connection.prepareStatement("INSERT INTO trainers (fiscal_code, name, surname, salary) VALUES ('test1', 'name1', 'surname1', 100)").executeUpdate();
        connection.prepareStatement("INSERT INTO trainers (fiscal_code, name, surname, salary) VALUES ('test2', 'name2', 'surname2', 200)").executeUpdate();
    }

    @Test
    public void When_GetAllTrainers_Expect_Success() {
        List<Trainer> trainers = trainerDAO.getAll();
        Assertions.assertEquals(2, trainers.size());
    }

    @Test
    public void When_GetTrainerByFiscalCode_Expect_Success() {
        Trainer trainer = trainerDAO.get("test1");
        Assertions.assertEquals("test1", trainer.getFiscalCode());
        Assertions.assertEquals("name1", trainer.getName());
        Assertions.assertEquals("surname1", trainer.getSurname());
        Assertions.assertEquals(100, trainer.getSalary());
    }

    @Test
    public void When_GetTrainerByFiscalCode_Expect_Null() {
        Trainer trainer = trainerDAO.get("test3");
        Assertions.assertNull(trainer);
    }

    @Test
    public void When_AddTrainer_Expect_Success() {
        Trainer trainer = new Trainer("test3", "name3", "surname3", 300);
        trainerDAO.insert(trainer);
        Assertions.assertEquals(3, trainerDAO.getAll().size());
    }

    @Test
    public void When_UpdateTrainer_Expect_Success() {
        Trainer trainer = new Trainer("test1", "name1_edited", "surname1_edited", 1000);
        trainerDAO.update(trainer);
        Assertions.assertEquals(2, trainerDAO.getAll().size());
        Assertions.assertEquals("name1_edited", trainerDAO.get("test1").getName());
        Assertions.assertEquals("surname1_edited", trainerDAO.get("test1").getSurname());
        Assertions.assertEquals(1000, trainerDAO.get("test1").getSalary());
    }

    @Test
    public void When_DeleteTrainer_Expect_Success() {
        Assertions.assertTrue(trainerDAO.delete("test1"));
        Assertions.assertEquals(1, trainerDAO.getAll().size());
    }

    @Test
    public void When_DeleteTrainer_Expect_Failure() {
        Assertions.assertFalse(trainerDAO.delete("test3"));
        Assertions.assertEquals(2, trainerDAO.getAll().size());
    }
}
