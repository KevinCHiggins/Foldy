/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import static java.lang.Math.sin;
import java.util.LinkedList;

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
    private int[] wave; // will hold the actual waveform used
    private LinkedList<int[]> cachedWaves = new LinkedList<>();
    
    // I'd hoped for something more exotic than an envelope for amplitude
    // for the moment I'll use this to store a fairly well-sampled (dozens?)
    // quadratic curve
    private Envelope env; 
    private int envIndex;
    private int envRun; // the length in samples of the current segment
    private int currentRun; // how far through the current segmetn we are, in samples
    public Note(double frequency, double lengthInSecs) { // basic constructor for now
	this.offset = 0;
	this.setFreq(frequency);
	this.length = Calc.secsToSamples(lengthInSecs);
	fillWaveformArray(); // basic method to start with
	System.out.println("Building note, about to call env, lenght is " + length);
	env = Envelope.getQuadratic(this.length, Short.MAX_VALUE, true);
    }
    // This should only be called from a single point in Note, as other calls will
    // suck out data intended for streaming and desync the envelope from the note!!
    private int ampFunction() { 
	return env.getNextY();
    }
    
    private void fillWaveformArray() {
	
	// do a simple caching here:
	// search for one of same length (obviously needs to be redone for
	// mixed waveforms) and if none found, insert current
	int index = 0;
	boolean match = true;
	if (cachedWaves.size() > 0) {
	    while (cachedWaves.get(index).length < wavelength) {
		index++;
	    }
	    if (cachedWaves.get(index).length > wavelength) { // next is too big so no match
		match = false;
	    }
	    else { // match!
	    wave = cachedWaves.get(index);
	    }
	}
	else { // nothing cached so can't be a match
	    match = false;
	}

	if (!match) { // no matching cached wave so make a new one
	    
	    wave = Waveform.getFoldedArray(Waveform.SINE, wavelength, 3, Short.MAX_VALUE);
	    cachedWaves.add(index, wave); // and stow it
	
	}
    }
    
    private void setFreq(double hertz) {
	targetFreq = hertz;
	wavelength = Calc.secsToSamples(1 / hertz);
	//System.out.println("Wavelength set to " + wavelength + " target " + hertz);
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
	int counter = 0;
	for (int i = 0; i < amount; i++) {
	    //System.out.println(ampFunction());
	    samples[i] = (short) (wave[offset % wavelength]* ampFunction() / (double)Short.MAX_VALUE);
	    //if (samples[i] > 0) {
	    /*
	    counter++;
	    if (counter > 20) {
		counter = 0;
		for (int j = 0; j < 30 + (samples[j]/ 500); j++) {
		System.out.print(" ");
		}
	   /* }
	    for (int j = 30; j < samples[j]/ 500; j++) {
		System.out.print(" ");
	    } 
	    System.out.println("|");
	    }
	    */
	    
	    offset++;
	}
	return samples;
    }

}
