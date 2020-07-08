/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Kevin Higgins
 */

// re: the sequenceControl, I don't think this class needs to listen for changes!
// if it does, I'll implement Document Listener here
public class Control extends JFrame implements WindowListener, ChangeListener, ActionListener, FocusListener {
    JButton play = new JButton("Play");
    JRadioButton save = new JRadioButton();
    JLabel saveLabel = new JLabel("Save to file");
    JSpinner tempo = new JSpinner(new SpinnerNumberModel(100, 20, 400, 1));
    JLabel tempoLabel = new JLabel("Tempo");
    JSpinner subdivision = new JSpinner(new SpinnerNumberModel(4, 1, 12, 1));
    JLabel subLabel = new JLabel("Subdivision");
    JLabel seqLabel = new JLabel("Sequence");
    SequenceControl sequence = new SequenceControl("69, 67, 64, 69, 67, 64, 67, 0, 64, 68, 0, 69");
    JPanel seqPanel = new JPanel();
    JComboBox envelope = new JComboBox(new DefaultComboBoxModel(Articulation.Env.values()));
    JLabel envLabel = new JLabel("Envelope");
    JSpinner length = new JSpinner(new SpinnerNumberModel(44100, 100, 60000, 100));
    JLabel lengthLabel = new JLabel("Length");
    JComboBox wave = new JComboBox(new DefaultComboBoxModel(Wave.Form.values()));
    JLabel waveLabel = new JLabel("Waveform");
    JSpinner foldingNumerator = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
    JLabel numLabel = new JLabel("Folding numerator");
    JSpinner foldingDenominator = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));

    JLabel denomLabel = new JLabel("Folding denominator");
    // not sure how these'll be updated, yet
    OmniPlot envPlot = new OmniPlot(new short[] { }, (0 - Short.MAX_VALUE), Short.MAX_VALUE, 1);
    OmniPlot wavePlot = new OmniPlot(new short[] { }, (0 - Short.MAX_VALUE), Short.MAX_VALUE, 1);
    SourceDataLine outputJack;
    public Control(SourceDataLine outputJack) {
	System.out.println("Insets on sequence JTextArea: " + sequence.getInsets());
	seqPanel.add(sequence);
	seqPanel.setBorder(BorderFactory.createEtchedBorder());
	this.setTitle("Foldy");
	this.outputJack = outputJack;
	this.getContentPane().setLayout(new GridBagLayout());
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	screen.setSize(screen.getWidth() / 2, screen.getHeight() / 2); // responsive design!
	this.setSize(screen);
	initComponents();
    }
    // convenience method to instantiate and setup the basics of a GridBagConstraints
    private GridBagConstraints setupC(int gridx, int gridy, int gridwidth, int gridheight) {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = gridx;
	gbc.gridy = gridy;
	gbc.gridwidth = gridwidth;
	gbc.gridheight = gridheight;
	gbc.insets = new Insets(10, 5, 10, 5);
	return gbc;
    }
    private GridBagConstraints setupCTop(int gridx, int gridy, int gridwidth, int gridheight) {
	GridBagConstraints gbc = setupC(gridx, gridy, gridwidth, gridheight);
	gbc.anchor = GridBagConstraints.PAGE_START;
	return gbc;
    }
    private void initComponents() {
	
	//GridBagConstraints c = new GridBagConstraints(); // I'll reuse this variable like Netbeans does
	this.addWindowListener(this);
	envelope.addActionListener(this);
	wave.addActionListener(this);
	play.addActionListener(this);
	foldingNumerator.addChangeListener(this);
	foldingDenominator.addChangeListener(this);	
	sequence.addFocusListener(this);
	this.add(play, setupC(0, 0, 2, 1));
	this.add(saveLabel, setupC(0, 1, 1, 1));
	this.add(save, setupC(1, 1, 1, 1));
	this.add(tempoLabel, setupC(0, 2, 1, 1));
	this.add(tempo, setupC(1, 2, 1, 1));
	this.add(subLabel, setupC(0, 3, 1, 1));
	this.add(subdivision, setupC(1, 3, 1, 1));
	this.add(seqLabel, setupC(0, 4, 2, 1));
	this.add(seqPanel, setupCTop(0, 5, 2, 4));
	this.add(envPlot, setupC(2, 0, 2, 4));
	this.add(envLabel, setupC(2, 4, 1, 1));
	this.add(envelope, setupC(3, 4, 1, 1));
	this.add(lengthLabel, setupC(2, 5, 1, 1));
	this.add(length, setupC(3, 5, 1, 1));
	this.add(wavePlot, setupC(4, 0, 2, 4));
	this.add(waveLabel, setupC(4, 4, 1, 1));
	this.add(wave, setupC(5, 4, 1, 1));
	this.add(numLabel, setupC(4, 5, 1, 1));
	this.add(foldingNumerator, setupC(5, 5, 1, 1));
	this.add(denomLabel, setupC(4, 6, 1, 1));
	this.add(foldingDenominator, setupC(5, 6, 1, 1));
	updateWave();
	updateEnv();
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
	if (arg0.getSource().equals(foldingNumerator) || arg0.getSource().equals(foldingDenominator)) {
	    updateWave();	 
	}
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	int width = 200; // size of plot to make
	if (arg0.getSource().equals(envelope)) {
	    // redraw the envelope - this is all stubbing as I only have linear envelopes so far
	    updateEnv();
	}
	else if(arg0.getSource().equals(wave)) {
	    updateWave();	    
	}
	else if (arg0.getSource().equals(play)) {
	    Manifester music = new Manifester();
	    int t = Integer.parseInt(tempo.getValue().toString());
	    int s = Integer.parseInt(subdivision.getValue().toString());
	    int l = Integer.parseInt(length.getValue().toString());
	    int n = Integer.parseInt(foldingNumerator.getValue().toString());
	    int d = Integer.parseInt(foldingDenominator.getValue().toString());
	    
	    music.bust(outputJack, save.isSelected(), t, s, sequence.getSeq(), (Articulation.Env) envelope.getSelectedItem(), l, (Wave.Form) wave.getSelectedItem(), new Fraction(n, d));
	    
	}
	else throw new UnsupportedOperationException("Only linear env supported so far.");

	    
    }
    private void updateEnv() {
	int width = 200;
	if (envelope.getSelectedItem() == Articulation.Env.LINEAR_FALLOFF) {
		
		short[] envData = new short[width];
		for (short i = 0; i < width; i++) {
		    envData[i] = (short) (i - 100); // OmniPlot has x-axis in the middle so straddle that
		}	
		this.remove(envPlot);
		envPlot = new OmniPlot(envData, 200, 0, 1);
		GridBagConstraints gbc = setupC(2, 0, 2, 4);
		this.add(envPlot, gbc);
		this.revalidate();
		this.repaint();
	}
    }
    private void updateWave() {
	int width = 200;
	Wave display = new Wave((Wave.Form) wave.getSelectedItem());
	short[] waveData = new short[width];
	Fraction folding = new Fraction((Integer)foldingNumerator.getValue(), (Integer)foldingDenominator.getValue());
	for (int i = 0; i < width; i++) {
	    waveData[i] = (short) (display.functionFoldedAt(i, 200, 1, folding) * 100 / Short.MAX_VALUE);
	}
	this.remove(wavePlot); // necessary?
	wavePlot = new OmniPlot(waveData, 200, 0, 1);
	GridBagConstraints gbc = setupC(4, 0, 2, 4);
	this.add(wavePlot, gbc);
	this.revalidate();
	this.repaint();
    }

    @Override
    public void focusGained(FocusEvent arg0) {

    }

    @Override
    public void focusLost(FocusEvent arg0) {
	if (arg0.getSource().equals(sequence)) {
	    String regex = "[,\\-\\s\\n0-9]*"; // regex for: space, comma or digit, as many as you like
	    try {
		System.out.println("Testing after losing focus");
		System.out.println(sequence.getDocument().getText(0,sequence.getDocument().getLength()));
		if (!sequence.getDocument().getText(0,sequence.getDocument().getLength()).matches(regex))
		{
		    JOptionPane.showMessageDialog(null, "Only comma, minus sign, space and digits, please.","Invalid sequence",JOptionPane.ERROR_MESSAGE);
		    sequence.requestFocus();
		}
	    }
	    catch (BadLocationException e) {}
	}
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
	System.out.println("Closing output SourceDataLine.");
	outputJack.close();
	this.dispose();
	
    }
    @Override
    public void windowClosed(WindowEvent arg0) {
	System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }





}
