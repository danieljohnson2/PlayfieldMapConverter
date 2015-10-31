/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 *
 * @author danj
 */
public class TranslatorFrame extends javax.swing.JFrame {

    /**
     * Creates new form TranslatorFrame
     */
    public TranslatorFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openButton = new javax.swing.JButton();
        splitPanel = new javax.swing.JSplitPane();
        mapPanel = new playfieldmapconverter.MapPanel();
        translationScrollPanel = new javax.swing.JScrollPane();
        translatedTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        openButton.setText("Open...");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        mapPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mapPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        splitPanel.setLeftComponent(mapPanel);

        translatedTextArea.setEditable(false);
        translatedTextArea.setColumns(20);
        translatedTextArea.setFont(new java.awt.Font("Ubuntu Mono", 0, 18)); // NOI18N
        translatedTextArea.setRows(5);
        translationScrollPanel.setViewportView(translatedTextArea);

        splitPanel.setRightComponent(translationScrollPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(openButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            loadMap(chooser.getSelectedFile());
        }
    }//GEN-LAST:event_openButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private playfieldmapconverter.MapPanel mapPanel;
    private javax.swing.JButton openButton;
    private javax.swing.JSplitPane splitPanel;
    private javax.swing.JTextArea translatedTextArea;
    private javax.swing.JScrollPane translationScrollPanel;
    // End of variables declaration//GEN-END:variables

    public void loadMap(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            RgbMap rgbMap = new RgbMap();

            String translated = rgbMap.translate(image.getRaster());

            mapPanel.loadImage(image);
            translatedTextArea.setText(translated);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
