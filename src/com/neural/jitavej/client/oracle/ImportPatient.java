package com.neural.jitavej.client.oracle;
/*
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

import com.neural.jitavej.client.util.ThaiEncodingUtils;
*/
public class ImportPatient {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.44.46:1521:hims", "sys", "sysistrue");

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.PT");
			int row = 0;
			int all = 0;
			Date date1 = new Date();
			while (rs.next()) {

				   try {
				        // Construct data
				        String data = URLEncoder.encode("hn", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("hn")), "UTF-8");
				        data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("fname")), "UTF-8");
				        data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("lname")), "UTF-8");
				    
				        // Send data
				        URL url = new URL("http://localhost:8080/jitavej/patient/put2");
				        URLConnection conn = url.openConnection();
				        conn.setDoOutput(true);
				        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				        wr.write(data);
				        wr.flush();
				    
				        // Get the response
				  //      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				 //       String line;
				 //       while ((line = rd.readLine()) != null) {
				        //    System.out.println(line);
				  //      }
				  //      wr.close();
				 //       rd.close();
				        
				        row++;
				        all++;
				        if(row > 1000){
				        	Date date2= new Date();
				        	double diff = date2.getTime() - date1.getTime();
				        	System.out.println("time use " + diff/1000 + " secs for " + all);
				        	row = 0;
				        	date1 = date2;
				        }
				        
				        
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
