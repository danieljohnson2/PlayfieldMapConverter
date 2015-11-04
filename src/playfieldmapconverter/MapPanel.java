/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import javax.swing.JPanel;

/**
 *
 * @author danj
 */
public class MapPanel extends JPanel implements MouseListener {

    private BufferedImage image;
    private double imageScale = 1.0;
    private int offsetX, offsetY;
    private int selectedX = -1, selectedY = -1;

    public MapPanel() {
        addMouseListener(this);
    }

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
            imageScale = Math.min(scaleX, scaleY);

            int drawWidth = (int) (imageWidth * imageScale);
            int drawHeight = (int) (imageHeight * imageScale);
            offsetX = (int) ((size.getWidth() - drawWidth) / 2);
            offsetY = (int) ((size.getHeight() - drawHeight) / 2);

            g.drawImage(image, offsetX, offsetY, drawWidth, drawHeight, null);

            if (selectedX >= 0 && selectedY >= 0) {
                int selSize = (int) Math.ceil(imageScale);

                g.drawRect(
                        (int) (selectedX * imageScale + offsetX),
                        (int) (selectedY * imageScale + offsetY),
                        selSize,
                        selSize);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectedX = -1;
        selectedY = -1;

        if (image != null) {
            int x = (int) ((e.getX() - offsetX) / imageScale);
            int y = (int) ((e.getY() - offsetY) / imageScale);

            Raster raster = image.getRaster();
            if (x >= 0 && x < raster.getWidth()
                    && y >= 0 && y < raster.getHeight()) {
                selectedX = x;
                selectedY = y;
            }
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
