package za.ac.cput.queuelessqms1.domain;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author admin Fortune
 */
public class Visit {

    private int visitId;
    private int ticketId;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String notes;

    public Visit(int ticketId, LocalDate visitDate, LocalTime visitTime, String notes) {
        this.ticketId = ticketId;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.notes = notes;
    }

    public Visit(int visitId, int ticketId, LocalDate visitDate, LocalTime visitTime, String notes) {
        this.visitId = visitId;
        this.ticketId = ticketId;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.notes = notes;
    }

    public int getVisitId() {
        return visitId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public LocalTime getVisitTime() {
        return visitTime;
    }

    public String getNotes() {
        return notes;
    }
}
