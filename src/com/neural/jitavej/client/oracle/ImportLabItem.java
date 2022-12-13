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

import com.neural.jitavej.client.util.ThaiEncodingUtils;
*/
public class ImportLabItem {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.L_SERVICE");
			URL url = new URL("http://localhost:8080/jitavej/item/importlabitem");
	
			while (rs.next()) {
			   try {

			        String data = URLEncoder.encode("oracle_id", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(1).trim()), "UTF-8");
			        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(2).trim()), "UTF-8");
			        data += "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(4).trim()), "UTF-8");
			        data += "&" + URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString(8).trim()), "UTF-8");
			    
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
