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
            Dimension size = this.getSize();

            g.drawImage(image, 0, 0, (int)size.getWidth(), (int)size.getHeight(), null);
        }
    }
}
