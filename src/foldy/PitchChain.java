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
public class PitchChain {
    private Pitch[] chain = new Pitch[128];
    public PitchChain() { // TESTING ONLY - SHOULD BE USING RatioSeries!!------------------------
	Pitch a = new Pitch();
	Pitch aSharp = new Pitch();
	aSharp.transpose(new Fraction(15, 16));
	Pitch b = new Pitch();
	b.transpose(new Fraction(9, 8));
	Pitch c = new Pitch();
	c.transpose(new Fraction(6, 5));
	Pitch cSharp = new Pitch();
	cSharp.transpose(new Fraction(5, 4));
	for (int i = 0; i < chain.length; i++) {
	    chain[i] = a;
	}
	chain[70] = aSharp;
	chain[71] = b;
	chain[72] = c;
	chain[73] = cSharp;
    }
    public Pitch get(int index) {
	return chain[index];
    }
}
