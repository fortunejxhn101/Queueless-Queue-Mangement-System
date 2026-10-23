package za.ac.cput.queuelessqms1.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author yanto
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/queueless";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void main(String[] args){
        try{
            Connection conn = getConnection();
            System.out.println("Connected to MySQL successfully!");
            conn.close();
        }catch (SQLException e){
            System.out.println("Connection failed:" + e.getMessage());
        }
    }
}
