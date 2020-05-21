/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * Wave functions
 * I WANT TO USE BHASKARA THE FIRST'S TASTY APPROXIMATION IN HERE
 * @author Kevin Higgins
 */
public class Wave {
    final static int TERMS_AMOUNT = 7;
    Fraction threshold; // fraction of maximum amplitude at which wave is folded
    public enum Form {
	SINE, SQUARE, TRIANGLE, SAW
    }
    public final Form form;
    public Wave(Form form) {
	threshold = new Fraction(1, 1); // no folding can happen anyway
	this.form = form;
    }
    public Wave(Form form, Fraction threshold) {
	this.threshold = threshold; // no folding can happen anyway
	this.form = form;
    }
    public short functionFoldedAt(int sample, int size, int multiple, Fraction threshold) {
	this.threshold = threshold;
	return functionFolded(sample, size, multiple);
    }
    // assumes use of normalisedFunction has multiplied amplitude by the 
    // inverse of threshold and then folds at Short.MAX_VALUE,
    // to avoid loss of resolution incurred by a scale-up (final result
    // is normalised to Short.MAX_VALUE
    public short functionFolded(int sample, int size, int multiple) {
	int max = Short.MAX_VALUE;
	int toBeFolded = normalisedFunction(sample, size, multiple);
	while (Math.abs(toBeFolded) > max) {
	    int diff = Math.abs(toBeFolded) - max;
	    if (toBeFolded > 0) {
		toBeFolded = max - diff;
	    }
	    else {
		toBeFolded = (0 - max) + diff;
	    }
	}
	return (short) toBeFolded;
    }
    
    // multiplies amplitude by the inverse of the threshold fraction
    // so that final amplitude after folding will be normalised to Short.MAX_VALUE,
    // not (Short.MAX_VALUE * threshold)
    private int normalisedFunction(int sample, int size, int multiple) {
	//I do the divisions each time to take advantage of the greater
	// precision of int division thanks to scaling the sample position int by
	// Short.MAX_VALUE
	// still plenty of obvious caching to do
	int result;
	if (form == Form.SINE) {
	    result = (SineTableBhaskara.get(sample * Short.MAX_VALUE * multiple / size));
	    System.out.println("Wave val " + result + " at sample " + sample);
	}
	else if (form == Form.SAW) {
	    result = ((sample * Short.MAX_VALUE * 2 * multiple) / size);
	    System.out.println("Wave val " + result + " at sample " + sample);
	}
	else if (form == Form.SQUARE) {
	    result = ((sample * Short.MAX_VALUE * multiple * 2) / size) > (Short.MAX_VALUE / 2)? 1 : -1;
	    result = result * Short.MAX_VALUE;
	    System.out.println("Wave val " + result + " at sample " + sample);
	}
	else if (form == Form.TRIANGLE) {
	    // x = m - abs(i % (2*m) - m
	    int period = size / multiple;
	    int half = period / 2;
	    int quarter = half / 2;
	    sample = sample + quarter; // change phase to avoid clicks!
	    int unMultiplied = ((quarter - Math.abs((sample % period) - half)));
	    result = unMultiplied *(Short.MAX_VALUE / (quarter + 1)); // fix to avoid overflow from rounding error   
	}
	else {
	    result = (SineTableBhaskara.get(sample * Short.MAX_VALUE * multiple / size));
	    System.out.println("Wave val " + result + " at sample " + sample);
	}
	//return (int) result;
	return threshold.inverseBy((int) result);
    } 
    /*
    private double powerSeriesSine(double arg) {
	arg = (arg % (Math.PI * 2));
	boolean flip = false;
	if (arg > Math.PI) {
	    arg -= Math.PI;
	    flip = true;
	}
	int signOfTerm = 1;
	int n = 1;
	double result = 0;
	while (n <= TERMS_AMOUNT) {
	    result += signOfTerm * (Math.pow(arg,(2* n) - 1)) / factorial(2*n-1);
	    n++;
	    signOfTerm = signOfTerm * -1;
	}
	if (flip) result = 0 - result;
	return result;
    }
    private int factorial(int n) {
	if (n == 1) return 1;
	int result = n;
	n--;
	do  {
	    result = result * n;
	    n--;
	} while (n > 0);
	return result;
    }
    private double bhaskara(double arg) {
	double result;
	arg = (arg % (2 * Math.PI));
	if (arg < Math.PI) {
	   result = (16 * arg * (Math.PI - arg))/((5*Math.PI*Math.PI) - ((4*arg)*(Math.PI -arg)));
	}
	else {
	    arg = arg - Math.PI;
	    result = 0 - (16 * arg * (Math.PI - arg))/((5*Math.PI*Math.PI) - ((4*arg)*(Math.PI -arg)));
	}
	System.out.println("Bhaskara approximation of sine of " + arg + ": " + result + ", actual: " + Math.sin(arg));
	
	return result;
    }
    private short function(int sample, int size, int multiple) {
	//I do the divisions each time to take advantage of the greater
	// precision of int division thanks to scaling the sample position int by
	// Short.MAX_VALUE
	// still plenty of obvious caching to do
	double result;
	if (form == Form.SINE) {
	    double revsBySamples = 2.0 * Math.PI * multiple / size; // only calc when needed, obvs!
	    // BHASKARA THE FIRST
	    // (16x(pi - x))/5(pi^2) - rx(pi -x)
	    
	    result = (short) (Short.MAX_VALUE * bhaskara(revsBySamples * sample));
	    System.out.println("Wave val for sine " + result + " at sample " + sample);
	}
	else if (form == Form.SAW) {
	    result = (short) ((sample * Short.MAX_VALUE * 2 * multiple) / size);
	    System.out.println("Wave val for saw " + result + " at sample " + sample);
	}
	else if (form == Form.SQUARE) {
	    result = ((sample * Short.MAX_VALUE * multiple * 2) / size) > (Short.MAX_VALUE / 2)? 1 : -1;
	    result = result * Short.MAX_VALUE;
	    System.out.println("Wave val for square " + result + " at sample " + sample);
	}
	else if (form == Form.TRIANGLE) {
	    // x = m - abs(i % (2*m) - m
	    int period = size / multiple;
	    int half = period / 2;
	    int quarter = half / 2;
	    sample = sample + quarter; // change phase to avoid clicks!
	    int unMultiplied = ((quarter - Math.abs((sample % period) - half)));
	    result = unMultiplied *(Short.MAX_VALUE / (quarter + 1)); // fix to avoid overflow from rounding error   
	}
	else {
	    double revsBySamples = 2.0 * Math.PI * multiple / size; // only calc when needed, obvs!
	    result = (short) (Short.MAX_VALUE * Math.sin(revsBySamples));
	}
	return (short) (result);
    } 
*/

}
