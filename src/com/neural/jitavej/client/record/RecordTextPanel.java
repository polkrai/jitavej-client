package com.neural.jitavej.client.record;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class RecordTextPanel extends VerticalPanel  {
	public static TextArea text;
	 
	public RecordTextPanel() {
		
		text = new TextArea();
		text.setPixelSize(920, 490);
		add(text);
	}

	
	public void setPatient(JSONObject patient){
		
		if(patient == null){
			text.setText("");
		}
		
		if(Jitavej.visit != null){
			
			try {
				
				RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/textinvisit");
				rb.setTimeoutMillis(30000);	
				StringBuffer params = new StringBuffer();
				params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
				rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
				rb.sendRequest(params.toString(), new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						GWT.log("Error response received during authentication of user", exception);
					}
					public void onResponseReceived(Request request, Response response) {
						if (response.getStatusCode() == 200) {
							
							text.setText(response.getText());
						} 
						else {
							
							Window.alert("response.getText() "+response.getText());
						}
					}
				});

			} 
			catch (RequestException e1) {
				GWT.log("Error > ", e1);
			}				
		}
	}

}
