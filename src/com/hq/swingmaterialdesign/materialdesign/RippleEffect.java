package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.animation.AnimationListener;
import com.hq.swingmaterialdesign.materialdesign.animation.Animator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A {@code RippleEffect} is applied into certain components, like buttons and
 * certain list elements. Basically, is that wave of color that appears when you
 * click stuff.
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class RippleEffect {

    private final JComponent target;
    private final RippleAnimation ripple = new RippleAnimation();

    private RippleEffect(final JComponent component) {
        this.target = component;
    }

    /**
     * Paints this effect. Each component is responsible of calling {@link
     * #paint(Graphics)} in order to display the effect. Here's an example of
     * how the ripple effect can be used:
     *
     * @param g canvas
     */
    public void paint(Graphics g) {
        if (ripple.isRippling()) {
            Graphics2D g2 = (Graphics2D) g;
            float rippleOpacity = (float) ripple.getRippleOpacity();
            Point rippleCenter = ripple.getRippleCenter();
            int rippleRadius = (int) ripple.getRippleRadius();
            Color fg = g2.getColor();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, rippleOpacity));
            g2.fillOval(rippleCenter.x - rippleRadius, rippleCenter.y - rippleRadius, 2 * rippleRadius, 2 * rippleRadius);
        }
    }

    /**
     * Adds a ripple at the given point.
     *
     * @param point point to add the ripple at
     * @param maxRadius the maximum radius of the ripple
     */
    private void addRipple(Point point, int maxRadius) {
        ripple.setRipple(point, maxRadius);
        ripple.start();
    }

    /**
     * Creates a ripple effect for the given component. Each component is
     * responsible of calling {@link #paint(Graphics)} in order to display the
     * effect. Here's an example of how the ripple effect can be used:
     *
     * @param target target component
     * @return ripple effect for that component
     * @see MaterialButton for an example of how the ripple effect is used
     */
    public static RippleEffect applyTo(final JComponent target) {
        final RippleEffect rippleEffect = new RippleEffect(target);
        target.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                rippleEffect.addRipple(e.getPoint(), target.getWidth());
            }
        });
        return rippleEffect;
    }

    /**
     * Creates a ripple effect for the given component that is limited to the
     * component's size and will always start in the center. Each component is
     * responsible of calling {@link #paint(Graphics)} in order to display the
     * effect. Here's an example of how the ripple effect can be used:
     *
     * @param target target component
     * @return ripple effect for that component
     * @see MaterialButton for an example of how the ripple effect is used
     */
    public static RippleEffect applyFixedTo(final JComponent target) {
        final RippleEffect rippleEffect = new RippleEffect(target);
        target.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                rippleEffect.addRipple(new Point(24, 24), target.getWidth() / 2);
            }
        });
        return rippleEffect;
    }

    /**
     * A ripple animation (one ripple circle after one click).
     */
    private class RippleAnimation {

        private final Animator animator;
        private Point rippleCenter;
        private int maxRadius;
        private double rippleRadius;
        private double targetRippleRadius;
        private double rippleRadiusCrement;
        private double rippleOpacity;

        private RippleAnimation() {
            animator = new Animator(new AnimationListener() {
                @Override
                public void onStart() {
                    rippleRadius = 0;
                    targetRippleRadius = maxRadius;
                    rippleRadiusCrement = +(double) (targetRippleRadius - rippleRadius);
                    rippleOpacity = 0.5;
                }

                @Override
                public void onAnimation(double percent) {
                    rippleRadius = rippleRadiusCrement * percent * percent;
                    rippleOpacity = 0.5 * Math.sin(3.0 * percent * percent);
                    target.repaint();
                }

                @Override
                public void onEnd() {
                    rippleRadius = 0;
                    target.repaint();
                }

                @Override
                public void onStop() {
                    rippleRadius = 0;
                    target.repaint();
                }
            })
                    .setDelay(0)
                    .setDuration(999);
        }

        void start() {
            animator.start();
        }

        public void setRipple(Point rippleCenter, int maxRadius) {
            this.rippleCenter = rippleCenter;
            this.maxRadius = maxRadius;
        }

        public double getRippleOpacity() {
            return rippleOpacity;
        }

        public Point getRippleCenter() {
            return rippleCenter;
        }

        public double getRippleRadius() {
            return rippleRadius;
        }

        public boolean isRippling() {
            return animator.isRunning();
        }
    }
}
