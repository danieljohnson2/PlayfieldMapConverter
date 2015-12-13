/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import com.sun.webkit.SharedBuffer;
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
 * This panel displays the map image, expanded to fit the space available. You
 * can select a color present in this image, and we'll hilight all pixels of the
 * color; you can double click and we'll invoke an action listener.
 *
 * @author danj
 */
public class MapPanel extends JPanel {

    private BufferedImage image;
    private double imageScale = 1.0;
    private int offsetX, offsetY;
    private Rgb selectedColor = null;
    private Shape selectedArea = null;
    private List<ActionListener> actionListeners = new ArrayList<>();

    public MapPanel() {
        addMouseListener(new MouseProxyListener());
    }

    /**
     * This method returns the image displayed in this panel.
     *
     * @return The image displayed, or null if none is.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * This method places an image in this panel, and displays it. This also
     * resets the selected color.
     *
     * @param image The image to display.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        this.selectedArea = null;
        this.selectedColor = null;

        repaint();
    }

    /**
     * This returns the selected color.
     *
     * @return The selected color, or null if no color is selected.
     */
    public Rgb getSelectedColor() {
        return selectedColor;
    }

    /**
     * this sets the selected color, and displays the pixels of that color with
     * a special hilight.
     *
     * @param newColor The new color to apply; may be null.
     */
    private void setSelectedColor(Rgb newColor) {
        if (!Objects.equals(newColor, selectedColor)) {
            Rgb oldColor = selectedColor;
            selectedColor = newColor;
            selectedArea = computeSelectedArea(newColor);

            repaint();
            firePropertyChange("SelectedColor", oldColor, newColor);
        }
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

            if (selectedArea != null) {
                g2.setColor(new Color(0, 0, 0, .4f));
                g2.fill(selectedArea);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2.0f / (float) imageScale));
                g2.draw(selectedArea);
            }

            g2.dispose();
        }
    }

    /**
     * This extracst the color found in the panel at a given point, measures in
     * the panel's co-ordinates (not the image's!).
     *
     * @param x The x co-ordinate, in the panel's co-ordinate space.
     * @param y The y co-ordinate, in the panel's co-ordinate space.
     * @return The color found there, or null if outside the image.
     */
    private Rgb getColorAt(int x, int y) {
        if (image != null) {
            int hitX = (int) ((x - offsetX) / imageScale);
            int hitY = (int) ((y - offsetY) / imageScale);

            if (hitX >= 0 && hitX < image.getWidth()
                    && hitY >= 0 && hitY < image.getHeight()) {

                return Rgb.fromPixel(image, hitX, hitY);
            }
        }

        return null;
    }

    /**
     * This method generates the shape that covers all the pixels of the image
     * of a given color, or null if no pixels are covered, or if the image is
     * missing or 'color' is null.
     *
     * The shape returned will be in the image's co-ordinate system.
     *
     * @param color The color to be hilighted.
     * @return The shape that covers the pixels, or null.
     */
    private Shape computeSelectedArea(Rgb color) {
        if (image != null && color != null) {
            int height = image.getHeight();
            int width = image.getWidth();

            Area area = new Area();

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    if (color.equals(Rgb.fromPixel(image, x, y))) {
                        area.add(new Area(new Rectangle(x, y, 1, 1)));
                    }
                }
            }

            return area;
        }

        return null;
    }

    /**
     * This method registers a listener to be invokes when the user
     * double-clicks in the panel.
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * This method unregisters a listener to be invokes when the user
     * double-clicks in the panel.
     *
     * @param listener
     */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    /**
     * This method invokes any action listeners that may be registered.
     */
    private void fireClickedAction() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_FIRST, "Clicked");

        for (ActionListener l : actionListeners) {
            l.actionPerformed(evt);

        }
    }

    /**
     * This little proxy object handles mouse events, so we don't have to expose
     * the methods publicly.
     */
    private class MouseProxyListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                fireClickedAction();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setSelectedColor(getColorAt(e.getX(), e.getY()));
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
}
