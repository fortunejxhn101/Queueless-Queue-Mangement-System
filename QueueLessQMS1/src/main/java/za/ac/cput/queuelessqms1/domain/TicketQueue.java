package za.ac.cput.queuelessqms1.domain;

import za.ac.cput.queuelessqms1.dao.TicketDAO;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Mihlali Tyawana
 */
public class TicketQueue {

    private static ArrayList<Ticket> allTickets = new ArrayList<>();
    private static ArrayList<QueueUpdateListener> listeners = new ArrayList<>();
    private static int ticketIdCounter = 0;
    private static final String PREFIX = "A";

    public static void addListener(QueueUpdateListener listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onQueueUpdated();
        }
    }

    public static synchronized Ticket generateTicket(String department, String triageLevel) {
        ticketIdCounter++;
        String ticketNumber = PREFIX + String.format("%03d", ticketIdCounter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        Ticket ticket = new Ticket(ticketIdCounter, ticketNumber, department, triageLevel,
                "Waiting", dateFormat.format(now), timeFormat.format(now));
        addTicket(ticket);
        return ticket;
    }

    public static void addTicket(Ticket ticket) {
        allTickets.add(ticket);
        TicketDAO.insertTicket(ticket);
        notifyListeners();
    }

    public static ArrayList<Ticket> getAllTickets() {
        return allTickets;
    }

    public static ArrayList<Ticket> getNowServing() {
        ArrayList<Ticket> result = new ArrayList<>();
        for (int i = 0; i < allTickets.size(); i++) {
            if (allTickets.get(i).getQueueStatus().equals("Called")) {
                result.add(allTickets.get(i));
            }
        }
        return result;
    }

    public static ArrayList<Ticket> getWaitingTickets() {
        ArrayList<Ticket> result = new ArrayList<>();
        for (int i = 0; i < allTickets.size(); i++) {
            Ticket t = allTickets.get(i);
            if (t.getQueueStatus().equals("Waiting") || t.getQueueStatus().equals("Held")) {
                result.add(t);
            }
        }
        return result;
    }

    public static void loadFromDatabase() {
        allTickets.clear();
        allTickets.addAll(TicketDAO.getAllTickets());
        notifyListeners();
    }

}
