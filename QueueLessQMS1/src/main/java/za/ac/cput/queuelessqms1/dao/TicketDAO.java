package za.ac.cput.queuelessqms1.dao;
import java.sql.*;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.connection.QueueLessDBConnection;

/**
 *
 * @author Mihlali T
 */
public class TicketDAO {
    public static void createTicketTable() {
        String createTicketTable = "CREATE TABLE Ticket ("
                + "ticket_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                + "identification_number VARCHAR(20) NOT NULL, "
                + "department_code VARCHAR(10) NOT NULL, "
                + "ticket_number VARCHAR(10) NOT NULL, "
                + "triage_level VARCHAR(20) NOT NULL, "
                + "queue_status VARCHAR(20) NOT NULL, "
                + "issue_date DATE NOT NULL, "
                + "issue_time TIME NOT NULL, "
                + "FOREIGN KEY (identification_number) REFERENCES Patient(identification_number))";

        try (Connection conn = QueueLessDBConnection.sqlConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTicketTable);
        } catch (SQLException e) {
            System.out.println("Error creating Ticket table: " + e.getMessage());
        }
    }
    
    public static int insertTicket(Ticket ticket, String identificationNumber) {
        String sql = "INSERT INTO Ticket (ticket_number, identification_number, department_code, triage_level, queue_status, issue_date, issue_time) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = QueueLessDBConnection.sqlConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ticket.getTicketNumber());
            stmt.setString(2, identificationNumber);
            stmt.setString(3, ticket.getDepartment());
            if (ticket.getTriageLevel() == null) {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(4, ticket.getTriageLevel());
            }
            stmt.setString(5, ticket.getQueueStatus());
            stmt.setDate(6, Date.valueOf(ticket.getIssueDate()));
            stmt.setTime(7, Time.valueOf(ticket.getIssueTime()));
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Error inserting ticket: " + e.getMessage());
            return -1;
        }
    }

    public static int getHighestTicketNumberSuffix() {
        String sql = "SELECT ticket_number FROM Ticket WHERE issue_date = CURRENT_DATE";
        int highest = 0;
        try (Connection conn = QueueLessDBConnection.sqlConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String ticketNumber = rs.getString("ticket_number");
                String digitsOnly = ticketNumber.replaceAll("[^0-9]", "");
                if (!digitsOnly.isEmpty()) {
                    int suffix = Integer.parseInt(digitsOnly);
                    if (suffix > highest) {
                        highest = suffix;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading highest ticket number: " + e.getMessage());
        }
        return highest;
    }
    
    public static int clearAllActiveTickets() {
        String sql = "UPDATE Ticket SET queue_status = 'Cleared' WHERE queue_status IN ('Waiting', 'Called', 'Held')";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error clearing queue: " + e.getMessage());
            return -1;
        }
    }

    public static boolean updateStatus(String ticketNumber, String newStatus) {
        String sql = "UPDATE Ticket SET queue_status = ? WHERE ticket_number = ?";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, ticketNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating ticket status: " + e.getMessage());
            return false;
        }
    }
    
    public static ArrayList<Ticket> getTodaysTickets() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT ticket_id, ticket_number, department_code, triage_level, queue_status, issue_date, issue_time "
                + "FROM Ticket WHERE issue_date = CURRENT_DATE";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getString("ticket_number"),
                        rs.getString("department_code"),
                        rs.getString("triage_level"),
                        rs.getString("queue_status"),
                        rs.getDate("issue_date").toString(),
                        rs.getTime("issue_time").toString()
                );
                tickets.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching today's tickets: " + e.getMessage());
        }
        return tickets;
    }

    public static ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT ticket_id, ticket_number, department_code, triage_level, queue_status, issue_date, issue_time FROM Ticket";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getString("ticket_number"),
                        rs.getString("department_code"),
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

    public static ArrayList<Ticket> findAllTicketsByIdentificationNumber(String identificationNumber) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT ticket_id, ticket_number, department_code, triage_level, queue_status, issue_date, issue_time "
                + "FROM Ticket WHERE identification_number = ? "
                + "ORDER BY issue_date DESC, issue_time DESC";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identificationNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new Ticket(
                            rs.getInt("ticket_id"),
                            rs.getString("ticket_number"),
                            rs.getString("department_code"),
                            rs.getString("triage_level"),
                            rs.getString("queue_status"),
                            rs.getDate("issue_date").toString(),
                            rs.getTime("issue_time").toString()
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding tickets for patient: " + e.getMessage());
        }
        return tickets;
    }

    public static Ticket findActiveTicketByIdentificationNumber(String identificationNumber) {
        String sql = "SELECT ticket_id, ticket_number, department_code, triage_level, queue_status, issue_date, issue_time "
                + "FROM Ticket WHERE identification_number = ? AND issue_date = CURRENT_DATE "
                + "AND queue_status IN ('Waiting', 'Called', 'Held') "
                + "ORDER BY issue_time DESC";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identificationNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("ticket_id"),
                            rs.getString("ticket_number"),
                            rs.getString("department_code"),
                            rs.getString("triage_level"),
                            rs.getString("queue_status"),
                            rs.getDate("issue_date").toString(),
                            rs.getTime("issue_time").toString()
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding active ticket for patient: " + e.getMessage());
        }
        return null;
    }

    public static Ticket findByTicketNumber(String ticketNumber) {
        String sql = "SELECT ticket_id, ticket_number, department_code, triage_level, queue_status, issue_date, issue_time "
                + "FROM Ticket WHERE ticket_number = ? AND issue_date = CURRENT_DATE";
        try (Connection conn = QueueLessDBConnection.sqlConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("ticket_id"),
                            rs.getString("ticket_number"),
                            rs.getString("department_code"),
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
