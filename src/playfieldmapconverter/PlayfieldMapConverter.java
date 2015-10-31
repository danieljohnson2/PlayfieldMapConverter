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
        File inputFile;

        if (args.length > 0) {
            inputFile = new File(args[0]);
        } else {
            throw new IllegalArgumentException("Filename is required.");
        }

        BufferedImage img = ImageIO.read(inputFile);
        RgbMap rgbMap = new RgbMap();
        rgbMap.translateTo(img.getRaster(), System.out);
    }
}
