package za.ac.cput.queuelessqms.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
/**
 *
 * @author Tyawana M
 */
public class GenerateTicketUI extends JFrame {
    private JLabel lblInstruction, lblHelper;
    private JButton btnGenerate;
    private GridBagConstraints gbc;
    private JPanel pnlHeader, pnlFooter, pnlContent;

    private final Color darkBlue = new Color(47, 62, 92);
    private final Color white = Color.WHITE;
    private final Color lightGrey = new Color(225, 225, 225);
    private final Color mutedGray = new Color(140, 145, 155);
    private final Color borderGrey = new Color(220, 222, 226);

    public GenerateTicketUI() {
        super("Generate Queue Ticket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(white);
        setLayout(new BorderLayout());

        pnlHeader = new JPanel();
        pnlHeader.setBackground(darkBlue);
        pnlHeader.setPreferredSize(new Dimension(0, 8));
        add(pnlHeader, BorderLayout.NORTH);

        pnlFooter = new JPanel();
        pnlFooter.setBackground(borderGrey);
        pnlFooter.setPreferredSize(new Dimension(0, 8));
        add(pnlFooter, BorderLayout.SOUTH);

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

        setVisible(true);
    }

    private void generateTicket() {
       Ticket newTicket = TicketQueue.generateTicket("General", null);

        JOptionPane.showMessageDialog(this,
                "Your ticket number: " + newTicket.getTicketNumber()
                + "\nPlease wait to be called.",
                "Ticket Generated",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DisplayScreenUI();
            new GenerateTicketUI();
        });
    }
}

class RoundedButton extends JButton {
    private int cornerRadius = 20;

    public RoundedButton(String text) {
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