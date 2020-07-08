/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * Note - calls OmniPlot
 * @author Kevin Higgins
 */
public class Chunk {
    
    short[] chunk;
    int size; // many samples per chunk - always should be more than 220
    int multiple; // how many cycles of the wave per chunk - a small whole number
    
    public Chunk(Pitch targetFreq, Wave wave) {
	size(targetFreq);
	make(wave);
    }
    
    private void size(Pitch pitch) {
	size = Calc.sampleRate * pitch.getDenominator() / pitch.getNumerator();
	multiple = 1;
	while (size < 220) {
	    multiple++;
	    size = (Calc.sampleRate * multiple * pitch.getDenominator() / pitch.getNumerator());
	}
	System.out.println("In chunk, mult " + multiple + ", size " + size + " for pitch " + pitch.getNumerator() + "/" + pitch.getDenominator());
	chunk = new short[size];
    }
    
    private void make(Wave wave) {
	//if (wave.form == Wave.Form.SINE) {
	    
	    
	    for (int i = 0; i < size; i++) {
		chunk[i] = wave.functionFolded(i, size, multiple);
		
	    }
	    //new OmniPlotWindow(chunk, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 5);
	//}
    }
    
    // because of multiplying, wavelength is likely a non-integer number
    public Fraction getActualWavelength() {
	return new Fraction(size, multiple);
    }
    public int getSmp(int index) {
	return chunk[index];
    }
    public int getSize() {
	return size;
    }
}
