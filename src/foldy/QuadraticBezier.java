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

public class QuadraticBezier {
    //private EnvPoint[] quad;
    int length;
    int maxAmplitude;
    double x0;
    double y0;
    double x1;
    double y1;
    double x2;
    double y2;	
    double x;
    double y;
    double t;
    public QuadraticBezier(int length, int maxAmplitude, double x0, double y0, double x1, double y1, double x2, double y2) {

	this.length = length;
	this.maxAmplitude = maxAmplitude;
	this.x0 = x0;
	this.y0 = y0;
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;	

    }
    public EnvPoint sample(double t) {	
	double x = ((1 - t) * (1 - t) * x0) + ((2 * t) * (1 - t) * x1) + (t * t * x2);
	double y = ((1 - t) * (1 - t) * y0) + ((2 * t) * (1 - t) * y1) + (t * t * y2);
	return new EnvPoint((int) x, (int) y * maxAmplitude);
    }
}