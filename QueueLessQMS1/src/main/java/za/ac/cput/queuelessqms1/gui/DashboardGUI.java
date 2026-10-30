package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

/**
 *
 * @author admin Fortune 
 */
public class DashboardGUI extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color TITLE_COLOR = new Color(30, 36, 49);
    private static final Color SUBTITLE_COLOR = new Color(107, 114, 128);
    private static final Color CARD_BORDER_COLOR = new Color(224, 227, 232);
    private static final Color CARD_BORDER_HOVER_COLOR = GREEN_ACCENT;
    private static final Color CARD_DESCRIPTION_COLOR = new Color(120, 128, 140);

    private static final int CARD_WIDTH = 370; // was 400 - narrowed slightly so three cards fit side by side
    private static final int CARD_HEIGHT = 360;
    private static final int CARD_CORNER_RADIUS = 16;
    private static final int CARD_GAP = 32; // was 40

    private JPanel contentPanel;
    private JLabel eyebrowLbl;
    private JLabel headingLbl;

    private FeatureCard queueOperationsCard;
    private FeatureCard visitRecordsCard;
    private FeatureCard departmentsCard;

    private Runnable onQueueOperationsClicked;
    private Runnable onVisitRecordsClicked;
    private Runnable onDepartmentsClicked;

    public DashboardGUI() {
        super("QueueLess: Dashboard");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        buildContent();

        add(new AccountBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public static DashboardGUI createConfigured() {
        DashboardGUI dashboard = new DashboardGUI();

        dashboard.setOnQueueOperationsClicked(() -> {
            ScreenTransition.navigateTo(dashboard, MainAppFrame::new);
        });

        dashboard.setOnVisitRecordsClicked(() -> {
            ScreenTransition.navigateTo(dashboard, VisitRecordsFrame::new);
        });

        dashboard.setOnDepartmentsClicked(() -> {
            ScreenTransition.navigateTo(dashboard, DepartmentUI::new);
        });

        return dashboard;
    }

    private void buildContent() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel innerColumn = new JPanel();
        innerColumn.setOpaque(false);
        innerColumn.setLayout(new BoxLayout(innerColumn, BoxLayout.Y_AXIS));

        eyebrowLbl = new JLabel(letterSpace("Dashboard"));
        eyebrowLbl.setFont(new Font("Inter", Font.BOLD, 13));
        eyebrowLbl.setForeground(GREEN_ACCENT);
        eyebrowLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        headingLbl = new JLabel("What would you like to do?");
        headingLbl.setFont(new Font("Inter", Font.BOLD, 30));
        headingLbl.setForeground(TITLE_COLOR);
        headingLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel cardsRow = new JPanel();
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new BoxLayout(cardsRow, BoxLayout.X_AXIS));
        cardsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        queueOperationsCard = new FeatureCard(
                "Queue Operations",
                "Generate tickets, register patients, manage the live queue, "
                + "and control the public display board.",
                "Generate Ticket  \u00b7  Patient Registration  \u00b7  Staff Dashboard  \u00b7  Display Screen",
                new QueueOperationsIcon(),
                clicked -> {
                    if (onQueueOperationsClicked != null) {
                        onQueueOperationsClicked.run();
                    }
                });

        visitRecordsCard = new FeatureCard(
                "Visit Records",
                "Look up and review a patient's past visits and history.",
                "Visit Screen",
                new VisitRecordsIcon(),
                clicked -> {
                    if (onVisitRecordsClicked != null) {
                        onVisitRecordsClicked.run();
                    }
                });

        departmentsCard = new FeatureCard(
                "Departments",
                "Add, search, update and remove the hospital departments "
                + "that patients can be registered under.",
                "Department Management",
                new DepartmentsIcon(),
                clicked -> {
                    if (onDepartmentsClicked != null) {
                        onDepartmentsClicked.run();
                    }
                });

        cardsRow.add(queueOperationsCard);
        cardsRow.add(Box.createRigidArea(new Dimension(CARD_GAP, 0)));
        cardsRow.add(visitRecordsCard);
        cardsRow.add(Box.createRigidArea(new Dimension(CARD_GAP, 0)));
        cardsRow.add(departmentsCard);

        innerColumn.add(eyebrowLbl);
        innerColumn.add(Box.createRigidArea(new Dimension(0, 10)));
        innerColumn.add(headingLbl);
        innerColumn.add(Box.createRigidArea(new Dimension(0, 40)));
        innerColumn.add(cardsRow);

        contentPanel.add(innerColumn, new GridBagConstraints());
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

    public void setOnQueueOperationsClicked(Runnable callback) {
        this.onQueueOperationsClicked = callback;
    }

    public void setOnVisitRecordsClicked(Runnable callback) {
        this.onVisitRecordsClicked = callback;
    }

    public void setOnDepartmentsClicked(Runnable callback) {
        this.onDepartmentsClicked = callback;
    }

    private static class FeatureCard extends JPanel {

        private boolean hovered = false;

        FeatureCard(String title, String description, String screensLine, Icon icon,
                Consumer<Void> onClick) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            int pad = 32;
            int textWidth = CARD_WIDTH - pad * 2;
            setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));

            JLabel iconLbl = new JLabel(icon);
            iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(new Font("Inter", Font.BOLD, 22));
            titleLbl.setForeground(TITLE_COLOR);
            titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            JComponent descriptionLbl = createWrappingLabel(
                    description, new Font("Inter", Font.PLAIN, 14), CARD_DESCRIPTION_COLOR, textWidth);

            JComponent screensLbl = createWrappingLabel(
                    screensLine, new Font("Inter", Font.PLAIN, 12), new Color(160, 166, 177), textWidth);

            JLabel enterLbl = new JLabel("Enter \u2192");
            enterLbl.setFont(new Font("Inter", Font.BOLD, 14));
            enterLbl.setForeground(GREEN_ACCENT);
            enterLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(iconLbl);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(titleLbl);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(descriptionLbl);
            add(Box.createVerticalGlue());
            add(screensLbl);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(enterLbl);

            setFocusable(true);

            MouseAdapter cardMouseHandler = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), FeatureCard.this);
                    if (!FeatureCard.this.contains(p)) {
                        hovered = false;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!SwingUtilities.isLeftMouseButton(e)) {
                        return;
                    }
                    Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), FeatureCard.this);
                    if (FeatureCard.this.contains(p)) {
                        onClick.accept(null);
                    }
                }
            };
            attachMouseHandler(this, cardMouseHandler);

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

            Action activate = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClick.accept(null);
                }
            };
            getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "activateCard");
            getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "activateCard");
            getActionMap().put("activateCard", activate);
        }

        private static void attachMouseHandler(Component comp, MouseAdapter handler) {
            comp.addMouseListener(handler);
            if (comp instanceof Container) {
                for (Component child : ((Container) comp).getComponents()) {
                    attachMouseHandler(child, handler);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, CARD_CORNER_RADIUS, CARD_CORNER_RADIUS);

            g2.setColor(Color.WHITE);
            g2.fill(roundRect);

            boolean highlighted = hovered || hasFocus();
            g2.setColor(highlighted ? CARD_BORDER_HOVER_COLOR : CARD_BORDER_COLOR);
            g2.setStroke(new BasicStroke(highlighted ? 2f : 1f));
            g2.draw(roundRect);

            g2.dispose();
            super.paintComponent(g);
        }

        private static JComponent createWrappingLabel(String text, Font font, Color color, int width) {
            JTextArea area = new JTextArea(text);
            area.setEditable(false);
            area.setFocusable(false);
            area.setEnabled(false);
            area.setDisabledTextColor(color);
            area.setOpaque(false);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setFont(font);
            area.setBorder(null);
            area.setAlignmentX(Component.LEFT_ALIGNMENT);
            area.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            area.setSize(width, Short.MAX_VALUE);
            Dimension wrapped = area.getPreferredSize();
            Dimension sized = new Dimension(width, wrapped.height);
            area.setPreferredSize(sized);
            area.setMaximumSize(sized);

            return area;
        }
    }

    private static class QueueOperationsIcon implements Icon {

        private static final int SIZE = 56;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(GREEN_ACCENT.getRed(), GREEN_ACCENT.getGreen(), GREEN_ACCENT.getBlue(), 30));
            g2.fillRoundRect(x, y, SIZE, SIZE, 14, 14);

            g2.setColor(GREEN_ACCENT);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int ix = x + 14, iy = y + 17, iw = SIZE - 28, ih = SIZE - 34;
            g2.drawRoundRect(ix, iy, iw, ih, 6, 6);
            g2.drawLine(ix + iw / 2, iy, ix + iw / 2, iy + ih);
            g2.drawLine(ix + 6, iy + ih / 2 + 4, ix + iw / 2 - 4, iy + ih / 2 + 4);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }

    private static class VisitRecordsIcon implements Icon {

        private static final int SIZE = 56;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(GREEN_ACCENT.getRed(), GREEN_ACCENT.getGreen(), GREEN_ACCENT.getBlue(), 30));
            g2.fillRoundRect(x, y, SIZE, SIZE, 14, 14);

            g2.setColor(GREEN_ACCENT);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int ix = x + 16, iy = y + 14, iw = SIZE - 32, ih = SIZE - 28;
            g2.drawRoundRect(ix, iy, iw, ih, 4, 4);
            int lineY = iy + 8;
            for (int i = 0; i < 3; i++) {
                g2.drawLine(ix + 5, lineY, ix + iw - 5, lineY);
                lineY += 7;
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }

    private static class DepartmentsIcon implements Icon {

        private static final int SIZE = 56;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(GREEN_ACCENT.getRed(), GREEN_ACCENT.getGreen(), GREEN_ACCENT.getBlue(), 30));
            g2.fillRoundRect(x, y, SIZE, SIZE, 14, 14);

            g2.setColor(GREEN_ACCENT);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int box = 12, gap = 6;
            int ix = x + (SIZE - (box * 2 + gap)) / 2;
            int iy = y + (SIZE - (box * 2 + gap)) / 2;
            g2.drawRoundRect(ix, iy, box, box, 4, 4);
            g2.drawRoundRect(ix + box + gap, iy, box, box, 4, 4);
            g2.drawRoundRect(ix, iy + box + gap, box, box, 4, 4);
            g2.drawRoundRect(ix + box + gap, iy + box + gap, box, box, 4, 4);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }
}