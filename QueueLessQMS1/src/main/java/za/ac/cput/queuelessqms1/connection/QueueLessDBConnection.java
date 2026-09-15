package za.ac.cput.queuelessqms1.connection;

import java.sql.*;

/**
 *
 * @author admin Fortune
 */
public class QueueLessDBConnection {

    public static Connection sqlConnection() throws SQLException {

        String dbUrl = "";
        String username = "";
        String password = "";
        Connection con = DriverManager.getConnection(dbUrl, username, password);
        return con;
    }
}
