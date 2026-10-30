package za.ac.cput.queuelessqms1.dao;

import java.sql.*;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.connection.QueueLessDBConnection;
import za.ac.cput.queuelessqms1.domain.QueueAction;

public class QueueActionDAO {

    public QueueActionDAO() {
        String createTableStmt = "CREATE TABLE Queue_Action("
                + "action_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "ticket_id INT NOT NULL, "
                + "staff_id INT NOT NULL, "
                + "action_type VARCHAR(20) NOT NULL, "
                + "time_stamp TIMESTAMP NOT NULL, "
                + "FOREIGN KEY (ticket_id) REFERENCES Ticket(ticket_id), "
                + "FOREIGN KEY (staff_id) REFERENCES Staff(staff_id))";

        try (Connection con = QueueLessDBConnection.sqlConnection()) {

            DatabaseMetaData meta = con.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "QUEUE_ACTION", null)) {
                if (tables.next()) {
                    return;
                }
            }

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(createTableStmt);
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
    }

    public int insertQueueAction(QueueAction action) {
        String insertStmt = "INSERT INTO Queue_Action (ticket_id, staff_id, action_type, time_stamp) VALUES (?, ?, ?, ?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(insertStmt, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, action.getTicketId());
            pstmt.setInt(2, action.getStaffId());
            pstmt.setString(3, action.getActionType());
            pstmt.setTimestamp(4, action.getTimeStamp());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
            return -1;
        }
    }

    public ArrayList<QueueAction> findActionsByTicketId(int ticketId) {
        ArrayList<QueueAction> actions = new ArrayList<>();
        String selectSql = "SELECT * FROM Queue_Action WHERE ticket_id = (?) ORDER BY time_stamp DESC";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(selectSql)) {

            pstmt.setInt(1, ticketId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    actions.add(new QueueAction(
                            rs.getInt("action_id"),
                            rs.getInt("ticket_id"),
                            rs.getInt("staff_id"),
                            rs.getString("action_type"),
                            rs.getTimestamp("time_stamp")
                    ));
                }
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return actions;
    }
}