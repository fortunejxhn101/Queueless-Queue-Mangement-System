package za.ac.cput.queuelessqms1.gui;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author admin Fortune 
 */
public final class ContentTransition {

    private static final int FADE_DURATION_MS = 120;
    private static final int FADE_STEP_MS = 10;

    private ContentTransition() {
    }

    public static void crossfadeTo(ContentHost host, JComponent newContent) {
        Component[] existing = host.getComponents();

        newContent.setBounds(0, 0, host.getWidth(), host.getHeight());
        newContent.revalidate();

        if (existing.length == 0) {
            host.add(newContent, JLayeredPane.DEFAULT_LAYER);
            host.revalidate();
            host.repaint();
            return;
        }

        Component oldContent = existing[0];

        FadeLayer fadeLayer = new FadeLayer(newContent);
        fadeLayer.setBounds(0, 0, host.getWidth(), host.getHeight());
        fadeLayer.setAlpha(0f);
        host.add(fadeLayer, JLayeredPane.PALETTE_LAYER);
        host.revalidate();

        int totalSteps = FADE_DURATION_MS / FADE_STEP_MS;
        int[] currentStep = {0};

        Timer timer = new Timer(FADE_STEP_MS, null);
        timer.addActionListener(e -> {
            currentStep[0]++;
            float progress = Math.min(1f, currentStep[0] / (float) totalSteps);
            fadeLayer.setAlpha(progress);

            if (progress >= 1f) {
                timer.stop();

                host.remove(oldContent);
                host.remove(fadeLayer);
                host.add(newContent, JLayeredPane.DEFAULT_LAYER);
                host.revalidate();
                host.repaint();
            }
        });
        timer.start();
    }

    public static class ContentHost extends JLayeredPane {

        public ContentHost() {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    for (Component c : getComponents()) {
                        c.setBounds(0, 0, getWidth(), getHeight());
                        if (c instanceof JComponent) {
                            ((JComponent) c).revalidate();
                        }
                    }
                }
            });
        }
    }

    private static class FadeLayer extends javax.swing.JPanel {

        private float alpha = 1f;

        FadeLayer(JComponent content) {
            setLayout(new java.awt.BorderLayout());
            setOpaque(false);
            add(content, java.awt.BorderLayout.CENTER);
        }

        void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintChildren(g2);
            g2.dispose();
        }
    }
}
