package dao;

import java.io.*;
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

    /**
     * Initialize the database with the schema.sql file
     *
     * @return The number of rows affected
     * @throws IOException If the schema.sql file is not found
     * @throws SQLException SQL query error
     */
    public static int initDatabase() throws IOException, SQLException {
        StringBuilder resultStringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/database/schema.sql"));
        String line;
        while ((line = br.readLine()) != null) {
            resultStringBuilder.append(line).append("\n");
        }

        Connection connection = getConnection();
        Statement stmt = getConnection().createStatement();
        int row = stmt.executeUpdate(resultStringBuilder.toString());

        stmt.close();
        closeConnection(connection);
        return row;
    }
}