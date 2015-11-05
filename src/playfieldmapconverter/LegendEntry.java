/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

import java.util.Objects;

/**
 * This holds the data for a single legend entry; a character and the text that
 * describes what that character means.
 *
 * @author danj
 */
final class LegendEntry {

    public final char letter;
    public final String definition;

    public LegendEntry(char letter, String definition) {
        this.letter = letter;
        this.definition = Objects.requireNonNull(definition);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", letter, definition);
    }
}
