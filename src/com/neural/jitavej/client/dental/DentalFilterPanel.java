package com.neural.jitavej.client.dental;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class DentalFilterPanel extends VerticalPanel  {

	public static RadioButton filter1r1 = new RadioButton("topic1", "1");
	public static RadioButton filter1r2 = new RadioButton("topic1", "2");
	public static RadioButton filter1r3 = new RadioButton("topic1", "3");
	public static RadioButton filter1r4 = new RadioButton("topic1", "4");

	public static RadioButton filter2r1 = new RadioButton("topic2", Jitavej.CONSTANTS.filter21());
	public static RadioButton filter2r2 = new RadioButton("topic2", Jitavej.CONSTANTS.filter22());
	
	public static RadioButton filter3r1 = new RadioButton("topic3", Jitavej.CONSTANTS.filter31());
	public static RadioButton filter3r2 = new RadioButton("topic3", Jitavej.CONSTANTS.filter32());

	public static RadioButton filter4r1 = new RadioButton("topic4", Jitavej.CONSTANTS.filter41());
	public static RadioButton filter4r2 = new RadioButton("topic4", Jitavej.CONSTANTS.filter42());

	
	public DentalFilterPanel() {

    	Grid panel = new Grid(7,5);
		panel.setStyleName("dental-bar");
		//panel.setWidth("930px"); 
		panel.getColumnFormatter().setWidth(0, "50%");
		panel.getColumnFormatter().setWidth(1, "10%");		
		panel.getColumnFormatter().setWidth(2, "10%");		
		panel.getColumnFormatter().setWidth(3, "10%");		
		panel.getColumnFormatter().setWidth(4, "10%");		
		//panel.setHeight("200"); 
		
       	panel.setHTML(0, 0, Jitavej.CONSTANTS.filter1());
       	panel.setWidget(0, 1, filter1r1);
       	panel.setWidget(0, 2, filter1r2);
       	panel.setWidget(0, 3, filter1r3);
       	panel.setWidget(0, 4, filter1r4);

       	panel.setHTML(2, 0, Jitavej.CONSTANTS.filter2());
       	panel.setWidget(2, 1, filter2r1);
       	panel.setWidget(2, 2, filter2r2);
		
       	panel.setHTML(4, 0, Jitavej.CONSTANTS.filter3());
       	panel.setWidget(4, 1, filter3r1);
       	panel.setWidget(4, 2, filter3r2);
		
       	panel.setHTML(6, 0, Jitavej.CONSTANTS.filter4());
       	panel.setWidget(6, 1, filter4r1);
       	panel.setWidget(6, 2, filter4r2);

       	//panel.setWidth("400px");
        setStyleName("dental-bar");
		add(panel);
		
	}

	
	public void setPatient(JSONObject patient){
		
		if(patient == null){
		//	text.setText("");
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
						//	text.setText(response.getText());
						} else {
							Window.alert("response.getText() "+response.getText());
						}
					}
				});

			} catch (RequestException e1) {
				GWT.log("Error > ", e1);
			}				
		}
	}

}
