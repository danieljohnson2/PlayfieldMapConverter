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
        put(new Rgb(0x7F, 0x7F, 0x7F), '#');
        put(new Rgb(0x00, 0xFF, 0x00), ',');
        put(new Rgb(0xFF, 0xFF, 0x00), '.');
        put(new Rgb(0x00, 0x7F, 0x00), '&');
        put(new Rgb(0x7F, 0x00, 0x00), '+');
        put(new Rgb(0x00, 0x00, 0xFF), '/');
    }

    public char getCharForRgb(Rgb rgb) {
        Character found = get(rgb);
        if (found == null) {
            found = nextChar;
            ++nextChar;
            put(rgb, found);
//            System.out.println(String.format("Found %s -> %s", rgb, found));
        }
        return found;
    }

    public Rgb getRgb(Raster raster, int x, int y) {
        raster.getPixel(x, y, pixel);
        return Rgb.fromBuffer(pixel);
    }
}
