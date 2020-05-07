/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * for the moment, I'll rough this with a bare array with a bounds test. -1 
 * only means a rest. 60 is the middle note and they can range from 0-128.
 * If the sequencer is played past its end, it will keep returning rests
 * unless it's looped
 * @author Kevin Higgins
 */
public class Sequence {
    boolean looped = false; // for later
    private int[] sequence;
    
    int playHead; // index for getting values out in order
    // a deep copy is safer, and can be done while testing
    public Sequence(int[] sequence) {
	
	for (int i = 0; i < sequence.length; i++) {
	    int candidate = sequence[i];
	    if (candidate >= 0) {
		if (candidate < 128) {
		    this.sequence[i] = sequence[i];
		}
		else {
		    this.sequence[i] = 127;
		}
	    }
	    else {
		this.sequence[i] = -1;
	    }
	}
    }
    
    public int play() {
	if (playHead < sequence.length - 1) {
	    return sequence[playHead++];
	}
	else {
	    if (looped) {
		playHead = 0;
		return sequence[playHead++];
	    }
	    else { // keep advancing play head (probably pointless but it feels right)
		playHead++;
		return -1; // return a rest
	    }
	}
    }
    public int getLength() {
	return sequence.length;
    }
}
