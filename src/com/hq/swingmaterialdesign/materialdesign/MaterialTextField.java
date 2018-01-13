package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.resource.MaterialColor;
import com.hq.swingmaterialdesign.materialdesign.resource.Roboto;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * A Material Design single-line text field is the basic way of getting user
 * input. It includes a descriptive label that appears as a placeholder and then
 * floats above the text field as content is written. You can also set a hint
 * for it to appear below the label when the text field is empty.
 *
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialTextField extends JTextField {

    public static final int HINT_OPACITY_MASK = 0x99000000;
    public static final int LINE_OPACITY_MASK = 0x66000000;

    private final MaterialFloatingLabel hintLabel = new MaterialFloatingLabel(this);
    private final MaterialLine line = new MaterialLine(this);

    private Color accentColor = MaterialColor.CYAN_500;

    /**
     * Default constructor for {@code MaterialTextField}. A default model is
     * created and the initial string is empty.
     */
    public MaterialTextField() {
        super();
        setBorder(null);
        setFont(Roboto.REGULAR.deriveFont(16f));
        hintLabel.setText("");
        setOpaque(false);
        setBackground(MaterialColor.WHITE);

        setCaret(new DefaultCaret() {
            @Override
            protected synchronized void damage(Rectangle r) {
                MaterialTextField.this.repaint(); //fix caret not being removed completely
            }
        });
        getCaret().setBlinkRate(500);
    }

    /**
     * Default constructor for {@code MaterialTextField}. A default model is
     * created and the initial string is the one provided.
     *
     * @param text An starting value for this text field
     */
    public MaterialTextField(String text) {
        super.setText(text);
    }

    /**
     * Gets the label text. The label will float above any contents input into
     * this text field.
     *
     * @return the text being used in the textfield label
     */
    public String getLabel() {
        return hintLabel.getText();
    }

    /**
     * Sets the label text. The label text is displayed when this textfield is
     * empty.
     *
     *
     * @param label the text to use in the floating label
     */
    public void setLabel(String label) {
        hintLabel.setText(label);
        repaint();
    }

    /**
     * Gets the color the label changes to when this {@code materialTextField}
     * is focused.
     *
     * @return the {@code "Color"} currently in use for accent. The default
     * value is {@link MaterialColor#CYAN_300}.
     */
    public Color getAccent() {
        return accentColor;
    }

    /**
     * Sets the color the label changes to when this {@code materialTextField}
     * is focused. The default value is {@link MaterialColor#CYAN_300}.
     *
     * @param accentColor the {@code "Color"} that should be used for accent.
     */
    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
        hintLabel.setAccent(accentColor);
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (hintLabel != null) {
            hintLabel.updateForeground();
        }
    }

    @Override
    public void setText(String string) {
        super.setText(string);
        hintLabel.update();
        line.update();
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        hintLabel.update();
        line.update();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        hintLabel.update();
        line.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // paint text baground
        g2.setColor(getBackground());
        g2.fillRect(0, (getHeight() / 2) - 4, getWidth(), getHeight() / 2);

        g2.translate(0, 9);
        super.paintComponent(g);
        g2.translate(0, -9);

        // hint label in text
        if (!getLabel().isEmpty() && getText().isEmpty() && (getLabel().isEmpty() || isFocusOwner())) {
            g.setFont(Roboto.REGULAR.deriveFont(16f));
            g2.setColor(MaterialUtils.applyAlphaMask(getForeground(), HINT_OPACITY_MASK));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(getLabel(), 0, metrics.getAscent() + getHeight() / 2);
        }

        // paint hint label
        hintLabel.paint(g2);

        // paint line under text
        g2.setColor(MaterialUtils.applyAlphaMask(getForeground(), LINE_OPACITY_MASK));
        g2.fillRect(0, getHeight() - 4, getWidth(), 1);

        // paint animated line under text
        g2.setColor(accentColor);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 5, (int) line.getWidth(), 2);
    }

    @Override
    protected void paintBorder(Graphics g) {
        //intentionally left blank
    }
}
