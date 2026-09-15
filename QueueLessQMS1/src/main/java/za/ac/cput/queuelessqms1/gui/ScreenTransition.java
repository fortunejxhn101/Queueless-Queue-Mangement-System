package za.ac.cput.queuelessqms1.gui;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.function.Supplier;

/**
 * @author admin Fortune
 */
public final class ScreenTransition {

    private static final int FADE_DURATION_MS = 180;
    private static final int FADE_STEP_MS = 15;

    private ScreenTransition() {
        
    }

    public static void navigateTo(JFrame currentFrame, Supplier<? extends JFrame> nextFrameSupplier) {
        JFrame next = nextFrameSupplier.get();

        boolean canAnimate = isTranslucencySupported()
                && currentFrame.isUndecorated()
                && next.isUndecorated();

        if (!canAnimate) {
            currentFrame.dispose();
            next.setVisible(true);
            return;
        }

        fadeOut(currentFrame, () -> {
            currentFrame.dispose();
            next.setOpacity(0f);
            next.setVisible(true);
            fadeIn(next);
        });
    }

    private static boolean isTranslucencySupported() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return device.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
    }

    private static void fadeOut(JFrame frame, Runnable onComplete) {
        int totalSteps = FADE_DURATION_MS / FADE_STEP_MS;
        int[] currentStep = {0};

        Timer timer = new Timer(FADE_STEP_MS, null);
        timer.addActionListener(e -> {
            currentStep[0]++;
            float progress = Math.min(1f, currentStep[0] / (float) totalSteps);
            frame.setOpacity(1f - progress);

            if (progress >= 1f) {
                timer.stop();
                onComplete.run();
            }
        });
        timer.start();
    }

    private static void fadeIn(JFrame frame) {
        int totalSteps = FADE_DURATION_MS / FADE_STEP_MS;
        int[] currentStep = {0};

        Timer timer = new Timer(FADE_STEP_MS, null);
        timer.addActionListener(e -> {
            currentStep[0]++;
            float progress = Math.min(1f, currentStep[0] / (float) totalSteps);
            frame.setOpacity(progress);

            if (progress >= 1f) {
                timer.stop();
            }
        });
        timer.start();
    }
}
