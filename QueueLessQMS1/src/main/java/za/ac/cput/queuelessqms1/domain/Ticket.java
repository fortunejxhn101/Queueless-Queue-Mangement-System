package za.ac.cput.queuelessqms1.domain;
/**
 *
 * @author Mihlali Tyawana
 */
public class Ticket {
    private int ticketId;
    private String ticketNumber;
    private String department;
    private String triageLevel;
    private String queueStatus;
    private String issueDate;
    private String issueTime;

    public Ticket(int ticketId, String ticketNumber, String department, String triageLevel, String queueStatus, String issueDate, String issueTime) {
        this.ticketId = ticketId;
        this.ticketNumber = ticketNumber;
        this.department = department;
        this.triageLevel = triageLevel;
        this.queueStatus = queueStatus;
        this.issueDate = issueDate;
        this.issueTime = issueTime;
    }

    public int getTicketId() { return ticketId; }
    public String getTicketNumber() { return ticketNumber; }
    public String getDepartment() { return department; }
    public String getTriageLevel() { return triageLevel; }
    public String getQueueStatus() { return queueStatus; }
    public void setQueueStatus(String queueStatus) { this.queueStatus = queueStatus; }
    public String getIssueDate() { return issueDate; }
    public String getIssueTime() { return issueTime; }
}