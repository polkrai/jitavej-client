package com.neural.jitavej.client.appointment;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.form.DateField;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class AppointmentDialog extends FormDialog {
	public FormPanel form;
	Hidden patient;
	JSONObject appointment;
	JSONObject dentist;
	Hidden dentist_id;
	SuggestBox suggestBox;
	DateField dateBox;
   	String[] months = {Jitavej.CONSTANTS.m1(),Jitavej.CONSTANTS.m2(),Jitavej.CONSTANTS.m3(),Jitavej.CONSTANTS.m4()};

	@SuppressWarnings("deprecation")
	public AppointmentDialog(JSONObject appointment, Date date0) {
		this.appointment = appointment;
		setHTML("<B>Add Appointment</B>");
		form = new FormPanel();
		form.setAction(Jitavej.server + "/appointment/put");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		Grid grid = new Grid(7,3);
		form.setWidget(grid);
		Hidden id = new Hidden();
		id.setName("appointment_id");
		id.setValue("0");
		try{
			id.setValue(appointment.get("id").isNumber().toString());
		}catch(Exception ex){
			GWT.log(ex.toString(), null);
		}
		id.setDefaultValue("0");
		id.setID("appointment");
		grid.setWidget(0, 0, new HTML(""));
		grid.setWidget(0, 1, id);

		patient = new Hidden();
		patient.setName("patient_id");
		patient.setValue(Jitavej.patient.get("id").isNumber().toString());
		try{
			patient.setValue(appointment.get("patient").isObject().get("id").isNumber().toString());
		}catch(Exception ex){
			GWT.log(ex.toString(), null);
		}
		
		grid.setWidget(1, 0, new HTML(Jitavej.CONSTANTS.appointment_patient()));
		grid.setWidget(1, 1, new HTML(Jitavej.patient.get("firstname").isString().stringValue() +" "+Jitavej.patient.get("lastname").isString().stringValue())); 
		try{
			patient.setValue(appointment.get("patient").isObject().get("id").isNumber().toString());
		}catch(Exception ex){
			GWT.log(ex.toString(), null);
		}
		grid.setWidget(1, 2, patient); 

		
		ListBox userBox = new ListBox();
		userBox.setName("user_id");
		for (int i = 0; i < Jitavej.users.size(); i++) {
			JSONObject user = Jitavej.users.get(i).isObject();
			userBox.addItem(user.get("firstname").isString().stringValue() +" "+ user.get("lastname").isString().stringValue(), user.get("id").isNumber().toString());
		}
		
		grid.setWidget(2, 0, new HTML(Jitavej.CONSTANTS.appointment_doctor()));
		grid.setWidget(2, 1, userBox); 
		
		if(Jitavej.component != null && (Jitavej.component.get("type").isString().stringValue().equals("recover") || Jitavej.component.get("type").isString().stringValue().equals("psychosocial"))){
			userBox.insertItem("", 0);
			userBox.setEnabled(false);
			userBox.setSelectedIndex(0);
		}
		

		ListBox comBox = new ListBox();
		comBox.setName("component_id");
		comBox.addItem("", "");

		for (int i = 0; i < Jitavej.components.size(); i++) {
			JSONObject component = Jitavej.components.get(i).isObject();
			if(component.get("type").isString() != null && (component.get("type").isString().stringValue().equals("lab") || component.get("type").isString().stringValue().equals("psycho") || component.get("type").isString().stringValue().equals("recover") || component.get("type").isString().stringValue().equals("psychosocial"))){
				comBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
			}
		}
		
		
		grid.setWidget(3, 0, new HTML(Jitavej.CONSTANTS.appointment_spacial()));
		grid.setWidget(3, 1, comBox); 
		
		if(Jitavej.component != null && Jitavej.component.get("type").isString().stringValue().equals("recover")){
			
			for (int i = 0; i < comBox.getItemCount(); i++) {
				if(comBox.getValue(i).equals(Jitavej.component.get("id").isNumber().toString())){
					comBox.setSelectedIndex(i);
				}
			}
		//	comBox.setEnabled(false);
		}
		
        dateBox = new DateField();
        dateBox.setValue(date0);
        dateBox.setDisabled(true);
		
		final Hidden date = new Hidden();
		date.setName("date");

	//	date.setEnabled(false);
	//	startdate.setText(Jitavej.dateFormat.format(date1));
		grid.setWidget(4, 0, new HTML("Date"));
		grid.setWidget(4, 1, dateBox);
		grid.setWidget(4, 2, date);
		
		
		TextBox comment = new TextBox();
		comment.setName("comment");
		grid.setWidget(5, 0, new HTML("Comment"));
		grid.setWidget(5, 1, comment);

		
		submit = new Button("Submit", new ClickListener() {
			public void onClick(Widget sender) {
				date.setValue(""+dateBox.getValue().getTime());
				form.submit();
			}
		});
		
		grid.setWidget(6, 0, new HTML(""));
		grid.setWidget(6, 1, submit);
		setWidget(form);

	}


}
