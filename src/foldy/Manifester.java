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
    final boolean LOOPED = false;
    SourceDataLine output;
    private class Segment {
	Note n;
	int duration;
	public Segment(Note n, int duration) {
	    this.n = n;
	    this.duration = duration;
	}
    }
    // turns a sequence of note attacks and rests into Segments consisting of
    // either a Note or a null signifying silence) and the duration it should be played
    public Segment[] seqToSegs(int[] seq) {
	PitchChain pitches = new PitchChain();
	TempoApproximator time = new TempoApproximator(120);
	Segment[] segs = new Segment[seq.length * 2]; // worst-case is every note falls short of next tatum
	Articulation art = new Articulation(44100, Articulation.Env.LINEAR_FALLOFF);
	Wave w = new Wave(Wave.Form.SINE);
	int tatumCounter = 0; 
	int segCounter = 0;
	int tatumLength = time.getSamplesPerTatum();
	while (tatumCounter < seq.length) {
	    // find next note
	    int searchForNext = tatumCounter;
	    do  {
		searchForNext++;
		if (searchForNext == seq.length) break;
	    } while (seq[searchForNext] == -1);
	    // calculate the span of time until that next note
	    int span = (searchForNext - tatumCounter) * tatumLength;
	    System.out.println("Span "  + span);
	    // if tatumCounter is 0 meaning we're at the sequence start,
	    // and it starts with a rest
	    if (tatumCounter == 0 && (seq[0] == -1)) {
		// lay down a rest spanning the time till the next note
		segs[segCounter++] = new Segment(null, span);
		
	    } // special case, there are no more notes left and we want the last note to ring out
	    else if (!LOOPED & searchForNext == seq.length) {
		System.out.println("Trying to ring out");
		segs[segCounter++] = new Segment(new Note(art, pitches.get(seq[tatumCounter]), w), art.duration);
	    }
	    else { // if not, we're in the typical case where we want to lay
		// down the note attacked at position tatumCounter;
		// either fill the span with its duration's worth of current note
		// or fill it with all of the current note and a silence
		if (art.duration >= span) {
		segs[segCounter++] = new Segment(new Note(art, pitches.get(seq[tatumCounter]), w), span);
		}
		else {
		    segs[segCounter++] = new Segment(new Note(art, pitches.get(seq[tatumCounter]), w), art.duration);
		    segs[segCounter++] = new Segment(null, span - art.duration); // silence
		}
	    }

	    tatumCounter = searchForNext;
	}
	Segment[] finalSegs = new Segment[segCounter];
	for (int i = 0; i < segCounter; i++) {
	    finalSegs[i] = segs[i];
	}
	return finalSegs;
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
		output.write(byteData.array(), 0, byteData.array().length);
	    }
	    else {
		// Not needed now, just shuts things down when buffer fills
		//throw new IOException();
	    }
	} while (audio.length - counter > 0);
	//System.out.println("Done streaming.");
    }

}
