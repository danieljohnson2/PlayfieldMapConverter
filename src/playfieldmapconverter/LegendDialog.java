/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author danj
 */
public class LegendDialog extends javax.swing.JDialog {

    private final RgbMap rgbMap;
    private final DefaultListModel<Entry> entryListModel = new DefaultListModel<>();

    /**
     * Creates new form LegendDialog
     */
    public LegendDialog(java.awt.Frame parent, RgbMap rgbMap, Rgb selectedColor) {
        super(parent, true);
        this.rgbMap = rgbMap;

        initComponents();

        int selectedIndex = -1;
        for (Map.Entry<Rgb, LegendEntry> e : rgbMap.entrySet()) {
            if (Objects.equals(e.getKey(), selectedColor)) {
                selectedIndex = entryListModel.getSize();
            }

            entryListModel.addElement(new Entry(e.getKey(), e.getValue()));
        }

        entryList.setCellRenderer(new CellRenderer());
        entryList.setModel(entryListModel);
        entryList.setSelectedIndex(selectedIndex);
    }

    private boolean trySave() {
        Set<Character> letters = new HashSet<>();

        for (int i = 0; i < entryListModel.size(); ++i) {
            Character letter = entryListModel.get(i).letter;
            if (!letters.add(letter)) {
                String msg = String.format(
                        "The map letter '%s' appears more than once.",
                        letter);
                JOptionPane.showMessageDialog(this, msg,
                        "Legendary Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        rgbMap.clear();

        for (int i = 0; i < entryListModel.size(); ++i) {
            Entry e = entryListModel.get(i);
            rgbMap.put(e.color, e.toLegendEntry());
        }

        return true;
    }

    public final class Entry {

        public final Rgb color;
        private char letter;
        private String definition;

        public Entry(Rgb color, LegendEntry legendEntry) {
            this.color = color;
            this.letter = legendEntry.letter;
            this.definition = legendEntry.definition;
        }

        public char getLetter() {
            return letter;
        }

        public void setLetter(char newLetter) {
            char oldLetter = letter;
            letter = newLetter;
            firePropertyChange("letter", oldLetter, newLetter);
            entryList.repaint();
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String newDefinition) {
            String oldDef = definition;
            definition = newDefinition;
            firePropertyChange("definition", oldDef, newDefinition);
            entryList.repaint();
        }

        public LegendEntry toLegendEntry() {
            return new LegendEntry(letter, definition);
        }

        @Override
        public String toString() {
            return toLegendEntry().toString();
        }
    }

    private final class CellRenderer extends JLabel implements ListCellRenderer<Entry> {

        @Override
        public void paint(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            super.paint(g);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Entry> list, Entry entry, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            Font font = list.getFont();
            int lineHeight = (int) list.getFontMetrics(font).getHeight();

            BufferedImage swatch = new BufferedImage(lineHeight, lineHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = swatch.createGraphics();
            g.setColor(entry.color.toColor());
            g.fillRect(0, 0, swatch.getWidth(), swatch.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, swatch.getWidth() - 1, swatch.getHeight() - 1);
            g.dispose();;

            setIcon(new ImageIcon(swatch));
            setText(entry.toString());
            setFont(font);
            return this;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        entryList = new javax.swing.JList();
        letterLabel = new javax.swing.JLabel();
        letterField = new javax.swing.JTextField();
        definitionLabel = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setName("legendDialog"); // NOI18N

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        entryList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(entryList);

        letterLabel.setText("Letter:");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, entryList, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.letter}"), letterField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        definitionLabel.setText("Definition:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, entryList, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.definition}"), jTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(definitionLabel)
                            .addComponent(letterLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(letterField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addGap(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(letterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(letterLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(definitionLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (trySave()) {
            dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel definitionLabel;
    private javax.swing.JList entryList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField letterField;
    private javax.swing.JLabel letterLabel;
    private javax.swing.JButton okButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
