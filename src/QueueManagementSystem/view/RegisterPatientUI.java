package QueueManagementSystem.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class RegisterPatientUI extends JFrame {

    private JLabel titleLabel;

    private JLabel patientName;
    private JLabel patientSurName;
    private JLabel dateOfBirthLabel;
    private JLabel genderLabel;
    private JLabel phoneNumber;
    private JLabel department;

    private JTextField nameTxt;
    private JTextField surnameTxt;
    private JTextField phoneNumberTxt;

    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> departmentComboBox;

    private JRadioButton maleRadio;
    private JRadioButton femaleRadio;
    private ButtonGroup genderGroup;

    private JButton registerBtn;
    private JButton clearBtn;

    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel dobPanel;
    private JPanel genderPanel;
    private JPanel buttonPanel;

    public RegisterPatientUI() {
        super("Patient Registration");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Header
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(94, 151, 50));
        titleLabel = new JLabel("Patient Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(237, 237, 237));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Labels
        patientName = new JLabel("Name:");
        patientSurName = new JLabel("Surname:");
        dateOfBirthLabel = new JLabel("Date of Birth:");
        genderLabel = new JLabel("Gender:");
        phoneNumber = new JLabel("Phone:");
        department = new JLabel("Department:");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        patientName.setFont(labelFont);
        patientSurName.setFont(labelFont);
        dateOfBirthLabel.setFont(labelFont);
        genderLabel.setFont(labelFont);
        phoneNumber.setFont(labelFont);
        department.setFont(labelFont);

        // Text fields
        nameTxt = new JTextField(20);
        surnameTxt = new JTextField(20);
        phoneNumberTxt = new JTextField(20);

        Font fieldFont = new Font("Arial", Font.PLAIN, 15);
        nameTxt.setFont(fieldFont);
        surnameTxt.setFont(fieldFont);
        phoneNumberTxt.setFont(fieldFont);

        // Date of Birth combo boxes
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }

        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };

        String[] years = new String[80];
        int startYear = 2026;
        for (int i = 0; i < 80; i++) {
            years[i] = String.valueOf(startYear - i);
        }

        dayComboBox = new JComboBox<>(days);
        monthComboBox = new JComboBox<>(months);
        yearComboBox = new JComboBox<>(years);

        dayComboBox.setFont(fieldFont);
        monthComboBox.setFont(fieldFont);
        yearComboBox.setFont(fieldFont);

        dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dobPanel.setBackground(new Color(237, 237, 237));
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);

        // Gender radio buttons
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");

        maleRadio.setBackground(new Color(237, 237, 237));
        femaleRadio.setBackground(new Color(237, 237, 237));
        maleRadio.setFont(fieldFont);
        femaleRadio.setFont(fieldFont);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        genderPanel.setBackground(new Color(237, 237, 237));
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);

        String[] departments = {
            "Select Department",
            "General",
            "Emergency",
            "Pediatrics",
            "Pharmacy",
            "Radiology",
            "Laboratory"
        };

        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setFont(fieldFont);

        formPanel = new JPanel(new GridLayout(6, 2, 15, 20));
        formPanel.setBackground(new Color(237, 237, 237));

        formPanel.add(patientName);
        formPanel.add(nameTxt);

        formPanel.add(patientSurName);
        formPanel.add(surnameTxt);

        formPanel.add(dateOfBirthLabel);
        formPanel.add(dobPanel);

        formPanel.add(genderLabel);
        formPanel.add(genderPanel);

        formPanel.add(phoneNumber);
        formPanel.add(phoneNumberTxt);

        formPanel.add(department);
        formPanel.add(departmentComboBox);

        registerBtn = new JButton("Register");
        clearBtn = new JButton("Clear");

        registerBtn.setFont(new Font("Arial", Font.BOLD, 15));
        clearBtn.setFont(new Font("Arial", Font.BOLD, 15));

        registerBtn.setBackground(new Color(94, 151, 50));
        registerBtn.setForeground(Color.BLACK);

        clearBtn.setBackground(new Color(47, 105, 199));
        clearBtn.setForeground(Color.BLACK);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(237, 237, 237));
        buttonPanel.add(registerBtn);
        buttonPanel.add(clearBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTxt.setText("");
                surnameTxt.setText("");
                phoneNumberTxt.setText("");
                dayComboBox.setSelectedIndex(0);
                monthComboBox.setSelectedIndex(0);
                yearComboBox.setSelectedIndex(0);
                departmentComboBox.setSelectedIndex(0);
                genderGroup.clearSelection();
            }
        });
    }
}
