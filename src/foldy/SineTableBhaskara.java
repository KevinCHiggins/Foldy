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
public class SineTableBhaskara {/*
    public static void main(String[] args) {
	//OmniPlotWindow plot = new OmniPlotWindow(sine, Short.MAX_VALUE, 0 - Short.MAX_VALUE, 40);
    }
    static short[] sine;
    static {
	sine = new short[Short.MAX_VALUE];
	int half = Short.MAX_VALUE / 2;
	int quarter = half / 2;
	int k = quarter * quarter * 5;
	int x = 0;
	short result;
	int top, bottom;
	while (x < half) {
	    top = 4 * x * (half - x);
	    bottom = (k - (x * (half - x)));
	    result = (short) (top / ((bottom / Short.MAX_VALUE) + 2)); // cheap overflow/rounding fix
	    sine[x] = result;
	    sine[x + half] = (short) (0 - result);
	    x++;
	}
    }
    public static short get(int offset) {
	offset = offset % Short.MAX_VALUE;
	return sine[offset];
    }
*/
}
