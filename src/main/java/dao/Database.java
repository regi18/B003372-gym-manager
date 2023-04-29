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
        return DriverManager.getConnection("jdbc:sqlite:gym.db");
    }

    /**
     * Close the given connection
     *
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}