/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author Kevin Higgins
 */
public class OutputJackListener implements LineListener {

    @Override
    public void update(LineEvent le) {
	System.out.println("Output jack reports event: " + le.getType());
    }

}
