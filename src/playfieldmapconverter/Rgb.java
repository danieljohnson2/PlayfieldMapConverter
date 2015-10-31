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
final class Rgb {
    private int rgb;

    public Rgb(int r, int g, int b) {
        rgb = r << 16 | g << 8 | b;
    }

    public static Rgb fromBuffer(int[] pixel) {
        int r = pixel[0] >>> 8;
        int g = pixel[1] >>> 8;
        int b = pixel[2] >>> 8;
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
