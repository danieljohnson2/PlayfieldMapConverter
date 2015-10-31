/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.Raster;
import java.util.HashMap;

/**
 * RgbMap is a mapping that maps RGB color values to characters that represent
 * them in the output file. This can allocate characters, but starts with a
 * library of 'known' characters.
 *
 * @author danj
 */
class RgbMap extends HashMap<Rgb, Character> {

    private char nextChar = 'A';
    private final int[] pixel = new int[3];

    public RgbMap() {
        putRgb(0x7F, 0x7F, 0x7F, '#');
        putRgb(0x00, 0xFF, 0x00, ',');
        putRgb(0xFF, 0xFF, 0x00, '.');
        putRgb(0x00, 0x7F, 0x00, '&');
        putRgb(0x7F, 0x00, 0x00, '+');
        putRgb(0x00, 0x00, 0xFF, '/');
    }

    /**
     * This method adds a color and letter to the map.
     *
     * @param r The red channel, 0-255.
     * @param g The blue channel, 0-255.
     * @param b The green channel, 0-255.
     * @param letter A letter to represent the color.
     */
    public void putRgb(int r, int g, int b, char letter) {
        put(Rgb.fromRgb(r, g, b), letter);
    }

    /**
     * This method selects a character to represent a specific color.
     *
     * @param rgb The color to represent.
     * @return The character to use for this color.
     */
    public char getCharForRgb(Rgb rgb) {
        Character found = get(rgb);
        if (found == null) {
            do {
                found = nextChar;
                ++nextChar;
            } while (containsKey(nextChar));

            put(rgb, found);
//            System.out.println(String.format("Found %s -> %s", rgb, found));
        }

        return found;
    }

    /**
     * Returns the Rgb for the color at a specific pixel in a raster.
     *
     * @param raster The image to read from.
     * @param x The x co-ordinate to read.
     * @param y The y co-ordinate to read.
     * @return An Rgb containing the color at this pixel.
     */
    public Rgb getRgb(Raster raster, int x, int y) {
        raster.getPixel(x, y, pixel);
        return Rgb.fromBuffer(pixel);
    }
}
