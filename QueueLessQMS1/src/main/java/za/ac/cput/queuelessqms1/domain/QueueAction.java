package za.ac.cput.queuelessqms1.domain;

import java.sql.Timestamp;

/**
 * @author admin
 */
public class QueueAction {

    private int actionId;
    private int ticketId;
    private int staffId;
    private String actionType;
    private Timestamp timeStamp;

    public QueueAction(int ticketId, int staffId, String actionType, Timestamp timeStamp) {
        this.ticketId = ticketId;
        this.staffId = staffId;
        this.actionType = actionType;
        this.timeStamp = timeStamp;
    }

    public QueueAction(int actionId, int ticketId, int staffId, String actionType, Timestamp timeStamp) {
        this.actionId = actionId;
        this.ticketId = ticketId;
        this.staffId = staffId;
        this.actionType = actionType;
        this.timeStamp = timeStamp;
    }

    public int getActionId() {
        return actionId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getActionType() {
        return actionType;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}
