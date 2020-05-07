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
public class Note {
    int chunkSize; // many samples per chunk - always should be more than 220
    int chunkMultiple; // how many times the waveform will cycle in the chunk
    //int fullChunksCount; // how many full chunks are in the note
    Articulation art;
    short[] chunk;
    short[] chunkLevels; // envelope point for the START of every chunk including any partial final chunk
    Fraction actualWavelength; // will store the achieved wavelength after chunking/approximation
    
    // how many samples there are of any final, partial chunk... this way Notes have sample accurate duration
    int remainderInSamples; 
    
    public short[] playShortBy(int shortfall) {
	int durationDesired = getDuration() - shortfall;
	return playFor(durationDesired);
    }
    public short[] playFor(int duration) {
	System.out.println("Test value from Note " + chunk[0]);
	// chunks count GIVEN THIS DURATION!! Different from fullChunksCount
	int chunksCount = duration / chunkSize; 
	int remainderInSamples = duration % chunkSize;
	short[] played = new short[duration];
	int index = 0;
	System.out.println("Chunk size " + chunkSize + ", count " + chunksCount + ", env points " + chunkLevels.length);
	
	for (int i = 0; i < chunksCount; i++) {
	    for(int j = 0; j < chunkSize; j++) {
		played[index] = (short)((chunk[j] * chunkLevels[i]) / Short.MAX_VALUE);

		index++;
		//System.out.println("Index" + index);
	    }
	}
	for (int j = 0; j < remainderInSamples; j++) {
	    played[index] = (short)((chunk[j] * chunkLevels[chunkLevels.length - 1]) / Short.MAX_VALUE);
	}
	// a tiny linear fadeout to mitigate clicking
	int fadeLengthMillis = 20; // ms
	int fadeLengthSamples = Calc.secsToSamples((double) fadeLengthMillis / 1000);
	System.out.println(Calc.secsToSamples(0.2));
	if (played.length > (fadeLengthSamples * 2)) {
	    System.out.println("FADE " + fadeLengthSamples);
	    // fadeout using integer arithmetic
	    for (int i = 0; i < fadeLengthSamples; i++) {
		int multiplier = (Short.MAX_VALUE * i) / fadeLengthSamples;
		//played[i] = (short)(played[i] * multiplier / Short.MAX_VALUE);
		played[played.length - (i + 1)] = (short)(played[played.length - (i + 1)] * multiplier / Short.MAX_VALUE);
	    }
	}
	return played;
    }
    public Note(Articulation art, Pitch pitch, Wave wave) {
	sizeChunk(pitch);
	makeChunk(wave);
	
	setChunkLevels(art);
	this.art = art;
	//fullChunksCount = art.duration / chunkSize;
	//remainderInSamples = art.duration % chunkSize;
    }
    private void sizeChunk(Pitch pitch) {
	chunkSize = Calc.sampleRate * pitch.getDenominator() / pitch.getNumerator();
	chunkMultiple = 1;
	
	while (chunkSize < 220) {
	    chunkMultiple++;
	    chunkSize = (Calc.sampleRate * chunkMultiple * pitch.getDenominator() / pitch.getNumerator());
	}
	
	System.out.println("In note, mult " + chunkMultiple + ", size " + chunkSize);
	
	// wavelength might not be an int if there are multiple cycles per chunk!
	actualWavelength = new Fraction(chunkSize, chunkMultiple);
	
	chunk = new short[chunkSize];
	
    }

    private void makeChunk(Wave wave) { // not bothering to check waveform right now
	int max = Short.MAX_VALUE / 2;
	int min = Short.MIN_VALUE;
	double revsBySamples = 2.0 * Math.PI * chunkMultiple / chunkSize;
	for (int i = 0; i < chunkSize; i++) {
	    chunk[i] = (short)(max * Math.sin(revsBySamples * i));
	    
	}
    }
    
    private void setChunkLevels(Articulation art) {
	
	int pointsCount = art.duration / chunkSize; 
	System.out.println("Setting chunk level points. Duration " + art.duration + " chunk size " + chunkSize);
	if (remainderInSamples > 0) pointsCount += 1; // add a volume point for start of remainder
	chunkLevels = new short[pointsCount];
	// gonna use Short.MAX_VALUE as my unit for proportion of note length
	// to keep things int
	// this variable will hold how many samples are still to come
	// counting from the start of the current chunk
	int samplesAhead;
	for (int i = 0; i < pointsCount; i++) {
	    samplesAhead = (art.duration - (i * chunkSize));
	    chunkLevels[i] = (short)((samplesAhead * Short.MAX_VALUE) / art.duration);
	}
	
    }
    public int getDuration() {
	return art.duration;
    }
}
