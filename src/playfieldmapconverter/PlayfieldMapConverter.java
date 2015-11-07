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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

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
        RgbMap rgbMap = RgbMap.createDefault();
        String translated = rgbMap.translate(img);
        System.out.print(translated);
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
