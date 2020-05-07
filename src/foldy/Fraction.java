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
public class Fraction {
    int numerator;
    int denominator;

    public Fraction(int numerator, int denominator) {
	this.numerator = numerator;
	this.denominator = denominator;
    }
    // this only parses fractions of the form "36/39" or "12" (improper fraction)
    public Fraction(String s) {
	char[] ar = s.toCharArray();
	boolean isFractional = false;
	for (int i = 0; i < s.length(); i++) {
	    if (ar[i] == '/') {
		this.numerator = Integer.parseInt(s.substring(0, i));
		this.denominator = Integer.parseInt(s.substring(i + 1, s.length()));
		isFractional = true;
		break;
	    }
	}
	if (!isFractional) {
	    this.numerator = Integer.parseInt(s);
	    this.denominator = 1;
	}
    }
}
