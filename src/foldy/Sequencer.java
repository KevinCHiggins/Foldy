/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.util.LinkedList;

/**
 * A sequencer working at the sample/buffer level, providing
 * info on what note attacks fall in a given interval (which
 * will also advance it to the end of that interval, perhaps
 * cycling it back to the start of its sequence). Stores timings
 * by sample but sets them (approximately) in BPM.
 * @author Kevin Higgins
 */
public class Sequencer {
    LinkedList<NoiseWindow> queue;
    public LinkedList<NoiseWindow> getQueue() {
	System.out.println("Returning" + queue.size() + "notes");
	return queue;
    }
    public Sequencer(int[] indices, FrequencySeries series) { // no timing at the mo;
	queue = new LinkedList<>();
	int noteLength = 8000; // HACK TEST - REMOVE, there should be BPM etc.
	//for (int j = 0; j < 2; j++) { // reps were for testing
	    for (int i = 0; i < indices.length; i++) {
		if (indices[i] == 0) {
		    queue.add(new NoiseWindow(null, noteLength)); // a moment's silence
		}
		else {
		    Note n = new Note(series.f(indices[i]), (double) noteLength / 44100);
		    queue.add(new NoiseWindow(n, n.getRemainingSamplesAmount()));
		}
	    }
	//}

    }
}

