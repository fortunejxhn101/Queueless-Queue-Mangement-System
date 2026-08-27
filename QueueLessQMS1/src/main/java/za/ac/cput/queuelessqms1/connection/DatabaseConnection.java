package za.ac.cput.queuelessqms1.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mihlali T
 */

public class DatabaseConnection {

    private static final String URL = "jdbc:derby://localhost:1527/QueuelessDB";
    private static final String USER = "administrator";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
