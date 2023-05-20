package dao;

import domainModel.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTrainerDAO implements TrainerDAO {

    @Override
    public Trainer get(String fiscalCode) {
        try {
            Connection con = Database.getConnection();
            Trainer trainer = null;
            PreparedStatement ps = con.prepareStatement("SELECT * FROM trainers WHERE fiscal_code = ?");
            ps.setString(1, fiscalCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                trainer = new Trainer(
                        rs.getString("fiscal_code"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getFloat("salary")
                );
            }

            rs.close();
            ps.close();
            Database.closeConnection(con);
            return trainer;
        } catch (SQLException e) {
            System.out.println("Unable to get trainer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Trainer> getAll() {
        try {
            Connection connection = Database.getConnection();
            List<Trainer> trainers = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trainers");

            while (rs.next()) {
                trainers.add(new Trainer(
                        rs.getString("fiscal_code"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getFloat("salary")
                ));
            }

            return trainers;
        } catch (SQLException e) {
            System.out.println("Unable to get all trainers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void insert(Trainer trainer) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO trainers (fiscal_code, name, surname, salary) VALUES (?, ?, ?, ?)");
            ps.setString(1, trainer.getFiscalCode());
            ps.setString(2, trainer.getName());
            ps.setString(3, trainer.getSurname());
            ps.setFloat(4, trainer.getSalary());
            ps.executeUpdate();
            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to insert trainer: " + e.getMessage());
        }
    }

    @Override
    public void update(Trainer trainer) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE trainers SET name = ?, surname = ?, salary = ? WHERE fiscal_code = ?");
            ps.setString(1, trainer.getName());
            ps.setString(2, trainer.getSurname());
            ps.setFloat(3, trainer.getSalary());
            ps.setString(4, trainer.getFiscalCode());
            ps.executeUpdate();
            ps.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            System.out.println("Unable to update trainer: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM trainers WHERE fiscal_code = ?");
            ps.setString(1, fiscalCode);
            int rows = ps.executeUpdate();
            ps.close();
            Database.closeConnection(connection);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Unable to delete trainer: " + e.getMessage());
        }
        return false;
    }
}
