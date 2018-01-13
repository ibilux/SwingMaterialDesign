package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.animation.AnimationListener;
import com.hq.swingmaterialdesign.materialdesign.animation.Animator;
import javax.swing.*;

/**
 * An animated line that appears below a component when it is focused.
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialLine {

    private final JComponent target;
    private final Animator animator;
    private double width;
    private double targetWidth;
    private double widthCrement;

    public MaterialLine(JComponent target) {
        this.target = target;
        animator = new Animator(new AnimationListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onAnimation(double percent) {
                width += widthCrement;
                target.repaint();
            }

            @Override
            public void onEnd() {
                width = targetWidth;
                target.repaint();
            }

            @Override
            public void onStop() {
                width = targetWidth;
                target.repaint();
            }
        })
                .setDelay(0)
                .setDuration(200);
    }

    public void update() {
        animator.stop();
        if (target.isFocusOwner()) {
            targetWidth = target.getWidth();
            widthCrement = +(double) target.getWidth() / 200;
        } else {
            widthCrement = -(double) target.getWidth() / 200;
            targetWidth = 0;
        }
        animator.start();
    }

    public double getWidth() {
        return width;
    }
}
