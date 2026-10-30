package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
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

    private static final Font NAV_FONT = new Font("Inter", Font.PLAIN, 18);
    private static final Font NAV_FONT_ACTIVE = new Font("Inter", Font.BOLD, 18);
    private static final Font LOGO_FONT = new Font("Inter", Font.BOLD, 22);

    private static final int SIDEBAR_WIDTH = 320;
    private static final int SIDEBAR_LEFT_MARGIN = 32;
    private static final int NAV_ROW_HEIGHT = 60;
    private static final int NAV_INDICATOR_STRIP_WIDTH = 4;

    private static final int LOGO_ICON_SIZE = 36;
    private static final int LOGO_ICON_TEXT_GAP = 12;

    private static final int LOGO_LEFT_BORDER = SIDEBAR_LEFT_MARGIN;
    private static final int NAV_TEXT_LEFT_INSET
            = (LOGO_LEFT_BORDER + LOGO_ICON_SIZE + LOGO_ICON_TEXT_GAP);

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color ACTIVE_ROW_COLOR = new Color(60, 80, 115);
    private static final Color HOVER_ROW_COLOR = new Color(56, 73, 106);
    private static final Color DIVIDER_COLOR = new Color(255, 255, 255, 40);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);

    private final Map<String, NavButton> navButtonsByKey = new LinkedHashMap<>();
    private String activeKey;

    public static LinkedHashMap<String, String> queueOperationsNavItems() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        items.put(PATIENT_REGISTRATION, "Patient Registration");
        items.put(GENERATE_TICKET, "Generate Ticket");
        items.put(DISPLAY_SCREEN, "Display Screen");
        items.put(STAFF_DASHBOARD, "Staff Dashboard");
        items.put(BACK_TO_DASHBOARD, "←  Back to Dashboard");
        return items;
    }

    public static LinkedHashMap<String, String> visitRecordsNavItems() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        items.put(VISIT_SCREEN, "Visit Screen");
        items.put(BACK_TO_DASHBOARD, "←  Back to Dashboard");
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

        String backText = null;
        for (Map.Entry<String, String> entry : navItems.entrySet()) {
            if (BACK_TO_DASHBOARD.equals(entry.getKey())) {
                backText = entry.getValue();
                continue;
            }
            addNavRow(topPanel, entry.getKey(), entry.getValue(), false, onNavigate);
        }

        add(topPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(ACCENT_COLOR);

        if (backText != null) {
            southPanel.add(new DividerLine());
            southPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            addNavRow(southPanel, BACK_TO_DASHBOARD, backText, true, onNavigate);
            southPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            southPanel.add(new DividerLine());
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(ACCENT_COLOR);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(16, SIDEBAR_LEFT_MARGIN, 20, 10));

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

        southPanel.add(bottomPanel);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void addNavRow(JPanel container, String key, String text, boolean secondary,
            Consumer<String> onNavigate) {
        NavButton navBtn = new NavButton(text, secondary,
                secondary ? SIDEBAR_LEFT_MARGIN : NAV_TEXT_LEFT_INSET);
        navBtn.addActionListener(e -> {
            if (!key.equals(activeKey)) {
                onNavigate.accept(key);
            }
        });

        navButtonsByKey.put(key, navBtn);
        container.add(navBtn);
    }

    public void setActiveScreen(String key) {
        if (activeKey != null) {
            NavButton previous = navButtonsByKey.get(activeKey);
            if (previous != null) {
                previous.setActive(false);
            }
        }

        activeKey = key;
        NavButton active = navButtonsByKey.get(key);
        if (active != null) {
            active.setActive(true);
        }
    }

    private static class NavButton extends JButton {

        private final boolean secondary;
        private final int textLeft;
        private boolean hovered = false;
        private boolean active = false;

        NavButton(String text, boolean secondary, int textLeft) {
            super(text);
            this.secondary = secondary;
            this.textLeft = textLeft;

            setAlignmentX(Component.LEFT_ALIGNMENT);
            setHorizontalAlignment(SwingConstants.LEFT);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setFocusable(true);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            Dimension size = new Dimension(SIDEBAR_WIDTH, NAV_ROW_HEIGHT);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, NAV_ROW_HEIGHT));

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

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });

            getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "navActivate");
            getActionMap().put("navActivate", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doClick();
                }
            });
        }

        void setActive(boolean active) {
            this.active = active;
            setCursor(Cursor.getPredefinedCursor(active ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Color rowColor = ACCENT_COLOR;
            if (active) {
                rowColor = ACTIVE_ROW_COLOR;
            } else if (hovered) {
                rowColor = HOVER_ROW_COLOR;
            }
            g2.setColor(rowColor);
            g2.fillRect(0, 0, w, h);

            if (active) {
                g2.setColor(GREEN_ACCENT);
                g2.fillRect(0, 0, NAV_INDICATOR_STRIP_WIDTH, h);
            } else if (hovered) {
                g2.setColor(new Color(GREEN_ACCENT.getRed(), GREEN_ACCENT.getGreen(),
                        GREEN_ACCENT.getBlue(), 140));
                g2.fillRect(0, 0, NAV_INDICATOR_STRIP_WIDTH, h);
            }

            g2.setFont(active ? NAV_FONT_ACTIVE : NAV_FONT);
            if (active || hovered) {
                g2.setColor(Color.WHITE);
            } else {
                g2.setColor(secondary ? new Color(190, 198, 212) : Color.LIGHT_GRAY);
            }
            FontMetrics fm = g2.getFontMetrics();
            int textX = textLeft + (secondary ? 0 : NAV_INDICATOR_STRIP_WIDTH);
            int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }
    }

    private static class DividerLine extends JComponent {

        DividerLine() {
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setPreferredSize(new Dimension(SIDEBAR_WIDTH, 1));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            setMinimumSize(new Dimension(0, 1));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(DIVIDER_COLOR);
            g.fillRect(SIDEBAR_LEFT_MARGIN, 0, getWidth() - SIDEBAR_LEFT_MARGIN * 2, 1);
        }
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