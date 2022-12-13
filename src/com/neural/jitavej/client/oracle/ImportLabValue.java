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

import com.neural.jitavej.client.util.ThaiEncodingUtils;
*/
public class ImportLabValue {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.L_ITEM");
			
			URL url = new URL("http://localhost:8080/jitavej/lab/importlabvalue");
	
			while (rs.next()) {
			   try {

			        String data = URLEncoder.encode("oracle_id", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(1).trim()), "UTF-8");
			        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(2).trim()), "UTF-8");
				
			        String type = "fill";
					try {
						int typenumber = rs.getInt(3);
						if(typenumber == 1){
							type = "number";
						}else if(typenumber == 2){
							type = "fill";
						}if(typenumber == 3){
							type = "choice";
						}
					} catch (Exception e) {

					}
					
			        data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + type;
			        String postfix = "";
					try {
						postfix = rs.getString(4).trim();
					} catch (Exception e) {
					}	
			        String standard = "";
					try {
						standard = rs.getString(5).trim();
					} catch (Exception e) {
					}						
			        String helplist = "";
					try {
						helplist = rs.getString(6).trim();
					} catch (Exception e) {
					}						
					
			        data += "&" + URLEncoder.encode("postfix", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(postfix), "UTF-8");
			        data += "&" + URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(standard), "UTF-8");
			        data += "&" + URLEncoder.encode("helplist", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(helplist), "UTF-8");
			    
			        
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
