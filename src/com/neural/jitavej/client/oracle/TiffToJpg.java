package com.neural.jitavej.client.oracle;
/*
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.*;
import javax.imageio.stream.*;

import com.asprise.util.tiff.TIFFReader;
*/
public class TiffToJpg {
/*
	public static void main(String[] args) {
		try {
			
			Iterator iter = ImageIO.getImageWritersByFormatName("png");
			ImageWriter writer = (ImageWriter)iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			
		//	iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		//	iwp.setCompressionQuality(0.2f);
			
		    File dir = new File("c:\\opdcard7");
		    
		    String[] children = dir.list();
		    if (children == null) {
		        // Either dir does not exist or is not a directory
		    } else {
		        for (int i=0; i<children.length; i++) {
		            // Get filename of file or directory
		            String filename = children[i];
		            System.out.println("c:\\opdcard7\\"+filename);
		
		            TIFFReader reader = new TIFFReader(new File("c:\\opdcard7\\"+filename)); 
	            	RenderedImage image0 = reader.getPage(0); // extract page
		          // 	ImageIO.write(image, "jpg", new File("c:\\opdcard8\\"+filename));

		           	File file = new File("c:\\opdcard8\\"+filename);
		           	FileImageOutputStream output = new FileImageOutputStream(file);
		           	writer.setOutput(output);
		           	IIOImage image = new IIOImage(image0, null, null);
		           	writer.write(null, image, iwp);
		           	
		        }
		    }

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
*/
}
