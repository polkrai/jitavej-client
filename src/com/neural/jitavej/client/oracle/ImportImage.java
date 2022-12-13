package com.neural.jitavej.client.oracle;
/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
*/
public class ImportImage {
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

			PreparedStatement pStmt = connection2.prepareStatement("SELECT prscno FROM hims.PRSC WHERE hn=? and prscdate=?");
			ResultSet rs = stmt.executeQuery("SELECT hn,date_,image FROM hims.OPDCARD WHERE date_ between to_date('2006-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS') and to_date('2006-12-31 23:59:59','YYYY-MM-DD HH24:MI:SS')");

			int row = 0;
			int all = 0;
			Date date1 = new Date();
			
			Locale.setDefault(new Locale("en","US"));
			
			while (rs.next()) {
				pStmt.setString(1, rs.getString(1));
				Timestamp timestamp = rs.getTimestamp(2);
				pStmt.setTimestamp(2, timestamp);
				ResultSet rs2 = pStmt.executeQuery();
				
				InputStream blobStream = rs.getBinaryStream("image");
	
				if(rs2.next()){
					
			        if(row >= 100){
			        	Date date2= new Date();
			        	double diff = date2.getTime() - date1.getTime();
			        	System.out.println(date2+" time use " + diff/1000 + " secs for " + all);
			        	row = 0;
			        	date1 = date2;
			        }
				//	System.out.println(row +" ==> "+ rs2.getString(1));
					row++;
					all++;
					
					Date vdate = new Date(timestamp.getTime());
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
					
					
					String month = dateFormat.format(vdate);
					String year = month.substring(0,4);
				
					String vn = rs2.getString(1).trim();
				//	System.out.println(" vn =  " + vn);
					
					File file = new File("c:\\opd_scan\\"+ year +"\\"+month+ "\\" + vn + ".jpg");
	
					File dir = file.getParentFile();
				//	System.out.println(" dir " + dir);
					if(!dir.exists()){
		
						dir.mkdir();

					}
					file.createNewFile();

					
					FileOutputStream fileOutStream = new FileOutputStream(file);
					byte[] buffer = new byte[10];
					int nbytes = 0;
					while ((nbytes = blobStream.read(buffer)) != -1){
					//	System.out.println(" nbytes ==> "+ nbytes);
						fileOutStream.write(buffer, 0, nbytes);
					}
					fileOutStream.flush();
					fileOutStream.close();
					blobStream.close();			
					
					
					
			        String data = URLEncoder.encode("vn", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(vn), "UTF-8");
			        data += "&" + URLEncoder.encode("image_path", "UTF-8") + "=" + URLEncoder.encode("/nano/opd_scan/"+year+"/"+month +"/"+ vn + ".jpg", "UTF-8");

			        // Send data
			        URL url = new URL("http://localhost:8080/jitavej/visit/putimage");
			        URLConnection conn = url.openConnection();
			        conn.setDoOutput(true);
			        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			        wr.write(data);
			        wr.flush();
			    
			        // Get the response
			        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			        String line;
			        while ((line = rd.readLine()) != null) {
			       //     System.out.println(line);
			        }
			        wr.close();
			        rd.close();
			        
			        
				}

				rs2.close();	
					
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
