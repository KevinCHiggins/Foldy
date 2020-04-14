/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.util.Arrays;

/**
 *
 * @author Kevin Higgins
 */
// once I start having various shapes, I'll implement built-in sorting 
// and checking of start and end points
// for the mo I will leave the end point one sample beyond the end of the 'lope
public class Envelope {
    DiffAnalyser currAnalyser;
    int currPoint;

    int getNextY() {
	if (!currAnalyser.hasNext()) {
	    System.out.println("new analyser");
	    currPoint++; // advance the current envelope point
	    affixDiffAnalyser(); // setup analyser with new envelope segment
	}
	return currAnalyser.getYAndAdvance();
    }
    public static final int MIN_SEG_SIZE = 80;
    public static final int FALLOFF = 0;
    public static final int DOUBLE_DIP = 1; // FIXME - implement exp!!
    private EnvPoint[] points;
    public static Envelope getQuadratic(int length, int maxAmplitude, boolean positive) {
	int divs = 30;
	
	System.out.println("Length at Envelope " + length + ", divs " + divs);
	// QUICKIE allows for switch between positive vals only and including neg
	double lowY = 0;
	if (!positive) lowY = -1;
	//**********
	QuadraticBezier bezier = new QuadraticBezier(length, maxAmplitude, 0, 1, 0, lowY, 1, lowY);
	EnvPoint[] quad = new EnvPoint[divs + 1];
	double t = 0;
	double tStep = (double)length / divs;
	for (int i = 0; i < divs; i++) {
	    quad[i] = bezier.sample(t);
	    
	    System.out.println("sampled at " + t + ": " + quad[i].getX() + " " + quad[i].getY() + positive);
	    t += tStep;
	}
	quad[divs] = new EnvPoint(length, (positive) ? 0 : (0 - maxAmplitude));
	return new Envelope(quad);
    }
    // positive is the normal case, it's false when we want the envelope to stretch down to negative
    // vals 
    public static Envelope getLinear(int shape, int length, int maxAmplitude, boolean positive) {
	switch (shape) {
	    case FALLOFF:
		EnvPoint start = new EnvPoint(0, maxAmplitude);
		EnvPoint end = new EnvPoint(Calc.secsToSamples(length), (positive) ? 0 : (0 - maxAmplitude));
		EnvPoint[] linear = new EnvPoint[] { start, end };
		return new Envelope(linear);		
	    case DOUBLE_DIP: // for testing!		
		System.out.println("double");
		EnvPoint tStart = new EnvPoint(0, maxAmplitude); // I don't like these names due to switch
		EnvPoint tMiddle = new EnvPoint((length / 2), (positive) ? 0 : (0 - maxAmplitude));
		EnvPoint tEnd = new EnvPoint(length, maxAmplitude);		
		EnvPoint[] test = new EnvPoint[] { tStart, tMiddle, tEnd };
		return new Envelope(test);
	    default:
		return getLinear(FALLOFF, length, maxAmplitude, true);
	}	
    }
    private Envelope (EnvPoint[] points) {
	this.points = points;
	sortAndCheck();
	currPoint = 0; // Envelopes, like notes, are played through, but in EnvPoints, not samples
	// the DiffAnalyser handles the offsets per-sample - here we affix it to the current segment
	affixDiffAnalyser(); 
	}
    // set up the analyser to analyse the current segment (between this EnvPoint and the next)
    private void affixDiffAnalyser() {
	System.out.println("About to create diffanalyser. currPoint.X (" + currPoint + ") " + points[currPoint].getX() + ", +! " + points[currPoint + 1].getX());
	currAnalyser = new DiffAnalyser(points[currPoint].getX(), points[currPoint].getY(), points[currPoint + 1].getX(), points[currPoint + 1].getY());
    }
    private void sortAndCheck() { 
	
	//System.out.println("zero" + points[0].getX());
	Arrays.sort(points);
	//System.out.println("zero" + points[0].getX());
	for (int i = 0; i < points.length; i++) {
	    if (points[i].getX() < 0) { // negative x coordinates are illegal
		System.out.println("neg");
		System.exit(1); // implement exceptions!
	    }
	    // simple check for duplicates will work due to sort just completed
	    if (i < points.length - 1) { // make sure not to compare with a non-existent point
		if (points[i].getX()== points[i + 1].getX()) {
		    System.out.println("dup");
		    Exception e = new Exception();
		    e.printStackTrace();
		    System.out.println("1: " + points[i].getX() + " 2: " + points[i + 1].getX());
		    System.exit(1); // implement exceptions
		}
	    }
	}
	if (points[0].getX() != 0) { // finally enforce location of first point
	    
	    System.out.println("zer0000o: " + points[0].getX());
	    System.exit(1); // exceptions
	}
    }
    
}
