package za.ac.cput.queuelessqms1.connection;

import java.sql.*;

/**
 *
 * @author admin Fortune
 */
public class QueueLessDBConnection {

    public static Connection sqlConnection() throws SQLException {

        String dbUrl = "jdbc:derby://localhost:1527/QueueLess";
        String username = "QueueLess";
        String password = "12345678";
        Connection con = DriverManager.getConnection(dbUrl, username, password);
        return con;
    }
}
