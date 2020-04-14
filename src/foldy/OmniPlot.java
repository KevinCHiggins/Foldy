package foldy;
/**
An app using code from John Russell, to test AWT Buttons and Labels. Dependent on my TestFrame class.
@author Kevin Higgins
10/11/19
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
public class OmniPlot extends JPanel {
    Point pixel = new Point();
    Point startOfPlot = new Point();
    Point startOfSquare = new Point();
    Point result = new Point();
    int[] sequence;
    private double side = 0;
    private double x;
    private double y;
    static final int PLOT_HEIGHT = 200;
    private int plotWidth;
    private int xStep;
    private int yStep;
    private double a = 100;
    private double b = 100;
    public OmniPlot (int[] sequence, int upperBound, int lowerBound, int xStep) {
	// width is determined by caller
	this.sequence = sequence;
	plotWidth = sequence.length / xStep; 
	this.xStep = xStep;
	yStep = (upperBound - lowerBound) / PLOT_HEIGHT; // height is fixed in this class
	setBorder(BorderFactory.createLineBorder(Color.black));
	//setSize(200, 200);
	//setBackground(Color.black);
    }
    public Dimension getPreferredSize() {
        return new Dimension(plotWidth, PLOT_HEIGHT);
    }
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(Color.black);
	    g.fillRect(0,0,plotWidth, PLOT_HEIGHT);
	    g.setColor(Color.white);
	    int yCentre = PLOT_HEIGHT / 2;
	    int oldX = 0;
	    int oldY = yCentre;
	    for (int x = 0; x < plotWidth; x++) {
		int y = yCentre + sequence[x] / yStep;
		g.drawLine(x, y, oldX, oldY);
		oldX = x;
		oldY = y;
	    }
	   
	}
	public void plot (Graphics g, Point p) {
		g.drawLine(p.x, p.y, p.x, p.y);
	}
}
