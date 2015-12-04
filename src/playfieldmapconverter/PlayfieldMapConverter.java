/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import static playfieldmapconverter.FileUtilities.*;

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
            for (String path : args) {
                mainCli(new File(path));
            }
        } else {
            mainGui();
        }
    }

    private static void mainCli(File imageFile) throws Exception {
        File rgbMapFile = RgbMap.getLegendFile(imageFile);
        File outputFile = replaceExtension(imageFile, ".txt");

        if (outputFile.equals(imageFile)) {
            return;
        }

        BufferedImage img = ImageIO.read(imageFile);
        RgbMap rgbMap;

        if (rgbMapFile.exists()) {
            rgbMap = RgbMap.readFrom(rgbMapFile);
        } else {
            rgbMap = new RgbMap();
        }

        String translated = rgbMap.translate(img);
        writeTextFile(outputFile, translated);
        System.out.println("Converted: " + outputFile.getName());
    }

    private static void mainGui() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }

                    TranslatorFrame frame = new TranslatorFrame();
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        );
    }
}
