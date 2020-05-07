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
public class Wave {
    public enum Form {
	SINE, SQUARE, TRIANGLE, SAW
    }
    private Form form;
    public Wave(Form form) {
	this.form = form;
    }

}
