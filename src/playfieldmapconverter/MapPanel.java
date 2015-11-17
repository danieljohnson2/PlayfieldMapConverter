/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;

/**
 *
 * @author danj
 */
public class MapPanel extends JPanel implements MouseListener {

    private BufferedImage image;
    private double imageScale = 1.0;
    private int offsetX, offsetY;
    private Rgb selectedColor = null;
    private Shape selected = null;
    private List<ActionListener> actionListeners = new ArrayList<>();

    public MapPanel() {
        addMouseListener(this);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.selected = null;
        this.selectedColor = null;
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();

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

            g2.translate(offsetX, offsetY);
            g2.scale(imageScale, imageScale);

            g2.drawImage(image, 0, 0, null);

            if (selected != null) {
                g2.setColor(new Color(0, 0, 0, .4f));
                g2.fill(selected);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2.0f / (float) imageScale));
                g2.draw(selected);
            }

            g2.dispose();
        }
    }

    public Rgb getSelectedColor() {
        return selectedColor;
    }

    private void setSelectedColor(Rgb newColor) {
        if (!Objects.equals(newColor, selectedColor)) {
            Rgb oldColor = selectedColor;
            selectedColor = newColor;

            repaint();
            firePropertyChange("SelectedColor", oldColor, newColor);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_FIRST, "Clicked");

            for (ActionListener l : actionListeners) {
                l.actionPerformed(evt);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = null;
        Rgb clickedColor = null;

        if (image != null) {
            int hitX = (int) ((e.getX() - offsetX) / imageScale);
            int hitY = (int) ((e.getY() - offsetY) / imageScale);

            Raster raster = image.getRaster();
            if (hitX >= 0 && hitX < raster.getWidth()
                    && hitY >= 0 && hitY < raster.getHeight()) {

                clickedColor = Rgb.fromPixel(image, hitX, hitY);

                int height = image.getHeight();
                int width = image.getWidth();

                Area area = new Area();

                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        if (clickedColor.equals(Rgb.fromPixel(image, x, y))) {
                            area.add(new Area(new Rectangle(x, y, 1, 1)));
                        }
                    }
                }

                selected = area;

                setSelectedColor(clickedColor);
            }
        }

        setSelectedColor(clickedColor);
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

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }
}
