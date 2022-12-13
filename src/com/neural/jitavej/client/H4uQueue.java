package com.neural.jitavej.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

public class H4uQueue {
	
	public static JSONObject queuedatareturn;
	
	public static String h4u_server = Jitavej.h4u_server;//"http://192.168.90.101";
	
	public static String token = Jitavej.token; //"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3N1ZSI6Img0dSIsImRlc2NyaXB0aW9uIjoiZm9yIGFjY2VzcyBRNFUgYXBpIiwiUVVFVUVfQ0VOVEVSX1RPUElDIjoicXVldWUvY2VudGVyIiwiU0VSVklDRV9QT0lOVF9UT1BJQyI6InF1ZXVlL3NlcnZpY2UtcG9pbnQiLCJERVBBUlRNRU5UX1RPUElDIjoicXVldWUvZGVwYXJ0bWVudCIsIk5PVElGWV9VU0VSIjoicTR1IiwiTk9USUZZX1BBU1NXT1JEIjoiIyNxNHUjIyIsIk5PVElGWV9TRVJWRVIiOiIxOTIuMTY4LjkwLjEwMSIsIk5PVElGWV9QT1JUIjoiODg4OCIsImlhdCI6MTU1MTE4MjUxMSwiZXhwIjoxNTgyNzQwMTExfQ.4tG7zdM_ksaQ9dLLMTtedMdu8IHR1BazVJSt6jGg2CA";
	
	public static void Requestqueue(){
		
		try {
			
			GWT.log("Requestqueue ", null);

			RequestBuilder request = new RequestBuilder(RequestBuilder.POST, H4uQueue.h4u_server + "/api/v1/api/call"); ///api/v1/api/call
	
			request.setTimeoutMillis(30000);
	
			StringBuffer senddata = new StringBuffer();
			
			senddata.append("&");
			
			senddata.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			senddata.append("hn=" + URL.encodeComponent(Jitavej.patient.get("hn").toString().replace('"', ' ').trim()));
			
			senddata.append("&");
			
			senddata.append("roomId=" + URL.encodeComponent(Jitavej.station.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			//senddata.append("servicePointId=10");
			senddata.append("servicePointId=" + URL.encodeComponent(Jitavej.station.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			senddata.append("token=" + URL.encodeComponent(token.toString()));
			
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");

			request.sendRequest(senddata.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable e) {
	
					GWT.log("Error response received during authentication of user", e);
	
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("json Response =" + response.getText(), null);
						
						queuedatareturn = JSONParser.parse(response.getText()).isObject();
						
						
						if (queuedatareturn.get("statusCode").isNumber().toString() == "500"){
							
							Window.alert("ไม่มีคิวนี้ในระบบ");
						}
						
						//GWT.log("json Response =" + queuedatareturn.get("queueId"), null);
//						System.out.print(response.getText());
					}
				}
			
			});
		}
		catch (RequestException e) {
			
			Window.alert(H4uQueue.h4u_server);
			Window.alert(e.getMessage());

		}
	}
	
	public static void Updatequeue(){
		
		try {
			
			//Window.alert(queuedatareturn.get("queueId").toString());
			
			GWT.log("Updatequeue ", null);
		
			RequestBuilder requestupdate = new RequestBuilder(RequestBuilder.POST, H4uQueue.h4u_server + "/api/v1/api/pending");
	
			requestupdate.setTimeoutMillis(30000);
			
			StringBuffer updatedata = new StringBuffer();
			
			updatedata.append("&");
			
			updatedata.append("queueId="+ URL.encodeComponent(H4uQueue.queuedatareturn.get("queueId").toString()));
			
			updatedata.append("&");
			
			updatedata.append("priorityId=" + URL.encodeComponent(H4uQueue.queuedatareturn.get("priorityId").toString()));
			
			updatedata.append("&");
			
			updatedata.append("servicePointId=" + URL.encodeComponent("27"));
			
			updatedata.append("&");
			
			updatedata.append("token=" + URL.encodeComponent(token.toString()));
			
			requestupdate.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			//Window.alert(updatedata.toString());
			
			requestupdate.sendRequest(updatedata.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable e) {
	
					GWT.log("Error RequestBuilder Updatequeue ", e);
	
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("json Update =" + response.getText(), null);
						
						//Window.alert(response.getText());
						//System.out.print(response.getText());
					}
				}
			
			});
		}
		catch (RequestException e) {
			
			Window.alert(H4uQueue.h4u_server);
			Window.alert(e.getMessage());

		}
	}

}
