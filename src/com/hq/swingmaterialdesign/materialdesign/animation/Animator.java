package com.hq.swingmaterialdesign.materialdesign.animation;

import javax.swing.*;

/**
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class Animator {

    private final AnimationListener animationListener;
    private Timer animatorTimer;

    private int duration;
    private int takenTime;
    private long startTime;

    public Animator(AnimationListener listener) {
        this.animationListener = listener;

        animatorTimer = new Timer(1, e -> {
            takenTime = (int) (System.currentTimeMillis() - startTime);
            animationListener.onAnimation((double) takenTime / duration);
            if (takenTime >= duration) {
                SwingUtilities.invokeLater(() -> {
                    animatorTimer.stop();
                    animationListener.onEnd();
                });
            }
        });
        animatorTimer.setCoalesce(true);
    }

    public Animator setDelay(int delay) {
        animatorTimer.setInitialDelay(delay);
        return this;
    }

    public Animator setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public void start() {
        animatorTimer.stop();
        animationListener.onStart();
        startTime = System.currentTimeMillis();

        animatorTimer.start();
    }

    public Animator stop() {
        if (animatorTimer != null && animatorTimer.isRunning()) {
            animationListener.onStop();
            animatorTimer.stop();
        }
        return this;
    }

    public boolean isRunning() {
        return animatorTimer != null && animatorTimer.isRunning();
    }
}
