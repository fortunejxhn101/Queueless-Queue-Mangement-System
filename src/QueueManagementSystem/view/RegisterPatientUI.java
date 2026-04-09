package QueueManagementSystem.view;

/**
 *
 * @author admin
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class RegisterPatientUI extends JFrame {

    private JLabel patientName;
    private JLabel patientSurName;
    private JLabel idNumber;
    private JLabel department;
    private JLabel phoneNumber;

    private JTextField nameTxt;
    private JTextField surnameTxt;
    private JTextField idNumberTxt;
    private JTextField phoneNumberTxt;

    private JComboBox departmentCmboBox;
    
    private JComboBox dayOfBirth; 
    private JComboBox dateOfBirth;
    private JComboBox monthOfBirth; 

    private JButton registerBtn;
    private JButton clearBtn;
    
    private JPanel formPanel; 
    private JPanel buttonPnl; 

    public RegisterPatientUI() {
        super("Patient Registration");

        setLayout(new GridLayout(2, 1));

        patientName = new JLabel("Name: ");
        patientSurName = new JLabel("Surname: ");
        idNumber = new JLabel("ID Number: ");
        department = new JLabel("Department: ");
        phoneNumber = new JLabel("Phone Number: ");

        nameTxt = new JTextField(15);
        surnameTxt = new JTextField(15);
        idNumberTxt = new JTextField(15);
        phoneNumberTxt = new JTextField(15);

        String[] departments = {
            "Select Department",
            "General",
            "Emergency",
            "Pediatrics",
            "Pharmacy",
            "Radiology",
            "Laboratory"
        };

        departmentCmboBox = new JComboBox(departments);
        
        registerBtn = new JButton("Register Patient");
        clearBtn = new JButton("Clear");
        
        
        formPanel = new JPanel();
        buttonPnl = new JPanel();
        
        formPanel.setLayout(new GridLayout(5,2));
        
        formPanel.add(patientName);
        formPanel.add(nameTxt);
        
        formPanel.add(patientSurName);
        formPanel.add(surnameTxt);
        
        formPanel.add(idNumber);
        formPanel.add(idNumberTxt);
        
        formPanel.add(phoneNumber);
        formPanel.add(phoneNumberTxt);        
        
        formPanel.add(department);
        formPanel.add(departmentCmboBox);
        
        
        add(formPanel);
        
        
        buttonPnl.setLayout(new FlowLayout());
        
        buttonPnl.add(registerBtn);
        buttonPnl.add(clearBtn);
        
        add(buttonPnl);

    }

}
