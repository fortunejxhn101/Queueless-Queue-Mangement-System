package za.ac.cput.queuelessqms1.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author admin Fortune
 */
public class TableHeaderBand extends JPanel {

    private static final Color BAND_COLOR = new Color(47, 62, 92);
    private static final int CORNER_RADIUS = 12;

    public TableHeaderBand() {
        setOpaque(false);
    }

    public TableHeaderBand(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Area band = new Area(new RoundRectangle2D.Float(0, 0, w, h + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS));
        band.intersect(new Area(new Rectangle2D.Float(0, 0, w, h)));

        g2.setColor(BAND_COLOR);
        g2.fill(band);
        g2.dispose();
        super.paintComponent(g);
    }
}