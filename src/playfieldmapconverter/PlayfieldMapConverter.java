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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author danj
 */
public class PlayfieldMapConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            mainCli(new File(args[0]));
        } else {
            mainGui();
        }
    }

    private static void mainCli(File inputFile) throws Exception {
        BufferedImage img = ImageIO.read(inputFile);
        RgbMap rgbMap = new RgbMap();
        rgbMap.translateTo(img.getRaster(), System.out);
    }

    private static void mainGui() throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                TranslatorFrame frame = new TranslatorFrame();
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
