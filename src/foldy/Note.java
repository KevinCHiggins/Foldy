/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * Alas, to make my Chunks idea work, I treat both the second-last and the last
 * (whole or partial) Chunk of the note separately to the rest. It's rough! It
 * does however now work reliably inside the 100-44100 samples range of note lengths.
 * @author Kevin Higgins
 */
public class Note {
    
    int chunkMultiple; // how many times the waveform will cycle in the chunk
    Chunk chunk;
    Articulation art;
    short[] chunkLevels; // envelope point for the START of every chunk including any partial final chunk
    
    public short[] playShortBy(int shortfall) {
	int durationDesired = getDuration() - shortfall;
	return playFor(durationDesired);
    }
    public short[] playFor(int duration) {
	
	// chunks count GIVEN THIS DURATION!! Typically less than full chunks count
	int chunksCount = duration / chunk.getSize(); 
	int remainderInSamples = duration % chunk.getSize();

	short[] played = new short[duration];
	int index = 0;
	int chunkSize = chunk.getSize();
	System.out.println("Chunk size " + chunkSize + ", count " + chunksCount + ", env points " + chunkLevels.length);
	int diff;
	int level;
	// only go the second last chunk, as the last one may also be at the end of
	// the array so needs a check in its difference calculation, so I 
	// do it separately afterwards
	for (int i = 0; i < chunksCount - 1; i++) {
	    for(int j = 0; j < chunkSize; j++) {
		
		diff = chunkLevels[i + 1] - chunkLevels[i]; // overall difference between points
		level = chunkLevels[i] + ((j * diff ) / chunkSize); // difference analysis

		played[index] = (short)((chunk.getSmp(j) * level) / Short.MAX_VALUE);
		
		//System.out.println("level " + chunkLevels[i] + "chunk " + i + " offset " + j + " data inside note: " + played[index]);
		//System.out.println("Index" + index);
		index++;
	    }
	}
	// deal with the two possibilities: either this chunk is the very last
	// in which case fade to zero, or it's not and difference analysis
	// is performed between its level and the next chunk's level
	int levelAfterEnd;
	// if this chunk is the last with no remainder...
	if (chunksCount % art.duration/chunk.getSize() == 0 && chunksCount == art.duration/chunk.getSize()) levelAfterEnd = 0;
	else levelAfterEnd = chunkLevels[chunksCount + 1];
	if (chunksCount > 0) {
	    for(int j = 0; j < chunkSize; j++) {

		diff = levelAfterEnd - chunkLevels[chunksCount - 1]; // overall difference between points
		level = chunkLevels[chunksCount - 1] + ((j * diff ) / chunkSize); // difference analysis

		played[index] = (short)((chunk.getSmp(j) * level) / Short.MAX_VALUE);

		//System.out.println("level " + chunkLevels[i] + "chunk " + i + " offset " + j + " data inside note: " + played[index]);
		//System.out.println("Index" + index);
		index++;
	    }
	}
	int endLength = chunkSize; // the length of the last chunk or partial chunk being played
	// adjust if it's the actual leftover chunk of the Note
	if (chunksCount == art.duration/chunkSize) endLength = (art.duration % chunkSize);
	for (int j = 0; j < remainderInSamples; j++) {
	    diff = 0 - chunkLevels[chunkLevels.length - 1];
	    level = chunkLevels[chunkLevels.length - 1] + ((j * diff) / endLength);
	    played[index] = (short)((chunk.getSmp(j) * level) / Short.MAX_VALUE);
	    System.out.println("Lev at " + (chunkLevels.length - 1) + ": " + chunkLevels[chunkLevels.length - 1] + ", Remainder size " + remainderInSamples+ " data at " + j + " : " + played[index]);
	    index++;
	}
	// a tiny linear fadeout to mitigate clicking
	int fadeLengthMillis = 20; // ms
	int fadeLengthSamples = Calc.secsToSamples((double) fadeLengthMillis) / 1000;
	System.out.println(Calc.secsToSamples(0.2));
	if (played.length > (fadeLengthSamples * 2)) {
	    System.out.println("FADE " + fadeLengthSamples);
	    // fadeout using integer arithmetic
	    for (int i = 0; i < fadeLengthSamples; i++) {
		int multiplier = (Short.MAX_VALUE * i) / fadeLengthSamples;
		//played[i] = (short)(played[i] * multiplier / Short.MAX_VALUE);
		//played[played.length - (i + 1)] = (short)(played[played.length - (i + 1)] * multiplier / Short.MAX_VALUE);
	    }
	}
	else System.out.println("TOO SHORT");
	return played;
    }
    public Note(Articulation art, Pitch pitch, Wave wave) {
	chunk = new Chunk(pitch, wave);
	
	setChunkLevels(art);
	this.art = art;
    }

    
    private void setChunkLevels(Articulation art) {
	
	int pointsCount = art.duration / chunk.getSize(); 
	int fullChunks = pointsCount;
	System.out.println("Setting chunk level points. Duration " + art.duration + " chunk size " + chunk.getSize());
	if (art.duration % chunk.getSize() >  0) pointsCount += 1; // add a volume point for start of remainder
	chunkLevels = new short[pointsCount];
	System.out.println(pointsCount + " env points.");
	System.out.println("fullchunks " + fullChunks + " max * size " + Short.MAX_VALUE * chunk.getSize() + " duration " + art.duration);
	for (int i = 0; i < pointsCount; i++) {
	    chunkLevels[i] = (short) (((long)(pointsCount - i) * Short.MAX_VALUE * chunk.getSize()) / art.duration);
	    System.out.println("chunklevel " + chunkLevels[i]);
	}
	// due to rounding, the first value is often overflowed so reset it
	chunkLevels[0] = Short.MAX_VALUE;
	
    }
    public int getDuration() {
	return art.duration;
    }
}
