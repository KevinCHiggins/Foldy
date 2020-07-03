/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Kevin Higgins
 */
public final class TimeControl extends JPanel {
    final private int MAX_DIV = 12;
    final private int DEFAULT_DIV = 4;
    final private int DEFAULT_TIME = 4;
    final private int DEFAULT_BPM = 120;
    //float targBPM = 120;
    double actualBPM;
    private int samplesPerBeat;
    private int samplesPerTatum;
    private int subdivision = DEFAULT_DIV;
    private boolean upToDate = false;
    JFormattedTextField tempoControl = new JFormattedTextField();
    JFormattedTextField divisionControl = new JFormattedTextField();
    public TimeControl() {
	tempoControl.setValue(DEFAULT_BPM);
	divisionControl.setValue(DEFAULT_DIV);
	calcSamplesPerTatum();
    }
    public TimeControl(float targBPM) {
	tempoControl.setValue(targBPM);
	divisionControl.setValue(DEFAULT_DIV);
	calcSamplesPerTatum();
    }
    public TimeControl(int targBPM, int subdivision) {
	tempoControl.setValue(targBPM);
	divisionControl.setValue(subdivision);
	calcSamplesPerTatum();
    }
    public void setBPM(int targBPM) {
	tempoControl.setValue(targBPM);
	upToDate = false;
    }
    public void setSubdivision(int subdivision) {
	if (subdivision < MAX_DIV) {
	    this.subdivision = subdivision;
	}
	upToDate = false;
    }
    public int getSamplesPerTatum() {
	if (!upToDate) calcSamplesPerTatum();
	return samplesPerTatum;
    }
    private void calcSamplesPerTatum() {
	int minuteInSamples = Calc.secsToSamples(60);
	samplesPerTatum = (int) (minuteInSamples / Float.parseFloat(tempoControl.getValue().toString()) / Integer.parseInt(divisionControl.getValue().toString()));
	samplesPerBeat = samplesPerTatum * Integer.parseInt(divisionControl.getValue().toString());
	actualBPM = minuteInSamples / (double)samplesPerBeat;
	System.out.println("Target: " + Float.parseFloat(tempoControl.getValue().toString()) + ", actual: " + actualBPM + ", per tatum: " + samplesPerTatum + ", per beat: " + samplesPerBeat );
	upToDate = true;
    }
}
