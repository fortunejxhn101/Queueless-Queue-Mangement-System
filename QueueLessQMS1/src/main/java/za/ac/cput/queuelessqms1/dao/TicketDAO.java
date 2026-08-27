package za.ac.cput.queuelessqms1.dao;

import java.sql.*;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.connection.DatabaseConnection;

/**
 *
 * @author Mihlali T
 */
public class TicketDAO {

    public static void insertTicket(Ticket ticket) {
        String sql = "INSERT INTO Ticket (ticket_number, department_name, triage_level, queue_status, issue_date, issue_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getTicketNumber());
            stmt.setString(2, ticket.getDepartment());
            if (ticket.getTriageLevel() == null) {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(3, ticket.getTriageLevel());
            }
            stmt.setString(4, ticket.getQueueStatus());
            stmt.setDate(5, Date.valueOf(ticket.getIssueDate()));
            stmt.setTime(6, Time.valueOf(ticket.getIssueTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting ticket: " + e.getMessage());
        }
    }

    public static void updateStatus(String ticketNumber, String newStatus) {
        String sql = "UPDATE Ticket SET queue_status = ? WHERE ticket_number = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, ticketNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating ticket status: " + e.getMessage());
        }
    }

    public static ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT ticket_id, ticket_number, department_name, triage_level, queue_status, issue_date, issue_time FROM Ticket";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getString("ticket_number"),
                        rs.getString("department_name"),
                        rs.getString("triage_level"),
                        rs.getString("queue_status"),
                        rs.getDate("issue_date").toString(),
                        rs.getTime("issue_time").toString()
                );
                tickets.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching tickets: " + e.getMessage());
        }
        return tickets;
    }

    public static Ticket findByTicketNumber(String ticketNumber) {
        String sql = "SELECT ticket_id, ticket_number, department_name, triage_level, queue_status, issue_date, issue_time "
                + "FROM Ticket WHERE ticket_number = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticketNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("ticket_id"),
                            rs.getString("ticket_number"),
                            rs.getString("department_name"),
                            rs.getString("triage_level"),
                            rs.getString("queue_status"),
                            rs.getDate("issue_date").toString(),
                            rs.getTime("issue_time").toString()
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding ticket: " + e.getMessage());
        }
        return null;
    }

}
