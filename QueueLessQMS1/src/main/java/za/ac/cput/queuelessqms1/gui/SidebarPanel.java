package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import za.ac.cput.queuelessqms1.domain.CurrentSession;
import za.ac.cput.queuelessqms1.domain.Staff;

/**
 * @author admin Fortune 
 */
public class SidebarPanel extends JPanel {

    public static final String HOME = "home";
    public static final String GENERATE_TICKET = "generateTicket";
    public static final String PATIENT_REGISTRATION = "patientRegistration";
    public static final String DISPLAY_SCREEN = "displayScreen";
    public static final String STAFF_DASHBOARD = "staffDashboard";
    public static final String VISIT_SCREEN = "visitScreen";
    public static final String BACK_TO_DASHBOARD = "backToDashboard";

    private static final Font NAV_FONT = new Font("Inter", Font.PLAIN, 17);
    private static final Font NAV_FONT_ACTIVE = new Font("Inter", Font.BOLD, 17);
    private static final Font LOGO_FONT = new Font("Inter", Font.BOLD, 22);

    private static final int SIDEBAR_WIDTH = 320;
    private static final int SIDEBAR_LEFT_MARGIN = 32;
    private static final int NAV_ROW_VERTICAL_PADDING = 18;
    private static final int NAV_ROW_HEIGHT = 52;
    private static final int NAV_INDICATOR_STRIP_WIDTH = 4;

    private static final int LOGO_ICON_SIZE = 36;
    private static final int LOGO_ICON_TEXT_GAP = 12;
    
    private static final int LOGO_LEFT_BORDER = SIDEBAR_LEFT_MARGIN;
    private static final int NAV_TEXT_LEFT_INSET
            = (LOGO_LEFT_BORDER + LOGO_ICON_SIZE + LOGO_ICON_TEXT_GAP) - NAV_INDICATOR_STRIP_WIDTH;

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color ACTIVE_ROW_COLOR = new Color(60, 80, 115);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);

    private final Map<String, JLabel> navLabelsByKey = new LinkedHashMap<>();
    private String activeKey;

    public static LinkedHashMap<String, String> queueOperationsNavItems() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        items.put(PATIENT_REGISTRATION, "Patient Registration");
        items.put(GENERATE_TICKET, "Generate Ticket");
        items.put(DISPLAY_SCREEN, "Display Screen");
        items.put(STAFF_DASHBOARD, "Staff Dashboard");
        items.put(BACK_TO_DASHBOARD, "\u2190 Back");
        return items;
    }

    public static LinkedHashMap<String, String> visitRecordsNavItems() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        items.put(VISIT_SCREEN, "Visit Screen");
        items.put(BACK_TO_DASHBOARD, "\u2190 Back");
        return items;
    }

    public SidebarPanel(Consumer<String> onNavigate) {
        this(onNavigate, queueOperationsNavItems());
    }

    public SidebarPanel(Consumer<String> onNavigate, LinkedHashMap<String, String> navItems) {
        setLayout(new BorderLayout());
        setBackground(ACCENT_COLOR);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(ACCENT_COLOR);

        JLabel logoLbl = new JLabel("QueueLess");
        logoLbl.setFont(LOGO_FONT);
        logoLbl.setForeground(Color.WHITE);
        logoLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        java.net.URL logoUrl = getClass().getResource("/images/QueueLessLogo.png");
        if (logoUrl != null) {
            Image scaledLogo = new ImageIcon(logoUrl).getImage()
                    .getScaledInstance(LOGO_ICON_SIZE, LOGO_ICON_SIZE, Image.SCALE_SMOOTH);
            logoLbl.setIcon(new ImageIcon(scaledLogo));
        } else {
            logoLbl.setIcon(new LogoMarkIcon(LOGO_ICON_SIZE));
        }
        logoLbl.setIconTextGap(LOGO_ICON_TEXT_GAP);
        logoLbl.setBorder(BorderFactory.createEmptyBorder(15, LOGO_LEFT_BORDER, 28, 16));

        topPanel.add(logoLbl);

        for (Map.Entry<String, String> entry : navItems.entrySet()) {
            addNavRow(topPanel, entry.getKey(), entry.getValue(), onNavigate);
        }

        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(ACCENT_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(14, SIDEBAR_LEFT_MARGIN, 20, 10));

        Staff loggedInStaff = CurrentSession.getLoggedInStaff();
        String displayName = loggedInStaff != null
                ? loggedInStaff.getFirstName() + " " + loggedInStaff.getLastName()
                : "Staff Member";

        JLabel staffNameLbl = new JLabel(displayName);
        staffNameLbl.setFont(new Font("Inter", Font.BOLD, 16));
        staffNameLbl.setForeground(Color.WHITE);
        staffNameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel staffRoleLbl = new JLabel("Admin");
        staffRoleLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        staffRoleLbl.setForeground(GREEN_ACCENT);
        staffRoleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottomPanel.add(staffNameLbl);
        bottomPanel.add(staffRoleLbl);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addNavRow(JPanel container, String key, String text, Consumer<String> onNavigate) {
        JLabel navLbl = new JLabel(text);
        navLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLbl.setOpaque(true);
        navLbl.setMaximumSize(new Dimension(SIDEBAR_WIDTH, NAV_ROW_HEIGHT));

        navLabelsByKey.put(key, navLbl);
        applyInactiveStyle(navLbl);

        navLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!key.equals(activeKey)) {
                    onNavigate.accept(key);
                }
            }
        });

        container.add(navLbl);
    }

    public void setActiveScreen(String key) {
        if (activeKey != null) {
            JLabel previous = navLabelsByKey.get(activeKey);
            if (previous != null) {
                applyInactiveStyle(previous);
            }
        }

        activeKey = key;
        JLabel active = navLabelsByKey.get(key);
        if (active != null) {
            applyActiveStyle(active);
        }
    }

    private void applyInactiveStyle(JLabel navLbl) {
        navLbl.setFont(NAV_FONT);
        navLbl.setForeground(Color.LIGHT_GRAY);
        navLbl.setBackground(ACCENT_COLOR);
        navLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, NAV_INDICATOR_STRIP_WIDTH, 0, 0, ACCENT_COLOR),
                BorderFactory.createEmptyBorder(NAV_ROW_VERTICAL_PADDING, NAV_TEXT_LEFT_INSET, NAV_ROW_VERTICAL_PADDING, 16)));
    }

    private void applyActiveStyle(JLabel navLbl) {
        navLbl.setFont(NAV_FONT_ACTIVE);
        navLbl.setForeground(Color.WHITE);
        navLbl.setBackground(ACTIVE_ROW_COLOR);
        navLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, NAV_INDICATOR_STRIP_WIDTH, 0, 0, GREEN_ACCENT),
                BorderFactory.createEmptyBorder(NAV_ROW_VERTICAL_PADDING, NAV_TEXT_LEFT_INSET, NAV_ROW_VERTICAL_PADDING, 16)));
    }

    private static class LogoMarkIcon implements Icon {

        private final int size;

        LogoMarkIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(GREEN_ACCENT);
            g2.fillRoundRect(x, y, size, size, 6, 6);
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
}
