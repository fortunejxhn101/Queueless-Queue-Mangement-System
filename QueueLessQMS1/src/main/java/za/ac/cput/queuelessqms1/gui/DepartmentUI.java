package za.ac.cput.queuelessqms1.gui;

import za.ac.cput.queuelessqms1.dao.DepartmentDAO;
import za.ac.cput.queuelessqms1.domain.Department;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
/**
 *
 * @author yantolo
 * 
 */
public class DepartmentUI extends JFrame{
    
    static{
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
    
    private static final Color NAVY = new Color(27, 58, 107);
    private static final Color GREEN = new Color(103, 172, 118);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GREY = new Color(244, 246, 249);
    private static final Color TEXT_GREY = new Color(68, 68, 68);
    private static final Color RED_COL = new Color(192, 57, 43);
    
    private JTextField txtCode = new JTextField(15);
    private JTextField txtName = new JTextField(25);
    
    private JButton btnAdd    = createButton("Add",      new Color(39, 174, 96));
    private JButton btnSearch = createButton("Search",   new Color(41, 128, 185));
    private JButton btnUpdate = createButton("Update",   new Color(230, 126, 34));
    private JButton btnDelete = createButton("Delete",   RED_COL);
    private JButton btnClear  = createButton("Clear",    new Color(127, 140, 141));
    private JButton btnShowAll = createButton("Show All", new Color(142, 68, 173));
 
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblStatus = new JLabel(" ");
    private DepartmentDAO dao = new DepartmentDAO();
 
    public DepartmentUI() {
        super("QueueLess - Department Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
 
        setLayout(new BorderLayout());
        add(buildTitleBar(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
 
        wireButtons();
        loadTable();
        setVisible(true);
    }
    
    private JPanel buildTitleBar() {
        JLabel title = new JLabel("Department Management");
        title.setFont(new Font("Inter", Font.BOLD, 20));
        title.setForeground(WHITE);
 
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        bar.setBackground(NAVY);
        bar.add(title);
        return bar;
    }
 
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel();
        center.setBackground(LIGHT_GREY);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(20, 20, 20, 20));
        center.add(buildFormPanel());
        center.add(Box.createRigidArea(new Dimension(0, 12)));
        center.add(buildButtonPanel());
        center.add(Box.createRigidArea(new Dimension(0, 8)));
        center.add(buildStatusPanel());
        center.add(Box.createRigidArea(new Dimension(0, 12)));
        center.add(buildTablePanel());
        return center;
    }
    
    private JPanel buildFormPanel() {
        JPanel form = new JPanel();
        form.setBackground(WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(208, 213, 221), 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
 
        form.add(buildFieldRow("Department Code:", txtCode));
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(buildFieldRow("Department Name:", txtName));
 
        return form;
    }
    
    private JPanel buildFieldRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
 
        JLabel lbl = makeLabel(labelText);
        lbl.setPreferredSize(new Dimension(160, 32));
        styleField(field);
 
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
    
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.add(btnAdd);
        panel.add(btnSearch);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnShowAll);
        panel.add(btnClear);
        return panel;
    }
 
    private JPanel buildStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        lblStatus.setFont(new Font("Inter", Font.PLAIN, 13));
        lblStatus.setForeground(TEXT_GREY);
        panel.add(lblStatus);
        return panel;
    }
 
    private JPanel buildTablePanel() {
        tableModel = new DefaultTableModel(
            new String[]{"Department Code", "Department Name"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Inter", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(214, 228, 248));
        table.setSelectionForeground(Color.BLACK);
        table.setBackground(WHITE);
        table.setFillsViewportHeight(true);
 
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? WHITE : LIGHT_GREY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        });
        
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(val != null ? val.toString() : "");
                lbl.setFont(new Font("Inter", Font.BOLD, 13));
                lbl.setForeground(WHITE);
                lbl.setBackground(NAVY);
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(0, 12, 0, 12));
                lbl.setPreferredSize(new Dimension(0, 36));
                return lbl;
            }
        });
        header.setBackground(NAVY);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtCode.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
            }
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(208, 213, 221), 1));
 
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
 
    private void wireButtons() {
        btnAdd.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            String name = txtName.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                showStatus("Please fill in both fields.", RED_COL); return;
            }
            if (code.length() > 10) {
                showStatus("Department code must be 10 characters or less.", RED_COL); return;
            }
            if (dao.addDepartment(new Department(code, name))) {
                showStatus("Department added successfully.", GREEN);
                loadTable(); clearFields();
            } else {
                showStatus("Failed. Code may already exist.", RED_COL);
            }
        });
        
        btnSearch.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            if (code.isEmpty()) {
                showStatus("Enter a department code to search.", RED_COL); return;
            }
            Department dept = dao.getDepartment(code);
            if (dept != null) {
                txtName.setText(dept.getDepartmentName());
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{dept.getDepartmentCode(), dept.getDepartmentName()});
                showStatus("Department found.", GREEN);
            } else {
                showStatus("No department found with code: " + code, RED_COL);
            }
        });
        
        btnUpdate.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            String name = txtName.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                showStatus("Please fill in both fields to update.", RED_COL); return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Update department " + code + " to '" + name + "'?",
                "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.updateDepartment(new Department(code, name))) {
                    showStatus("Department updated successfully.", GREEN);
                    loadTable(); clearFields();
                } else {
                    showStatus("Update failed. Code not found.", RED_COL);
                }
            }
        });
        
        btnDelete.addActionListener(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            if (code.isEmpty()) {
                showStatus("Enter a department code to delete.", RED_COL); return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete department: " + code + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteDepartment(code)) {
                    showStatus("Department deleted successfully.", GREEN);
                    loadTable(); clearFields();
                } else {
                    showStatus("Delete failed. Code not found.", RED_COL);
                }
            }
        });
        
        btnShowAll.addActionListener(e -> {
            loadTable();
            showStatus("Showing all departments.", GREEN);
        });
 
        btnClear.addActionListener(e -> {
            clearFields();
            showStatus(" ", TEXT_GREY);
            loadTable();
        });
    }
    
    private void loadTable() {
        tableModel.setRowCount(0);
        List<Department> all = dao.getAllDepartments();
        for (Department d : all) {
            tableModel.addRow(new Object[]{d.getDepartmentCode(), d.getDepartmentName()});
        }
    }
 
    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        table.clearSelection();
    }
 
    private void showStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }
 
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Inter", Font.BOLD, 13));
        lbl.setForeground(TEXT_GREY);
        return lbl;
    }
    
    private void styleField(JTextField field) {
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(208, 213, 221), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }
 
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 34));
        return btn;
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new DepartmentUI();
        });
    }
}

