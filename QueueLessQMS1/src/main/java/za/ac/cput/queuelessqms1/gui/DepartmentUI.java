package za.ac.cput.queuelessqms1.gui;

import za.ac.cput.queuelessqms1.dao.DepartmentDAO;
import za.ac.cput.queuelessqms1.dao.TicketDAO;
import za.ac.cput.queuelessqms1.domain.Department;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author yantolo 
 */
public class DepartmentUI extends JFrame {

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    // ---- QueueLess palette (same values used by Dashboard / Registration / Visit screens) ----
    private static final Color HEADING_COLOR = new Color(30, 36, 49);
    private static final Color ACCENT_COLOR = new Color(47, 62, 92);      // navy
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);   // brand green
    private static final Color GREEN_ACCENT_DARK = new Color(84, 148, 98);
    private static final Color RED_ACCENT = new Color(196, 74, 74);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);   // inputs, cards
    private static final Color TITLE_COLOR = new Color(30, 36, 49);
    private static final Color SUBTITLE_COLOR = new Color(107, 114, 128);
    private static final Color MUTED_GRAY = new Color(140, 145, 155);
    private static final Color TABLE_HEADER_COLOR = new Color(148, 156, 168);
    private static final Color TABLE_HEADER_BG = new Color(250, 250, 251);
    private static final Color ROW_DIVIDER = new Color(240, 241, 244);
    private static final Color SELECTION_BG = new Color(230, 240, 235);

    private static final int INPUT_CORNER_RADIUS = 8;
    private static final int BUTTON_CORNER_RADIUS = 10;
    private static final int CARD_CORNER_RADIUS = 12;
    private static final int FIELD_WIDTH = 500;
    private static final int FIELD_HEIGHT = 36;
    private static final int COLUMN_WIDTH = 960;
    private static final int MAX_CODE_LENGTH = 10;
    private static final int MAX_NAME_LENGTH = 50;

    private static final Border INPUT_BORDER_NORMAL = new RoundedBorder(BORDER_COLOR, INPUT_CORNER_RADIUS);
    private static final Border INPUT_BORDER_FOCUSED = new RoundedBorder(GREEN_ACCENT, INPUT_CORNER_RADIUS);

    private final JTextField txtCode = new JTextField();
    private final JTextField txtName = new JTextField();

    private final JButton btnAdd = new RoundedButton("Add", ACCENT_COLOR, Color.WHITE, null);
    private final JButton btnUpdate = new RoundedButton("Update", GREEN_ACCENT, Color.WHITE, null);
    private final JButton btnDelete = new RoundedButton("Delete", RED_ACCENT, Color.WHITE, null);
    private final JButton btnSearch = new RoundedButton("Search", Color.WHITE, ACCENT_COLOR, ACCENT_COLOR);
    private final JButton btnShowAll = new RoundedButton("Show All", Color.WHITE, ACCENT_COLOR, BORDER_COLOR);
    private final JButton btnClear = new RoundedButton("Clear", Color.WHITE, ACCENT_COLOR, BORDER_COLOR);

    private DefaultTableModel tableModel;
    private JTable table;
    private CardLayout tableCardLayout;
    private JPanel tableCardHost;
    private JLabel emptyStateLbl;
    private final JLabel lblStatus = new JLabel(" ");
    private final DepartmentDAO dao = new DepartmentDAO();

    public DepartmentUI() {
        super("QueueLess: Department Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        add(AccountBar.backOnly(() -> ScreenTransition.navigateTo(this, DashboardGUI::createConfigured)), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        wireButtons();
        loadTable();
        setVisible(true);
    }

    // =====================================================================
    // LAYOUT
    // =====================================================================
    private JComponent buildContent() {
        JPanel column = new JPanel();
        column.setOpaque(false);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setPreferredSize(new Dimension(COLUMN_WIDTH, 100));

        JLabel eyebrowLbl = new JLabel(letterSpace("Departments"));
        eyebrowLbl.setFont(new Font("Inter", Font.BOLD, 13));
        eyebrowLbl.setForeground(GREEN_ACCENT);
        eyebrowLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headingLbl = new JLabel("Department Management");
        headingLbl.setFont(new Font("Inter", Font.BOLD, 30));
        headingLbl.setForeground(HEADING_COLOR);
        headingLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLbl = new JLabel("Add, search, update and remove the hospital departments patients are registered under.");
        subLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        subLbl.setForeground(SUBTITLE_COLOR);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        column.add(eyebrowLbl);
        column.add(Box.createRigidArea(new Dimension(0, 8)));
        column.add(headingLbl);
        column.add(Box.createRigidArea(new Dimension(0, 6)));
        column.add(subLbl);
        column.add(Box.createRigidArea(new Dimension(0, 28)));
        column.add(buildFormCard());
        column.add(Box.createRigidArea(new Dimension(0, 24)));
        column.add(buildTableCard());

        // centre the fixed-width column horizontally, let it use the full height
        JPanel host = new JPanel(new GridBagLayout());
        host.setBackground(Color.WHITE);
        host.setBorder(BorderFactory.createEmptyBorder(16, 24, 32, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        host.add(column, gbc);
        return host;
    }

    private JPanel buildFormCard() {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titleLbl = new JLabel("Department Details");
        titleLbl.setFont(new Font("Inter", Font.BOLD, 18));
        titleLbl.setForeground(ACCENT_COLOR);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        styleInput(txtCode);
        styleInput(txtName);
        ((AbstractDocument) txtCode.getDocument()).setDocumentFilter(new LengthFilter(MAX_CODE_LENGTH, true));
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new LengthFilter(MAX_NAME_LENGTH, false));

        gbc.gridy = 0;
        gbc.gridx = 0;
        formGrid.add(fieldLabel("Department Code:"), gbc);
        gbc.gridx = 1;
        formGrid.add(txtCode, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formGrid.add(fieldLabel("Department Name:"), gbc);
        gbc.gridx = 1;
        formGrid.add(txtName, gbc);

        // status message on the left, buttons on the right - primary action furthest right
        lblStatus.setFont(new Font("Inter", Font.BOLD, 13));
        lblStatus.setForeground(MUTED_GRAY);

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonsRow.setOpaque(false);
        buttonsRow.add(btnClear);
        buttonsRow.add(btnShowAll);
        buttonsRow.add(btnSearch);
        buttonsRow.add(btnDelete);
        buttonsRow.add(btnUpdate);
        buttonsRow.add(btnAdd);

        JPanel bottomRow = new JPanel(new BorderLayout(16, 0));
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        bottomRow.add(lblStatus, BorderLayout.WEST);
        bottomRow.add(buttonsRow, BorderLayout.EAST);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(formGrid);

        JLabel hintLbl = new JLabel("Code: up to " + MAX_CODE_LENGTH + " letters or numbers (saved in capitals).   Search matches part of a code or name.   "
                + "Name: up to " + MAX_NAME_LENGTH + " characters.");
        hintLbl.setFont(new Font("Inter", Font.PLAIN, 12));
        hintLbl.setForeground(MUTED_GRAY);
        hintLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(hintLbl);
        card.add(bottomRow);

        // don't let the form card stretch vertically when the window is maximised
        Dimension pref = card.getPreferredSize();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height + 8));
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"DEPARTMENT CODE", "DEPARTMENT NAME"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Inter", Font.PLAIN, 13));
        table.setForeground(Color.DARK_GRAY);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null); // header drawn separately below so it matches the Visit screen
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(Color.DARK_GRAY);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                    boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, false, row, col);
                setBackground(sel ? SELECTION_BG : Color.WHITE);
                setForeground(col == 0 ? ACCENT_COLOR : Color.DARK_GRAY);
                setFont(new Font("Inter", col == 0 ? Font.BOLD : Font.PLAIN, 13));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, ROW_DIVIDER),
                        BorderFactory.createEmptyBorder(0, 20, 0, 20)));
                return this;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtCode.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.getVerticalScrollBar().setUnitIncrement(16);

        emptyStateLbl = new JLabel("No departments to show yet.", SwingConstants.CENTER);
        emptyStateLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        emptyStateLbl.setForeground(MUTED_GRAY);

        JPanel emptyStatePanel = new JPanel(new GridBagLayout());
        emptyStatePanel.setBackground(Color.WHITE);
        emptyStatePanel.add(emptyStateLbl);

        tableCardLayout = new CardLayout();
        tableCardHost = new JPanel(tableCardLayout);
        tableCardHost.setOpaque(false);
        tableCardHost.add(emptyStatePanel, "EMPTY");
        tableCardHost.add(tableScroll, "TABLE");

        JPanel header = new TableHeaderBand(new GridLayout(1, 2));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        for (String label : new String[]{"DEPARTMENT CODE", "DEPARTMENT NAME"}) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Inter", Font.BOLD, 12));
            lbl.setForeground(Color.WHITE);
            header.add(lbl);
        }

        card.add(header, BorderLayout.NORTH);
        card.add(tableCardHost, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(COLUMN_WIDTH, 300));
        return card;
    }

    // =====================================================================
    // BEHAVIOUR (unchanged logic)
    // =====================================================================
    private void wireButtons() {
        btnAdd.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            String name = txtName.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                showMessage("Missing Information", "Please fill in both fields.", false);
                return;
            }
            String codeError = validateCode(code);
            if (codeError != null) {
                showMessage("Invalid Department Code", codeError, false);
                return;
            }
            String nameError = validateName(name);
            if (nameError != null) {
                showMessage("Invalid Department Name", nameError, false);
                return;
            }
            try {
                if (dao.getDepartment(code) != null) {
                    showMessage("Duplicate Department", "A department with code " + code + " already exists.", false);
                    return;
                }
                if (dao.addDepartment(new Department(code, name))) {
                    loadTable();
                    clearFields();
                    showMessage("Department Added", "Department added successfully.", true);
                } else {
                    showMessage("Add Failed", "Could not save the department. Please try again.", false);
                }
            } catch (Exception ex) {
                showMessage("Add Failed", "Something went wrong while adding the department.", false);
            }
        });

        btnSearch.addActionListener(e -> {
            String codeText = txtCode.getText().trim().toUpperCase();
            String nameText = txtName.getText().trim().toLowerCase();
            if (codeText.isEmpty() && nameText.isEmpty()) {
                showMessage("Missing Information", "Enter part of a department code or name to search.", false);
                return;
            }
            try {
                List<Department> all = dao.getAllDepartments();
                all.sort(Comparator.comparing(Department::getDepartmentCode, String.CASE_INSENSITIVE_ORDER));
                tableModel.setRowCount(0);
                for (Department d : all) {
                    boolean codeMatches = codeText.isEmpty()
                            || d.getDepartmentCode().toUpperCase().contains(codeText);
                    boolean nameMatches = nameText.isEmpty()
                            || d.getDepartmentName().toLowerCase().contains(nameText);
                    if (codeMatches && nameMatches) {
                        tableModel.addRow(new Object[]{d.getDepartmentCode(), d.getDepartmentName()});
                    }
                }
                if (tableModel.getRowCount() == 0) {
                    loadTable();
                    showMessage("Not Found", "No departments match your search.", false);
                } else {
                    tableCardLayout.show(tableCardHost, "TABLE");
                    showStatus(tableModel.getRowCount() + " department(s) found.", GREEN_ACCENT);
                }
            } catch (Exception ex) {
                showMessage("Search Failed", "Something went wrong while searching.", false);
            }
        });

        btnUpdate.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            String name = txtName.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                showMessage("Missing Information", "Please fill in both fields to update.", false);
                return;
            }
            String nameError = validateName(name);
            if (nameError != null) {
                showMessage("Invalid Department Name", nameError, false);
                return;
            }
            if (dao.getDepartment(code) == null) {
                showMessage("Not Found", "No department found with code: " + code, false);
                return;
            }
            boolean confirmed = showStyledDialog("Confirm Update",
                    "Update department " + code + " to '" + name + "'?",
                    "Update", GREEN_ACCENT, true);
            if (confirmed) {
                try {
                    if (dao.updateDepartment(new Department(code, name))) {
                        loadTable();
                        clearFields();
                        showMessage("Department Updated", "Department updated successfully.", true);
                    } else {
                        showMessage("Update Failed", "Update failed. Code not found.", false);
                    }
                } catch (Exception ex) {
                    showMessage("Update Failed", "Something went wrong while updating.", false);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            if (code.isEmpty()) {
                showMessage("Missing Information", "Enter a department code to delete.", false);
                return;
            }
            if (dao.getDepartment(code) == null) {
                showMessage("Not Found", "No department found with code: " + code, false);
                return;
            }
            int activeTickets = TicketDAO.countActiveTicketsByDepartment(code);
            if (activeTickets < 0) {
                showMessage("Delete Unavailable", "Could not check whether this department is in use. Please try again.", false);
                return;
            }
            if (activeTickets > 0) {
                showMessage("Department In Use", "Department " + code + " still has " + activeTickets
                        + " active ticket(s) in the queue. Complete or clear them before deleting it.", false);
                return;
            }
            boolean confirmed = showStyledDialog("Confirm Delete",
                    "Are you sure you want to delete department: " + code + "?",
                    "Delete", RED_ACCENT, true);
            if (confirmed) {
                try {
                    if (dao.deleteDepartment(code)) {
                        loadTable();
                        clearFields();
                        showMessage("Department Deleted", "Department deleted successfully.", true);
                    } else {
                        showMessage("Delete Failed", "Delete failed. Code not found.", false);
                    }
                } catch (Exception ex) {
                    showMessage("Delete Failed", "Something went wrong while deleting.", false);
                }
            }
        });

        btnShowAll.addActionListener(e -> {
            loadTable();
            showStatus("Showing " + tableModel.getRowCount() + " department(s).", GREEN_ACCENT);
        });

        btnClear.addActionListener(e -> {
            clearFields();
            showStatus(" ", MUTED_GRAY);
            loadTable();
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            List<Department> all = dao.getAllDepartments();
            all.sort(Comparator.comparing(Department::getDepartmentCode, String.CASE_INSENSITIVE_ORDER));
            for (Department d : all) {
                tableModel.addRow(new Object[]{d.getDepartmentCode(), d.getDepartmentName()});
            }
        } catch (Exception ex) {
            showMessage("Load Failed", "Could not load departments.", false);
        }
        tableCardLayout.show(tableCardHost, tableModel.getRowCount() == 0 ? "EMPTY" : "TABLE");
    }

    private String validateCode(String code) {
        if (code.isEmpty()) {
            return "Department code is required.";
        }
        if (code.length() > MAX_CODE_LENGTH) {
            return "Department code must be " + MAX_CODE_LENGTH + " characters or less.";
        }
        if (!code.matches("[A-Z0-9]+")) {
            return "Department code can only contain letters and numbers (no spaces or symbols).";
        }
        return null;
    }

    private String validateName(String name) {
        if (name.isEmpty()) {
            return "Department name is required.";
        }
        if (name.length() < 2) {
            return "Department name must be at least 2 characters.";
        }
        if (name.length() > MAX_NAME_LENGTH) {
            return "Department name must be " + MAX_NAME_LENGTH + " characters or less.";
        }
        if (!name.matches(".*[a-zA-Z].*")) {
            return "Department name must contain at least one letter.";
        }
        return null;
    }

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        table.clearSelection();
    }

    private void showMessage(String title, String message, boolean success) {
        showStatus(" ", MUTED_GRAY);
        showStyledDialog(title, message, "OK", success ? GREEN_ACCENT : RED_ACCENT, false);
    }

    private void showStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    // =====================================================================
    // STYLING HELPERS
    // =====================================================================
    private JPanel roundedCard() {
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
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Inter", Font.BOLD, 14));
        lbl.setForeground(ACCENT_COLOR);
        lbl.setPreferredSize(new Dimension(170, FIELD_HEIGHT));
        return lbl;
    }

    private void styleInput(JTextField input) {
        input.setFont(new Font("Inter", Font.PLAIN, 14));
        input.setBorder(INPUT_BORDER_NORMAL);

        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        input.setPreferredSize(size);
        input.setMinimumSize(size);
        input.setMaximumSize(size);

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

    private static String letterSpace(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(Character.toUpperCase(c));
            if (c != ' ') {
                sb.append(' ');
            }
        }
        return sb.toString().trim();
    }

    /**
     * Styled dialog matching the rest of the app (white rounded card, green accent
     * strip, navy title). Used instead of JOptionPane for the update/delete
     * confirmations. Returns true if the confirm button was pressed.
     */
    private boolean showStyledDialog(String title, String message, String confirmLabel,
            Color confirmColor, boolean showCancel) {
        final boolean[] result = {false};

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
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, INPUT_CORNER_RADIUS * 2, INPUT_CORNER_RADIUS * 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 4, 20, 24));

        JPanel accentStrip = new JPanel();
        accentStrip.setBackground(confirmColor);
        accentStrip.setPreferredSize(new Dimension(4, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Inter", Font.BOLD, 16));
        titleLbl.setForeground(ACCENT_COLOR);

        JLabel messageLbl = new JLabel("<html><body style='width: 260px'>" + message.replace("&", "&amp;").replace("<", "&lt;") + "</body></html>");
        messageLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        messageLbl.setForeground(Color.DARK_GRAY);
        messageLbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        JButton confirmBtn = new RoundedButton(confirmLabel, confirmColor, Color.WHITE, null);
        confirmBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        if (showCancel) {
            JButton cancelBtn = new RoundedButton("Cancel", Color.WHITE, ACCENT_COLOR, BORDER_COLOR);
            cancelBtn.addActionListener(e -> dialog.dispose());
            btnRow.add(cancelBtn);
        }
        btnRow.add(confirmBtn);

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
        return result[0];
    }

    // =====================================================================
    // CUSTOM COMPONENTS
    // =====================================================================
    private static class LengthFilter extends DocumentFilter {

        private final int max;
        private final boolean upperCase;

        LengthFilter(int max, boolean upperCase) {
            this.max = max;
            this.upperCase = upperCase;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, text, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                text = "";
            }
            if (upperCase) {
                text = text.toUpperCase();
            }
            int room = max - (fb.getDocument().getLength() - length);
            if (room <= 0) {
                return;
            }
            if (text.length() > room) {
                text = text.substring(0, room);
            }
            super.replace(fb, offset, length, text, attrs);
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

    /** Flat rounded button: filled colour, optional outline, darkens on hover. */
    private static class RoundedButton extends JButton {

        private boolean hovered = false;
        private final Color outlineColor;

        RoundedButton(String text, Color background, Color foreground, Color outlineColor) {
            super(text);
            this.outlineColor = outlineColor;
            setBackground(background);
            setForeground(foreground);
            setFont(new Font("Inter", Font.BOLD, 13));
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
            Color bg = getBackground();
            // white (outlined) buttons get a faint tint on hover; filled ones darken
            Color fill = hovered
                    ? (Color.WHITE.equals(bg) ? new Color(245, 247, 246) : bg.darker())
                    : bg;
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);
            if (outlineColor != null) {
                g2.setColor(outlineColor);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BUTTON_CORNER_RADIUS, BUTTON_CORNER_RADIUS);
            }
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width + 24, Math.max(d.height, 40));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new DepartmentUI();
        });
    }
}