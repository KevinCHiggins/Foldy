/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import javax.swing.JPanel;

/**
 *
 * @author Kevin Higgins
 */
public class ArticulationControl extends JPanel {
    public int duration;
    public enum Env {
	LINEAR_FALLOFF
    }
    public Env env;

    public ArticulationControl(int duration, Env env) {
	this.env = env;
	this.duration = duration;
    }
    
}
