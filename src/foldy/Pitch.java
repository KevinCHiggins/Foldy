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
 * @author Kevin Higgins
 */
public class Pitch {
    
    final static int aEquals = 110;
    Fraction[] ratios = new Fraction[] {new Fraction(aEquals, 1)};
    public void transpose(Fraction ratio) {
	Fraction[] ratiosPlusTransposition = new Fraction[ratios.length + 1];
	for (int i = 0; i < ratios.length; i++) {
	    ratiosPlusTransposition[i] = ratios[i];
	}
	ratiosPlusTransposition[ratios.length] = ratio;
	ratios = ratiosPlusTransposition;
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
}
