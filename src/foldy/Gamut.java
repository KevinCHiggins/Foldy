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
public class Gamut {
    private Pitch[] chain = new Pitch[128];
    public Gamut() { 
	for (int i = 0; i < chain.length; i++) {
	    chain[i] = new Pitch(i);
	}
    }
    public static Gamut getHarmonicGamut(int generator) {
	Gamut g = new Gamut();
	if (generator < 128 && generator > 0) {
	    int counter = 1;
	    // subharmonics
	    for (int i = generator - 1; i > 0; i--) {
		counter++;
		g.chain[i] = new Pitch(generator);
		g.chain[i].transpose(new Fraction(1, counter));	
		System.out.println("Generated pitch " + g.chain[i].getFreq());
	    }
	    counter = 1;
	    // subharmonics
	    for (int i = generator + 1; i < 128; i++) {
		counter++;
		g.chain[i] = new Pitch(generator);
		g.chain[i].transpose(new Fraction(counter, 1));
		System.out.println("Generated pitch " + g.chain[i].getFreq());
	    }
	}
	return g;
    }
    public Pitch get(int index) {
	return chain[index];
    }
}
