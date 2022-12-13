package com.neural.jitavej.client.appointment;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;

public class DayPanel extends FocusPanel {  
	
	VerticalPanel ver;
	Date date;
	HTML html;
	
    public DayPanel() {  
    	setWidth("150");
    	setHeight("400");
    	setStyleName("appointment-day-box");
    	ver = new VerticalPanel();
    	ver.setWidth("100%");
    	
    	html = new HTML();
    	
  //  	ver.add(new AppointmentBox(null));
  //  	ver.add(new AppointmentBox(null));
    	
    	DockPanel dock = new DockPanel();
    	
    	
    	dock.add(html, DockPanel.NORTH);
    	dock.add(ver, DockPanel.CENTER); 
    	
    	add(dock);
    	
    	
    	addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if( Jitavej.patient == null){
					return;
				}
				JSONObject appointment = new JSONObject();
				appointment.put("patient", Jitavej.patient);
				final AppointmentDialog dialog = new AppointmentDialog(appointment, date);
				dialog.form.addFormHandler(new FormHandler() {
					public void onSubmitComplete(FormSubmitCompleteEvent event) {
					//	Window.alert(event.getResults());
						dialog.hide();
						setDate(date);
					}

					public void onSubmit(FormSubmitEvent event) {
					}
				});
				dialog.show();
				dialog.center();
	
			}    		
    	});
    	
    }  
    
    public void setDate(Date date){
		this.date = date;
		html.setHTML("<b>"+Jitavej.dateFormat.format(date)+"</b>");
    	try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/appointment/list2");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			params.append("component_id=" + AppointmentPanel.componentBox.getValue(AppointmentPanel.componentBox.getSelectedIndex()));
			params.append("&");
			params.append("user_id=" + AppointmentPanel.userBox.getValue(AppointmentPanel.userBox.getSelectedIndex()));
			params.append("&");			
			params.append("date=" + date.getTime());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						JSONValue list = JSONParser.parse(response.getText());
						JSONArray appointments = list.isArray();
						ver.clear();
						for (int i = 0; i < appointments.size(); i++) {
							final JSONObject appointment = appointments.get(i).isObject();
							ver.add(new AppointmentBox(appointment));
						}
				    	
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}    	
    }
    
    
}  