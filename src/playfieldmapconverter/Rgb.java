/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playfieldmapconverter;

/**
 * Rgb represents a color, and stores it as a 32 bit integer;
 * we use this as a key in the RgpMap class.
 * @author danj
 */
final class Rgb {
    private int rgb;

    public Rgb(int r, int g, int b) {
        rgb = r << 16 | g << 8 | b;
    }

    /**
     * This method generates an Rgb instance from a buffer
     * that is used with a Raster instance; the pixels are
     * actually 16-bits wide here. They are all ints because
     * they are postivie numbers, and a 'short' won't hold
     * the maximum value 0xFFFF.
     * 
     * @param buffer The buffer to read pixel data from.
     * @return An Rgb instance holding the buffer data.
     */
    public static Rgb fromBuffer(int[] buffer) {
        int r = buffer[0] >>> 8;
        int g = buffer[1] >>> 8;
        int b = buffer[2] >>> 8;
        return new Rgb(r, g, b);
    }

    @Override
    public String toString() {
        return String.format("%06X", rgb);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.rgb;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rgb other = (Rgb) obj;
        if (this.rgb != other.rgb) {
            return false;
        }
        return true;
    }    
}
