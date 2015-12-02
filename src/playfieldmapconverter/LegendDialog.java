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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

        this.getRootPane().setDefaultButton(okButton);

        ActionListener escapeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };

        this.getRootPane().registerKeyboardAction(escapeListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        int selectedIndex = -1;
        for (Map.Entry<Rgb, LegendEntry> e : rgbMap.sortedEntryList()) {
            if (Objects.equals(e.getKey(), selectedColor)) {
                selectedIndex = entryListModel.getSize();
            }

            entryListModel.addElement(new Entry(e.getKey(), e.getValue()));
        }

        entryList.setCellRenderer(new CellRenderer());
        entryList.setModel(entryListModel);
        entryList.setSelectedIndex(selectedIndex);

        DocumentListener textFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveEntry();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                saveEntry();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                saveEntry();
            }
        };

        letterField.getDocument().addDocumentListener(textFieldListener);
        definitionField.getDocument().addDocumentListener(textFieldListener);
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

    private Entry loadedEntry;

    private void loadEntry() {
        saveEntry();

        Entry e = (Entry) entryList.getSelectedValue();
        loadedEntry = null; // prevent an implicit save when we do setText().

        if (e != null) {
            letterField.setText(Character.toString(e.letter));
            definitionField.setText(e.definition);
            loadedEntry = e;
        }
    }

    private void saveEntry() {
        if (loadedEntry != null) {
            if (letterField.getText().length() > 0) {
                loadedEntry.letter = letterField.getText().charAt(0);
            }

            loadedEntry.definition = definitionField.getText();

            entryList.repaint();
        }
    }

    public final class Entry {

        public final Rgb color;
        public char letter;
        public String definition;

        public Entry(Rgb color, LegendEntry legendEntry) {
            this.color = color;
            this.letter = legendEntry.letter;
            this.definition = legendEntry.definition;
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

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        entryList = new javax.swing.JList();
        letterLabel = new javax.swing.JLabel();
        letterField = new javax.swing.JTextField();
        definitionLabel = new javax.swing.JLabel();
        definitionField = new javax.swing.JTextField();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Legend");
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
        entryList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        entryList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                entryListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(entryList);

        letterLabel.setText("Letter:");

        definitionLabel.setText("Definition:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(definitionLabel)
                            .addComponent(letterLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(letterField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(definitionField, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                            .addComponent(definitionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(definitionLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelButton)
                            .addComponent(okButton)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

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

    private void entryListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_entryListValueChanged
        loadEntry();
    }//GEN-LAST:event_entryListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField definitionField;
    private javax.swing.JLabel definitionLabel;
    private javax.swing.JList entryList;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField letterField;
    private javax.swing.JLabel letterLabel;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
