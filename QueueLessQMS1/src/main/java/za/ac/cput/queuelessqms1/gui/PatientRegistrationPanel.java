package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.time.Month;
import za.ac.cput.queuelessqms1.domain.Patient;
import za.ac.cput.queuelessqms1.domain.CurrentPatient;
import za.ac.cput.queuelessqms1.dao.*;

/**
 * @author admin Fortune 
 */
public class PatientRegistrationPanel extends JPanel {

    private JLabel patientHeaderLbl;
    private JLabel firstNameLbl;
    private JLabel lastNameLbl;
    private JLabel dateOfBirth;
    private JLabel genderLbl;
    private JLabel idNumberLbl;
    private JLabel phoneNumberLbl;
    private JLabel departmentLbl;
    private JLabel triageLevelLbl;

    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JTextField idNumberTxt;
    private JTextField phoneNumberTxt;

    private JComboBox yearcmBoBox;
    private JComboBox monthcmBoBox;
    private JComboBox daycmBoBox;

    private JRadioButton male;
    private JRadioButton female;
    private ButtonGroup genderButtons;

    private JComboBox departmentCmboBox;
    private JComboBox triageLevelCmboBox;

    private JButton registerBtn;
    private JButton updateBtn;
    private JButton clearBtn;
    private JButton findPatientBtn;

    private JPanel patientHeaderPanel;
    private JPanel datePanel;
    private JPanel genderPanel;
    private JPanel formComponentsPanel;
    private JPanel buttonsPanel;

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Color GREEN_ACCENT = new Color(103, 172, 118);
    private static final Color GREEN_ACCENT_DARK = new Color(84, 148, 98);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color LOCKED_FIELD_BACKGROUND = new Color(240, 241, 244);
    private static final Color LOCKED_FIELD_BORDER = new Color(200, 203, 209);
    private static final int INPUT_CORNER_RADIUS = 8;
    private static final int BUTTON_CORNER_RADIUS = 10;
    private static final int LEFT_MARGIN = 24;
    private static final int SIDEBAR_LEFT_MARGIN = 32;

    private static final int FIELD_WIDTH = 500;
    private static final int FIELD_HEIGHT = 36;

    private static final Border INPUT_BORDER_NORMAL = new RoundedBorder(BORDER_COLOR, INPUT_CORNER_RADIUS);
    private static final Border INPUT_BORDER_FOCUSED = new RoundedBorder(GREEN_ACCENT, INPUT_CORNER_RADIUS);

    public PatientRegistrationPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        patientHeaderLbl = new JLabel("Patient Registration");
        patientHeaderLbl.setFont(new Font("Inter", Font.BOLD, 22));

        firstNameLbl = new JLabel("First Name: ");
        firstNameLbl.setFont(new Font("Inter", Font.BOLD, 16));

        lastNameLbl = new JLabel("Last Name: ");
        lastNameLbl.setFont(new Font("Inter", Font.BOLD, 16));

        dateOfBirth = new JLabel("Date of Birth: ");
        dateOfBirth.setFont(new Font("Inter", Font.BOLD, 16));

        genderLbl = new JLabel("Gender: ");
        genderLbl.setFont(new Font("Inter", Font.BOLD, 16));

        idNumberLbl = new JLabel("ID Number: ");
        idNumberLbl.setFont(new Font("Inter", Font.BOLD, 16));

        phoneNumberLbl = new JLabel("Phone Number: ");
        phoneNumberLbl.setFont(new Font("Inter", Font.BOLD, 16));

        departmentLbl = new JLabel("Department: ");
        departmentLbl.setFont(new Font("Inter", Font.BOLD, 16));

        triageLevelLbl = new JLabel("Triage Level: ");
        triageLevelLbl.setFont(new Font("Inter", Font.BOLD, 16));

        firstNameTxt = new JTextField(20);
        lastNameTxt = new JTextField(20);
        idNumberTxt = new JTextField(20);
        phoneNumberTxt = new JTextField(15);

        findPatientBtn = new RoundedButton("Find Patient", BUTTON_CORNER_RADIUS) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, FIELD_HEIGHT);
            }
        };
        findPatientBtn.setFont(new Font("Inter", Font.BOLD, 12));
        findPatientBtn.setBackground(Color.WHITE);
        findPatientBtn.setForeground(GREEN_ACCENT);
        ((RoundedButton) findPatientBtn).setOutlineColor(GREEN_ACCENT);
        findPatientBtn.setMaximumSize(findPatientBtn.getPreferredSize());
        findPatientBtn.setMinimumSize(findPatientBtn.getPreferredSize());

        male = new JRadioButton("Male");
        male.setFont(new Font("Inter", Font.BOLD, 13));
        styleRadioButton(male);

        female = new JRadioButton("Female");
        female.setFont(new Font("Inter", Font.BOLD, 13));
        styleRadioButton(female);

        String[] years = new String[80];
        int startYear = 2026;
        for (int i = 0; i < 80; i++) {
            years[i] = String.valueOf(startYear - i);
        }
        yearcmBoBox = new JComboBox(years);
        yearcmBoBox.setFont(new Font("Inter", Font.PLAIN, 12));

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthcmBoBox = new JComboBox(months);
        monthcmBoBox.setFont(new Font("Inter", Font.PLAIN, 12));

        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        daycmBoBox = new JComboBox(days);
        daycmBoBox.setFont(new Font("Inter", Font.PLAIN, 12));

        String[] dep = {"Select Department", "General", "Emergency", "Pediatrics", "Pharmacy", "Radiology", "Laboratory"};
        departmentCmboBox = new JComboBox(dep);
        departmentCmboBox.setFont(new Font("Inter", Font.PLAIN, 12));

        String[] triageLvl = {"Select Triage", "P1 - Critical", "P2 - Urgent", "P3 - Routine", "Non - Urgent"};
        triageLevelCmboBox = new JComboBox(triageLvl);
        triageLevelCmboBox.setFont(new Font("Inter", Font.PLAIN, 12));

        JComponent[] fullWidthInputs = {firstNameTxt, lastNameTxt, idNumberTxt, phoneNumberTxt, departmentCmboBox, triageLevelCmboBox};
        for (JComponent input : fullWidthInputs) {
            styleInput(input);
        }

        JComboBox[] dateComboBoxes = {daycmBoBox, monthcmBoBox, yearcmBoBox};
        for (JComboBox dateBox : dateComboBoxes) {
            styleDateComboBox(dateBox);
        }

        registerBtn = new RoundedButton("Register Patient", BUTTON_CORNER_RADIUS);
        registerBtn.setFont(new Font("Inter", Font.BOLD, 13));
        registerBtn.setBackground(ACCENT_COLOR);
        registerBtn.setForeground(Color.WHITE);

        updateBtn = new RoundedButton("Update Patient", BUTTON_CORNER_RADIUS);
        updateBtn.setFont(new Font("Inter", Font.BOLD, 13));
        updateBtn.setBackground(GREEN_ACCENT);
        updateBtn.setForeground(Color.WHITE);

        clearBtn = new RoundedButton("Clear", BUTTON_CORNER_RADIUS);
        clearBtn.setFont(new Font("Inter", Font.BOLD, 13));
        clearBtn.setBackground(Color.WHITE);
        clearBtn.setForeground(ACCENT_COLOR);
        ((RoundedButton) clearBtn).setOutlineColor(BORDER_COLOR);

        genderButtons = new ButtonGroup();
        genderButtons.add(male);
        genderButtons.add(female);

        patientHeaderPanel = new JPanel();
        patientHeaderPanel.setBackground(Color.WHITE);
        patientHeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        patientHeaderPanel.setBorder(BorderFactory.createEmptyBorder(22, SIDEBAR_LEFT_MARGIN, 15, LEFT_MARGIN));
        patientHeaderPanel.add(patientHeaderLbl);

        datePanel = new JPanel();
        datePanel.setLayout(new GridLayout(1, 3, 8, 0));
        datePanel.setBackground(Color.WHITE);
        Dimension dateRowSize = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        datePanel.setPreferredSize(dateRowSize);
        datePanel.setMinimumSize(dateRowSize);
        datePanel.setMaximumSize(dateRowSize);
        datePanel.add(daycmBoBox);
        datePanel.add(monthcmBoBox);
        datePanel.add(yearcmBoBox);

        genderPanel = new JPanel();
        genderPanel.setBackground(Color.WHITE);
        genderPanel.setLayout(new BoxLayout(genderPanel, BoxLayout.X_AXIS));
        genderPanel.add(male);
        genderPanel.add(Box.createHorizontalStrut(5));
        genderPanel.add(female);

        formComponentsPanel = new JPanel();
        formComponentsPanel.setBackground(Color.WHITE);
        formComponentsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        gbc.gridy = 0;
        gbc.gridx = 0;
        formComponentsPanel.add(firstNameLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(firstNameTxt, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formComponentsPanel.add(lastNameLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(lastNameTxt, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        formComponentsPanel.add(dateOfBirth, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(datePanel, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        formComponentsPanel.add(genderLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(genderPanel, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        formComponentsPanel.add(idNumberLbl, gbc);
        gbc.gridx = 1;
        JPanel idNumberRow = new JPanel();
        idNumberRow.setLayout(new BoxLayout(idNumberRow, BoxLayout.X_AXIS));
        idNumberRow.setBackground(Color.WHITE);
        idNumberRow.add(idNumberTxt);
        idNumberRow.add(Box.createHorizontalStrut(10));
        idNumberRow.add(findPatientBtn);
        formComponentsPanel.add(idNumberRow, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        formComponentsPanel.add(phoneNumberLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(phoneNumberTxt, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        formComponentsPanel.add(departmentLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(departmentCmboBox, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        formComponentsPanel.add(triageLevelLbl, gbc);
        gbc.gridx = 1;
        formComponentsPanel.add(triageLevelCmboBox, gbc);

        Dimension departmentReferenceSize = departmentCmboBox.getPreferredSize();
        JTextField[] fieldsToMatchDepartmentWidth = {firstNameTxt, lastNameTxt, idNumberTxt, phoneNumberTxt};
        for (JTextField field : fieldsToMatchDepartmentWidth) {
            field.setPreferredSize(departmentReferenceSize);
            field.setMinimumSize(departmentReferenceSize);
            field.setMaximumSize(departmentReferenceSize);
        }

        buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, LEFT_MARGIN, 30, LEFT_MARGIN));
        buttonsPanel.add(clearBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(registerBtn);

        add(patientHeaderPanel, BorderLayout.NORTH);
        add(formComponentsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        registerBtn.addActionListener(this::handleRegister);
        updateBtn.addActionListener(this::handleUpdate);
        findPatientBtn.addActionListener(this::handleFindPatient);
        clearBtn.addActionListener(e -> clearForm());

        updateBtn.setEnabled(false);
    }

    private void handleFindPatient(ActionEvent e) {
        
        String idNumber = idNumberTxt.getText().trim();

        if (idNumber.isEmpty()) {
            showStyledMessageDialog("Missing Information", "Please enter an identification number to search for.");
            return;
        }

        String idNumberError = validateIdNumber(idNumber);
        if (idNumberError != null) {
            showStyledMessageDialog(idNumberError);
            return;
        }

        PatientDAO patientDao = new PatientDAO();
        Patient found = patientDao.findPatientByIdNumber(idNumber);

        if (found == null) {
            showStyledMessageDialog("Patient Not Found", "No patient record exists under this identification number.");
            updateBtn.setEnabled(false);
            return;
        }

        firstNameTxt.setText(found.getFirstName());
        lastNameTxt.setText(found.getLastName());
        phoneNumberTxt.setText(found.getPhoneNumber());

        LocalDate dob = found.getDateOfBirth();
        String monthName = dob.getMonth().toString();
        String monthTitleCase = monthName.charAt(0) + monthName.substring(1).toLowerCase();
        daycmBoBox.setSelectedItem(String.valueOf(dob.getDayOfMonth()));
        monthcmBoBox.setSelectedItem(monthTitleCase);
        yearcmBoBox.setSelectedItem(String.valueOf(dob.getYear()));

        genderButtons.clearSelection();
        if ("Male".equalsIgnoreCase(found.getGender())) {
            male.setSelected(true);
        } else if ("Female".equalsIgnoreCase(found.getGender())) {
            female.setSelected(true);
        }

        lockIdNumberField();
        updateBtn.setEnabled(true);

        String department = departmentCmboBox.getSelectedItem().toString();
        String triageLevel = triageLevelCmboBox.getSelectedItem().toString();
        boolean departmentAndTriageProvided = !department.equals("Select Department")
                && !triageLevel.equals("Select Triage");

        if (departmentAndTriageProvided) {
            CurrentPatient.set(idNumber, department, triageLevel);
            showStyledMessageDialog("Patient Found",
                    "Loaded the existing record for " + found.getFirstName() + " " + found.getLastName()
                    + ". Patient Updated Successfully. You can now generate their ticket.");
        } else {
            showStyledMessageDialog("Patient Found",
                    "Loaded the existing record for " + found.getFirstName() + " " + found.getLastName()
                    + ". You can now edit their details and click Update Patient.");
        }
    }

    private void handleUpdate(ActionEvent e) {
        
        String firstName = firstNameTxt.getText().trim();
        String lastName = lastNameTxt.getText().trim();
        String idNumber = idNumberTxt.getText().trim();
        String phoneNumber = phoneNumberTxt.getText().trim().replaceAll("[\\s\\-()+]", "");
        String department = departmentCmboBox.getSelectedItem().toString();
        String triageLevel = triageLevelCmboBox.getSelectedItem().toString();

        if (firstName.isEmpty() && lastName.isEmpty() && idNumber.isEmpty() && phoneNumber.isEmpty()
                && !male.isSelected() && !female.isSelected()
                && department.equals("Select Department") && triageLevel.equals("Select Triage")) {
            showStyledMessageDialog("Please fill in the patient's details before updating");
            return;
        }

        String firstNameError = validateName("First name", firstName);
        if (firstNameError != null) {
            showStyledMessageDialog(firstNameError);
            return;
        }

        String lastNameError = validateName("Last name", lastName);
        if (lastNameError != null) {
            showStyledMessageDialog(lastNameError);
            return;
        }

        LocalDate birthDate;
        try {
            String dayString = daycmBoBox.getSelectedItem().toString();
            String monthString = monthcmBoBox.getSelectedItem().toString();
            String yearString = yearcmBoBox.getSelectedItem().toString();

            int dayNum = Integer.parseInt(dayString);
            int monthNum = Month.valueOf(monthString.toUpperCase()).getValue();
            int yearNum = Integer.parseInt(yearString);

            birthDate = LocalDate.of(yearNum, monthNum, dayNum);
        } catch (Exception ex) {
            showStyledMessageDialog("Please select a valid date of birth");
            return;
        }

        if (birthDate.isAfter(LocalDate.now())) {
            showStyledMessageDialog("Date of birth cannot be in the future");
            return;
        }

        String gender = "";
        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()) {
            gender = "Female";
        }

        if (!male.isSelected() && !female.isSelected()) {
            showStyledMessageDialog("Select a Gender for the Patient");
            return;
        }

        String idNumberError = validateIdNumber(idNumber);
        if (idNumberError != null) {
            showStyledMessageDialog(idNumberError);
            return;
        }

        try {
            PatientDAO existsCheckDao = new PatientDAO();
            if (!existsCheckDao.isIdNumberTaken(idNumber)) {
                showStyledMessageDialog("Patient Not Found",
                        "No patient record exists under this identification number to update.");
                return;
            }
        } catch (Exception ex) {
            showStyledMessageDialog("Verification Unavailable",
                    "ID verification is temporarily unavailable. Please attempt the update again in a moment.");
            return;
        }

        String phoneNumberError = validatePhoneNumber(phoneNumber);
        if (phoneNumberError != null) {
            showStyledMessageDialog(phoneNumberError);
            return;
        }

        boolean departmentAndTriageProvided = !department.equals("Select Department")
                && !triageLevel.equals("Select Triage");

        updateBtn.setEnabled(false);
        try {
            PatientDAO patient = new PatientDAO();
            Patient patientRecord = new Patient(idNumber, firstName, lastName, phoneNumber, birthDate, gender);
            patient.updatePatientRecord(patientRecord);

            if (departmentAndTriageProvided) {
                CurrentPatient.set(idNumber, department, triageLevel);
                showStyledMessageDialog("Success",
                        "Patient Updated Successfully. You can now generate their ticket.");
            } else {
                showStyledMessageDialog("Success", "Patient Updated Successfully.");
            }
        } catch (Exception ex) {
            showStyledMessageDialog("Update Failed",
                    "Something went wrong while updating the patient. Please try again.");
        } finally {
            updateBtn.setEnabled(true);
        }
    }

    private void handleRegister(ActionEvent e) {
        
        String firstName = firstNameTxt.getText().trim();
        String lastName = lastNameTxt.getText().trim();
        String idNumber = idNumberTxt.getText().trim();
        String phoneNumber = phoneNumberTxt.getText().trim().replaceAll("[\\s\\-()+]", "");
        String department = departmentCmboBox.getSelectedItem().toString();
        String triageLevel = triageLevelCmboBox.getSelectedItem().toString();

        if (firstName.isEmpty() && lastName.isEmpty() && idNumber.isEmpty() && phoneNumber.isEmpty()
                && !male.isSelected() && !female.isSelected()
                && department.equals("Select Department") && triageLevel.equals("Select Triage")) {
            showStyledMessageDialog("Please fill in the patient's details before registering");
            return;
        }

        String firstNameError = validateName("First name", firstName);
        if (firstNameError != null) {
            showStyledMessageDialog(firstNameError);
            return;
        }

        String lastNameError = validateName("Last name", lastName);
        if (lastNameError != null) {
            showStyledMessageDialog(lastNameError);
            return;
        }

        LocalDate birthDate;
        try {
            String dayString = daycmBoBox.getSelectedItem().toString();
            String monthString = monthcmBoBox.getSelectedItem().toString();
            String yearString = yearcmBoBox.getSelectedItem().toString();

            int dayNum = Integer.parseInt(dayString);
            int monthNum = Month.valueOf(monthString.toUpperCase()).getValue();
            int yearNum = Integer.parseInt(yearString);

            birthDate = LocalDate.of(yearNum, monthNum, dayNum);
        } catch (Exception ex) {
            showStyledMessageDialog("Please select a valid date of birth");
            return;
        }

        if (birthDate.isAfter(LocalDate.now())) {
            showStyledMessageDialog("Date of birth cannot be in the future");
            return;
        }

        String gender = "";
        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()) {
            gender = "Female";
        }

        if (!male.isSelected() && !female.isSelected()) {
            showStyledMessageDialog("Select a Gender for the Patient");
            return;
        }

        String idNumberError = validateIdNumber(idNumber);
        if (idNumberError != null) {
            showStyledMessageDialog(idNumberError);
            return;
        }

        try {
            PatientDAO dupCheckDao = new PatientDAO();
            if (dupCheckDao.isIdNumberTaken(idNumber)) {
                showStyledMessageDialog("Duplicate Patient Record",
                        "A patient record already exists under this identification number.");
                return;
            }
        } catch (Exception ex) {
            showStyledMessageDialog("Verification Unavailable",
                    "ID verification is temporarily unavailable. Please attempt registration again in a moment.");
            return;
        }

        String phoneNumberError = validatePhoneNumber(phoneNumber);
        if (phoneNumberError != null) {
            showStyledMessageDialog(phoneNumberError);
            return;
        }

        if (department.equals("Select Department") && triageLevel.equals("Select Triage")) {
            showStyledMessageDialog("Select a Department and Triage Level for a Patient");
            return;
        } else if (department.equals("Select Department")) {
            showStyledMessageDialog("Select a Department for a Patient");
            return;
        } else if (triageLevel.equals("Select Triage")) {
            showStyledMessageDialog("Select a Triage Level for a Patient");
            return;
        }

        registerBtn.setEnabled(false);
        try {
            PatientDAO patient = new PatientDAO();
            Patient patientRecord = new Patient(idNumber, firstName, lastName, phoneNumber, birthDate, gender);
            patient.insertPatientRecord(patientRecord);

            CurrentPatient.set(idNumber, department, triageLevel);

            showStyledMessageDialog("Success",
                    "Patient Registered Successfully. You can now generate their ticket.");
        } catch (Exception ex) {
            showStyledMessageDialog("Registration Failed",
                    "Something went wrong while registering the patient. Please try again.");
        } finally {
            registerBtn.setEnabled(true);
        }
    }

    private void lockIdNumberField() {
        idNumberTxt.setEditable(false);
        idNumberTxt.setBackground(LOCKED_FIELD_BACKGROUND);
        idNumberTxt.setBorder(new RoundedBorder(LOCKED_FIELD_BORDER, INPUT_CORNER_RADIUS));
    }

    private void unlockIdNumberField() {
        idNumberTxt.setEditable(true);
        idNumberTxt.setBackground(Color.WHITE);
        idNumberTxt.setBorder(INPUT_BORDER_NORMAL);
    }

    private void clearForm() {
        firstNameTxt.setText("");
        lastNameTxt.setText("");
        yearcmBoBox.setSelectedItem("2026");
        monthcmBoBox.setSelectedItem("January");
        daycmBoBox.setSelectedItem("1");
        genderButtons.clearSelection();
        idNumberTxt.setText("");
        phoneNumberTxt.setText("");
        departmentCmboBox.setSelectedItem("Select Department");
        triageLevelCmboBox.setSelectedItem("Select Triage");

        unlockIdNumberField();
        updateBtn.setEnabled(false);
    }

    private String validateName(String label, String value) {
        if (value.isEmpty()) {
            return label + " is required";
        }
        if (!value.matches("[a-zA-Z\\-' ]{2,50}")) {
            return "Please enter a valid " + label.toLowerCase();
        }
        return null;
    }

    private String validateIdNumber(String value) {
        
        if (value.isEmpty()) {
            return "Identification number is required";
        }
        
        if (!value.matches("[a-zA-Z0-9]+")) {
            return "Please enter a valid identification number";
        }
        
        if (value.matches("[a-zA-Z]+")) {
            return "Please enter a valid identification number";
        }
        
        if (value.length() < 9 || value.length() > 15) {
            return "Please enter a valid identification number";
        }
        return null;
    }

    private String validatePhoneNumber(String value) {
        
        if (value.isEmpty()) {
            return "Phone number is required";
        }
        
        if (!value.matches("\\d+")) {
            return "Phone number must contain numbers only";
        }
        
        if (value.length() != 10) {
            return "Phone number must contain exactly 10 digits";
        }
        return null;
    }

    private void showStyledMessageDialog(String message) {
        showStyledMessageDialog("Missing Information", message);
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

    private void styleInput(JComponent input) {
        if (input instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) input;
            comboBox.setUI(new FullHeightComboBoxUI());
            modernizeComboBox(comboBox);
        }

        input.setBorder(INPUT_BORDER_NORMAL);

        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        input.setPreferredSize(size);
        input.setMinimumSize(size);
        input.setMaximumSize(size);

        addFocusBorderSwap(input);
    }

    private void styleDateComboBox(JComboBox comboBox) {
        comboBox.setUI(new FullHeightComboBoxUI());
        modernizeComboBox(comboBox);
        comboBox.setBorder(INPUT_BORDER_NORMAL);
        addFocusBorderSwap(comboBox);
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

    private void modernizeComboBox(JComboBox comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.DARK_GRAY);
        comboBox.setOpaque(true);

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                boolean isDropdownRow = index != -1;
                boolean highlight = isDropdownRow && isSelected;

                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, highlight, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Inter", Font.PLAIN, 13));

                boolean isPlaceholder = index == 0 && value != null && value.toString().startsWith("Select");
                if (highlight) {
                    label.setBackground(ACCENT_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(isPlaceholder ? Color.GRAY : Color.DARK_GRAY);
                }
                if (isPlaceholder) {
                    label.setFont(label.getFont().deriveFont(Font.ITALIC));
                }
                return label;
            }
        });

        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Object popup = comboBox.getUI().getAccessibleChild(comboBox, 0);
                if (popup instanceof JPopupMenu) {
                    ((JPopupMenu) popup).setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private void styleRadioButton(JRadioButton radio) {
        radio.setIcon(new RoundIcon(false));
        radio.setSelectedIcon(new RoundIcon(true));
        radio.setFocusPainted(false);
        radio.setBackground(Color.WHITE);
    }

    private static class FullHeightComboBoxUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(ACCENT_COLOR);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;
                    g2.drawLine(cx - 5, cy - 2, cx, cy + 3);
                    g2.drawLine(cx, cy + 3, cx + 5, cy - 2);
                    g2.dispose();
                }
            };
            button.setPreferredSize(new Dimension(28, FIELD_HEIGHT));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
            button.setRolloverEnabled(false);
            button.setFocusable(false);
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Color previous = g.getColor();
            g.setColor(comboBox.getBackground());
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.setColor(previous);
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            ListCellRenderer renderer = comboBox.getRenderer();
            Component c = renderer.getListCellRendererComponent(
                    listBox, comboBox.getSelectedItem(), -1, false, false);

            c.setFont(comboBox.getFont());
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
                c.setBackground(comboBox.getBackground());
            }

            boolean shouldValidate = c instanceof JPanel;
            currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
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

        private static final Color DISABLED_FILL = new Color(214, 217, 222);
        private static final Color DISABLED_TEXT = new Color(150, 155, 163);

        private final int radius;
        private Color baseBackground;
        private Color outlineColor;
        private Color baseForeground;

        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled() && baseBackground != null && baseBackground.equals(GREEN_ACCENT)) {
                        setBackground(GREEN_ACCENT_DARK);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEnabled() && baseBackground != null) {
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
        public void setForeground(Color fg) {
            super.setForeground(fg);
            if (fg != null && isEnabled()) {
                baseForeground = fg;
            }
        }

        @Override
        public Color getForeground() {
            if (!isEnabled()) {
                return DISABLED_TEXT;
            }
            return baseForeground != null ? baseForeground : super.getForeground();
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setCursor(Cursor.getPredefinedCursor(enabled ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            repaint();
        }

        void setOutlineColor(Color outlineColor) {
            this.outlineColor = outlineColor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled() ? getBackground() : DISABLED_FILL);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            if (outlineColor != null) {
                g2.setColor(isEnabled() ? outlineColor : DISABLED_FILL);
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

    private static class RoundIcon implements Icon {

        private static final int SIZE = 18;
        private final boolean filled;

        RoundIcon(boolean filled) {
            this.filled = filled;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Float(x, y, SIZE, SIZE));
            g2.setColor(filled ? GREEN_ACCENT : BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new Ellipse2D.Float(x, y, SIZE, SIZE));
            if (filled) {
                g2.setColor(GREEN_ACCENT);
                int pad = 5;
                g2.fill(new Ellipse2D.Float(x + pad, y + pad, SIZE - pad * 2, SIZE - pad * 2));
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
}
