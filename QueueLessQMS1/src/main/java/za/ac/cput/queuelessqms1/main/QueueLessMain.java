package za.ac.cput.queuelessqms1.main;

import za.ac.cput.queuelessqms1.gui.*;
import za.ac.cput.queuelessqms1.dao.TicketDAO;
import za.ac.cput.queuelessqms1.dao.PatientDAO;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
import javax.swing.*;

/**
 * @author admin Fortune
 */
public class QueueLessMain {

    private static final int SPLASH_DURATION_MS = 5000;

    public static void main(String[] args) {
        
        new PatientDAO();
        TicketDAO.createTicketTable();

        TicketQueue.loadFromDatabase();

        TicketQueue.startDailyRolloverWatcher();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            HomeGUI homeScreen = new HomeGUI();
            homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            homeScreen.setVisible(true);
            homeScreen.fadeContentIn();

            Timer splashTimer = new Timer(SPLASH_DURATION_MS, e -> {
                homeScreen.fadeContentOut(() -> {
                    homeScreen.dispose();

                    StaffLoginGUI loginScreen = new StaffLoginGUI();
                    loginScreen.setVisible(true);
                    loginScreen.fadeContentIn();
                });
            });
            splashTimer.setRepeats(false);
            splashTimer.start();
        });
    }
}
