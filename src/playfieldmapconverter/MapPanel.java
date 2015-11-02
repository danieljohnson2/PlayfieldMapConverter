/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author danj
 */
public class MapPanel extends JPanel {

    private BufferedImage image;

    public void loadImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            Dimension size = this.getSize();

            double scaleX = size.getWidth() / imageWidth;
            double scaleY = size.getHeight() / imageHeight;
            double scale = Math.min(scaleX, scaleY);

            int drawWidth = (int) (imageWidth * scale);
            int drawHeight = (int) (imageHeight * scale);
            int offsetX = (int) ((size.getWidth() - drawWidth) / 2);
            int offsetY = (int) ((size.getHeight() - drawHeight) / 2);

            g.drawImage(image, offsetX, offsetY, drawWidth, drawHeight, null);
        }
    }
}
