/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foldy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Kevin
 */
public class Foldy {

    /**
     * A monophonic wavefolding sequencing synthesiser.
     * Gonna keep samples in short type.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	/*Envelope q = Envelope.getQuadratic(8, 44100, Short.MAX_VALUE);
	for (int i = 0; i < 44100; i++) {
	    //System.out.println(q.getNextY());
	}
	*/
	SoundSetup setup = new SoundSetup();
	Calc convert = new Calc(setup);
	SourceDataLine outputJack;
	AudioFormat format = setup.getFormat();
	Processor play = new Processor();
	String lineInfoReport = "";
	try {
	    outputJack = setup.getOutputJack();
	    lineInfoReport = outputJack.getLineInfo().toString();
	    outputJack.open(setup.getFormat(), setup.getBufferSize());
	    System.out.println(setup.reportAvailableMixers());
	    System.out.println("Opened " + lineInfoReport + ", using " + setup.getFormat());
	    play.testUsing(outputJack);
	    outputJack.close();
	}
	catch (LineUnavailableException ex) {	    
	    System.out.println("Despite previous check, no line available");
	    System.exit(1);
	}
	
    

	
	

    }
    
}
