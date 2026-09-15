package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
import za.ac.cput.queuelessqms1.domain.QueueUpdateListener;

/**
 *
 * @author Tyawana M
 */
public class DisplayScreenPanel extends JPanel implements QueueUpdateListener {

    private final Color darkBlue = new Color(47, 62, 92);
    private final Color green = new Color(72, 165, 122);
    private final Color white = Color.WHITE;
    private final Color borderGrey = new Color(220, 222, 226);
    private final Color headerGrey = new Color(140, 145, 155);

    private static final int NEXT_UP_COUNT = 2;
    private JPanel pnlMain;

    public DisplayScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(white);

        pnlMain = new JPanel(new GridLayout(1, 3, 25, 0));
        pnlMain.setBackground(white);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(pnlMain, BorderLayout.CENTER);

        TicketQueue.addListener(this);
        refresh();
    }

    @Override
    public void onQueueUpdated() {
        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        pnlMain.removeAll();

        ArrayList<Ticket> nowServing = TicketQueue.getNowServing();
        ArrayList<Ticket> waiting = TicketQueue.getWaitingTickets();

        ArrayList<Ticket> nextUp = new ArrayList<>();
        ArrayList<Ticket> waitingQueue = new ArrayList<>();
        for (int i = 0; i < waiting.size(); i++) {
            if (i < NEXT_UP_COUNT) {
                nextUp.add(waiting.get(i));
            } else {
                waitingQueue.add(waiting.get(i));
            }
        }

        pnlMain.add(buildColumn("NOW SERVING", nowServing, darkBlue, white, null, 90));
        pnlMain.add(buildColumn("WAITING QUEUE", waitingQueue, white, darkBlue, borderGrey, 60));
        pnlMain.add(buildColumn("NEXT UP", nextUp, green, white, null, 90));

        pnlMain.revalidate();
        pnlMain.repaint();
    }

    private JPanel buildColumn(String headerText, ArrayList<Ticket> tickets, Color cardColor,
            Color textColor, Color borderColor, int cardHeight) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(white);

        JLabel header = new JLabel(headerText);
        header.setFont(new Font("Inter", Font.BOLD, 13));
        header.setForeground(headerGrey);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 4, 15, 0));
        column.add(header);

        for (int i = 0; i < tickets.size(); i++) {
            RoundedPanel card = new RoundedPanel(cardColor);
            card.setLayout(new GridBagLayout());
            card.setBorderColor(borderColor);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
            card.setPreferredSize(new Dimension(280, cardHeight));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblTicket = new JLabel(tickets.get(i).getTicketNumber());
            lblTicket.setFont(new Font("Inter", Font.BOLD, 26));
            lblTicket.setForeground(textColor);
            card.add(lblTicket);

            column.add(card);
            column.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        return column;
    }

    private static class RoundedPanel extends JPanel {

        private final int cornerRadius = 12;
        private Color borderColor = null;

        RoundedPanel(Color bgColor) {
            setBackground(bgColor);
            setOpaque(false);
        }

        void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
