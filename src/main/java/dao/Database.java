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
     * @param dbName Name of the database file
     *
     * @return Connection to the SQLite database
     */
    public static Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbName);
    }

    /**
     * Get the default connection instance
     *
     * @return Connection to the SQLite database
     */
    public static Connection getConnection() throws SQLException {
        return getConnection("gym.db");
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