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
public class SineTableTaylor {
    public static void main(String[] args) {
	//OmniPlotWindow plot = new OmniPlotWindow(sine, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 40);
    }
    static short[] sine;
    static {
	sine = new short[Short.MAX_VALUE];
	int half = Short.MAX_VALUE / 2;
	final int p = 15;
	final int r = 11;
	final int s =	14;
	int quarter = half / 2;
	int x = quarter;
	short result;
	// I go backwards to avoid having to check for an edge case with the 
	// off-by-one line which otherwise overwrites the last value at z = 1
	// with 0. This way the wrong value gets overwritten and no need for an if.
	while (x > 0) { 
	    // lifted with gratitude from https://www.coranac.com/2009/07/sines/
	    result = (short) (x * ( (3<<p) - (x*x>>r) ) >> s);
	    
	    // instead of his cool reflection technique using a truth table, I keep it newb
	    sine[Short.MAX_VALUE - x - 1] = (short) (0 - result);
	    sine[x] = result;
	    sine[x + half] = (short) (0 - result);
	    sine[half - x] = (result);
	    x--;
	}
	//OmniPlotWindow om = new OmniPlotWindow(sine, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 5);
    }
    public static short get(int offset) {
	offset = offset % Short.MAX_VALUE;
	return sine[offset];
    }
}
