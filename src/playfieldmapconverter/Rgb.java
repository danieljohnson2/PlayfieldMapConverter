/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Rgb represents a color, and stores it as a 32 bit integer; we use this as a
 * key in the RgpMap class.
 *
 * @author danj
 */
final class Rgb {

    private final int rgb;

    private Rgb(int r, int g, int b) {
        this.rgb = r << 16 | g << 8 | b;
    }

    /**
     * This method returns the red channel of this Rgb.
     *
     * @return The red channel, 0-255.
     */
    public int getRed() {
        return rgb >> 16;
    }

    /**
     * This method returns the green channel of this Rgb.
     *
     * @return The green channel, 0-255.
     */
    public int getGreen() {
        return (rgb >> 8) & 0xFF;
    }

    /**
     * This method returns the blue channel of this Rgb.
     *
     * @return The blue channel, 0-255.
     */
    public int getBlue() {
        return rgb & 0xFF;
    }

    /**
     * This converts an RGB to a Java Color object, for drawing.
     *
     * @return This same color, as a Java Color object.
     */
    public Color toColor() {
        return new Color(rgb);
    }

    /**
     * This method generates an Rgb, but it interns the most common colors to
     * avoid too many allocations.
     *
     * @param r The red channel, 0-255.
     * @param g The green channel, 0-255.
     * @param b The blue channel, 0-255.
     * @return An RGB containing these values.
     */
    public static Rgb fromRgb(int r, int g, int b) {
        int index = getInternIndex(r, g, b);

        if (index < 0) {
            return new Rgb(r, g, b);
        }

        Rgb found = internedRgbs[index];

        if (found == null) {
            found = new Rgb(r, g, b);
            internedRgbs[index] = found;
        }

        return found;
    }

    /**
     * This method retreives a pixel from an image.
     *
     * @param image The image to read.
     * @param x The x co-ordinate to read from.
     * @param y The y co-ordinate to read from.
     * @return The RGB color found (without the alpha component).
     */
    public static Rgb fromPixel(BufferedImage image, int x, int y) {
        int color = image.getRGB(x, y);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return fromRgb(r, g, b);
    }

    private static Rgb[] internedRgbs = new Rgb[27];

    /**
     * This method figures out what index in 'intenedRgbs' a color shoudl be
     * interned at; it returns -1 for non-interned colors.
     *
     * @param r The red channel, 0-255.
     * @param g The green channel, 0-255.
     * @param b The blue channel, 0-255.
     * @return The index to intern at, or -1 to not do so.
     */
    private static int getInternIndex(int r, int g, int b) {
        int bi = getChannelInternIndex(b);

        if (bi < 0) {
            return -1;
        }

        int gi = getChannelInternIndex(g);

        if (gi < 0) {
            return -1;
        }

        int ri = getChannelInternIndex(r);

        if (ri < 0) {
            return -1;
        }

        return bi + gi * 3 + ri * 9;
    }

    /**
     * Works out an index to use to work out interning for a single channel; for
     * the most common values this returns a small index, and for all others -1.
     *
     * @param channel The color channel value, 0-255.
     * @return An index, 0-2 or -1 to not intern the color.
     */
    private static int getChannelInternIndex(int channel) {
        switch (channel) {
            case 0x00:
                return 0;
            case 0x7F:
                return 1;
            case 0xFF:
                return 2;
            default:
                return -1;
        }
    }

    public static Rgb parse(String text) {
        int rgb = Integer.parseInt(text, 16);

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return fromRgb(r, g, b);
    }

    @Override
    public String toString() {
        return String.format("%06X", rgb);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.rgb;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rgb other = (Rgb) obj;
        if (this.rgb != other.rgb) {
            return false;
        }
        return true;
    }
}
