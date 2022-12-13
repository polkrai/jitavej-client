package com.neural.jitavej.client.oracle;
/*
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import javax.swing.JFrame;

import com.java4less.vision.Barcode1DReader;
import com.java4less.vision.BarcodeData;
import com.java4less.vision.RImage;
*/
public class RenameImage {
/*	
	private String[][] testSet={{"scan.jpg",""+Barcode1DReader.CODE39}};

	
	private String dir="c:\\";
	
	public static void main(String[] args)  {
	
		RenameImage sample=new RenameImage();
		try {
		
			
			while(true){
				sample.scanImage("scan3.jpg", Barcode1DReader.CODE39);
				Thread.sleep(2000);
			}
			

			
			//	sample.runTests();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

	public void scanImage(String file,int type) throws Exception {
		
		Image im=loadImage(dir+file);
		
		Barcode1DReader reader=new Barcode1DReader();
		reader.setSymbologies(type);
	
		
		long prv=System.currentTimeMillis();
		
		RImage rim=new RImage((BufferedImage) im);

        long tim=System.currentTimeMillis();
		BarcodeData[] barcodes=reader.scan(rim);
		rim=null;
		im=null;
		
	//	System.out.print("End. "+(System.currentTimeMillis()-tim)+". ");
		if (barcodes.length==0) System.out.println("*** NOT FOUND ***");
		
		for (int i=0;i<barcodes.length;i++) {
		//	System.out.println("Barcode  found "+barcodes[i].toString());
			System.out.println("VN >>>>>>>>>>>>>>>>> "+barcodes[i].getValue());			
		}
						
	}

	Image im2=null;
	public Image loadImage(String f) throws Exception {
			im2=null;
			java.awt.MediaTracker mt2=null;

			java.io.FileInputStream in=null;
			byte[] b=null;
			int size=0;


			in=new java.io.FileInputStream( f);
			if (in!=null) {
				size=in.available();
				b=new byte[size];
				in.read(b);
				im2=java.awt.Toolkit.getDefaultToolkit().createImage(b);
				in.close();
			}

		//	im2 = new JFrame().createImage(new FilteredImageSource(im2.getSource(),new CropImageFilter(520, -20, 1100, 110)));
	
	
		    JFrame frame = new JFrame(){
		    		  public void paint(Graphics g) {
		    		    super.paint(g);
		    		    g.drawImage(im2, 0, 0, this);
		    		  }		    	
		    };
		    frame.setSize(1100, 110);
		    frame.show();
		
			mt2 = new java.awt.MediaTracker(new Canvas());		
			
			
			
			if (im2!=null) {
				if (mt2!=null) {mt2.addImage(im2,0); mt2.waitForID(0);}
				int WidthIm = im2.getWidth(null);
			}


		BufferedImage input = new BufferedImage(im2.getWidth(null),im2.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		
	
		
		Graphics g=input.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,im2.getWidth(null),im2.getHeight(null));
		g.drawImage(im2,0,0,null);
		g.dispose();
		g=null;
		
		return input;


		}	
*/
}
