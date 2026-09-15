package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import za.ac.cput.queuelessqms1.domain.Ticket;
import za.ac.cput.queuelessqms1.domain.Visit;
import za.ac.cput.queuelessqms1.dao.TicketDAO;

/**
 * @author admin
 */
public class VisitScreenPanel extends JPanel {

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color MUTED_GRAY = new Color(140, 145, 155);
    private static final Color TABLE_HEADER_COLOR = new Color(148, 156, 168);

    private static final int INPUT_CORNER_RADIUS = 8;
    private static final int BUTTON_CORNER_RADIUS = 10;
    private static final int FIELD_WIDTH = 500;
    private static final int FIELD_HEIGHT = 36;
    private static final int LEFT_MARGIN = 24;

    private static final Border INPUT_BORDER_NORMAL = new RoundedBorder(BORDER_COLOR, INPUT_CORNER_RADIUS);
    private static final Border INPUT_BORDER_FOCUSED = new RoundedBorder(GREEN_ACCENT, INPUT_CORNER_RADIUS);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");

    private JTextField idNumberTxt;
    private JTextField visitDateTxt;
    private JTextField visitTimeTxt;
    private JTextArea notesTxt;

    private JButton searchBtn;
    private JButton clearBtn;
    private JButton saveBtn;

    private DefaultTableModel tableModel;
    private JTable ticketsTable;
    private CardLayout tableCardLayout;
    private JPanel tableCardHost;
    private JLabel searchContextLbl;

    private ArrayList<Ticket> searchResults = new ArrayList<>();

    public VisitScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(0, LEFT_MARGIN, LEFT_MARGIN, LEFT_MARGIN));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        content.add(buildFormCard());
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(buildTableCard());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildFormCard() {
        JPanel card = roundedCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titleLbl = new JLabel("Visit Details");
        titleLbl.setFont(new Font("Inter", Font.BOLD, 18));
        titleLbl.setForeground(Color.BLACK);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        JLabel idLbl = fieldLabel("Identification Number:");
        idNumberTxt = new JTextField();
        styleInput(idNumberTxt);

        searchBtn = new RoundedButton("Search", BUTTON_CORNER_RADIUS) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, FIELD_HEIGHT);
            }
        };
        searchBtn.setFont(new Font("Inter", Font.BOLD, 12));
        searchBtn.setBackground(Color.WHITE);
        searchBtn.setForeground(GREEN_ACCENT);
        ((RoundedButton) searchBtn).setOutlineColor(GREEN_ACCENT);
        searchBtn.setMaximumSize(searchBtn.getPreferredSize());
        searchBtn.setMinimumSize(searchBtn.getPreferredSize());
        searchBtn.addActionListener(this::handleSearch);

        JLabel dateLbl = fieldLabel("Visit Date:");
        visitDateTxt = new JTextField(LocalDate.now().format(DATE_FORMAT));
        styleInput(visitDateTxt);

        JLabel timeLbl = fieldLabel("Visit Time:");
        visitTimeTxt = new JTextField(LocalTime.now().format(TIME_FORMAT));
        styleInput(visitTimeTxt);

        JLabel notesLbl = fieldLabel("Notes:");
        notesTxt = new JTextArea(4, 20);
        notesTxt.setLineWrap(true);
        notesTxt.setWrapStyleWord(true);
        notesTxt.setFont(new Font("Inter", Font.PLAIN, 14));
        notesTxt.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane notesScroll = new JScrollPane(notesTxt);
        notesScroll.setBorder(INPUT_BORDER_NORMAL);
        notesScroll.setPreferredSize(new Dimension(FIELD_WIDTH, 100));
        notesScroll.setMinimumSize(new Dimension(FIELD_WIDTH, 100));
        notesScroll.setMaximumSize(new Dimension(FIELD_WIDTH, 100));
        addFocusBorderSwap(notesTxt, notesScroll);

        gbc.gridy = 0;
        gbc.gridx = 0;
        formGrid.add(idLbl, gbc);
        gbc.gridx = 1;
        JPanel idRow = new JPanel();
        idRow.setLayout(new BoxLayout(idRow, BoxLayout.X_AXIS));
        idRow.setOpaque(false);
        idRow.add(idNumberTxt);
        idRow.add(Box.createHorizontalStrut(10));
        idRow.add(searchBtn);
        formGrid.add(idRow, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formGrid.add(dateLbl, gbc);
        gbc.gridx = 1;
        formGrid.add(visitDateTxt, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        formGrid.add(timeLbl, gbc);
        gbc.gridx = 1;
        formGrid.add(visitTimeTxt, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formGrid.add(notesLbl, gbc);
        gbc.gridx = 1;
        formGrid.add(notesScroll, gbc);

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonsRow.setOpaque(false);
        buttonsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        clearBtn = new RoundedButton("Clear", BUTTON_CORNER_RADIUS);
        clearBtn.setFont(new Font("Inter", Font.BOLD, 13));
        clearBtn.setBackground(Color.WHITE);
        clearBtn.setForeground(ACCENT_COLOR);
        ((RoundedButton) clearBtn).setOutlineColor(BORDER_COLOR);
        clearBtn.addActionListener(e -> clearForm());

        saveBtn = new RoundedButton("Save Visit", BUTTON_CORNER_RADIUS);
        saveBtn.setFont(new Font("Inter", Font.BOLD, 13));
        saveBtn.setBackground(ACCENT_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(this::handleSaveVisit);

        buttonsRow.add(clearBtn);
        buttonsRow.add(saveBtn);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(formGrid);
        card.add(buttonsRow);

        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = roundedCard();
        card.setLayout(new BorderLayout());

        String[] columns = {"TICKET NUMBER", "DEPARTMENT", "STATUS", "DATE", "TIME"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketsTable = new JTable(tableModel);
        ticketsTable.setFont(new Font("Inter", Font.PLAIN, 13));
        ticketsTable.setForeground(Color.DARK_GRAY);
        ticketsTable.setRowHeight(36);
        ticketsTable.setShowGrid(false);
        ticketsTable.setIntercellSpacing(new Dimension(0, 0));
        ticketsTable.setTableHeader(null);
        ticketsTable.setFillsViewportHeight(true);
        ticketsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ticketsTable.setSelectionBackground(new Color(230, 240, 235));
        ticketsTable.setSelectionForeground(Color.DARK_GRAY);

        JScrollPane tableScroll = new JScrollPane(ticketsTable);
        tableScroll.setBorder(null);
        tableScroll.setPreferredSize(new Dimension(0, 240));

        JLabel emptyStateLbl = new JLabel("Search a patient to see their tickets.", SwingConstants.CENTER);
        emptyStateLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        emptyStateLbl.setForeground(MUTED_GRAY);

        JPanel emptyStatePanel = new JPanel(new GridBagLayout());
        emptyStatePanel.setBackground(Color.WHITE);
        emptyStatePanel.setPreferredSize(new Dimension(0, 240));
        emptyStatePanel.add(emptyStateLbl);

        tableCardLayout = new CardLayout();
        tableCardHost = new JPanel(tableCardLayout);
        tableCardHost.add(emptyStatePanel, "EMPTY");
        tableCardHost.add(tableScroll, "TABLE");
        tableCardLayout.show(tableCardHost, "EMPTY");

        searchContextLbl = new JLabel(" ");
        searchContextLbl.setFont(new Font("Inter", Font.BOLD, 12));
        searchContextLbl.setForeground(GREEN_ACCENT);
        searchContextLbl.setBorder(BorderFactory.createEmptyBorder(14, 20, 0, 20));

        JPanel headerColumn = new JPanel();
        headerColumn.setLayout(new BoxLayout(headerColumn, BoxLayout.Y_AXIS));
        headerColumn.setBackground(new Color(250, 250, 251));
        headerColumn.add(searchContextLbl);
        headerColumn.add(buildTableHeaderRow());

        card.add(headerColumn, BorderLayout.NORTH);
        card.add(tableCardHost, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildTableHeaderRow() {
        JPanel header = new JPanel(new GridLayout(1, 5));
        header.setBackground(new Color(250, 250, 251));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        for (String label : new String[]{"TICKET NUMBER", "DEPARTMENT", "STATUS", "DATE", "TIME"}) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Inter", Font.BOLD, 12));
            lbl.setForeground(TABLE_HEADER_COLOR);
            header.add(lbl);
        }

        return header;
    }

    private void handleSearch(ActionEvent e) {
        String idNumber = idNumberTxt.getText().trim();

        if (idNumber.isEmpty()) {
            showStyledMessageDialog("Missing Information", "Please enter an identification number to search for.");
            return;
        }

        searchResults = TicketDAO.findAllTicketsByIdentificationNumber(idNumber);
        tableModel.setRowCount(0);

        if (searchResults.isEmpty()) {
            tableCardLayout.show(tableCardHost, "EMPTY");
            searchContextLbl.setText(" ");
            showStyledMessageDialog("No Tickets Found", "No tickets exist for this identification number.");
            return;
        }

        for (Ticket t : searchResults) {
            tableModel.addRow(new Object[]{
                t.getTicketNumber(), t.getDepartment(), t.getQueueStatus(), t.getIssueDate(), t.getIssueTime()
            });
        }
        searchContextLbl.setText("Showing tickets for: " + idNumber);
        tableCardLayout.show(tableCardHost, "TABLE");
    }

    private void handleSaveVisit(ActionEvent e) {
        int selectedRow = ticketsTable.getSelectedRow();

        if (selectedRow == -1) {
            showStyledMessageDialog("No Ticket Selected",
                    "Please search for a patient and select which ticket this visit note belongs to.");
            return;
        }

        Ticket selectedTicket = searchResults.get(selectedRow);
        String notes = notesTxt.getText().trim();

        showStyledMessageDialog("Not Yet Available",
                "Saving visit notes isn't available in this build yet - the Visit table and its DAO "
                + "haven't been added to the project.");
    }

    private void clearForm() {
        idNumberTxt.setText("");
        notesTxt.setText("");
        visitDateTxt.setText(LocalDate.now().format(DATE_FORMAT));
        visitTimeTxt.setText(LocalTime.now().format(TIME_FORMAT));
        searchResults.clear();
        tableModel.setRowCount(0);
        searchContextLbl.setText(" ");
        tableCardLayout.show(tableCardHost, "EMPTY");
    }

    private JPanel roundedCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
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
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    private void styleInput(JTextField input) {
        input.setFont(new Font("Inter", Font.PLAIN, 14));
        input.setBorder(INPUT_BORDER_NORMAL);

        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        input.setPreferredSize(size);
        input.setMinimumSize(size);
        input.setMaximumSize(size);

        addFocusBorderSwap(input, input);
    }

    private void addFocusBorderSwap(JComponent input, JComponent borderTarget) {
        input.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                borderTarget.setBorder(INPUT_BORDER_FOCUSED);
            }

            @Override
            public void focusLost(FocusEvent e) {
                borderTarget.setBorder(INPUT_BORDER_NORMAL);
            }
        });
    }

    private void showStyledMessageDialog(String title, String message) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner);
        dialog.setModal(true);
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
        accentStrip.setBackground(GREEN_ACCENT);
        accentStrip.setPreferredSize(new Dimension(4, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Inter", Font.BOLD, 16));
        titleLbl.setForeground(ACCENT_COLOR);

        JLabel messageLbl = new JLabel("<html><body style='width: 260px'>" + message + "</body></html>");
        messageLbl.setFont(new Font("Inter", Font.PLAIN, 14));
        messageLbl.setForeground(Color.DARK_GRAY);
        messageLbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        JButton okBtn = new RoundedButton("OK", BUTTON_CORNER_RADIUS);
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
        private boolean hovered = false;
        private Color outlineColor;

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

        void setOutlineColor(Color outlineColor) {
            this.outlineColor = outlineColor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? getBackground().darker() : getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            if (outlineColor != null) {
                g2.setColor(outlineColor);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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
}
