/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.Raster;
import java.io.PrintStream;
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
     * This method converts a bitmap into text, which it writes to the stream
     * given. If needed characters are allocated in the map, which means this
     * map may be changed.
     *
     * @param raster The bitmap to translate.
     * @param output The stream to write the textual version to.
     */
    public void translateTo(Raster raster, PrintStream output) {
        int[] pixel = new int[3];

        int width = raster.getWidth();
        int height = raster.getHeight();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                raster.getPixel(x, y, pixel);
                Rgb rgb = Rgb.fromBuffer(pixel);

                output.print(getCharForRgb(rgb));
            }

            output.println();
        }
    }
}
