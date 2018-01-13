package com.hq.swingmaterialdesign.materialdesign;

import java.awt.Color;

/**
 * This class provides utilitary methods for Swing Material. These are public
 * and thus can be used directly.
 *
 * @author DragShot
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class MaterialUtils {

    /**
     * Determines if a given {@link Color} is dark enough for white text to be
     * seen more easily than black text. This tries to stick to the Material
     * Color Guide as much as possible, and although two or three of the color
     * pairs doesn't match, the results are still good enough.
     *
     * @param color a {@link Color} to evaluate
     * @return {@code true} if the provided color is dark, {@code false}
     * otherwise.
     * @author DragShot
     */
    public static boolean isDark(Color color) {
        //return (color.getRed()*0.299 + color.getGreen()*0.587 + color.getBlue()*0.114) < (0.6*255);
        //return (color.getRed() + color.getGreen() + color.getBlue())/3 < (0.63*255);
        return (color.getRed() * 0.2125 + color.getGreen() * 0.7154 + color.getBlue() * 0.0721) < (0.535 * 255);
        //return (color.getRed()*0.21 + color.getGreen()*0.72 + color.getBlue()*0.07) < (0.54*255);
    }

    /**
     * Utilitary method for getting a copy of a provided Color but using an
     * specific opacity mask. Intented for use within the library.
     *
     * @param color the color to use as base
     * @param bitMask the bitmask to apply, where the bits 25 to 32 are used
     * @return a copy of the given color, with a modified alpha value
     */
    public static Color applyAlphaMask(Color color, int bitMask) {
        return new Color(color.getRGB() & 0x00FFFFFF | (bitMask & 0xFF000000), true);
    }

    /**
     * Utilitary method for getting a darker version of a provided Color. Unlike
     * {@link Color#darker()}, this decreases color at a fixed step instead of a
     * proportional.
     *
     * @param color the original color
     * @return a {@link Color} sightly darker than the one input.
     */
    public static Color darken(Color color) {
        int r = wrapU8B(color.getRed() - 30);
        int g = wrapU8B(color.getGreen() - 30);
        int b = wrapU8B(color.getBlue() - 30);
        return new Color(r, g, b, color.getAlpha());
    }

    /**
     * Utilitary method for getting a darker version of a provided Color. Unlike
     * {@link Color#brighter()}, this increases color at a fixed step instead of
     * a proportional.
     *
     * @param color the original color
     * @return a {@link Color} sightly brighter than the one input.
     */
    public static Color brighten(Color color) {
        int r = wrapU8B(color.getRed() + 30);
        int g = wrapU8B(color.getGreen() + 30);
        int b = wrapU8B(color.getBlue() + 30);
        return new Color(r, g, b, color.getAlpha());
    }

    public static Color brighten(Color color, int level) {
        int r = wrapU8B(color.getRed() + level);
        int g = wrapU8B(color.getGreen() + level);
        int b = wrapU8B(color.getBlue() + level);
        return new Color(r, g, b, color.getAlpha());
    }

    private static int wrapU8B(int i) {
        return Math.min(255, Math.max(0, i));
    }
}
