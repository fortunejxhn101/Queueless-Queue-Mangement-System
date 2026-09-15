package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author admin Fortune
 */
public class HeaderPanel extends JPanel {

    private static final Color ACCENT_COLOR = new Color(47, 62, 92);
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 12);

    private static final int HEADER_HEIGHT = computeNaturalHeight();

    public HeaderPanel() {
        this("QueueLess - Queue Management System");
    }

    public HeaderPanel(String titleText) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(ACCENT_COLOR);

        Dimension fixedSize = new Dimension(0, HEADER_HEIGHT);
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));

        JLabel titleLbl = new JLabel(titleText);
        titleLbl.setFont(TITLE_FONT);
        titleLbl.setForeground(Color.WHITE);
        add(titleLbl);
    }

    private static int computeNaturalHeight() {
        JPanel probe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel probeLbl = new JLabel("QueueLess - Queue Management System");
        probeLbl.setFont(TITLE_FONT);
        probe.add(probeLbl);
        return probe.getPreferredSize().height;
    }
}
