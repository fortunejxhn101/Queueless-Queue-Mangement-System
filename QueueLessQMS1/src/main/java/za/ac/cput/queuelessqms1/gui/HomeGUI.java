package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author admin
 */
public class HomeGUI extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static final Color GREEN_ACCENT = new Color(103, 172, 118);

    private static final int FADE_DURATION_MS = 1400;
    private static final int FADE_STEP_MS = 15;

    private JPanel centerPanel;
    private BigLogoLabel bigLogo;
    private JLabel welcomeTitleLbl;
    private JLabel sloganLbl;
    private FadeLayer fadeLayer;

    public HomeGUI() {
        super("QueueLess: Queue Management System");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        buildCenterContent();

        fadeLayer = new FadeLayer(centerPanel);
        add(fadeLayer, BorderLayout.CENTER);
    }

    private void buildCenterContent() {
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel innerColumn = new JPanel();
        innerColumn.setOpaque(false);
        innerColumn.setLayout(new BoxLayout(innerColumn, BoxLayout.Y_AXIS));

        bigLogo = new BigLogoLabel(150);
        bigLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeTitleLbl = new JLabel("Welcome to QueueLess");
        welcomeTitleLbl.setFont(new Font("Inter", Font.BOLD, 34));
        welcomeTitleLbl.setForeground(Color.BLACK);
        welcomeTitleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dividerLine = new JPanel();
        dividerLine.setBackground(GREEN_ACCENT);
        Dimension dividerSize = new Dimension(56, 3);
        dividerLine.setPreferredSize(dividerSize);
        dividerLine.setMaximumSize(dividerSize);
        dividerLine.setMinimumSize(dividerSize);
        dividerLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        sloganLbl = new JLabel(letterSpace("Queue Less. Care More."));
        sloganLbl.setFont(new Font("Inter", Font.PLAIN, 15));
        sloganLbl.setForeground(Color.BLACK);
        sloganLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        innerColumn.add(bigLogo);
        innerColumn.add(Box.createRigidArea(new Dimension(0, 28)));
        innerColumn.add(welcomeTitleLbl);
        innerColumn.add(Box.createRigidArea(new Dimension(0, 16)));
        innerColumn.add(dividerLine);
        innerColumn.add(Box.createRigidArea(new Dimension(0, 16)));
        innerColumn.add(sloganLbl);

        centerPanel.add(innerColumn, new GridBagConstraints());
    }

    private static String letterSpace(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                sb.append("   ");
            } else {
                sb.append(Character.toUpperCase(c)).append('\u2009');
            }
        }
        return sb.toString().trim();
    }

    public void fadeContentIn() {
        animateAlpha(0f, 1f, null);
    }

    public void fadeContentOut(Runnable onComplete) {
        animateAlpha(1f, 0f, onComplete);
    }

    private void animateAlpha(float from, float to, Runnable onComplete) {
        fadeLayer.setAlpha(from);

        int totalSteps = FADE_DURATION_MS / FADE_STEP_MS;
        int[] currentStep = {0};

        Timer timer = new Timer(FADE_STEP_MS, null);
        timer.addActionListener(e -> {
            currentStep[0]++;
            float t = Math.min(1f, currentStep[0] / (float) totalSteps);
            float eased = smoothStep(t);
            fadeLayer.setAlpha(from + (to - from) * eased);

            if (t >= 1f) {
                timer.stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        timer.start();
    }

    private static float smoothStep(float t) {
        return t * t * (3f - 2f * t);
    }

    private static class FadeLayer extends JPanel {

        private float alpha = 1f;

        FadeLayer(Component content) {
            setLayout(new BorderLayout());
            setOpaque(true);
            setBackground(Color.WHITE);
            add(content, BorderLayout.CENTER);
        }

        void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintChildren(g2);
            g2.dispose();
        }
    }

    private static class BigLogoLabel extends JComponent {

        private final int size;
        private Image logoImage;

        BigLogoLabel(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));

            java.net.URL logoUrl = getClass().getResource("/images/QueueLessLogo.png");
            if (logoUrl != null) {
                logoImage = new ImageIcon(logoUrl).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (logoImage != null) {
                g2.drawImage(logoImage, 0, 0, size, size, this);
            } else {
                int arc = size / 4;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, size, size, arc, arc);
                g2.setColor(GREEN_ACCENT);
                g2.fill(roundRect);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Inter", Font.BOLD, (int) (size * 0.44)));
                FontMetrics fm = g2.getFontMetrics();
                String text = "Q";
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int x = (size - textWidth) / 2;
                int y = (size + textHeight) / 2 - (size / 24);
                g2.drawString(text, x, y);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            HomeGUI homeGUI = new HomeGUI();
            homeGUI.setVisible(true);
            homeGUI.fadeContentIn();
        });
    }
}