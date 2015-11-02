/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

/**
 *
 * @author danj
 */
final class LegendEntry {

    public final char letter;
    public final String definition;

    public LegendEntry(char letter, String definition) {
        this.letter = letter;
        this.definition = definition;
    }
}
