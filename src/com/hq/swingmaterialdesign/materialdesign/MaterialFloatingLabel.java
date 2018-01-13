package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.resource.MaterialColor;
import com.hq.swingmaterialdesign.materialdesign.resource.Roboto;
import com.hq.swingmaterialdesign.materialdesign.animation.AnimationListener;
import com.hq.swingmaterialdesign.materialdesign.animation.Animator;

import javax.swing.*;
import java.awt.*;

/**
 * A floating label of a text field.
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialFloatingLabel {

    public static final int HINT_OPACITY_MASK = 0x99000000;

    private final JTextField target;
    private final Animator animator;
    private Color color;
    private String text;
    private Color accentColor = MaterialColor.CYAN_500;
    private double fontCrement;
    private double targetFontSize;
    private double fontSize;

    MaterialFloatingLabel(JTextField target) {
        this.target = target;
        targetFontSize = fontSize = 16d;
        color = MaterialUtils.applyAlphaMask(target.getForeground(), HINT_OPACITY_MASK);
        animator = new Animator(new AnimationListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onAnimation(double percent) {
                fontSize += fontCrement;
                target.repaint();
            }

            @Override
            public void onEnd() {
                fontSize = targetFontSize;
                target.repaint();
            }

            @Override
            public void onStop() {
                fontSize = targetFontSize;
                target.repaint();
            }
        })
                .setDelay(0)
                .setDuration(100);
    }

    void update() {
        animator.stop();
        targetFontSize = target.isFocusOwner() ? 12d : 16d;
        if (fontSize != targetFontSize) {
            fontCrement = +(double) (targetFontSize - fontSize) / 100;
            animator.start();
        }

        if (target.isFocusOwner()) {
            color = accentColor;
        } else {
            color = MaterialUtils.applyAlphaMask(target.getForeground(), HINT_OPACITY_MASK);
        }
    }

    public void updateForeground() {
        color = (MaterialUtils.applyAlphaMask(target.getForeground(), HINT_OPACITY_MASK));
    }

    public Color getAccent() {
        return accentColor;
    }

    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    void paint(Graphics2D g) {
        g.setFont(Roboto.REGULAR.deriveFont((float) fontSize));
        g.setColor(color);
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        g.drawString(getText(), 0, metrics.getAscent() + 0);
    }
}
