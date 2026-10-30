package za.ac.cput.queuelessqms1.dao;
import java.util.*;
import java.sql.*;
import za.ac.cput.queuelessqms1.connection.*;
import za.ac.cput.queuelessqms1.domain.*;

/**
 *
 * @author admin Fortune
 */
public class StaffDAO {

    public StaffDAO(){
        String createStaffTable = "CREATE TABLE Staff (staff_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL, "
                + "username VARCHAR(20) NOT NULL UNIQUE, "
                + "password VARCHAR(20) NOT NULL)";

        try (Connection con = QueueLessDBConnection.sqlConnection()) {

            DatabaseMetaData meta = con.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "STAFF", null)) {
                if (tables.next()) {
                    return;
                }
            }

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(createStaffTable);
                System.out.println("Staff Table creation: Successfull in the QueueLess Database");
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    public boolean insertStaffSignUpDetails(Staff staffMember){

        String insertStaffSignUpDetails = "INSERT INTO Staff (first_name, last_name, username, password) VALUES (?,?,?,?)";

        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(insertStaffSignUpDetails)) {

            pstmt.setString(1, staffMember.getFirstName());
            pstmt.setString(2, staffMember.getLastName());
            pstmt.setString(3, staffMember.getUserName());
            pstmt.setString(4, staffMember.getPassword());

            return pstmt.executeUpdate() > 0;
        }
        catch (SQLException sql) {
            System.out.println(sql.getMessage());
            return false;
        }
    }

    public Staff findByUsername(String userName) throws SQLException {

        String selectUserRecord = "SELECT * FROM Staff WHERE username = (?)";

        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(selectUserRecord)) {

            pstmt.setString(1, userName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(rs.getString("first_name"), rs.getString("last_name"),
                            rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    public boolean isUsernameTaken(String userName) throws SQLException {
        return findByUsername(userName) != null;
    }

    public ArrayList <Staff> retrieveUserRecord (String userName){

        ArrayList <Staff> retrieveUserRecord = new ArrayList <>();

        try {
            Staff found = findByUsername(userName);
            if (found != null) {
                retrieveUserRecord.add(found);
            }
        }
        catch(SQLException sql){
            System.out.println(sql.getMessage());
        }
        return retrieveUserRecord;
    }

    public int findStaffIdByUsername(String username) {
        String selectSql = "SELECT staff_id FROM Staff WHERE username = (?)";
        try (Connection localCon = QueueLessDBConnection.sqlConnection();
                PreparedStatement localPstmt = localCon.prepareStatement(selectSql)) {
            localPstmt.setString(1, username);
            try (ResultSet rs = localPstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("staff_id");
                }
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return -1;
    }

}