package za.ac.cput.queuelessqms1.main;

import java.util.List;
import za.ac.cput.queuelessqms1.gui.*;
import za.ac.cput.queuelessqms1.dao.TicketDAO;
import za.ac.cput.queuelessqms1.dao.PatientDAO;
import za.ac.cput.queuelessqms1.dao.StaffDAO;
import za.ac.cput.queuelessqms1.dao.QueueActionDAO;
import za.ac.cput.queuelessqms1.dao.VisitDAO;
import za.ac.cput.queuelessqms1.domain.TicketQueue;
import javax.swing.*;
import za.ac.cput.queuelessqms1.dao.DepartmentDAO;
import za.ac.cput.queuelessqms1.domain.Department;

/**
 * @author admin Fortune
 */
public class QueueLessMain {

    private static final int SPLASH_DURATION_MS = 5000;

    public static void main(String[] args) {

        new DepartmentDAO();
        new StaffDAO();
        new PatientDAO();
        TicketDAO.createTicketTable();
        new QueueActionDAO();
        new VisitDAO();

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