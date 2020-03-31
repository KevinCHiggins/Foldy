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
public class EnvPoint {
    private double x;
    private double y;

    public double getX() {
	return x;
    }

    public void setX(double x) {
	this.x = x;
	if (x > 1) { x = 1; } else if (x < 0) { x = 0; }
    }

    public double getY() {
	return y;
    }

    public void setY(double y) {
	this.y = y;	
	if (x > 1) { x = 1; } else if (x < 0) { x = 0; }
    }

}
