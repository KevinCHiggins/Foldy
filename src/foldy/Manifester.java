/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.sound.sampled.SourceDataLine;

/**
 * The engine of Foldy - uses attacks and rests from a sequence to create an
 * array of Segments - slices defined by the Note or silence that they contain,
 * and a duration (which, if they contain a Note, must be equal to or less than
 * its duration, and stretching from any Note start or end. So, Notes get made
 * then referred to by the Segments. A null ref to a Note is a silence. Then
 * this info is manifested.
 * @author Kevin Higgins
 */
public class Manifester {
    final int BLOCK_SIZE_BYTES = 2048;
    SourceDataLine output;
    private class Segment {
	Note n;
	int duration;
	public Segment(Note n, int duration) {
	    this.n = n;
	    this.duration = duration;
	}
    }
    // okay I'll half-ass this for testing
    // will just be a mirror of the sequence AND WON'T WORK IF NOTES ARE
    // SMALLER THAN TATUM
    // later will: stretch notes for as long as they're playing uninterrupted
    // and fill in silence and collate together silences
    public Segment[] seqToSegs(int[] seq) {
	PitchChain pitches = new PitchChain();
	TempoApproximator time = new TempoApproximator(70);
	Segment[] toBeCropped = new Segment[seq.length];
	Articulation art = new Articulation(44100, Articulation.Env.LINEAR_FALLOFF);
	Wave w = new Wave(Wave.Form.SINE);
	int restCounter = 0; // for collating together adjacent rests into one seg
	int duration = time.getSamplesPerTatum();
	for (int i = 0; i < seq.length; i++) {
	    if (seq[i] != -1) {
		toBeCropped[i] = new Segment(new Note(art, pitches.get(seq[i]), w), duration);
	    }
	    else {
		toBeCropped[i] = new Segment(null, duration);
	    }
	}
	return toBeCropped;
    }
    public void testUsing(SourceDataLine out) {
	int[] seq = new int[] {69, 71, -1, -1, -1, 72, -1, 69, 71};
	//int[] seq = new int[] {69};
	Segment[] segs = seqToSegs(seq); 
	System.out.println("Segments " + segs.length);
	Pitch b = new Pitch();
	b.transpose(new Fraction("9/8"));
	//Note n = new Note(new Articulation(44100, Articulation.Env.LINEAR_FALLOFF), new Pitch(), new Wave(Wave.Form.SINE));
	//System.out.println(" " + n.chunkSize);
	//System.exit(1);
	this.output = out;
	output.start();
	try {
	    for (int i = 0; i < segs.length; i++) {
		System.out.println("Segment " + i + ", dur " + segs[i].duration);
		if (segs[i].n != null) {
		    streamAll(segs[i].n.playFor(segs[i].duration));
		    //short[] test = segs[i].n.playFor(segs[i].duration);
		    //OmniPlotWindow display = new OmniPlotWindow(segs[i].n.playShortBy(0), Short.MAX_VALUE, Short.MIN_VALUE, 20);
		}
		else {
		    streamZeros(segs[i].duration);
		}
		//System.out.println("Streamed");
	    }
	}
	catch (IOException ioe) {
	    System.out.println("Ran out of notes.");
	    //System.exit(1);
	}
	//try {Thread.sleep(1000); }catch (InterruptedException e){};
	
	
	
    }
    public void streamZeros(int duration) throws IOException {
	short[] silence = new short[duration];
	for (int i = 0; i < duration; i++) {
	    silence[i] = 0;
	}
	streamAll(silence);
    }
    public void streamAll(short[] audio) throws IOException { 
	//System.out.println("Streaming " + audio.length + ", test values "+ audio[40] + " " + audio[81] + " " + audio[159]);
	int counter = 0;
	do {
	    if ((output.available() * Calc.sampleRate) > BLOCK_SIZE_BYTES) { 
		int sizeInSamples = ((audio.length - counter) * Calc.bytesPerSample > BLOCK_SIZE_BYTES)? BLOCK_SIZE_BYTES / Calc.bytesPerSample : (audio.length - counter);
		ByteBuffer byteData = ByteBuffer.allocate(sizeInSamples * Calc.bytesPerSample);
		byteData.order(ByteOrder.LITTLE_ENDIAN);
		//System.out.println(sizeInSamples);
		while (sizeInSamples > 0) {
		    byteData.putShort(audio[counter++]);
		    //System.out.println(audio[counter]);
		    sizeInSamples--;
		    //System.out.println(sizeInSamples);
		}
		//System.out.println(counter);
		//System.out.println("BUffer size " + byteData.capacity());
		//System.out.println("Test byte" + byteData.array()[40]);

		/*for (int i = 0; i < byteData.array().length; i++) {
		    System.out.println(byteData.array()[i]);
		}*/
		// output whatever you got (it's not guaranteed to be BLOCK_SIZE_BYTES)
		output.write(byteData.array(), 0, byteData.array().length);
	    }
	    else {
		// Not needed now, just shuts things down when buffer fills
		//throw new IOException();
	    }
	} while (audio.length - counter > 0);
	//System.out.println("Done streaming.");
    }
    // okay a first try at this: a slice of sound or silence (null note)


}
