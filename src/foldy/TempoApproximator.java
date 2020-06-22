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
public final class TempoApproximator {
    final private int MAX_DIV = 12;
    final private int DEFAULT_DIV = 4;
    final private int DEFAULT_TIME = 4;
    final private int DEFAULT_BPM = 120;
    float targBPM = 120;
    double actualBPM;
    private int samplesPerBeat;
    private int samplesPerTatum;
    private int subdivision = DEFAULT_DIV;
    private boolean upToDate = false;
    public TempoApproximator() {
	calcSamplesPerTatum();
    }
    public TempoApproximator(float targBPM) {
	this.targBPM = targBPM;
	calcSamplesPerTatum();
    }
    public TempoApproximator(int targBPM, int subdivision) {
	this.targBPM = targBPM;
	this.subdivision = subdivision;
	calcSamplesPerTatum();
    }
    public void setBPM(int bpm) {
	targBPM = bpm;
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
	samplesPerTatum = (int) (minuteInSamples / targBPM / subdivision);
	samplesPerBeat = samplesPerTatum * subdivision;
	actualBPM = minuteInSamples / (double)samplesPerBeat;
	System.out.println("Target: " + targBPM + ", actual: " + actualBPM + ", per tatum: " + samplesPerTatum + ", per beat: " + samplesPerBeat );
	upToDate = true;
    }
}
