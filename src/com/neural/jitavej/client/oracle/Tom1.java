package com.neural.jitavej.client.oracle;
/*
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
*/

public class Tom1 {
/*
	public static void main(String[] args) {
		try {

			FileInputStream myxls = new FileInputStream(new File("c:\\tom1.xls"));
			POIFSFileSystem fs = new POIFSFileSystem(myxls);
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wb.getSheetAt(0); // first sheet

			
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File("c:\\tom11.xls"), ws);
			WritableSheet s = workbook.createSheet("Sheet1", 0);			


			//	s.mergeCells(1, 1, 2, 2);
			
			s.setColumnView(0, 15);
			s.setColumnView(1, 15);		
			s.setColumnView(2, 12);

			
			//	Locale.setDefault(new Locale("th", "TH"));
			//	Locale.setDefault(new Locale("en", "US"));

			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			WritableCellFormat cf = new WritableCellFormat(wf);
			
			cf.setWrap(true);
			cf.setShrinkToFit(true);
			cf.setBackground(Colour.YELLOW2);
			cf.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
			WritableCellFormat cf2 = new WritableCellFormat(wf2);
			cf2.setWrap(true);
			cf2.setAlignment(Alignment.GENERAL);

			WritableCellFormat cf3 = new WritableCellFormat(wf2);
			cf3.setWrap(true);
			cf3.setAlignment(Alignment.RIGHT);
			
			s.addCell(new Label(0, 0, "", cf));		
			s.addCell(new Label(1, 0, "", cf));		
			s.addCell(new Label(2, 0, "", cf));

			int errorCount = 0;
			int row = 1;
			while(errorCount < 10){
				try {
					HSSFRow row2 = sheet.getRow(row);
					HSSFCell cell = row2.getCell((short) 0);
					String name1 = cell.getStringCellValue();
					cell = row2.getCell((short) 1);
					String name2 = cell.getStringCellValue();		
					cell = row2.getCell((short) 2);
					String name3 = cell.getStringCellValue();							
				//	System.out.println(row +"  name = " + name + ", description=" + description);


					s.addCell(new Label(0, row, name1, cf2));
					s.addCell(new Label(1, row, ThaiEncodingUtils.ASCII2Unicode(name2), cf2));
					s.addCell(new Label(2, row, ThaiEncodingUtils.ASCII2Unicode(name3), cf2));

					
					
				
				} catch (Exception ex) {
					System.out.println(ex);
					errorCount++;
				}
				row++;

			}
			workbook.write();
			workbook.close();
			
		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
*/
}
