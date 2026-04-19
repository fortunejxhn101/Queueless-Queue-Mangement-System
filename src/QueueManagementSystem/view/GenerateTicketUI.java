package QueueManagementSystem.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Tyawana M
 */
public class GenerateTicketUI extends JFrame {

    private JLabel lblInstruction, lblHelper;
    private JButton btnGenerate;
    private GridBagConstraints gbc;
    private JPanel pnlOuter, pnlInner;

    private static int ticketCounter = 0;
    private static final String PREFIX = "N";

    public GenerateTicketUI() {
        super("Generate Queue Ticket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        Color navy = new Color(28, 48, 95);
        Color green = new Color(58, 99, 71);
        Color softGreen = new Color(235, 242, 237);

        pnlOuter = new JPanel(new BorderLayout());
        pnlOuter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(navy, 2),
                "Generate Queue Ticket",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Monospaced", Font.BOLD, 14),
                navy));
        pnlOuter.setBackground(Color.WHITE);

        pnlInner = new JPanel(new GridBagLayout());
        pnlInner.setBackground(softGreen);                        // whisper of green
        pnlInner.setBorder(BorderFactory.createDashedBorder(green, 4, 4));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblInstruction = new JLabel("<html><div style='text-align:center;font-family:monospace;font-size:12px'>"
                + "Please press the button below to<br>generate your queue ticket</div></html>",
                SwingConstants.CENTER);
        lblInstruction.setForeground(navy);
        gbc.gridy = 0;
        pnlInner.add(lblInstruction, gbc);

        btnGenerate = new JButton("Issue Ticket");
        btnGenerate.setFont(new Font("Monospaced", Font.PLAIN, 13));
        btnGenerate.setPreferredSize(new Dimension(220, 40));
        btnGenerate.setBorder(BorderFactory.createLineBorder(green, 2));
        btnGenerate.setBackground(Color.WHITE);
        btnGenerate.setForeground(green);
        btnGenerate.setFocusPainted(false);
        gbc.gridy = 1;
        pnlInner.add(btnGenerate, gbc);

        lblHelper = new JLabel("<html><div style='text-align:center;font-family:monospace;font-size:12px'>"
                + "Ticket will appear in a<br>pop-up message box</div></html>",
                SwingConstants.CENTER);
        lblHelper.setForeground(navy);
        gbc.gridy = 2;
        pnlInner.add(lblHelper, gbc);

        pnlOuter.add(pnlInner, BorderLayout.CENTER);

        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                issueTicket();
            }
        });

        add(pnlOuter);
        setVisible(true);
    }

    private void issueTicket() {
        ticketCounter++;
        String ticketNum = PREFIX + String.format("%03d", ticketCounter);

        JOptionPane.showMessageDialog(this,
                "Your ticket number: " + ticketNum
                + "\nPlease wait to be called.",
                "Ticket Generated",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
