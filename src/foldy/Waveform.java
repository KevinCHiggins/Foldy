/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class throws around one array, meaning access from other threads may get
 * modified data (e.g. Swing trying to plot the waveform) - but I'll keep it like
 * this for now because array copies would be less performant and all this activity
 * is private to this class (when I'm not plotting for testing purposes).
 * @author Kevin Higgins
 */
public class Waveform {
    final static int SINE = 0;
    
    final static int BEZIER_QUAD = 1;
    final static int SAW = 2;
    // threshold is represented in the range 0-1 because we're in Waveform land
    // not the concretised amplitudes of less abstract classes like Note... ?
    public static int[] getFoldedArray(int shape, int wavelength, int iterations, int threshold) {
	int[] vis = getArray(shape, wavelength);
	
	OmniPlotWindow u = new OmniPlotWindow(vis, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 1);
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException ex) {
	    Logger.getLogger(Waveform.class.getName()).log(Level.SEVERE, null, ex);
	}
	int[] folding = vis;
	// don't see the need for searching for the threshold - as I could
	// be dealing later with waveforms with multiple peaks
	// i.e. more than one crossing-point
	
	// JUST ONE ITERATION FIRST
	// this is the threshold for the current fold which approaches the final threshold
	int sinkingThreshold; 
	// (smallest) difference between sinkingThreshold and threshold for initial fold
	int diff = threshold / (2 << iterations);
	for (int folds = 0; folds < iterations; folds++) {
	    sinkingThreshold = threshold - diff;
	    for (int i = 0; i < folding.length; i++) {

		if (Math.abs(folding[i]) > sinkingThreshold) {
		    //System.out.print("Diff: " + diff + " t " + sinkingThreshold + ". Was " + folding[i]);
		    folding[i] = flipAround(folding[i], sinkingThreshold);
		    //System.out.println(", now " + folding[i]);
		}
	    }	
	    diff = diff * 2;
	    System.out.println("DIFF " + diff);
	}
	
	OmniPlotWindow p = new OmniPlotWindow(folding, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 1);
	return folding;
    }
    private static int flipAround(int origWaveVal, int threshold) { // this'll work if folds are always half or smaller?
	//System.out.println ("FLIP");
	boolean positive = origWaveVal > 0;
	int result = threshold + (threshold - Math.abs(origWaveVal));
	if (!positive) {
	    result = result * -1;
	}
	return result;
	
    }
    public static int[] getArray(int shape, int wavelength) {
	int[] waveform;
	if (shape == SINE) {	    
	    waveform = new int[wavelength];
	    for (int i = 0; i < wavelength; i++) {
	    waveform[i] = (int)(Short.MAX_VALUE * Math.sin(Math.toRadians(((double) i / wavelength) * 360)));
	    //System.out.println((double) i / waveformlength + ", " + (((double) i / waveformlength) * 360) + ", " + waveform[i]);
	    }
	}
	else if (shape == BEZIER_QUAD) {
	    waveform = new int[wavelength]; // FIXME********************************************
	    Envelope bezier = Envelope.getQuadratic(wavelength, Short.MAX_VALUE, false);
	    for (int i = 0; i < wavelength; i++) {
		int samp = bezier.getNextY();
		System.out.println("Reporting from Waveform - bezier... " + samp);
		waveform[i] = samp;
	    }
	}
	else if (shape == SAW) {
	    waveform = new int[wavelength]; // FIXME********************************************
	    Envelope bezier = Envelope.getLinear(Envelope.FALLOFF, wavelength, Short.MAX_VALUE, false);
	    for (int i = 0; i < wavelength; i++) {
		waveform[i] = bezier.getNextY();
	    }
	}
	else {
	    waveform = getArray(SINE, wavelength); // default
	}
	
	return waveform;
    }

}
