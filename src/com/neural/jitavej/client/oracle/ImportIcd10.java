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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
*/
public class ImportIcd10 {
/*
	public static void main(String[] args) {
		try {

			FileInputStream myxls = new FileInputStream(new File("c:\\icd10.xls"));
			POIFSFileSystem fs = new POIFSFileSystem(myxls);
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wb.getSheetAt(0); // first sheet
			
			URL url = new URL("http://localhost:8080/jitavej/icd10/putoracle");
			int errorCount = 0;
			int row = 0;
			while(errorCount < 10){
				try {
					HSSFRow row2 = sheet.getRow(row);
					HSSFCell cell = row2.getCell((short) 0);
					String code = cell.getStringCellValue().trim();
					cell = row2.getCell((short) 1);
					String name = cell.getStringCellValue().trim();					
				//	System.out.println(row +"  name = " + name + ", description=" + description);

					   try {
					        // Construct data
					        String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");
					        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
					    
					        // Send data
					        
					        URLConnection conn = url.openConnection();
					        conn.setDoOutput(true);
					        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					        wr.write(data);
					        wr.flush();
					    
					        // Get the response
					        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					        String line;
					        while ((line = rd.readLine()) != null) {
					        		System.out.println(line);
					        }
					        wr.close();
					        rd.close();
					    } catch (Exception e) {
					    	System.out.println(e);
					    }
				
				} catch (Exception ex) {
					System.out.println(ex);
					errorCount++;
				}
				row++;

			}

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
*/
}
