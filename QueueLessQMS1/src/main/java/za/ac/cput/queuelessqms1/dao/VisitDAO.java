package za.ac.cput.queuelessqms1.dao;

import java.sql.*;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.connection.QueueLessDBConnection;
import za.ac.cput.queuelessqms1.domain.Visit;

/**
 * QueueLess - Visit DAO
 *
 */
public class VisitDAO {

    public VisitDAO() {
        String createTableStmt = "CREATE TABLE Visit("
                + "visit_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "ticket_id INT NOT NULL, "
                + "visit_date DATE NOT NULL, "
                + "visit_time TIME NOT NULL, "
                + "notes VARCHAR(500), "
                + "FOREIGN KEY (ticket_id) REFERENCES Ticket(ticket_id))";

        try (Connection con = QueueLessDBConnection.sqlConnection()) {

            DatabaseMetaData meta = con.getMetaData();
            try (ResultSet tables = meta.getTables(null, null, "VISIT", null)) {
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

    public int insertVisitRecord(Visit visit) {
        String insertStmt = "INSERT INTO Visit (ticket_id, visit_date, visit_time, notes) VALUES (?, ?, ?, ?)";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(insertStmt, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, visit.getTicketId());
            pstmt.setDate(2, Date.valueOf(visit.getVisitDate()));
            pstmt.setTime(3, Time.valueOf(visit.getVisitTime()));
            if (visit.getNotes() == null || visit.getNotes().isEmpty()) {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(4, visit.getNotes());
            }
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

    public ArrayList<Visit> findVisitsByTicketId(int ticketId) {
        ArrayList<Visit> visits = new ArrayList<>();
        String selectSql = "SELECT * FROM Visit WHERE ticket_id = (?) ORDER BY visit_date DESC, visit_time DESC";
        try (Connection con = QueueLessDBConnection.sqlConnection();
                PreparedStatement pstmt = con.prepareStatement(selectSql)) {

            pstmt.setInt(1, ticketId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    visits.add(new Visit(
                            rs.getInt("visit_id"),
                            rs.getInt("ticket_id"),
                            rs.getDate("visit_date").toLocalDate(),
                            rs.getTime("visit_time").toLocalTime(),
                            rs.getString("notes")
                    ));
                }
            }
        } catch (SQLException sql) {
            System.out.println(sql.getMessage());
        }
        return visits;
    }
}