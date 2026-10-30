package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author admin Fortune
 */
public class VisitRecordsFrame extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static final int SIDE_MARGIN = 24;
    private static final int COLUMN_WIDTH = VisitScreenPanel.CONTENT_WIDTH + 2 * SIDE_MARGIN
            + 2 * VisitScreenPanel.SCROLLBAR_WIDTH;

    public VisitRecordsFrame() {
        super("QueueLess: Visit Records");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        JPanel column = new JPanel(new BorderLayout());
        column.setOpaque(false);
        column.setPreferredSize(new Dimension(COLUMN_WIDTH, 100));
        column.add(new VisitScreenPanel(), BorderLayout.CENTER);

        JPanel host = new JPanel(new GridBagLayout());
        host.setBackground(Color.WHITE);
        host.setBorder(BorderFactory.createEmptyBorder(16, SIDE_MARGIN, 0, SIDE_MARGIN));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        host.add(column, gbc);

        add(AccountBar.backOnly(() -> ScreenTransition.navigateTo(this, DashboardGUI::createConfigured)), BorderLayout.NORTH);
        add(host, BorderLayout.CENTER);
    }
}