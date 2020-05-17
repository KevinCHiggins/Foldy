/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package foldy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Kevin Higgins
 */
public class RawAudioWarehouser {
    final String filenameAndPath = "C:\\Users\\Kevin\\Documents\\NetBeansProjects\\Foldy\\test.raw";
    //Path path = Paths.get(filenameAndPath);
    File file = new File(filenameAndPath);
    OutputStream out;
    public void save(byte[] rawAudio) {
	System.out.println("Saving file.");
	try {
	    
	    if (file.createNewFile()) {
		System.out.println("File ready");
	    }
	    else {
		System.out.println("File was already there.");
	    }

	    out = new FileOutputStream(file);
	    out.write(rawAudio);
	    out.close();
	}
	catch (IOException ioe) {System.out.println("Error: " + ioe.getMessage());}
	    
	
	
    }
    
}
