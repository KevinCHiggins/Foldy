/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import static java.lang.Math.sin;

/**
 * Represents a single note to be sounded by tapping its data which is
 * generated on request. A Note is like sampled audio conceptually, in that
 * it always evaluates the same, to digital data (at the app's given
 * sample rate etc.). ONCE PLAYED, a Note can never be played again!
 * Parameters include length (in samples but set in seconds), an amplitude
 * curve (we'll start with just a line), a function (later to be spun out into
 * a class) to generate the wave from freq and amp curve, and a wavelength
 * in samples (set in Hz). 
 * (The wave creating function is going to become wave folding later.)
 * Because of my current level of knowledge, this class won't allow
 * pitch envelopes or curves right now. All I'll do is repeat copies
 * of a whole waveform while diminishing amplitude to get a simple attenuating
 * tone.
 * @author Kevin Higgins
 */
public class Note {
    private double targetFreq;
    private int wavelength; // IN SAMPLES!
    private int length; // IN SAMPLES
    private int offset; // samples
    private double[] wave; // will hold the actual waveform used
    public Note(double frequency, double length) { // basic constructor for now
	this.offset = 0;
	this.setFreq(frequency);
	this.length = Calc.secsToSamples(length);
	buildWaveformArray(); // basic method to start with
    }
    public double ampFunction() { // linear falloff to start
	if (offset > 0){
	    return 1 - (((double) offset) / length);
	}
	else {
	    return 1;
	}
    }
    private void buildWaveformArray() {
	wave = new double[wavelength];
	for (int i = 0; i < wavelength; i++) {
	    wave[i] = Math.sin(Math.toRadians(((double) i / wavelength) * 360));
	    System.out.println((double) i / wavelength + ", " + (((double) i / wavelength) * 360) + ", " + wave[i]);
	}
    }
    
    private void setFreq(double hertz) {
	targetFreq = hertz;
	wavelength = Calc.secsToSamples(1 / hertz);
	System.out.println("Wavelength set to " + wavelength + " target " + hertz);
    }
    public double getTargetFreq() {
	return targetFreq;
    }
    public int getWavelengthInSamples() {
	return wavelength;
    }
    public int getRemainingSamplesAmount() {
	return length - offset;
    }
    public short[] takeSamples(int amount) {
	// this is not great - providing no guarantees -
	// either return amount or whatever's left. I guess it's got a crude
	// intuitive logic.
	if (getRemainingSamplesAmount() < amount) {
	    amount = getRemainingSamplesAmount();
	}
	short[] samples = new short[amount];
	for (int i = 0; i < amount; i++) {
	   
	    short wavePos = (short) (wave[offset % wavelength]* ampFunction() * 50);
	    samples[i] = wavePos;
	    if (wavePos < 0) {
		for (int j = 0; j < 50 + wavePos; j++) {
		    System.out.print(" ");
		}
	    }
	    else {
		for (int j = 0; j < wavePos + 50; j++) {
		    System.out.print(" ");
		}
	    }
	    System.out.println("|" + wavePos);
	    offset++;
	}
	return samples;
    }

}
