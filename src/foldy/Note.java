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
    short[] chunks;
    Fraction actualWavelength; // will store the achieved wavelength after chunking/approximation
    
    // how many samples there are of any final, partial chunk... this way Notes have sample accurate duration
    int remainderInSamples; 
    
    public short[] playShortBy(int shortfall) {
	int durationDesired = getDuration() - shortfall;
	return playFor(durationDesired);
    }
    public short[] playFor(int duration) {
	System.out.println("Test value from Note " + chunks[0]);
	int fullChunksCount = duration / chunkSize;
	int remainderInSamples = duration % chunkSize;
	short[] played = new short[duration];
	int index = 0;

	for (int i = 0; i < fullChunksCount; i++) {
	    for(int j = 0; j < chunkSize; j++) {
		played[index] = chunks[j];

		index++;
	    }
	}
	for (int i = 0; i < remainderInSamples; i++) {
	    played[index] = chunks[i];
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
	
	chunks = new short[chunkSize];
    }

    private void makeChunk(Wave wave) { // not bothering to check waveform right now
	int max = Short.MAX_VALUE / 2;
	int min = Short.MIN_VALUE;
	double revsBySamples = 2.0 * Math.PI * chunkMultiple / chunkSize;
	for (int i = 0; i < chunkSize; i++) {
	    chunks[i] = (short)(max * Math.sin(revsBySamples * i));
	    
	}
    }
    
    private void setChunkLevels(Articulation art) {
	
    }
    public int getDuration() {
	return art.duration;
    }
}
