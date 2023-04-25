package dao;

import java.sql.*;

/**
 * Database class that manages a database connection.
 * Implements the Singleton pattern in order to have a single instance of the Database.
 */
public class Database {
    // Private constructor (Singleton pattern)
    private Database() {
    }

    /**
     * Get the connection instance
     *
     * @return Connection to the SQLite database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:C:/sqlite/db/gym.db");
    }

    /**
     * Close the given connection
     *
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Close the given statement
     *
     * @param statement Statement to close
     */
    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    /**
     * Close the given prepared statement
     *
     * @param preparedStatement PreparedStatement to close
     */
    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    /**
     * Close the given result set
     *
     * @param resultSet ResultSet to close
     */
    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }
}