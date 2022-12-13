package com.neural.jitavej.client.oracle;
/*
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
public class ImportOrder {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.PRSC WHERE prscno != -1");
			
			URL url = new URL("http://localhost:8080/jitavej/visit/putoracleorder");
		//	URL url = new URL("http://localhost:8080/jitavej/visit/putoracle");

			
			int row = 0;
			int all = 0;
			Date date1 = new Date();
			while (rs.next()) {
				   try {

				        String data = URLEncoder.encode("hn1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("hn").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("vn1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("prscno").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("medrec1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("issprsn").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("sit1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("pttype").trim()), "UTF-8");
						try {
							String med = rs.getString("stf").trim();
					        data += "&" + URLEncoder.encode("med1", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(med), "UTF-8");
						} catch (Exception e) {
						}	
				        data += "&date1=" + rs.getDate("prscdate").getTime();
					    row++;
				        all++;

				        
				        for(int i=2; i<=10; i++){
				        	if(rs.next()){
						        data += "&" + URLEncoder.encode("hn"+i, "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("hn").trim()), "UTF-8");
						        data += "&" + URLEncoder.encode("vn"+i, "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("prscno").trim()), "UTF-8");
						        data += "&" + URLEncoder.encode("medrec"+i, "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("issprsn").trim()), "UTF-8");
						        data += "&" + URLEncoder.encode("sit"+i, "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("pttype").trim()), "UTF-8");
								try {
									String med = rs.getString("stf").trim();
							        data += "&" + URLEncoder.encode("med"+i, "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(med), "UTF-8");
								} catch (Exception e) {
								}	
						        data += "&date"+i+"=" + rs.getDate("prscdate").getTime();
							    row++;
						        all++;					        		
				        	}
				        }
				        
				        
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
				      //      System.out.println(line);
				        }
				        wr.close();
				        rd.close();
				        

				        if(row > 100){
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
