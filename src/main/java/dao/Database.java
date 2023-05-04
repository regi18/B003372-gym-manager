package dao;

import java.sql.*;

/**
 * Database class that manages a database connection.
 * Implements the Singleton pattern in order to have a single instance of the Database.
 */
public class Database {
    private static String dbName = "gym.db";

    // Private constructor (Singleton pattern)
    private Database() {
    }

    /**
     * Set the database name
     *
     * @param dbName Name of the database file
     */
    public static void setDatabase(String dbName) {
        Database.dbName = dbName;
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
        return getConnection(dbName);
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