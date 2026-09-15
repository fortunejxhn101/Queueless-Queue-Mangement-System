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

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);

    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private ContentTransition.ContentHost contentHost;

    private VisitScreenPanel visitScreenPanel;

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

        headerPanel = new HeaderPanel();

        sidebarPanel = new SidebarPanel(this::navigateTo, SidebarPanel.visitRecordsNavItems());
        contentHost = new ContentTransition.ContentHost();

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentHost, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> navigateTo(SidebarPanel.VISIT_SCREEN));
    }

    public void navigateTo(String screenKey) {
        
        if (SidebarPanel.BACK_TO_DASHBOARD.equals(screenKey)) {
            ScreenTransition.navigateTo(this, DashboardGUI::createConfigured);
            return;
        }

        JComponent content = resolveContent(screenKey);
        if (content == null) {
            return;
        }

        ContentTransition.crossfadeTo(contentHost, content);
        sidebarPanel.setActiveScreen(screenKey);
    }

    private JComponent resolveContent(String screenKey) {
        switch (screenKey) {
            case SidebarPanel.VISIT_SCREEN:
                if (visitScreenPanel == null) {
                    visitScreenPanel = new VisitScreenPanel();
                }
                return visitScreenPanel;

            default:
                return placeholderPanel(screenKey);
        }
    }

    private JComponent placeholderPanel(String screenKey) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("\"" + screenKey + "\" screen not built yet");
        label.setFont(new Font("Inter", Font.PLAIN, 16));
        label.setForeground(new Color(148, 156, 168));
        panel.add(label);

        return panel;
    }
}
