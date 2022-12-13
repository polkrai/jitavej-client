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
public class ImportAll {
/*
	public static void main(String[] args) {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Connection connection =
			// DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:hims");
		//	Connection connection = DriverManager.getConnection("jdbc:oracle:oci8:@hims", "sys", "sysistrue");
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");
			Connection connection2 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");			
			Connection connection3 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.90.1:1521:ORCL", "sys", "sysistrue");	
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM hims.PRSC WHERE prscno != -1 and prscdate between to_date('2009-02-27 00:00:00','YYYY-MM-DD HH24:MI:SS') and to_date('2009-02-27 10:12:59','YYYY-MM-DD HH24:MI:SS')");

			Statement stmt2 = connection2.createStatement();

			Statement stmt3 = connection3.createStatement();
			
			URL url = new URL("http://localhost:8080/jitavej/visit/putalloracle");

			
			int row = 0;
			int all = 0;
			Date date1 = new Date();
			while (rs.next()) {
				   try {
					   String hn = rs.getString("hn").trim();
				        String data = URLEncoder.encode("hn", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(hn), "UTF-8");
				        data += "&" + URLEncoder.encode("vn", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("prscno").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("medrec", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("issprsn").trim()), "UTF-8");
				        data += "&" + URLEncoder.encode("sit", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs.getString("pttype").trim()), "UTF-8");
						try {
							String med = rs.getString("stf").trim();
					        data += "&" + URLEncoder.encode("med", "UTF-8") + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(med), "UTF-8");
						} catch (Exception e) {
						}	
						Date date  = rs.getTimestamp("prscdate");
				        data += "&date=" + date.getTime();
		
						ResultSet rs2 = stmt2.executeQuery("select hims.meditem.meditem,hims.medusage.medusage,qty,hims.prscdt.salerate from hims.prscdt,hims.meditem,hims.medusage where hims.prscdt.meditem = hims.meditem.meditem and hims.prscdt.medusage = hims.medusage.medusage and prscno =" + rs.getString("prscno").trim());
						
						int i = 0;
						while (rs2.next()) {
							i++;
						//	System.out.println(rs2.getString(1).trim());
					        data += "&drug" + i + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs2.getString(1).trim()), "UTF-8");					        
					        data += "&dose" + i +"=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs2.getString(2).trim()), "UTF-8");
					        data += "&qty" + i +"=" + rs2.getDouble(3);
					        data += "&price" + i +"=" + rs2.getDouble(4);
				        		
				        }
						data  += "&itemcount=" + i;
						
				//		System.out.println(date.toString().substring(0,19));
						ResultSet rs3 = stmt3.executeQuery("select icd10 from hims.ovstdiag where vstdate = to_date('"+date.toString().substring(0,19)+"','YYYY-MM-DD HH24:MI:SS') and hn = '" +rs.getString("hn").trim() +"'");
						
	
						int k = 0;
						while (rs3.next()) {
							k++;
						//	System.out.println(rs2.getString(1).trim());
					        data += "&icd10" + k + "=" + URLEncoder.encode(ThaiEncodingUtils.ASCII2Unicode(rs3.getString(1).trim()), "UTF-8");					        
			        		
				        }
						data  += "&icd10count=" + k;						
						
						
						
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
				        

				        if(row >= 1){
				        	Date date2= new Date();
				        	double diff = date2.getTime() - date1.getTime();
				        	System.out.println(date2+" time use " + diff/1000 + " secs for " + all + " HN = " +hn);
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
