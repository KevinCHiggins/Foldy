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
	Gamut pitches = new Gamut();
	TempoApproximator time = new TempoApproximator(66, 3);
	Segment[] segs = new Segment[seq.length * 2]; // worst-case is every note falls short of next tatum
	Articulation art = new Articulation(20100, Articulation.Env.LINEAR_FALLOFF);
	Wave w = new Wave(Wave.Form.SINE, new Fraction(1, 3));
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
	    System.out.println("Filling timespan of " + span + " samples.");
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
		    System.out.println("Segment " + segCounter + " has span shorter than note duration of " + art.duration);
		    segs[segCounter++] = new Segment(new Note(art, pitches.get(seq[tatumCounter]), w), span);
		}
		else {
		    System.out.println("Segment " + segCounter + " has span " + span + " longer than note duration of " + art.duration);
		    segs[segCounter++] = new Segment(new Note(art, pitches.get(seq[tatumCounter]), w), art.duration);
		    System.out.println("Adding segment of silence length " + (span - art.duration));
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
	int[] seq = new int[] {31, -1, -1, -1, 35, 38, 41, -1, -1, 43, -1, 41};
	//int[] seq = new int[] {69};
	Segment[] segs = seqToSegs(seq); 
	System.out.println("Segments " + segs.length);
	int fullLength = 0;
	for (int i = 0; i < segs.length; i++) {
	    System.out.println("Seg " + i + " dur " + segs[i].duration);
	    fullLength += segs[i].duration;
	}
	System.out.println(fullLength + " shorts is " + (fullLength * Calc.bytesPerSample) + " bytes.");
	// lash it all into a buffer
	ByteBuffer full = ByteBuffer.allocate(fullLength * Calc.bytesPerSample);
	full.order(ByteOrder.LITTLE_ENDIAN);
	for (int i = 0; i < segs.length; i++) {
	    if (segs[i].n != null) {
		//System.out.println("Buffering seg " + i + " dur " + segs[i].duration);
		short[] audio = segs[i].n.playFor(segs[i].duration);
		int counter = 0;
		for (; counter < segs[i].duration; counter++) {
		    //System.out.println("Putting short " + counter + " from seg " + i + " at position " + full.position());
		    full.putShort(audio[counter]);
		}
		System.out.println(" --- Put " + counter + " shorts, ByteBuffer capacity left " + full.remaining());
	    }
	    else {
		short[] audio = new short[segs[i].duration];
		int counter = 0;
		for (; counter < segs[i].duration; counter++) {
		    System.out.println("Putting silent short " + counter + " from seg " + i);
		    full.putShort((short) 0);
		    //full.putShort((short) 0);
		    counter++;
		}
		System.out.println(" --- Put " + counter + " shorts of silence, ByteBuffer capacity left " + full.remaining());
	    
	    }
	}
	
	byte[] bytes = full.array();
	RawAudioWarehouser raw = new RawAudioWarehouser();
	raw.save(bytes);
	int counter = bytes.length;
	out.start();
	do {
	    //System.out.println(counter);
	    if (out.available() > BLOCK_SIZE_BYTES) {
		if (counter > BLOCK_SIZE_BYTES) { // typical case
		    //System.out.println("Out" + counter);
		    out.write(bytes, bytes.length - counter, BLOCK_SIZE_BYTES);
		    counter -= BLOCK_SIZE_BYTES;
		    
		}
		else {
		    System.out.println("End");
		    out.write(bytes, bytes.length-counter, counter);
		    counter = 0;
		    //break;
		}
	    }

	} while (counter > 0);
	
	
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
