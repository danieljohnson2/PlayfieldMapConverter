/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.HashMap;
import javax.imageio.*;

/**
 *
 * @author danj
 */
public class PlayfieldMapConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("/home/danj/Venice.png"));
        Raster raster = img.getRaster();

        int width = raster.getWidth();
        int height = raster.getHeight();

        RgbMap rgbMap = new RgbMap();

        int[] pixel = new int[3];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int rgb = rgbMap.getRgb(raster, x, y);
                System.out.print(rgbMap.getCharForRgb(rgb));
            }

            System.out.println();
        }
    }

    private static class RgbMap extends HashMap<Integer, Character> {

        private char nextChar = 'A';
        private final int[] pixel = new int[3];

        public char getCharForRgb(int rgb) {
            Character found = get(rgb);

            if (found == null) {
                found = nextChar;
                ++nextChar;
                put(rgb, found);
            }

            return found;
        }

        public int getRgb(Raster raster, int x, int y) {
            raster.getPixel(x, y, pixel);
            int r = pixel[0] >>> 8;
            int g = pixel[1] >>> 8;
            int b = pixel[2] >>> 8;
            return r << 16 | g << 8 | b;
        }
    }
}
