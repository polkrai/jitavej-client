package com.neural.jitavej.client.oracle;
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
*/
public class ImportRecord {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.44.46:1521:hims", "sys", "sysistrue");

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select prscno,hims.meditem.name,shortname,qty,hims.prscdt.salerate from hims.prscdt,hims.meditem,hims.medusage where hims.prscdt.meditem = hims.meditem.meditem and  hims.prscdt.medusage = hims.medusage.medusage");

			URL url = new URL("http://localhost:8080/jitavej/visit/putalloracle");

			
			int row = 0;
			int all = 0;
			Date date1 = new Date();
			while (rs.next()) {

				   try {
	
				        String data = URLEncoder.encode("prscno1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(1).trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("dosetext1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(2).trim() +" "+ rs.getString(3).trim()), "UTF-8");
				        data += "&qty1=" + rs.getDouble(4);
				        data += "&price1=" + rs.getDouble(5);
					    row++;
				        all++;
				        
				        for(int i=2; i<=10; i++){
				        	if(rs.next()){
						        data += "&prscno" + i + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(1).trim()), "UTF-8");
						        data += "&dosetext" + i +"=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(2).trim() +" "+ rs.getString(3).trim()), "UTF-8");
						        data += "&qty" + i +"=" + rs.getDouble(4);
						        data += "&price" + i +"=" + rs.getDouble(5);
							    row++;
						        all++;					        		
				        	}
				        }

						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);	
			
				        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				        wr.write(data);
				        wr.flush();

				
				        // Get the response
				        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				        String line;
				        while ((line = rd.readLine()) != null) {
				      //      System.out.println(line);
				        }
				        wr.close();
				        rd.close();
				        

				        if(row > 1000){
				        	Date date2= new Date();
				        	double diff = date2.getTime() - date1.getTime();
				        	System.out.println(date2+" time use " + diff/1000 + " secs for " + all);
				        	row = 0;
				        	date1 = date2;
				        }
				        
				        
				    } catch (Exception e) {
				    	e.printStackTrace();
				    }			

			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
*/
}
