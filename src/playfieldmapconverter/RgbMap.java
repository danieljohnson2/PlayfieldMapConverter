/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RgbMap is a mapping that maps RGB color values to cells that can be placed in
 * a Playfield map. This can allocate characters, but starts with a library of
 * 'known' characters.
 *
 * @author danj
 */
final class RgbMap extends HashMap<Rgb, LegendEntry> {

    private char nextChar = 'a';

    public RgbMap() {
    }

    private static Map<Rgb, LegendEntry> defaultEntries = createDefault();

    private static Map<Rgb, LegendEntry> createDefault() {
        RgbMap map = new RgbMap();
        map.putRgb(0x7F, 0x7F, 0x7F, '#', "Wall");
        map.putRgb(0xFF, 0xFF, 0x00, ',', "Path");
        map.putRgb(0x00, 0xFF, 0x00, '.', "Grass");
        map.putRgb(0x00, 0x7F, 0x00, '&', "Tree");
        map.putRgb(0x7F, 0x00, 0x00, '+', "Door");
        map.putRgb(0x00, 0x00, 0xFF, '/', "Water");

        String autoDoorChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < autoDoorChars.length(); ++i) {
            char letter = autoDoorChars.charAt(i);

            String def = String.format("Door : AutoDoor%s", letter);

            map.putRgb(i + 1, i + 1, i + 1, letter, def);
        }

        return Collections.unmodifiableMap(map);
    }

    /**
     * This method adds a color and letter to the map.
     *
     * @param r The red channel, 0-255.
     * @param g The blue channel, 0-255.
     * @param b The green channel, 0-255.
     * @param letter A letter to represent the color.
     * @param defintiion The definition text for the final file.
     */
    public void putRgb(int r, int g, int b, char letter, String definition) {
        put(Rgb.fromRgb(r, g, b), new LegendEntry(letter, definition));
    }

    /**
     * This method selects an entry to represent a specific color, or creates
     * one if required..
     *
     * @param rgb The color to represent.
     * @return The legend entry for for this color.
     */
    public LegendEntry getOrCreate(Rgb rgb) {
        LegendEntry found = get(rgb);

        if (found != null) {
            return found;
        }

        LegendEntry defaultEntry = defaultEntries.get(rgb);
        String definition = "undefined";

        if (defaultEntry != null) {
            definition = defaultEntry.definition;

            if (!containsKey(defaultEntry.letter)) {
                found = new LegendEntry(defaultEntry.letter, definition);
                put(rgb, found);
                return found;
            }
        }

        for (; nextChar < 256; ++nextChar) {
            if (!containsLetter(nextChar)) {
                found = new LegendEntry(nextChar, definition);
                put(rgb, found);
                return found;
            }
        }

        throw new IllegalStateException("Too many letters have been used.");
    }

    /**
     * This method converts a bitmap into text, which it writes to the stream
     * given. If needed characters are allocated in the map, which means this
     * map may be changed.
     *
     * @param raster The bitmap to translate.
     * @return The translated text.
     */
    public String translate(BufferedImage image) {
        StringBuilder b = new StringBuilder();
        String newLine = String.format("%n");

        int width = image.getWidth();
        int height = image.getHeight();

        Set<Character> lettersEncountered = new HashSet<>();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Rgb rgb = Rgb.fromPixel(image, x, y);
                char letter = getOrCreate(rgb).letter;
                lettersEncountered.add(letter);
                b.append(letter);
            }

            b.append(newLine);
        }

        b.append("-" + newLine);

        for (Entry<Rgb, LegendEntry> e : sortedEntryList()) {
            if (lettersEncountered.contains(e.getValue().letter)) {
                b.append(e.getValue() + newLine);
            }
        }

        return b.toString();
    }

    /**
     * This methpd returns a file that indicate where to place the legend file
     * for this map.
     *
     * This is just the file named 'Legend.txt" in the same directory as
     * imageFile; this means all images in the same directory will share this
     * file.
     *
     * @param imageFile The image file whoe legend is needed.
     * @return The file where that legend should be stored.
     */
    public static File getLegendFile(File imageFile) {
        return new File(
                imageFile.getParentFile(),
                "Legend.txt");
    }

    /**
     * This method reads a legend file and returns a new RgbMap with that
     * content.
     *
     * This method wraps IOExceptions for you; you are not expected to handle
     * them.
     *
     * @param file The file to read.
     * @return A new RgbMap containing the data.
     */
    public static RgbMap readFrom(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return readFrom(reader);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method reads a reader and generates an RgbMap from it.
     *
     * @param reader The reader to drain.
     * @return A new RgbMap containing the data.
     * @throws IOException If an IO error occurs.
     */
    private static RgbMap readFrom(BufferedReader reader) throws IOException {
        RgbMap rgbMap = new RgbMap();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() < 9
                    || line.charAt(6) != ':' || line.charAt(8) != ':') {
                throw new IllegalStateException(String.format(
                        "The line '%s' does not have the color-letter prefix.",
                        line));
            }

            String colorText = line.substring(0, 6).trim();
            char letter = line.charAt(7);
            String defintition = line.substring(9).trim();

            Rgb rgb = Rgb.parse(colorText);
            rgbMap.put(rgb, new LegendEntry(letter, defintition));
        }

        return rgbMap;
    }

    /**
     * This method writes this RgbMap to a file, and will replace an existing
     * file, if there is one.
     *
     * This method wraps IOExceptions for you; you are not expected to handle
     * them.
     *
     * @param file The file to overwrite with the legend file.
     */
    public void writeTo(File file) {
        try {
            try (PrintStream s = new PrintStream(file)) {
                writeTo(s);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeTo(PrintStream stream) throws IOException {
        for (Entry<Rgb, LegendEntry> e : sortedEntryList()) {
            stream.println(String.format("%s:%s:%s",
                    e.getKey(),
                    e.getValue().letter,
                    e.getValue().definition));
        }
    }

    /**
     * This method returns a list of the entries of this map, but they are
     * sorted by the letter that designates the entry in the translated map
     * file.
     *
     * @return A list of entries.
     */
    public List<Entry<Rgb, LegendEntry>> sortedEntryList() {
        List<Entry<Rgb, LegendEntry>> list = new ArrayList<>(entrySet());

        list.sort(new Comparator<Map.Entry<Rgb, LegendEntry>>() {
            @Override
            public int compare(Entry<Rgb, LegendEntry> left, Entry<Rgb, LegendEntry> right) {
                return Character.compare(
                        left.getValue().letter,
                        right.getValue().letter);
            }
        });

        return Collections.unmodifiableList(list);
    }

    /**
     * This method returns true if any entry has the letter given.
     *
     * @param letter The letter to look for.
     * @return True if the letter is already in some entry.
     */
    public boolean containsLetter(char letter) {
        for (LegendEntry e : values()) {
            if (e.letter == letter) {
                return true;
            }
        }

        return false;
    }
}
