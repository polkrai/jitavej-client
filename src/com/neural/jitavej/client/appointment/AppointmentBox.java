package com.neural.jitavej.client.appointment;

import java.util.Date;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;

public class AppointmentBox extends FocusPanel {		
  public int contentHeight;
  public int contentWidth;  
  public Widget header;
  public Widget user;
  public Widget content;
  FocusPanel line;
  VerticalPanel verticalPanel;
  JSONObject appointment;
  
  public AppointmentBox(JSONObject appointment) {
    this.appointment = appointment;
    setStyleName("appointment-box");
    setHeight("60");
	header = new HTML("Header");
	if(appointment.get("component") != null){
		header = new HTML(appointment.get("component").isObject().get("name").isString().stringValue());
	}else{
		header = new HTML();
	}
	
	user = new HTML("Usre");
	if(appointment.get("user") != null){
		user = new HTML(appointment.get("user").isObject().get("username").isString().stringValue());
	}else{
		user = new HTML();
	}	
	
	
	content = new HTML();
	
	if(appointment.get("date") != null){
		content = new HTML(appointment.get("patient").isObject().get("firstname").isString().stringValue() +" "+appointment.get("patient").isObject().get("lastname").isString().stringValue());
	}else{
		content = new HTML();
	}
	
	
//	GWT.log("AppointmentBox date = " + DentalLive.dateFormat.parse(appointment.get("start_date").isString().stringValue()), null);
	
    header.addStyleName("appointment-header");
    content.addStyleName("appointment-content");

    addClickListener(new ClickListener() {

      public void onClick(Widget sender) {
			JSONObject appointment = new JSONObject();
			appointment.put("patient", Jitavej.patient);
			final AppointmentDialog dialog = new AppointmentDialog(appointment, new Date(Long.parseLong(appointment.get("date").toString())));
			dialog.form.addFormHandler(new FormHandler() {
				public void onSubmitComplete(FormSubmitCompleteEvent event) {
				//	Window.alert(event.getResults());
					dialog.hide();
				//	setDate(new Date(Long.parseLong(appointment.get("date").toString())));
				}

				public void onSubmit(FormSubmitEvent event) {
				}
			});
			dialog.show();
			dialog.center();
      }
    });


    verticalPanel = new VerticalPanel();
    verticalPanel.setSpacing(0);
    verticalPanel.add(header);
    verticalPanel.add(user);
    verticalPanel.add(content);

    add(verticalPanel);
    
 //   setContentSize(100, 100);
    
 
  }

}
