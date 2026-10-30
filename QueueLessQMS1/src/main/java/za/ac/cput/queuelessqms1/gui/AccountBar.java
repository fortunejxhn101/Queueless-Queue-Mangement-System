package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import za.ac.cput.queuelessqms1.domain.CurrentSession;
import za.ac.cput.queuelessqms1.domain.Staff;

/**
 * @author admin Fortune
 */
public class AccountBar extends JPanel {

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final int SIDE_MARGIN = 24;

    private static final int BAR_HEIGHT = 68;

    /** Dashboard bar: staff name plus Log out. */
    public AccountBar() {
        this(true, null);
    }

    /**
     * @param showLogout whether the Log out button is shown next to the name
     * @param onBack     if not null, a "Back to Dashboard" link is shown at the left
     */
    public AccountBar(boolean showLogout, Runnable onBack) {
        this(true, showLogout, onBack);
    }

    /** Sub-screen bar: only the Back to Dashboard link, no staff name. */
    public static AccountBar backOnly(Runnable onBack) {
        return new AccountBar(false, false, onBack);
    }

    private AccountBar(boolean showAccount, boolean showLogout, Runnable onBack) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(0, SIDE_MARGIN, 0, SIDE_MARGIN));
        setPreferredSize(new Dimension(100, BAR_HEIGHT));

        JLabel staffNameLbl = new JLabel(resolveStaffName());
        staffNameLbl.setFont(new Font("Inter", Font.BOLD, 16));
        staffNameLbl.setForeground(ACCENT_COLOR);
        staffNameLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel staffRoleLbl = new JLabel("Admin");
        staffRoleLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        staffRoleLbl.setForeground(GREEN_ACCENT);
        staffRoleLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel staffInfo = new JPanel();
        staffInfo.setOpaque(false);
        staffInfo.setLayout(new BoxLayout(staffInfo, BoxLayout.Y_AXIS));
        staffInfo.add(staffNameLbl);
        staffInfo.add(staffRoleLbl);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        right.setOpaque(false);
        right.add(staffInfo);

        if (showLogout) {
            LogoutButton logoutBtn = new LogoutButton("Log out");
            logoutBtn.addActionListener(e -> {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JFrame) {
                    CurrentSession.clear();
                    ScreenTransition.navigateTo((JFrame) window, StaffLoginGUI::new);
                }
            });
            right.add(logoutBtn);
        }

        if (onBack != null) {
            BackLink backLink = new BackLink("\u2190  Back to Dashboard");
            backLink.addActionListener(e -> onBack.run());
            JPanel left = new JPanel(new GridBagLayout());
            left.setOpaque(false);
            left.add(backLink);
            add(left, BorderLayout.WEST);
        }

        JPanel rightWrap = new JPanel(new GridBagLayout());
        rightWrap.setOpaque(false);
        rightWrap.add(right);

        if (showAccount) {
            add(rightWrap, BorderLayout.EAST);
        }
    }

    private String resolveStaffName() {
        Staff staff = CurrentSession.getLoggedInStaff();
        if (staff == null) {
            return "Staff Member";
        }
        return staff.getFirstName() + " " + staff.getLastName();
    }

    private static class BackLink extends JButton {

        private boolean hovered = false;

        BackLink(String text) {
            super(text);
            setFont(new Font("Inter", Font.BOLD, 14));
            setForeground(ACCENT_COLOR);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 243, 246));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width + 24, Math.max(d.height, 38));
        }
    }

    private static class LogoutButton extends JButton {

        private boolean hovered = false;

        LogoutButton(String text) {
            super(text);
            setFont(new Font("Inter", Font.BOLD, 13));
            setForeground(ACCENT_COLOR);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            g2.setColor(hovered ? new Color(245, 247, 246) : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width + 24, Math.max(d.height, 38));
        }
    }
}