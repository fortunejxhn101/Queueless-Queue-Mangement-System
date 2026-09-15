package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
import za.ac.cput.queuelessqms1.domain.QueueAction;
import za.ac.cput.queuelessqms1.domain.QueueUpdateListener;
import za.ac.cput.queuelessqms1.domain.CurrentSession;
import za.ac.cput.queuelessqms1.domain.Staff;
import za.ac.cput.queuelessqms1.dao.TicketDAO;

/**
 * @author admin
 */
public class StaffDashBoardPanel extends JPanel implements QueueUpdateListener {

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color AMBER_ACCENT = new Color(217, 158, 54);
    private static final Color RED_ACCENT = new Color(196, 74, 74);
    private static final Color BLUE_ACCENT = new Color(90, 120, 176);
    private static final Color BORDER_COLOR = new Color(224, 227, 232);
    private static final Color MUTED_GRAY = new Color(148, 156, 168);
    private static final Color SUBTITLE_GRAY = new Color(107, 114, 128);
    private static final Color LINK_BLUE = new Color(59, 95, 196);

    private JLabel currentTicketLbl;
    private JLabel departmentLbl;
    private JLabel nextTicketValueLbl;
    private JLabel heldCountLbl;

    public StaffDashBoardPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JPanel queueCard = buildQueueManagementCard();
        queueCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel controlsCard = buildControlsCard();
        controlsCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        column.add(queueCard);
        column.add(Box.createRigidArea(new Dimension(0, 24)));
        column.add(controlsCard);

        add(column, new GridBagConstraints());

        TicketQueue.addListener(this);
        refresh();
    }

    @Override
    public void onQueueUpdated() {
        refresh();
    }

    private void refresh() {
        Ticket current = findFirstByStatus(TicketQueue.getNowServing(), null);
        Ticket nextWaiting = findOldestByStatus("Waiting");
        int heldCount = countByStatus("Held");

        if (current != null) {
            currentTicketLbl.setText(current.getTicketNumber());
            departmentLbl.setText(resolveDepartmentDisplayName(current.getDepartment()));
        } else {
            currentTicketLbl.setText("\u2014");
            departmentLbl.setText("No ticket currently being served");
        }

        nextTicketValueLbl.setText(nextWaiting != null ? nextWaiting.getTicketNumber() : "\u2014");
        heldCountLbl.setText("On Hold: " + heldCount);
    }

    private String resolveDepartmentDisplayName(String departmentCode) {
        
        return departmentCode;
    }

    private JPanel buildQueueManagementCard() {
        RoundedPanel card = new RoundedPanel(Color.WHITE, BORDER_COLOR, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32, 56, 32, 56));

        JLabel eyebrowLbl = new JLabel(letterSpace("Queue Management"));
        eyebrowLbl.setFont(new Font("Inter", Font.BOLD, 12));
        eyebrowLbl.setForeground(MUTED_GRAY);
        eyebrowLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel servingLbl = new JLabel(letterSpace("Currently Serving"));
        servingLbl.setFont(new Font("Inter", Font.PLAIN, 13));
        servingLbl.setForeground(MUTED_GRAY);
        servingLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentTicketLbl = new JLabel("\u2014");
        currentTicketLbl.setFont(new Font("Inter", Font.BOLD, 56));
        currentTicketLbl.setForeground(Color.BLACK);
        currentTicketLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        departmentLbl = new JLabel(" ");
        departmentLbl.setFont(new Font("Inter", Font.PLAIN, 16));
        departmentLbl.setForeground(SUBTITLE_GRAY);
        departmentLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nextRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        nextRow.setOpaque(false);
        nextRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nextLabelLbl = new JLabel("Next ticket:");
        nextLabelLbl.setFont(new Font("Inter", Font.PLAIN, 15));
        nextLabelLbl.setForeground(Color.DARK_GRAY);

        nextTicketValueLbl = new JLabel("\u2014");
        nextTicketValueLbl.setFont(new Font("Inter", Font.BOLD, 15));
        nextTicketValueLbl.setForeground(LINK_BLUE);

        nextRow.add(nextLabelLbl);
        nextRow.add(nextTicketValueLbl);

        card.add(eyebrowLbl);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(servingLbl);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(currentTicketLbl);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(departmentLbl);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(nextRow);

        return card;
    }

    private JPanel buildControlsCard() {
        RoundedPanel card = new RoundedPanel(ACCENT_COLOR, null, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(24, 32, 28, 32));

        JLabel eyebrowLbl = new JLabel(letterSpace("Controls"));
        eyebrowLbl.setFont(new Font("Inter", Font.BOLD, 12));
        eyebrowLbl.setForeground(MUTED_GRAY);
        eyebrowLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        ControlButton callNextBtn = new ControlButton("Call Next", GREEN_ACCENT);
        callNextBtn.addActionListener(this::handleCallNext);

        ControlButton holdBtn = new ControlButton("Hold", AMBER_ACCENT);
        holdBtn.addActionListener(this::handleHold);

        ControlButton skipBtn = new ControlButton("Skip", RED_ACCENT);
        skipBtn.addActionListener(this::handleSkip);

        ControlButton completeBtn = new ControlButton("Complete", BLUE_ACCENT);
        completeBtn.addActionListener(this::handleComplete);

        buttonRow.add(callNextBtn);
        buttonRow.add(holdBtn);
        buttonRow.add(skipBtn);
        buttonRow.add(completeBtn);

        JPanel divider = new JPanel();
        divider.setOpaque(true);
        divider.setBackground(new Color(255, 255, 255, 30));
        divider.setPreferredSize(new Dimension(1, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel holdRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        holdRow.setOpaque(false);
        holdRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        heldCountLbl = new JLabel("On Hold: 0");
        heldCountLbl.setFont(new Font("Inter", Font.PLAIN, 13));
        heldCountLbl.setForeground(MUTED_GRAY);

        ControlButton releaseHoldBtn = new ControlButton("Release Next Hold", MUTED_GRAY);
        releaseHoldBtn.addActionListener(this::handleReleaseHold);

        holdRow.add(heldCountLbl);
        holdRow.add(releaseHoldBtn);

        JPanel dividerTwo = new JPanel();
        dividerTwo.setOpaque(true);
        dividerTwo.setBackground(new Color(255, 255, 255, 30));
        dividerTwo.setPreferredSize(new Dimension(1, 1));
        dividerTwo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        dividerTwo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel clearRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        clearRow.setOpaque(false);
        clearRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        ControlButton clearQueueBtn = new ControlButton("Clear Queue", RED_ACCENT.darker());
        clearQueueBtn.addActionListener(this::handleClearQueue);
        clearRow.add(clearQueueBtn);

        card.add(eyebrowLbl);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(buttonRow);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(divider);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(holdRow);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(dividerTwo);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(clearRow);

        return card;
    }

    private void handleCallNext(ActionEvent e) {
        Ticket next = findOldestByStatus("Waiting");
        if (next == null) {
            showMessage("No Tickets Waiting", "There are no waiting tickets to call.");
            return;
        }
        callTicket(next);
        refresh();
    }

    private void handleHold(ActionEvent e) {
        Ticket current = findFirstByStatus(TicketQueue.getNowServing(), null);
        if (current == null) {
            showMessage("No Ticket In Progress", "There is no ticket currently being served to hold.");
            return;
        }

        if (!changeStatus(current, "Held", "Hold")) {
            return;
        }

        autoAdvance();
        refresh();
    }

    private void handleSkip(ActionEvent e) {
        Ticket current = findFirstByStatus(TicketQueue.getNowServing(), null);
        if (current == null) {
            showMessage("No Ticket In Progress", "There is no ticket currently being served to skip.");
            return;
        }

        if (!changeStatus(current, "Skipped", "Skip")) {
            return;
        }

        autoAdvance();
        refresh();
    }

    private void handleComplete(ActionEvent e) {
        Ticket current = findFirstByStatus(TicketQueue.getNowServing(), null);
        if (current == null) {
            showMessage("No Ticket In Progress", "There is no ticket currently being served to complete.");
            return;
        }

        if (!changeStatus(current, "Completed", "Complete")) {
            return;
        }

        autoAdvance();
        refresh();
    }

    private void handleReleaseHold(ActionEvent e) {
        Ticket oldestHeld = findOldestByStatus("Held");
        if (oldestHeld == null) {
            showMessage("No Held Tickets", "There are no held tickets to release.");
            return;
        }
        callTicket(oldestHeld);
        refresh();
    }

    private void handleClearQueue(ActionEvent e) {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "This will clear every currently active ticket from the queue. "
                + "Tickets are not deleted - they remain in the database, but will "
                + "no longer appear here or on the Display Screen. Continue?",
                "Clear Queue",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<Ticket> affectedTickets = TicketQueue.clearActiveQueue();

        for (Ticket t : affectedTickets) {
            logAction(t, "Clear Queue");
        }

        refresh();
    }

    private void autoAdvance() {
        Ticket next = findOldestByStatus("Waiting");
        if (next != null) {
            callTicket(next);
        }
    }

    private void callTicket(Ticket ticket) {
        changeStatus(ticket, "Called", "Call Next");
    }

    private boolean changeStatus(Ticket ticket, String newStatus, String actionType) {
        boolean updated = TicketDAO.updateStatus(ticket.getTicketNumber(), newStatus);
        if (!updated) {
            showMessage("Update Failed", "Something went wrong while updating the ticket. Please try again.");
            return false;
        }

        logAction(ticket, actionType);
        TicketQueue.loadFromDatabase();
        return true;
    }

    private void logAction(Ticket ticket, String actionType) {
        int staffId = resolveCurrentStaffId();
        if (staffId == -1) {
            
            showMessage("Audit Log Not Recorded",
                    "The queue action succeeded, but could not be logged to Queue_Action "
                    + "because the current staff member's ID could not be resolved.");
            return;
        }
    }

    private int resolveCurrentStaffId() {
        
        return -1;
    }

    private Ticket findFirstByStatus(ArrayList<Ticket> tickets, String status) {
        for (Ticket t : tickets) {
            if (status == null || t.getQueueStatus().equals(status)) {
                return t;
            }
        }
        return null;
    }

    private Ticket findOldestByStatus(String status) {
        Ticket oldest = null;
        for (Ticket t : TicketQueue.getWaitingTickets()) {
            if (!t.getQueueStatus().equals(status)) {
                continue;
            }
            if (oldest == null || isOlder(t, oldest)) {
                oldest = t;
            }
        }
        return oldest;
    }

    private int countByStatus(String status) {
        int count = 0;
        for (Ticket t : TicketQueue.getWaitingTickets()) {
            if (t.getQueueStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    private boolean isOlder(Ticket a, Ticket b) {
        int dateCompare = a.getIssueDate().compareTo(b.getIssueDate());
        if (dateCompare != 0) {
            return dateCompare < 0;
        }
        return a.getIssueTime().compareTo(b.getIssueTime()) < 0;
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private static String letterSpace(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(Character.toUpperCase(c));
            if (c != ' ') {
                sb.append('\u2009');
            }
        }
        return sb.toString().trim();
    }

    private static class RoundedPanel extends JPanel {

        private final Color fillColor;
        private final Color borderColor;
        private final int cornerRadius;

        RoundedPanel(Color fillColor, Color borderColor, int cornerRadius) {
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ControlButton extends JButton {

        private final Color baseColor;
        private boolean hovered = false;

        ControlButton(String text, Color baseColor) {
            super(text);
            this.baseColor = baseColor;
            setFont(new Font("Inter", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? baseColor.darker() : baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
