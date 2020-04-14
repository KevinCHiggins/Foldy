/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;
import java.lang.IndexOutOfBoundsException;
/**
 * Okay, so the ideal version of this would:
 *  - be boundable at either end (or not, i.e. possibly having negative keys)
 *  - be makeable as logarithmic octave divisions, harmonic series, and scales
 *  - have premade series and also ways to build new ones
 *  - hohoho... what about averaging or adding two of them?
 * @author Kevin Higgins
 */
public class FrequencySeries {
    // for the mo
    public static final int HARMONIC = 0;
    public static final int SUBHARMONIC = 1;
    private int typeOfSeries; // quick starting point
    private double base;
    public FrequencySeries (double baseTone, int standardType) {
	if (standardType == HARMONIC || standardType == SUBHARMONIC) this.typeOfSeries = standardType;
	else this.typeOfSeries = 0;
	this.base = baseTone;
    }
    public double f(int index) throws IndexOutOfBoundsException { // I don't know the proper term... index?
	if (index < 1) throw new IndexOutOfBoundsException();
	if (typeOfSeries == HARMONIC) {
	    return (base * (index));
	}
	else {
	    return (base / (index));
	}	
    }

}
