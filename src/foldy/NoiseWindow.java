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
    public class NoiseWindow {
	private Note note;
	private int duration; // samples
	public NoiseWindow(Note n, int duration) {
	    this.note = n;
	    this.duration = duration;
	}

	public int getDuration () {
	    return duration;
	}
	public short[] takeSamples(int amount) {
	    short[] result;
	    if (note != null) {
		result = note.takeSamples(amount);
	    }
	    else { // silence is intended
		short[] silent = new short[amount];
		for (int i = 0; i < amount; i++) { // DO I NEED IT?
		    silent[i] = 0;
		}
		result = silent;
	    }
	    duration = duration - result.length;
	    return result;
	}
	
    }