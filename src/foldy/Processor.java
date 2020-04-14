/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	FrequencySeries sub = new FrequencySeries(8000, 1);
	Sequencer seq;
	//seq = new Sequencer(new int[] {8, 8, 16, 16, 0, 0, 0, 16, 14, 14, 15, 15, 16, 16, 18, 18, 0, 0, 8, 8, 0, 0, 0, 16, 15, 15, 18, 18, 0, 0, 0, 0}, sub);
	
	/* descending subharmonics
	int[] vals = new int[13];
	for (int i = 5; i < 18; i++) {
	    vals[i - 5] = i;
	}
	for (int j : vals) {
	    System.out.print(j + " ");
	}
	seq = new Sequencer(vals, sub);
	*/
	//seq = new Sequencer(new int[] {4, 5, 3, 4, 5, 2, 3, 4, 5}, sub);
	seq = new Sequencer(new int[] {10}, sub);
	this.output = out;
	queue = seq.getQueue();
	output.start();
	while (true) {
	    try {
		streamQueue();
		//System.out.println("Streamed");
	    }
	    catch (IOException ioe) {
		System.out.println("Ran out of notes.");
		//System.exit(1);
	    }
	}
	};
    public void streamQueue() throws IOException { //exception when queue is utterly exhausted
	
	if ((output.available() * Calc.sampleRate) > CHUNK_SIZE) { // add a chunk from current NoiseWindow
	    NoiseWindow audio = queue.peek();
	    //System.out.println("Duration " + audio.getDuration());
	    if (audio.getDuration() <= 0) { // PROBLEM, WHAT IF NOISEWINDOW HAS LENGTH 0 FROM GET GO?
		queue.pop();
		
		audio = queue.peek();
	    }
	    if (audio != null) {
		// this seems clunky... Note will give CHUNK_SIZE of samples
		// or else all that it has left... not providing guarantees at all!
		short[] data = audio.takeSamples(CHUNK_SIZE);
		ByteBuffer byteData = ByteBuffer.allocate(data.length * Calc.bytesPerSample);
		byteData.order(ByteOrder.LITTLE_ENDIAN);
		for (short s : data) {
		    byteData.putShort(s);
		    
		}
		
		//System.out.println("Test byte" + byteData.array()[40]);
		
		/*for (int i = 0; i < byteData.array().length; i++) {
		    System.out.println(byteData.array()[i]);
		}
		*/
		
		// output whatever you got (it's not guaranteed to be CHUNK_SIZE)
		output.write(byteData.array(), 0, byteData.array().length);
		
	    }
	    else {
		throw new IOException();
	    }

	}
	
    }
    // okay a first try at this: a slice of sound or silence (null note)


}
