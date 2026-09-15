package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
import za.ac.cput.queuelessqms1.domain.CurrentPatient;

/**
 * @author Tyawana M
 */
public class GenerateTicketPanel extends JPanel {

    private JLabel lblInstruction, lblHelper;
    private JButton btnGenerate;
    private GridBagConstraints gbc;
    private JPanel pnlContent;

    private final Color darkBlue = new Color(47, 62, 92);
    private final Color greenAccent = new Color(103, 172, 118);
    private final Color white = Color.WHITE;
    private final Color lightGrey = new Color(225, 225, 225);
    private final Color mutedGray = new Color(140, 145, 155);
    private final Color borderGrey = new Color(220, 222, 226);

    public GenerateTicketPanel() {
        setBackground(white);
        setLayout(new BorderLayout());

        pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(white);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 40, 25, 40);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblInstruction = new JLabel("<html><div style='text-align:center;font-family:Inter;font-size:16px'>"
                + "Please press the button below to<br>generate your queue ticket.</div></html>",
                SwingConstants.CENTER);
        lblInstruction.setForeground(darkBlue);
        gbc.gridy = 0;
        pnlContent.add(lblInstruction, gbc);

        btnGenerate = new RoundedButton("Generate Ticket");
        btnGenerate.setFont(new Font("Inter", Font.PLAIN, 20));
        btnGenerate.setPreferredSize(new Dimension(320, 60));
        btnGenerate.setBackground(darkBlue);
        btnGenerate.setForeground(lightGrey);
        btnGenerate.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        btnGenerate.setFocusPainted(false);
        gbc.gridy = 1;
        pnlContent.add(btnGenerate, gbc);

        lblHelper = new JLabel("<html><div style='text-align:center;width:320px;font-family:Inter;font-size:13px'>"
                + "Tickets reset daily. Contact reception if reprint is needed.</html>",
                SwingConstants.CENTER);
        lblHelper.setForeground(mutedGray);
        gbc.gridy = 2;
        pnlContent.add(lblHelper, gbc);

        add(pnlContent, BorderLayout.CENTER);

        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateTicket();
            }
        });
    }

    private void generateTicket() {
        
        if (!CurrentPatient.hasPatient()) {
            JOptionPane.showMessageDialog(this,
                    "No patient is currently ready for a ticket. Please visit the Patient "
                    + "Registration screen first: register a new patient, or find and update "
                    + "an existing one, then return here to generate their ticket.",
                    "No Patient Selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String departmentName = CurrentPatient.getDepartment();
        String departmentCode = departmentName;

        if (departmentCode == null) {
            JOptionPane.showMessageDialog(this,
                    "The selected department (\"" + departmentName + "\") could not be matched "
                    + "to a department record in the database. Please contact an administrator.",
                    "Department Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ticket newTicket = TicketQueue.generateTicket(
                departmentCode,
                CurrentPatient.getTriageLevel(),
                CurrentPatient.getIdentificationNumber());

        if (newTicket == null) {
            JOptionPane.showMessageDialog(this,
                    "The ticket could not be saved to the database. Please try again, "
                    + "or contact support if the problem continues.",
                    "Ticket Not Generated",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        showTicketGeneratedDialog(newTicket.getTicketNumber());

        CurrentPatient.clear();
    }

    private void showTicketGeneratedDialog(String ticketNumber) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner);
        dialog.setModal(true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(white);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.setColor(borderGrey);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(440, 380));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 32, 40));

        JLabel checkIconLbl = new JLabel(new CheckCircleIcon(64));
        checkIconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel eyebrowLbl = new JLabel(letterSpace("Ticket Generated"));
        eyebrowLbl.setFont(new Font("Inter", Font.BOLD, 13));
        eyebrowLbl.setForeground(greenAccent);
        eyebrowLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ticketNumberLbl = new JLabel(ticketNumber);
        ticketNumberLbl.setFont(new Font("Inter", Font.BOLD, 56));
        ticketNumberLbl.setForeground(darkBlue);
        ticketNumberLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("Please wait to be called");
        subLbl.setFont(new Font("Inter", Font.PLAIN, 15));
        subLbl.setForeground(mutedGray);
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton okBtn = new RoundedButton("OK");
        okBtn.setFont(new Font("Inter", Font.BOLD, 14));
        okBtn.setBackground(darkBlue);
        okBtn.setForeground(Color.WHITE);
        okBtn.setPreferredSize(new Dimension(140, 46));
        okBtn.setMaximumSize(new Dimension(140, 46));
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.addActionListener(e -> dialog.dispose());

        content.add(checkIconLbl);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(eyebrowLbl);
        content.add(Box.createRigidArea(new Dimension(0, 14)));
        content.add(ticketNumberLbl);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(subLbl);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(okBtn);

        card.add(content, BorderLayout.CENTER);

        dialog.setContentPane(card);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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

    private class CheckCircleIcon implements Icon {

        private final int size;

        CheckCircleIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(greenAccent);
            g2.fillOval(x, y, size, size);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = x + size / 2;
            int cy = y + size / 2;
            g2.drawLine(cx - size / 4, cy, cx - size / 12, cy + size / 5);
            g2.drawLine(cx - size / 12, cy + size / 5, cx + size / 3, cy - size / 5);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static class RoundedButton extends JButton {

        private final int cornerRadius = 20;

        RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
