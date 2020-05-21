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
public class RootFinder {
    public static void main(String[] args) {
	//System.out.println(getNthRootOfTwo(6));
	getNthRootOfTwo(2);
	//power(4, 3);
	//sout("3 ^ 12 " i)
    }
    static public Fraction getNthRootOfTwo(int nth) {
	Fraction guess = new Fraction(7, 4);
	System.out.println("Testing if " + guess.numerator + "/" + guess.denominator + " is bigger than the " + nth + "th root of two:");
	int n = guess.numerator;
	int d = guess.denominator;
	int max = Short.MAX_VALUE;
	int bigger = ((max / 2) * power(n, nth))/power(d, nth);
	System.out.println(bigger);
	return guess;
    }
    static int power(int base, int exp) {
	System.out.print("Calculating... " + base + " ^ " + exp);
	int result = 1;
	while (exp > 0) {
	    result = result * base;
	    exp--;
	}
	System.out.println(" = " + result);
	return result;
    }

}
