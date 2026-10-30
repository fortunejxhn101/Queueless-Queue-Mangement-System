package za.ac.cput.queuelessqms1.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author admin Fortune
 */
public final class ContentTransition {

    private static final int DURATION_MS = 150;
    private static final int FRAME_MS = 12;
    private static final int RISE_PX = 0;

    private ContentTransition() {

    }

    public static void crossfadeTo(ContentHost host, JComponent next) {
        host.transitionTo(next);
    }

    public static class ContentHost extends JPanel {

        private JComponent current;
        private BufferedImage fromImage;
        private BufferedImage toImage;
        private boolean animating = false;
        private long startNanos;
        private float progress = 1f;
        private Timer timer;

        public ContentHost() {
            super(null);
            setOpaque(true);
            setBackground(Color.WHITE);
        }

        @Override
        public void doLayout() {
            if (current != null) {
                current.setBounds(0, 0, getWidth(), getHeight());
            }
        }

        void transitionTo(JComponent next) {
            if (next == null || next == current) {
                return;
            }

            boolean canAnimate = isShowing() && getWidth() > 0 && getHeight() > 0 && current != null;

            if (!canAnimate) {
                stopTimer();
                animating = false;
                fromImage = null;
                toImage = null;
                swapIn(next);
                revalidate();
                repaint();
                return;
            }

            BufferedImage snapshotOfNow = captureHost();

            stopTimer();
            animating = false;
            swapIn(next);
            doLayout();
            layoutTree(next);

            BufferedImage snapshotOfNext = new BufferedImage(
                    getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = snapshotOfNext.createGraphics();
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
            next.paint(g2);
            g2.dispose();

            fromImage = snapshotOfNow;
            toImage = snapshotOfNext;
            animating = true;
            progress = 0f;
            startNanos = System.nanoTime();

            timer = new Timer(FRAME_MS, e -> {
                float t = (System.nanoTime() - startNanos) / (DURATION_MS * 1_000_000f);
                if (t >= 1f) {
                    finishAnimation();
                } else {
                    progress = easeOutCubic(t);
                    repaint();
                }
            });
            timer.setCoalesce(true);
            timer.start();
        }

        private void swapIn(JComponent next) {
            if (current != null) {
                remove(current);
            }
            current = next;
            add(current);
        }

        private BufferedImage captureHost() {
            BufferedImage image = new BufferedImage(
                    getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);
            g2.dispose();
            return image;
        }

        private void finishAnimation() {
            stopTimer();
            animating = false;
            fromImage = null;
            toImage = null;
            progress = 1f;
            revalidate();
            repaint();
        }

        private void stopTimer() {
            if (timer != null) {
                timer.stop();
                timer = null;
            }
        }

        private static void layoutTree(java.awt.Component c) {
            if (c instanceof java.awt.Container) {
                java.awt.Container container = (java.awt.Container) c;
                container.doLayout();
                for (java.awt.Component child : container.getComponents()) {
                    layoutTree(child);
                }
            }
        }

        private static float easeOutCubic(float t) {
            float inv = 1f - t;
            return 1f - inv * inv * inv;
        }

        @Override
        protected void paintChildren(Graphics g) {
            if (!animating) {
                super.paintChildren(g);
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int w = getWidth();
            int h = getHeight();

            if (fromImage != null) {
                g2.drawImage(fromImage, 0, 0, w, h, null);
            }

            if (toImage != null) {
                int offsetY = Math.round((1f - progress) * RISE_PX);
                g2.setComposite(AlphaComposite.SrcOver.derive(Math.max(0f, Math.min(1f, progress))));
                g2.drawImage(toImage, 0, offsetY, w, h, null);
            }

            g2.dispose();
        }
    }
}