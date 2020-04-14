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
public class DiffAnalyser {
    int startX;
    int startY;
    int endX;
    int endY;
    int dY;
    int dX;
    double slope;
    double currentY;
    int currentX;
    // Will make a (functionally equivalent) constructor with EnvPoints, later
    // once I've fixed their types
    
    public DiffAnalyser(int startX, int startY, int endX, int endY) {
	this.startX = startX;
	this.startY = startY;
	this.endX = endX;
	this.endY = endY;
	dY = endY - startY;
	dX = endX - startX;
	slope = (double) dY / dX;
	System.out.println("DiffAnal w/ slope of " + slope);
	currentY = this.startY;
	currentX = this.startX;	
    }
    public int getYAndAdvance() {
	int returnY = (int) Math.round(currentY); // or should return type be long? THink I'd cast anyway later
	currentY = currentY + (slope);
	currentX++;
	System.out.println("CUrrent Y from DiffANalyser" + returnY + " X " + currentX);
	return returnY;
    }
    public boolean hasNext() {
	System.out.println("EndX " + endX);
	return (currentX < endX);
    }

}
