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
import java.awt.*;
import javax.swing.*;
public class OmniPlotWindow extends JFrame {
    public OmniPlotWindow (int[] sequence, int upperBound, int lowerBound, int xStep) {
	OmniPlot plot = new OmniPlot(sequence, upperBound, lowerBound, xStep);
	this.add(plot);
	this.setBounds(100,100,800,800);
	this.setVisible(true);		
	UIManager.put("swing.boldMetal", Boolean.FALSE);
    }
}
