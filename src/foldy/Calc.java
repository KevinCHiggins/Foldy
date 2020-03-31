/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 *
 * @author Kevin Higgins
 */
public class Calc {
    static int sampleRate; // samples per second
    static int bytesPerSample;
    public Calc(SoundSetup setup) {
	
	this.sampleRate = setup.getSampleRate();
	this.bytesPerSample = setup.getBytesPerSample();
    }
    // official timing approximator for the whole app!
    public static int secsToSamples(double secs) {
	System.out.println("In Calc, getting samples in " + secs + " secs, rate " + sampleRate);
	return (int) (secs * sampleRate);
    }

}
