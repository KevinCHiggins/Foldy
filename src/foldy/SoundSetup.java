/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Class to tackle one-time setup of Java Sound API, then
 * provide related constants and conversions and audio output lines
 * @author Kevin Higgins
 */
public class SoundSetup {
    private final int SAMPLE_RATE = 44100;
    private final int BIT_RATE = 16;
    private final int CHANNELS_AMOUNT = 1;
    private final int BUFFER_SIZE = 32768;
    private final boolean SIGNED = true;
    private final boolean BIG_ENDIANNESS = false;
    private Mixer.Info[] availableMixers;
    AudioFormat format;

    public SoundSetup() {
	
	availableMixers = AudioSystem.getMixerInfo();

	
	// 44,100Hz 16-bit mono signed little-endian
	format = new AudioFormat(SAMPLE_RATE, BIT_RATE, CHANNELS_AMOUNT, SIGNED, BIG_ENDIANNESS);

    }
    public SourceDataLine getOutputJack() throws LineUnavailableException {
	SourceDataLine outputJack;
	DataLine.Info outputJackAttr = new DataLine.Info(SourceDataLine.class, format);
	if (!AudioSystem.isLineSupported(outputJackAttr)) {
	    System.out.println("No line found for " + format);
	    System.exit(1);
	}
	SourceDataLine l = (SourceDataLine) AudioSystem.getLine(outputJackAttr);
	l.addLineListener(new OutputJackListener());
	return l;

    }
    public AudioFormat getFormat() {
	return format;
    }
    public int getBufferSize() {
	return BUFFER_SIZE;
    }
    public int getBytesPerSample() {
	return BIT_RATE / 8;
    }
    public int getSampleRate() {
	return SAMPLE_RATE;
    }
    public String reportAvailableMixers() {
	String report = "";
	for (Mixer.Info m : availableMixers) {
	    report += m.toString() + "\n";
	}
	return report;
    }



}
