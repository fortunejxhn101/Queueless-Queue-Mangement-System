package za.ac.cput.queuelessqms1.domain;
import za.ac.cput.queuelessqms1.dao.TicketDAO;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import za.ac.cput.queuelessqms1.domain.QueueUpdateListener;
import za.ac.cput.queuelessqms1.domain.Ticket;

/**
 *
 * @author Mihlali Tyawana
 */
public class TicketQueue {
    private static ArrayList<Ticket> allTickets = new ArrayList<>();
    private static ArrayList<QueueUpdateListener> listeners = new ArrayList<>();
    private static int ticketIdCounter = 0;
    private static final String PREFIX = "A";
    private static String lastKnownDate = null;
    private static Timer dayRolloverTimer = null;
    private static final int ROLLOVER_CHECK_INTERVAL_MS = 60000;

    public static void addListener(QueueUpdateListener listener) {
        listeners.add(listener);
    }
    private static void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onQueueUpdated();
        }
    }
    public static synchronized Ticket generateTicket(String department, String triageLevel, String identificationNumber) {
        ticketIdCounter++;
        String ticketNumber = PREFIX + String.format("%03d", ticketIdCounter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String issueDate = dateFormat.format(now);
        String issueTime = timeFormat.format(now);

        Ticket placeholder = new Ticket(0, ticketNumber, department, triageLevel, "Waiting", issueDate, issueTime);
        int realTicketId = TicketDAO.insertTicket(placeholder, identificationNumber);

        if (realTicketId == -1) {
            return null;
        }

        Ticket saved = new Ticket(realTicketId, ticketNumber, department, triageLevel, "Waiting", issueDate, issueTime);
        allTickets.add(saved);
        notifyListeners();
        return saved;
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
        allTickets.addAll(TicketDAO.getTodaysTickets());
        notifyListeners();

        ticketIdCounter = TicketDAO.getHighestTicketNumberSuffix();
        lastKnownDate = currentDateString();
    }

    public static ArrayList<Ticket> clearActiveQueue() {
        ArrayList<Ticket> affected = new ArrayList<>();
        for (Ticket t : allTickets) {
            String status = t.getQueueStatus();
            if (status.equals("Waiting") || status.equals("Called") || status.equals("Held")) {
                affected.add(t);
            }
        }

        TicketDAO.clearAllActiveTickets();
        loadFromDatabase();
        return affected;
    }

    public static void startDailyRolloverWatcher() {
        if (dayRolloverTimer != null) {
            return;
        }

        lastKnownDate = currentDateString();

        dayRolloverTimer = new Timer(ROLLOVER_CHECK_INTERVAL_MS, e -> {
            String today = currentDateString();
            if (!today.equals(lastKnownDate)) {
                loadFromDatabase();
            }
        });
        dayRolloverTimer.setRepeats(true);
        dayRolloverTimer.start();
    }

    private static String currentDateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
