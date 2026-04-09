package QueueManagementSystem.view;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author admin
 */

public class RunRegisterPatientUI {
    public static void main(String[] args) {
        
        RegisterPatientUI patient = new RegisterPatientUI();
        
        patient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        patient.setSize(900,600);
        patient.setVisible(true);
        
    }
    
}
