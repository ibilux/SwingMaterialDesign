package com.hq.swingmaterialdesign.materialdesign;

import com.hq.swingmaterialdesign.materialdesign.resource.MaterialColor;
import static com.hq.swingmaterialdesign.materialdesign.MaterialTextField.HINT_OPACITY_MASK;
import static com.hq.swingmaterialdesign.materialdesign.MaterialTextField.LINE_OPACITY_MASK;
import com.hq.swingmaterialdesign.materialdesign.resource.Roboto;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;

/**
 * A Material Design combo box.
 *
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialComboBox<T> extends JComboBox<T> {

    private MaterialLine line = new MaterialLine(this);
    private Color accentColor = MaterialColor.PINK_500;
    private String hint = "";

    public MaterialComboBox() {
        setModel(new DefaultComboBoxModel<>());
        setRenderer(new FieldRenderer<>(this));
        setUI(new BasicComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popupBox = new Popup(comboBox);
                popupBox.getAccessibleContext().setAccessibleParent(comboBox);
                return popupBox;
            }

            @Override
            protected JButton createArrowButton() {
                JButton button = new javax.swing.plaf.basic.BasicArrowButton(
                        javax.swing.plaf.basic.BasicArrowButton.SOUTH,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT,
                        MaterialColor.TRANSPARENT);
                button.setName("ComboBox.arrowButton");
                return button;
            }
        });
        setOpaque(false);
        setBackground(MaterialColor.WHITE);
    }

    /**
     * Gets the color the label changes to when this {@code materialTextField}
     * is focused.
     *
     * @return the {@code "Color"} currently in use for accent. The default
     * value is {@link MaterialColor#PINK_500}.
     */
    public Color getAccent() {
        return accentColor;
    }

    /**
     * Sets the color the label changes to when this {@code materialTextField}
     * is focused. The default value is {@link MaterialColor#PINK_500}.
     *
     * @param accentColor the {@code "Color"} that should be used for accent.
     */
    public void setAccent(Color accentColor) {
        this.accentColor = accentColor;
    }

    /**
     * Gets the hint text. The hint text is displayed when the list inside this
     * combo box is empty or no element has been selected yet.
     *
     * @return hint text
     */
    public String getHint() {
        return hint;
    }

    /**
     * Sets the hint text. The hint text is displayed when the list inside this
     * combo box is empty or no element has been selected yet.
     *
     * @param hint hint text
     */
    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        line.update();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // paint text baground
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight() - 4);

        // paint the selected item
        g.setFont(Roboto.REGULAR.deriveFont(16f));
        g.setColor(getSelectedItem() == null ? MaterialUtils.applyAlphaMask(getForeground(), HINT_OPACITY_MASK) : getForeground());
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String text = getSelectedItem() != null ? getSelectedItem().toString() : (hint != null ? hint : "");
        g.drawString(text, 0, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);

        // paint the Polygon <
        if (isFocusOwner()) {
            g2.setColor(accentColor);
        }
        g2.fillPolygon(new int[]{getWidth() - 5, getWidth() - 10, getWidth() - 15}, new int[]{getHeight() / 2 - 3, getHeight() / 2 + 3, getHeight() / 2 - 3}, 3);

        // paint line under text
        g2.setColor(MaterialUtils.applyAlphaMask(getForeground(), LINE_OPACITY_MASK));
        g2.fillRect(0, getHeight() - 4, getWidth(), 1);

        // paint animated line under text
        g2.setColor(accentColor);
        g2.fillRect((int) ((getWidth() - line.getWidth()) / 2), getHeight() - 5, (int) line.getWidth(), 2);
    }

    public static class FieldRenderer<T> extends JComponent implements ListCellRenderer<T> {

        private final MaterialComboBox comboBox;
        private String text;
        private boolean mouseOver = false;
        private boolean selected = false;

        public FieldRenderer(MaterialComboBox comboBox) {
            this.comboBox = comboBox;
        }

        @Override
        public Component getListCellRendererComponent(JList jList, Object o, int index, boolean isSelected, boolean cellHasFocus) {
            text = o != null ? o.toString() : "";
            setSize(jList.getWidth(), 56);
            setPreferredSize(new Dimension(jList.getWidth(), 32));
            setOpaque(true);
            mouseOver = isSelected;
            selected = comboBox.getSelectedIndex() == index;
            return this;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (mouseOver) {
                g.setColor(MaterialUtils.isDark(comboBox.getBackground()) ? MaterialUtils.brighten(comboBox.getBackground()) : MaterialUtils.darken(comboBox.getBackground()));
            } else {
                g.setColor(comboBox.getBackground());
            }
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(Roboto.REGULAR.deriveFont(15f));
            if (selected) {
                g2.setColor(comboBox.accentColor);
            } else {
                g2.setColor(comboBox.getForeground());
            }
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString(text, 24, metrics.getAscent() + (getHeight() - metrics.getHeight()) / 2);
        }
    }

    public static class Popup extends BasicComboPopup {

        public Popup(JComboBox combo) {
            super(combo);
            setBackground(combo.getBackground());
            setOpaque(true);
            setBorderPainted(false);
        }

        @Override
        protected JScrollPane createScroller() {
            JScrollPane scrol = super.createScroller();
            scrol.setVerticalScrollBar(new ScrollBar(comboBox, Adjustable.VERTICAL));
            scrol.setBorder(new MatteBorder(16, 0, 16, 0, Color.WHITE));
            return scrol;
        }

        @Override
        protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
            return super.computePopupBounds(px, py - 2, (int) Math.max(comboBox.getPreferredSize().getWidth(), pw), ph);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }
    }

    public static class ScrollBar extends JScrollBar {

        public ScrollBar(final JComboBox comboBox, int orientation) {
            super(orientation);
            setPreferredSize(new Dimension(5, 100));

            setUI(new BasicScrollBarUI() {
                @Override
                protected ScrollListener createScrollListener() {
                    setUnitIncrement(56);
                    setBlockIncrement(560);
                    return super.createScrollListener();
                }

                @Override
                protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                    g.setColor(comboBox.getBackground());
                    g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    JButton dummyButton = new JButton();
                    dummyButton.setPreferredSize(new Dimension(0, 0));
                    return dummyButton;
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    JButton dummyButton = new JButton();
                    dummyButton.setPreferredSize(new Dimension(0, 0));
                    return dummyButton;
                }

                @Override
                protected Dimension getMinimumThumbSize() {
                    return new Dimension(5, 50);
                }

                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    if (!thumbBounds.isEmpty() && this.scrollbar.isEnabled()) {
                        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        boolean isVertical = ScrollBar.this.getOrientation()
                                == Adjustable.VERTICAL;
                        g.setColor(MaterialColor.GREY_500);
                        g.fillRoundRect(thumbBounds.x, thumbBounds.y,
                                thumbBounds.width, thumbBounds.height,
                                isVertical ? thumbBounds.width : thumbBounds.height,
                                isVertical ? thumbBounds.width : thumbBounds.height);
                    }
                }

                @Override
                public void layoutContainer(Container scrollbarContainer) {
                    super.layoutContainer(scrollbarContainer);
                    incrButton.setBounds(0, 0, 0, 0);
                    decrButton.setBounds(0, 0, 0, 0);
                }
            });
        }

        @Override
        public Component add(Component comp) {
            if (comp != null) {
                return super.add(comp);
            }
            return null;
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paint(g);
        }
    }
}
