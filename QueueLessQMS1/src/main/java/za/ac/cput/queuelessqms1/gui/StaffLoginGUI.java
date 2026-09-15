package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import za.ac.cput.queuelessqms1.domain.*;
import za.ac.cput.queuelessqms1.dao.*;

/**
 * @author admin
 */
public class StaffLoginGUI extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color GREEN_ACCENT_DARK = new Color(84, 148, 98);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color MUTED_GRAY = new Color(140, 145, 155);

    private static final int INPUT_CORNER_RADIUS = 8;
    private static final int CARD_CORNER_RADIUS = 20;
    private static final int CARD_WIDTH = 460;
    private static final int FIELD_WIDTH = 340;
    private static final int FIELD_HEIGHT = 40;

    private static final Border INPUT_BORDER_NORMAL = new RoundedBorder(BORDER_COLOR, INPUT_CORNER_RADIUS);
    private static final Border INPUT_BORDER_FOCUSED = new RoundedBorder(GREEN_ACCENT, INPUT_CORNER_RADIUS);

    private CardLayout formsCardLayout;
    private JPanel formsContainer;

    private ToggleTab loginTab;
    private ToggleTab signUpTab;

    private JTextField loginUsernameTxt;
    private JPasswordField loginPasswordTxt;

    private JTextField signUpFirstNameTxt;
    private JTextField signUpLastNameTxt;
    private JTextField signUpUsernameTxt;
    private JPasswordField signUpPasswordTxt;
    private JPasswordField signUpConfirmPasswordTxt;

    private FadeLayer fadeLayer;

    public StaffLoginGUI() {
        super("QueueLess: Staff Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 950);
        setMinimumSize(new Dimension(900, 850));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.WHITE);

        JPanel card = buildCard();

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(card, new GridBagConstraints());

        fadeLayer = new FadeLayer(centerWrapper);
        add(fadeLayer, BorderLayout.CENTER);
    }

    public void fadeContentIn() {
        fadeLayer.animateAlpha(0f, 1f, null);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_CORNER_RADIUS, CARD_CORNER_RADIUS);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_CORNER_RADIUS, CARD_CORNER_RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension natural = super.getPreferredSize();
                return new Dimension(CARD_WIDTH, natural.height);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(44, 48, 40, 48));

        JLabel logoIconLbl = new JLabel();
        java.net.URL logoUrl = getClass().getResource("/images/QueueLessLogo.png");
        if (logoUrl != null) {
            Image scaledLogo = new ImageIcon(logoUrl).getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            logoIconLbl.setIcon(new ImageIcon(scaledLogo));
        } else {
            logoIconLbl.setIcon(new LogoMarkIcon(48));
        }
        logoIconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel wordmarkLbl = new JLabel("QueueLess");
        wordmarkLbl.setFont(new Font("Inter", Font.BOLD, 22));
        wordmarkLbl.setForeground(Color.BLACK);
        wordmarkLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLbl = new JLabel("Staff Access Portal");
        subtitleLbl.setFont(new Font("Inter", Font.PLAIN, 13));
        subtitleLbl.setForeground(MUTED_GRAY);
        subtitleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel toggleRow = buildToggleRow();
        toggleRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        formsCardLayout = new CardLayout();
        formsContainer = new JPanel(formsCardLayout);
        formsContainer.setOpaque(false);
        formsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        formsContainer.add(buildLoginForm(), "LOGIN");
        formsContainer.add(buildSignUpForm(), "SIGNUP");

        card.add(logoIconLbl);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(wordmarkLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subtitleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 28)));
        card.add(toggleRow);
        card.add(Box.createRigidArea(new Dimension(0, 28)));
        card.add(formsContainer);

        return card;
    }

    private JPanel buildToggleRow() {
        JPanel track = new JPanel(new GridLayout(1, 2, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 241, 244));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        track.setOpaque(false);
        Dimension trackSize = new Dimension(FIELD_WIDTH, 44);
        track.setPreferredSize(trackSize);
        track.setMaximumSize(trackSize);
        track.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        loginTab = new ToggleTab("Log In");
        signUpTab = new ToggleTab("Sign Up");

        loginTab.addActionListener(e -> switchTo("LOGIN"));
        signUpTab.addActionListener(e -> switchTo("SIGNUP"));

        track.add(loginTab);
        track.add(signUpTab);

        setActiveTab(loginTab, signUpTab);

        return track;
    }

    private void switchTo(String cardName) {
        formsCardLayout.show(formsContainer, cardName);
        if ("LOGIN".equals(cardName)) {
            setActiveTab(loginTab, signUpTab);
        } else {
            setActiveTab(signUpTab, loginTab);
        }
    }

    private void setActiveTab(ToggleTab active, ToggleTab inactive) {
        active.setActive(true);
        inactive.setActive(false);
    }

    private JPanel buildLoginForm() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        loginUsernameTxt = new JTextField();
        loginPasswordTxt = new JPasswordField();
        
        loginPasswordTxt.addActionListener(this::handleLogin);

        form.add(fieldBlock("Username", loginUsernameTxt));
        form.add(Box.createRigidArea(new Dimension(0, 18)));
        form.add(fieldBlock("Password", loginPasswordTxt));
        form.add(Box.createRigidArea(new Dimension(0, 26)));

        RoundedButton loginBtn = new RoundedButton("Log In", 10);
        loginBtn.setFont(new Font("Inter", Font.BOLD, 14));
        loginBtn.setBackground(ACCENT_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension btnSize = new Dimension(FIELD_WIDTH, 44);
        loginBtn.setPreferredSize(btnSize);
        loginBtn.setMaximumSize(btnSize);
        loginBtn.addActionListener(this::handleLogin);

        form.add(loginBtn);
        form.add(Box.createRigidArea(new Dimension(0, 18)));
        form.add(buildSwitchLink("Don't have an account? ", "Sign Up", "SIGNUP"));

        return form;
    }

    private JPanel buildSignUpForm() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        signUpFirstNameTxt = new JTextField();
        signUpLastNameTxt = new JTextField();
        signUpUsernameTxt = new JTextField();
        signUpPasswordTxt = new JPasswordField();
        signUpConfirmPasswordTxt = new JPasswordField();

        form.add(fieldBlock("First Name", signUpFirstNameTxt));
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(fieldBlock("Last Name", signUpLastNameTxt));
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(fieldBlock("Create Username", signUpUsernameTxt));
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(fieldBlock("Create Password", signUpPasswordTxt));
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(fieldBlock("Confirm Password", signUpConfirmPasswordTxt));
        form.add(Box.createRigidArea(new Dimension(0, 26)));

        RoundedButton signUpBtn = new RoundedButton("Sign Up", 10);
        signUpBtn.setFont(new Font("Inter", Font.BOLD, 14));
        signUpBtn.setBackground(GREEN_ACCENT);
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension btnSize = new Dimension(FIELD_WIDTH, 44);
        signUpBtn.setPreferredSize(btnSize);
        signUpBtn.setMaximumSize(btnSize);
        signUpBtn.addActionListener(this::handleSignUp);

        form.add(signUpBtn);
        form.add(Box.createRigidArea(new Dimension(0, 18)));
        form.add(buildSwitchLink("Already have an account? ", "Log In", "LOGIN"));

        return form;
    }

    private JPanel buildSwitchLink(String promptText, String linkText, String targetCard) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel promptLbl = new JLabel(promptText);
        promptLbl.setFont(new Font("Inter", Font.PLAIN, 13));
        promptLbl.setForeground(MUTED_GRAY);

        JLabel linkLbl = new JLabel(linkText);
        linkLbl.setFont(new Font("Inter", Font.BOLD, 13));
        linkLbl.setForeground(GREEN_ACCENT);
        linkLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchTo(targetCard);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Font f = linkLbl.getFont();
                linkLbl.setFont(f.deriveFont(f.getStyle() | Font.ITALIC));
                linkLbl.setForeground(GREEN_ACCENT.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font f = linkLbl.getFont();
                linkLbl.setFont(f.deriveFont(f.getStyle() & ~Font.ITALIC));
                linkLbl.setForeground(GREEN_ACCENT);
            }
        });

        row.add(promptLbl);
        row.add(linkLbl);
        return row;
    }

    private JPanel fieldBlock(String labelText, JComponent input) {
        JPanel block = new JPanel();
        block.setOpaque(false);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Inter", Font.BOLD, 13));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        input.setFont(new Font("Inter", Font.PLAIN, 14));
        input.setBorder(INPUT_BORDER_NORMAL);
        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        input.setPreferredSize(size);
        input.setMinimumSize(size);
        input.setMaximumSize(size);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        addFocusBorderSwap(input);

        block.add(label);
        block.add(Box.createRigidArea(new Dimension(0, 6)));
        block.add(input);

        return block;
    }

    private void addFocusBorderSwap(JComponent input) {
        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                input.setBorder(INPUT_BORDER_FOCUSED);
            }

            @Override
            public void focusLost(FocusEvent e) {
                input.setBorder(INPUT_BORDER_NORMAL);
            }
        });
    }

    private String validateName(String label, String value) {
        if (value.isEmpty()) {
            return label + " is required";
        }
        if (!value.matches("[a-zA-Z\\-' ]{2,50}")) {
            return "Please enter a valid " + label.toLowerCase() + " (letters only, 2-50 characters)";
        }
        return null;
    }

    private String validateUsername(String value) {
        if (value.isEmpty()) {
            return "Username is required";
        }
        if (!value.matches("[a-zA-Z][a-zA-Z0-9_]{2,29}")) {
            return "Username must be 3-30 characters, start with a letter, "
                    + "and contain only letters, numbers, and underscores";
        }
        return null;
    }

    private String validatePassword(String value) {
        if (value.isEmpty()) {
            return "Password is required";
        }
        if (value.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!value.matches(".*[a-zA-Z].*") || !value.matches(".*\\d.*")) {
            return "Password must contain at least one letter and one number";
        }
        return null;
    }

    private void handleLogin(ActionEvent e) {
        String username = loginUsernameTxt.getText().trim();
        String password = new String(loginPasswordTxt.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showStyledMessageDialog("Missing Information", "Please enter your username and password.");
            return;
        }

        String usernameError = validateUsername(username);
        if (usernameError != null) {
            showStyledMessageDialog("Invalid Username", usernameError);
            return;
        }

        Staff matchedStaff = new Staff("Staff", "Member", username, password);
        CurrentSession.setLoggedInStaff(matchedStaff);
        goToDashboard();
    }

    private void handleSignUp(ActionEvent e) {
        String firstName = signUpFirstNameTxt.getText().trim();
        String lastName = signUpLastNameTxt.getText().trim();
        String username = signUpUsernameTxt.getText().trim();
        String password = new String(signUpPasswordTxt.getPassword());
        String confirmPassword = new String(signUpConfirmPasswordTxt.getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showStyledMessageDialog("Missing Information", "Please fill in all fields to sign up.");
            return;
        }

        String firstNameError = validateName("First name", firstName);
        if (firstNameError != null) {
            showStyledMessageDialog("Invalid First Name", firstNameError);
            return;
        }

        String lastNameError = validateName("Last name", lastName);
        if (lastNameError != null) {
            showStyledMessageDialog("Invalid Last Name", lastNameError);
            return;
        }

        String usernameError = validateUsername(username);
        if (usernameError != null) {
            showStyledMessageDialog("Invalid Username", usernameError);
            return;
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            showStyledMessageDialog("Weak Password", passwordError);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showStyledMessageDialog("Password Mismatch", "Passwords do not match. Please try again.");
            return;
        }

        Staff staffMember = new Staff(firstName, lastName, username, password);

        CurrentSession.setLoggedInStaff(staffMember);
        goToDashboard();
    }

    private void goToDashboard() {
        ScreenTransition.navigateTo(this, DashboardGUI::createConfigured);
    }

    private void showStyledMessageDialog(String title, String message) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(new BorderLayout(16, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, INPUT_CORNER_RADIUS * 2, INPUT_CORNER_RADIUS * 2);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, INPUT_CORNER_RADIUS * 2, INPUT_CORNER_RADIUS * 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 4, 20, 24));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(GREEN_ACCENT);
        accentStrip.setPreferredSize(new Dimension(4, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Inter", Font.BOLD, 16));
        titleLbl.setForeground(Color.BLACK);

        JLabel messageLbl = new JLabel("<html><body style='width: 260px'>" + message + "</body></html>");
        messageLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        messageLbl.setForeground(Color.DARK_GRAY);
        messageLbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        RoundedButton okBtn = new RoundedButton("OK", 10);
        okBtn.setFont(new Font("Inter", Font.BOLD, 13));
        okBtn.setBackground(ACCENT_COLOR);
        okBtn.setForeground(Color.WHITE);
        okBtn.addActionListener(e -> dialog.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.add(okBtn);

        JPanel textColumn = new JPanel();
        textColumn.setOpaque(false);
        textColumn.setLayout(new BoxLayout(textColumn, BoxLayout.Y_AXIS));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        textColumn.add(titleLbl);
        textColumn.add(messageLbl);
        textColumn.add(btnRow);

        panel.add(accentStrip, BorderLayout.WEST);
        panel.add(textColumn, BorderLayout.CENTER);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private static class ToggleTab extends JButton {

        private boolean active = false;

        ToggleTab(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setFont(new Font("Inter", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        void setActive(boolean active) {
            this.active = active;
            setForeground(active ? Color.WHITE : MUTED_GRAY);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (active) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    private static class RoundedBorder implements Border {

        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {

        private final int radius;
        private Color baseBackground;

        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (baseBackground != null && baseBackground.equals(GREEN_ACCENT)) {
                        setBackground(GREEN_ACCENT_DARK);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (baseBackground != null) {
                        setBackground(baseBackground);
                    }
                }
            });
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if (bg != null && !bg.equals(GREEN_ACCENT_DARK)) {
                baseBackground = bg;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class FadeLayer extends JPanel {

        private static final int FADE_DURATION_MS = 1400;
        private static final int FADE_STEP_MS = 15;

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

        void animateAlpha(float from, float to, Runnable onComplete) {
            setAlpha(from);

            int totalSteps = FADE_DURATION_MS / FADE_STEP_MS;
            int[] currentStep = {0};

            Timer timer = new Timer(FADE_STEP_MS, null);
            timer.addActionListener(e -> {
                currentStep[0]++;
                float t = Math.min(1f, currentStep[0] / (float) totalSteps);
                float eased = t * t * (3f - 2f * t);
                setAlpha(from + (to - from) * eased);

                if (t >= 1f) {
                    timer.stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            });
            timer.start();
        }

        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintChildren(g2);
            g2.dispose();
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
            g2.fillRoundRect(x, y, size, size, size / 4, size / 4);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Inter", Font.BOLD, (int) (size * 0.5)));
            FontMetrics fm = g2.getFontMetrics();
            String letter = "Q";
            int textWidth = fm.stringWidth(letter);
            int textHeight = fm.getAscent();
            g2.drawString(letter, x + (size - textWidth) / 2, y + (size + textHeight) / 2 - 3);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new StaffLoginGUI().setVisible(true);
        });
    }
}
