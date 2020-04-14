/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

/**
 * A convenience class for a single point on an amplitude curve or
 * envelope (not clear yet which it's gonna be, or when the transition
 * from curve to interpolated envelope will take place). Values go
 * from 0 to 1 in X and Y.
 * @author Kevin Higgins
 */
public class EnvPoint implements Comparable<EnvPoint> {
    private int x; //representing how far this point is along the note, in samples
    private int y; // representing amplitude in **15** bits - POSITIVE VAL ONLY
    public EnvPoint(int x, int y) {
	setX(x);
	setY(y);
	
	//System.out.println("EnvPoint w/ X " + getX() + " and Y " + getY());
    }
    public EnvPoint(double timeInSecs, int y) { // x pos can be in seconds
	this(Calc.secsToSamples(timeInSecs), y);

	System.out.println("double to int");	
    }
    public int getX() {
	return x;
    }

    private void setX(int x) {
	this.x = x;
	if (x < 0) { x = 0; }
    }

    public int getY() {
	return y;
    }

    private void setY(int y) {
	this.y = y;	
	if (y < 0) { y = 0; } // positive values only
	if (y > Short.MAX_VALUE) { y = Short.MAX_VALUE; } // using it as ceiling for amplitude values
    }

    public int compareTo(EnvPoint ep) {
	if (ep.x > this.x) {
	    return -1;
	}
	else if (ep.x == this.x) {
	    return 0;
	}
	else {
	    return 0;
	}
    }

}
