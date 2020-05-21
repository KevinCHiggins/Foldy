/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * Pitch expressed as a fraction, in hertz (multiply all ratios together to get pitch)
 * Needs simplification but I'm unsure currently whether that should remove
 * info about all the transpositions it's had. Prob yes - or cache the result
 * Actually I'm thinking not until necessary. And I'll store a reference note
 * in here (which is the **generator** of the Pitch, generally, if it uses
 * harmonic or G.R. ratios - not BTW an overall A = 440).
 * @author Kevin Higgins
 */
public class Pitch {
    public static Fraction[] REF = new Fraction[] {
	new Fraction(26163, 100), 
	new Fraction(27718, 100),
	new Fraction(29366, 100), 
	new Fraction(31113, 100),
	new Fraction(32963, 100),
	new Fraction(34923, 100),
	new Fraction(36999, 100),
	new Fraction(392, 1),
	new Fraction(4153, 10),
	new Fraction(440, 1),
	new Fraction(46616, 100),
	new Fraction(49388, 100)};
    private Fraction refHz; // fraction representing reference tone in hz
    private int refMidiNote; // which MIDI note it corresponds to (not enforced but should be close)
    private Fraction[] ratios;
    public Pitch() {
	Fraction refHz = new Fraction(110, 1);
	refMidiNote = 45; // A two octaves down
	ratios = new Fraction[] {refHz};
    }
    public Pitch(int midiNote) { // this constructor merely returns the ET tuned MIDI note
	refMidiNote = midiNote;
	int top = 1;
	int bottom = 1;
	int octaveNormalised = midiNote; // a marker to be shifted until it's in the referenc octave
	while (octaveNormalised < 60) { // shift up an octave
	    
	    bottom = bottom * 2;
	    octaveNormalised += 12;
	}
	while (octaveNormalised > 71) { // shift down an octave
	    top = top * 2;
	    octaveNormalised -= 12;
	}
	refHz = REF[octaveNormalised - 60]; // use the normalised note as index, now normalised to 0
	Fraction octaveTransposition = new Fraction(top, bottom);
	ratios = new Fraction[] {refHz};
	if (octaveTransposition.numerator - octaveTransposition.denominator != 0) this.transpose(octaveTransposition);
    }
    
    public void transpose(Fraction ratio) {
	Fraction[] ratiosPlusTransposition = new Fraction[ratios.length + 1];
	for (int i = 0; i < ratios.length; i++) {
	    ratiosPlusTransposition[i] = ratios[i];
	}
	ratiosPlusTransposition[ratios.length] = ratio;
	ratios = ratiosPlusTransposition;
	System.out.println("Transposed by " + ratio.toString());
    }
    public int getNumerator() {
	int result = 1;
	for (Fraction f : ratios) {
	    result = result * f.numerator;
	}
	return result;
    }
    public int getDenominator() {
	int result = 1;
	for (Fraction f : ratios) {
	    result = result * f.denominator;
	}
	return result;
    }
    public int getFreq() {
	return getNumerator() / getDenominator();
    }
    public String toString() {
	return (getNumerator() + "/" + getDenominator());
    }
}
