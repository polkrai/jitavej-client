package com.neural.jitavej.client.oracle;
/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
*/
public class OracleOldNew {
/*
	public static void main(String[] args) {
		WritableWorkbook workbook = null;
		try {

			FileInputStream myxls = new FileInputStream(new File("c:\\MEDITEM1400.xls"));
			POIFSFileSystem fs = new POIFSFileSystem(myxls);
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wb.getSheetAt(0); // first sheet
		
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.createWorkbook(new File("c:\\MEDITEM2.xls"), ws);
			WritableSheet s = workbook.createSheet("Sheet1", 0);				
			
			s.setColumnView(0, 15);
			s.setColumnView(1, 40);		
			s.setColumnView(2, 15);
			s.setColumnView(3, 40);
			
			
			
			int errorCount = 0;
			int row = 0;
			
			
			while(errorCount < 10){
	
				HSSFRow row2 = sheet.getRow(row);
				HSSFCell cell = row2.getCell((short) 0);
				String id = cell.getStringCellValue().trim();
				cell = row2.getCell((short) 1);
				String name = cell.getStringCellValue();					
				
				
		//		System.out.println(row);
				
			   try {
			        // Construct data
			        String data = URLEncoder.encode("oracle", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
	
			        // Send data
			        URL url = new URL("http://localhost:8080/jitavej/drug/findByOracle");
			        URLConnection conn = url.openConnection();
			        conn.setDoOutput(true);
			        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			        wr.write(data);
			        wr.flush();
			    
			        // Get the response
			        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			        String line = "";
			        String id2 = "";
			        String name2 = "";
			        while ((line = rd.readLine()) != null) {
			        	System.out.println(line);
			        	int index = line.indexOf(" ");
			        	if(index != -1){
			        		id2 = line.substring(0,index);
			        		name2 = line.substring(index, line.length());
			        	}
			        	
			        }
					s.addCell(new Label(0, row, id));
					s.addCell(new Label(1, row, ThaiEncodingUtils.ASCII2Unicode(name)));
					s.addCell(new Label(2, row, ThaiEncodingUtils.ASCII2Unicode(id2)));
					s.addCell(new Label(3, row, ThaiEncodingUtils.ASCII2Unicode(name2)));
					
			        wr.close();
			        rd.close();
			        
			        row++;
			        
			    } catch (Exception e) {
			    	System.out.println(e);
		
					errorCount++;
			    }
			

			}

		} catch (Exception ex) {
			System.out.println(ex);

		}
		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
*/
}
