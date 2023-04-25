package dao;

import models.Trainer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerDAOsqlite implements TrainerDAO {

    @Override
    public Trainer get(int id) throws SQLException {
        Connection con = Database.getConnection();
        Trainer trainer = null;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM trainers WHERE fiscal_code = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            trainer = new Trainer(
                    rs.getString("fiscal_code"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getFloat("salary")
            );
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
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
    public int insert(Trainer trainer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO trainers (fiscal_code, name, surname, salary) VALUES (?, ?, ?, ?)");
        ps.setString(1, trainer.getFiscalCode());
        ps.setString(2, trainer.getName());
        ps.setString(3, trainer.getSurname());
        ps.setFloat(4, trainer.getSalary());
        int rows = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int update(Trainer trainer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE trainers SET name = ?, surname = ?, salary = ? WHERE fiscal_code = ?");
        ps.setString(1, trainer.getName());
        ps.setString(2, trainer.getSurname());
        ps.setFloat(3, trainer.getSalary());
        ps.setString(4, trainer.getFiscalCode());
        int rows = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }

    @Override
    public int delete(Trainer trainer) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM trainers WHERE fiscal_code = ?");
        ps.setString(1, trainer.getFiscalCode());
        int rows = ps.executeUpdate();
        Database.closePreparedStatement(ps);
        Database.closeConnection(connection);
        return rows;
    }
}
