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
public class ImportRemed {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.44.87:1521:HIMS", "sys", "sysistrue");
			Connection connection2 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.44.87:1521:HIMS", "sys", "sysistrue");			
		
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.REMEDICATIONHEAD");

			Statement stmt2 = connection2.createStatement();
			
			URL url = new URL("http://localhost:8080/jitavej/visit/putremed");

			int row = 0;
			int all = 0;
			Date date1 = new Date();
			while (rs.next()) {
				   try {
				        String data = URLEncoder.encode("hn", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("HN").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("oracle_id", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("S_REMEDICATIONHEAD")), "UTF-8");

				        try {
							String stf = rs.getString("STF").trim();
					        data += "&" + URLEncoder.encode("doctor", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(stf), "UTF-8");

						} catch (Exception e) {
						}	
						
						
						Date startdate  = rs.getTimestamp("STARTDATE");
				        data += "&startdate=" + startdate.getTime();						
						Date enddate  = rs.getTimestamp("ENDDATE");
				        data += "&enddate=" + enddate.getTime();
		
						ResultSet rs2 = stmt2.executeQuery("select hims.meditem.meditem,hims.meditem.name,hims.medusage.medusage,hims.REMEDICATIONTAIL.qty from hims.REMEDICATIONTAIL,hims.meditem,hims.medusage where hims.REMEDICATIONTAIL.meditem = hims.meditem.meditem and hims.REMEDICATIONTAIL.medusage = hims.medusage.medusage and S_REMEDICATIONHEAD =" + rs.getString("S_REMEDICATIONHEAD").trim());
						
						int i = 0;
						while (rs2.next()) {
							i++;
						//	System.out.println(rs2.getString(1).trim());
					        data += "&drug" + i + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs2.getString(1).trim()), "UTF-8");
					        data += "&drugtext" + i + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs2.getString(2).trim()), "UTF-8");
					        data += "&dose" + i +"=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs2.getString(3).trim()), "UTF-8");
					        data += "&qty" + i +"=" + rs2.getDouble(4);
				        		
				        }
						data  += "&itemcount=" + i;
							
					//	System.out.println("oracle");
						
					    row++;
				        all++;					        
						
						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);	
			
				        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				        wr.write(data);
				        wr.flush();

				
				        // Get the response
				        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				        String line;
				        while ((line = rd.readLine()) != null) {
				   //         System.out.println(line);
				        }
				        wr.close();
				        rd.close();
				        

				        if(row >= 1000){
				        	Date date2= new Date();
				        	double diff = date2.getTime() - date1.getTime();
				        	System.out.println(date2+" time use " + diff/1000 + " secs for " + all);
				        	row = 0;
				        	date1 = date2;
				        }
				        
				     //   System.out.println("postgres");
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
