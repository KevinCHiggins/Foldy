/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.sound.sampled.SourceDataLine;

/**
 * The engine of Foldy - queries output lines about buffer availability,
 * queries the sequencer about what new Notes fall in the samples it decides to
 * write, then either continues streaming the current Note to the output,
 * --- FIXME --- polyphony!
 * or silence, or cuts to silence or a new note (for each note the
 * sequencer tells it about)
 * @author Kevin Higgins
 */
public class Processor {
    final int CHUNK_SIZE = 512;
    SourceDataLine output;
    LinkedList<NoiseWindow> queue = new LinkedList<>();
    public void testUsing(SourceDataLine out) {
	this.output = out;
	Note n = new Note(1760, .5);
	queue.add(new NoiseWindow(n, n.getRemainingSamplesAmount()));
	queue.add(new NoiseWindow(null, 22500)); // a moment's silence
	n = new Note(880, 1);
	queue.add(new NoiseWindow(n, n.getRemainingSamplesAmount()));	
	queue.add(new NoiseWindow(null, 22500)); // a moment's silence	
	n = new Note(440, 2);
	
	queue.add(new NoiseWindow(n, n.getRemainingSamplesAmount()));
	output.start();
	while (true) {
	    try {
		streamQueue();
		//System.out.println("Streamed");
	    }
	    catch (IOException ioe) {
		System.out.println("Ran out of notes.");
		System.exit(1);
	    }
	}
    }
    public void streamQueue() throws IOException { //exception when queue is utterly exhausted
	
	if ((output.available() * Calc.sampleRate) > CHUNK_SIZE) { // add a chunk from current NoiseWindow
	    NoiseWindow audio = queue.peek();
	    System.out.println("Duration " + audio.getDuration());
	    if (audio.getDuration() <= 0) { // PROBLEM, WHAT IF NOISEWINDOW HAS LENGTH 0 FROM GET GO?
		queue.pop();
		
		audio = queue.peek();
	    }
	    if (audio != null) {
		// this seems clunky... Note will give CHUNK_SIZE of samples
		// or else all that it has left... not providing guarantees at all!
		short[] data = audio.takeSamples(CHUNK_SIZE);
		ByteBuffer byteData = ByteBuffer.allocate(data.length * Calc.bytesPerSample);
		for (short s : data) {
		    byteData.putShort(s);
		    
		}
		// output whatever you got (it's not guaranteed to be CHUNK_SIZE)
		//System.out.println("Test byte" + byteData.array()[40]);
		output.write(byteData.array(), 0, byteData.array().length);
		
	    }
	    else {
		throw new IOException();
	    }

	}
	
    }
    // okay a first try at this: a slice of sound or silence (null note)
    private class NoiseWindow {
	private Note note;
	private int duration; // samples
	public NoiseWindow(Note n, int duration) {
	    this.note = n;
	    this.duration = duration;
	}

	public int getDuration () {
	    return duration;
	}
	private short[] takeSamples(int amount) {
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

}
