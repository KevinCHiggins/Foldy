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
	Pitch d = new Pitch();
	d.transpose(new Fraction(4, 3));
	Pitch dSharp = new Pitch();
	dSharp.transpose(new Fraction(45, 32));
	Pitch e = new Pitch();
	e.transpose(new Fraction(3, 2));
	Pitch lowE = new Pitch();
	lowE.transpose(new Fraction(3, 4));
	Pitch lowG = new Pitch();
	lowG.transpose(new Fraction(8, 9));
	for (int i = 0; i < chain.length; i++) {
	    chain[i] = a;
	}
	chain[64] = lowE;
	chain[67] = lowG;
	chain[70] = aSharp;
	chain[71] = b;
	chain[72] = c;
	chain[73] = cSharp;
	chain[74] = d;
	chain[75] = dSharp;
	chain[76] = e;
    }
    public Pitch get(int index) {
	return chain[index];
    }
}
