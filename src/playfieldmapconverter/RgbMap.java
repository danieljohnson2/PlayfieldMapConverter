/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * RgbMap is a mapping that maps RGB color values to cells that can be placed in
 * a Playfield map. This can allocate characters, but starts with a library of
 * 'known' characters.
 *
 * @author danj
 */
final class RgbMap extends HashMap<Rgb, LegendEntry> {

    private char nextChar = 'A';

    public RgbMap() {
        putRgb(0x7F, 0x7F, 0x7F, '#', "Wall");
        putRgb(0x00, 0xFF, 0x00, ',', "Path");
        putRgb(0xFF, 0xFF, 0x00, '.', "Grass");
        putRgb(0x00, 0x7F, 0x00, '&', "Grass, Trees");
        putRgb(0x7F, 0x00, 0x00, '+', "Door");
        putRgb(0x00, 0x00, 0xFF, '/', "Water");
    }

    /**
     * This method adds a color and letter to the map.
     *
     * @param r The red channel, 0-255.
     * @param g The blue channel, 0-255.
     * @param b The green channel, 0-255.
     * @param letter A letter to represent the color.
     * @param defintiion The definition text for the final file.
     */
    public void putRgb(int r, int g, int b, char letter, String definition) {
        put(Rgb.fromRgb(r, g, b), new LegendEntry(letter, definition));
    }

    /**
     * This method selects an entry to represent a specific color, or creates
     * one if required..
     *
     * @param rgb The color to represent.
     * @return The legend entry for for this color.
     */
    public LegendEntry getOrCreate(Rgb rgb) {
        LegendEntry found = get(rgb);
        if (found == null) {
            do {
                found = new LegendEntry(nextChar, "undefined");
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
     * @return The translated text.
     */
    public String translate(BufferedImage image) {
        StringBuilder b = new StringBuilder();
        String newLine = String.format("%n");

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Rgb rgb = Rgb.fromPixel(image, x, y);
                b.append(getOrCreate(rgb).letter);
            }

            b.append(newLine);
        }

        b.append("-" + newLine);

        for (LegendEntry e : values()) {
            b.append(e + newLine);
        }

        return b.toString();
    }
}
