package com.hq.swingmaterialdesign.materialdesign;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * A renderer for Material shadows. Shadows are a sign of elevation, and help
 * distinguishing elements inside a Material-based GUI.
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialShadow {

    /**
     * The default offset between the border of the shadow and the top of a
     * Material component.
     */
    public static final int OFFSET_TOP = 2;
    /**
     * The default offset between the border of the shadow and the left of a
     * Material component.
     */
    public static final int OFFSET_LEFT = 2;
    /**
     * The default offset between the border of the shadow and the bottom of a
     * Material component.
     */
    public static final int OFFSET_BOTTOM = 2;
    /**
     * The default offset between the border of the shadow and the right of a
     * Material component.
     */
    public static final int OFFSET_RIGHT = 2;

    /**
     * Creates a {@link BufferedImage} containing a shadow1 projected from a
     * square component of the given width and height.
     *
     * @param width the component's width, inpixels
     * @param height the component's height, inpixels
     * @param level the elevation level [0~5]
     * @return A {@link BufferedImage} with the contents of the shadow1 for a
     * circular component of the given radius.
     */
    public static BufferedImage renderShadow(int width, int height, double level) {
        return renderShadow(width, height, level, 3);
    }

    /**
     * Creates a {@link BufferedImage} containing a shadow1 projected from a
     * square component of the given width and height.
     *
     * @param width the component's width, inpixels
     * @param height the component's height, inpixels
     * @param level the elevation level [0~5]
     * @param borderRadius an applicable radius to the border of the shadow1
     * @return A {@link BufferedImage} with the contents of the shadow1 for a
     * circular component of the given radius.
     */
    public static BufferedImage renderShadow(int width, int height, double level, double borderRadius) {

        if (level <= 0.0) {
            level = 0.0;
        }
        if (level >= 2.0) {
            level = 2.0;
        }

        // y = a * x + b
        float opacity1f = (float) ((2.0 / (1 + Math.exp(-2 * level))) - 1.0);
        float radius1f = (float) ((4.0 / (1 + Math.exp(-2 * level))) - 2.0);

        //float radius1f = (float) (2.5 * Math.sin(0.41 * level * level) + 0.5);
        //System.out.println("level: " + level+" > radius1f: " + radius1f +" > opacity1f: " + opacity1f);
        BufferedImage shadowBlurImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = (Graphics2D) shadowBlurImage.getGraphics();
        //graphics2D.setComposite(AlphaComposite.SrcOver);
        graphics2D.setColor(new Color(0f, 0f, 0f, opacity1f));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(OFFSET_LEFT, OFFSET_TOP, width - OFFSET_LEFT - OFFSET_RIGHT, height - OFFSET_TOP - OFFSET_BOTTOM, (float) borderRadius, (float) borderRadius));
        graphics2D.dispose();
        FastGaussianBlur.blur(shadowBlurImage, radius1f, 3);

        return shadowBlurImage;

    }

    private int pWd, pHt;
    private double pLv, pRd;
    private BufferedImage shadowBg;

    /**
     * Default constructor for a {@code MaterialShadow}. It is recommended to
     * keep a single instance for each component that requires it. The
     * components bundled in this library already handle this by themselves.
     */
    public MaterialShadow() {
    }

    /**
     * Renders this {@link MaterialShadow} into a {@link BufferedImage} and
     * returns it. A copy of the latest render is kept in case a shadow1 of the
     * same dimensions and elevation is needed in order to decrease CPU usage
     * when the component is idle.
     *
     * @param width the witdh of the square component casting a shadow1, or
     * diameter if it is circular.
     * @param height the height of the square component casting a shadow1.
     * @param radius the radius of the borders of a square component casting a
     * shadow1.
     * @param level the depth of the shadow1 [0~5]
     * @return A {@link BufferedImage} with the contents of the shadow1.
     * @see Type#SQUARE
     * @see Type#CIRCULAR
     */
    public BufferedImage render(int width, int height, double radius, double level) {
        if (pWd != width || pHt != height || pRd != radius || pLv != level) {
            shadowBg = MaterialShadow.renderShadow(width, height, level, radius);
            pWd = width;
            pHt = height;
            pRd = radius;
            pLv = level;
        }
        return shadowBg;
    }
}
