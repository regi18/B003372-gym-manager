package dao;

import domainModel.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteTrainerDAO implements TrainerDAO {

    @Override
    public Trainer get(String fiscalCode) throws SQLException {
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
    }

    @Override
    public List<Trainer> getAll() throws SQLException {
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
    }

    @Override
    public void insert(Trainer trainer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO trainers (fiscal_code, name, surname, salary) VALUES (?, ?, ?, ?)");
        ps.setString(1, trainer.getFiscalCode());
        ps.setString(2, trainer.getName());
        ps.setString(3, trainer.getSurname());
        ps.setFloat(4, trainer.getSalary());
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public void update(Trainer trainer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE trainers SET name = ?, surname = ?, salary = ? WHERE fiscal_code = ?");
        ps.setString(1, trainer.getName());
        ps.setString(2, trainer.getSurname());
        ps.setFloat(3, trainer.getSalary());
        ps.setString(4, trainer.getFiscalCode());
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public boolean delete(String fiscalCode) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM trainers WHERE fiscal_code = ?");
        ps.setString(1, fiscalCode);
        int rows = ps.executeUpdate();
        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }
}
