/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.Component;
import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;

/**
 * FileUtilities provides simple methods to do simple things that Java makes
 * complicated because Java.
 *
 * @author danj
 */
public final class FileUtilities {

    private static final String MAP_DIR_PREF_KEY = "mapDir";

    private FileUtilities() {
    }

    /**
     * This method shows an open-file dialog and les the user pick a file, which
     * it returns. The method returns null if the user cancels.
     *
     * @param parent The parent window for the dialog.
     * @param filter The filter to restrict what files are shown.
     * @return The file selected, or null if the user cancels.
     */
    public static File chooseOpenFile(Component parent, FileFilter filter) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(FileUtilities.class);
            String mapDir = prefs.get(MAP_DIR_PREF_KEY, "");

            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(filter);

            if (mapDir.length() > 0) {
                File mapDirFile = new File(mapDir);

                if (mapDirFile.exists() && mapDirFile.isDirectory()) {
                    chooser.setCurrentDirectory(mapDirFile);
                }
            }

            int result = chooser.showOpenDialog(parent);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selFile = chooser.getSelectedFile();
                prefs.put(MAP_DIR_PREF_KEY, selFile.getParent());
                prefs.sync();

                return selFile;
            } else {
                return null;
            }
        } catch (BackingStoreException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method shows a dialog that the user can use to select a file, which
     * it returns. If the user cancels, this method returns null.
     *
     * @param parent The parent window for the dialog.
     * @param defaultFile The default file to pre-select in the dialog, or null.
     * @param filter The filter that indicates what files can be viewed.
     * @return The file selected, or null if the user cancels.
     */
    public static File chooseSaveFile(Component parent, File defaultFile, FileFilter filter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);

        if (defaultFile != null) {
            chooser.setSelectedFile(defaultFile);
        }

        int result = chooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * This method writs text given to a specific file, which it will replace if
     * it already exists.
     *
     * @param file The file to overwrite or create.
     * @param text The text to write into the file.
     */
    public static void writeTextFile(File file, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method returns a new file that is the same as the old one, but its
     * file extensions is removed and replaced with the one given.
     *
     * The exntesion you supply may have an initial '.'; if not we supply one,
     * If it is null, however, this method merely strips the extension.
     *
     * If 'file' is itself null, this method returns null.
     *
     * @param file The file to update; may be null.
     * @param extension The new file extensions; may be null.
     * @return The update file, or null if 'file' is null.
     */
    public static File replaceExtension(File file, String extension) {
        if (file == null) {
            return null;
        }

        StringBuilder path = new StringBuilder(file.getPath());

        int pos = path.lastIndexOf(".");

        if (pos > 0) {
            path.delete(pos, Integer.MAX_VALUE);
        }

        if (extension != null) {
            if (!extension.startsWith(".")) {
                path.append('.');
            }

            path.append(extension);
        }

        return new File(path.toString());
    }
}
