/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Kevin Higgins
 */
public class SequenceControl extends JTextArea {
    
    public SequenceControl(String s) {
	super(s, 4, 20);
	this.setLineWrap(true);
	this.setWrapStyleWord(true);
	//this.setPreferredSize(new Dimension(20, 20));
	System.out.println("Seq length" + s.length());
	//this.setColumns(10);
    }
    public int[] getSeq() {
	LinkedList<Integer> l = new LinkedList<Integer>();
	Scanner delimitInts = new Scanner(this.getText()).useDelimiter(",\\s*");
	while(delimitInts.hasNextInt()) l.add(delimitInts.nextInt());
	int[] seqArray = new int[l.size()];
	for (int i = 0; i < l.size(); i++) {
	    seqArray[i] = l.get(i);
	}
	return seqArray;
    }


}
