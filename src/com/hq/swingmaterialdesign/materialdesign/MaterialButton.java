package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.resource.MaterialColor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/**
 * A Material Design button.
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialButton extends JButton {

    private RippleEffect ripple;
    private ElevationEffect elevation;
    private Type type = Type.DEFAULT;
    private boolean isMousePressed = false;
    private boolean isMouseOver = false;
    private Color rippleColor = Color.WHITE;
    private Cursor cursor = super.getCursor();
    private int borderRadius = 2;

    /**
     * Creates a new button.
     */
    public MaterialButton() {
        ripple = RippleEffect.applyTo(this);
        elevation = ElevationEffect.applyTo(this, 0);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                isMousePressed = true;
                elevation.setLevel(getElevation());
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                isMousePressed = false;
                elevation.setLevel(getElevation());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                isMouseOver = true;
                elevation.setLevel(getElevation());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isMouseOver = false;
                elevation.setLevel(getElevation());
            }
        });

        setUI(new BasicButtonUI() {
            @Override
            public boolean contains(JComponent c, int x, int y) {
                return x > MaterialShadow.OFFSET_LEFT && y > MaterialShadow.OFFSET_TOP
                        && x < getWidth() - MaterialShadow.OFFSET_RIGHT && y < getHeight() - MaterialShadow.OFFSET_BOTTOM;
            }
        });
    }

    /**
     * Gets the type of this button.
     *
     * @return the type of this button
     * @see Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of this button.
     *
     * @param type the type of this button
     * @see Type
     */
    public void setType(Type type) {
        this.type = type;
        repaint();
    }

    /**
     * Sets the background color of this button.
     * <p>
     * Keep on mind that setting a background color in a Material component like
     * this will also set the foreground color to either white or black and the
     * ripple color to a brighter or darker shade of the color, depending of how
     * bright or dark is the chosen background color. If you want to use a
     * custom foreground color and ripple color, ensure the background color has
     * been set first.
     * <p>
     * <b>NOTE:</b> It is up to the look and feel to honor this property, some
     * may choose to ignore it. To avoid any conflicts, using the
     * <a href="https://docs.oracle.com/javase/7/docs/api/javax/swing/plaf/metal/package-summary.html">
     * Metal Look and Feel</a> is recommended.
     *
     * @param bg
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        setForeground(MaterialUtils.isDark(bg) ? MaterialColor.WHITE : MaterialColor.BLACK);
        setRippleColor(MaterialUtils.isDark(bg) ? MaterialColor.WHITE : MaterialUtils.darken(MaterialUtils.darken(bg)));
    }

    /**
     * Gets the ripple color.
     *
     * @return the ripple color
     */
    public Color getRippleColor() {
        return rippleColor;
    }

    /**
     * Sets the ripple color. You should only do this for flat buttons.
     *
     * @param rippleColor the ripple color
     */
    public void setRippleColor(Color rippleColor) {
        this.rippleColor = rippleColor;
    }

    /**
     * Gets the current border radius of this button.
     *
     * @return the current border radius of this button, in pixels.
     */
    public int getBorderRadius() {
        return borderRadius;
    }

    /**
     * Sets the border radius of this button. You can define a custom radius in
     * order to get some rounded corners in your button, making it look like a
     * pill or even a circular action button.
     *
     * @param borderRadius the new border radius of this button, in pixels.
     */
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        elevation.setBorderRadius(borderRadius);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        super.setCursor(b ? cursor : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        this.cursor = cursor;
    }

    @Override
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        super.processMouseEvent(mouseEvent);
    }

    private int getElevation() {
        if (isMousePressed) {
            return 2;
        } else if (type == Type.RAISED || isMouseOver) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if ((type != Type.FLAT) && isEnabled()) {
            elevation.paint(g);
            g2.translate(MaterialShadow.OFFSET_LEFT, MaterialShadow.OFFSET_TOP);
        }

        int offset_lr;
        int offset_td;
        if (type == Type.FLAT) {
            offset_td = 0;
            offset_lr = 0;
        } else {
            offset_td = MaterialShadow.OFFSET_TOP + MaterialShadow.OFFSET_BOTTOM;
            offset_lr = MaterialShadow.OFFSET_LEFT + MaterialShadow.OFFSET_RIGHT;
        }

        if (isEnabled()) {
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, borderRadius, borderRadius));
            g2.setColor(new Color(rippleColor.getRed() / 255f, rippleColor.getBlue() / 255f, rippleColor.getBlue() / 255f, 0.12f));
            if (type == Type.FLAT) {
                elevation.paint(g);
                //g2.setColor(MaterialUtils.brighten(getBackground()));
                //g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, borderRadius, borderRadius));
            }
        } else {
            Color bg = getBackground();
            g2.setColor(new Color(bg.getRed() / 255f, bg.getGreen() / 255f, bg.getBlue() / 255f, 0.6f));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, borderRadius * 2, borderRadius * 2));
        }

        FontMetrics metrics = g.getFontMetrics(getFont());
        int x = (getWidth() - offset_lr - metrics.stringWidth(getText().toUpperCase())) / 2;
        int y = (getHeight() - offset_td - metrics.getHeight()) / 2 + metrics.getAscent();
        g2.setFont(getFont());
        if (isEnabled()) {
            g2.setColor(getForeground());
        } else {
            Color fg = getForeground();
            g2.setColor(new Color(fg.getRed() / 255f, fg.getGreen() / 255f, fg.getBlue() / 255f, 0.6f));
        }
        g2.drawString(getText().toUpperCase(), x, y);

        if (isEnabled()) {
            g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth() - offset_lr, getHeight() - offset_td, Math.max(borderRadius * 2 - 4, 0), Math.max(borderRadius * 2 - 4, 0)));
            g2.setColor(rippleColor);
            ripple.paint(g2);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        //intentionally left blank
    }

    /**
     * Button types.
     */
    public enum Type {
        /**
         * A default button.
         */
        DEFAULT,
        /**
         * A raised button. Raised buttons have a shadow even if they are not
         * focused.
         */
        RAISED,
        /**
         * A flat button. Flat buttons don't have shadows and are typically
         * transparent.
         */
        FLAT
    }
}
