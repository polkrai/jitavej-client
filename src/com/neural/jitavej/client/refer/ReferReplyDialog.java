package com.neural.jitavej.client.refer;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class ReferReplyDialog extends FormDialog {
	public FormPanel form;
	public TextBox no = new TextBox();
	public TextBox to = new TextBox();

	public TextArea examination = new TextArea();
	public TextArea diagnosis = new TextArea();
	public TextArea treatment = new TextArea();

	public TextArea comment = new TextArea();

	@SuppressWarnings("deprecation")
	public ReferReplyDialog() {
		setHTML("<B>Reply Refer</B>");
		
		form = new FormPanel();
		form.setAction(Jitavej.server + "/refer/putreply");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		Grid grid = new Grid(16,3);
		form.setWidget(grid);
		
	//	grid.setWidth("920");
	//	grid.setCellPadding(8);
		grid.setCellSpacing(8);
		
		to.setWidth("800");

		examination.setPixelSize(800, 30);
		diagnosis.setPixelSize(800, 40);
		treatment.setPixelSize(800, 80);
	
		comment.setPixelSize(800, 30);

		no.setName("no");
		to.setName("to");

		examination.setName("examination");
		diagnosis.setName("diagnosis");
		treatment.setName("treatment");

		comment.setName("comment");		

		Hidden patient_id = new Hidden();
		patient_id.setName("patient_id");
		
		final Hidden user_id = new Hidden();
		user_id.setName("user_id");
		
		grid.setWidget(0, 0, new HTML(Jitavej.CONSTANTS.refer_no()));
		grid.setWidget(0, 1, no);
		grid.setWidget(0, 2, user_id);
		
		grid.setWidget(1, 0, new HTML(Jitavej.CONSTANTS.refer_date()));
		grid.setWidget(1, 1, new HTML(Jitavej.dateFormat.format(new Date())));
		
		grid.setWidget(2, 0, new HTML(Jitavej.CONSTANTS.refer_from()));
		grid.setWidget(2, 1, new HTML(Jitavej.CONSTANTS.refer_jitavej()));

		grid.setWidget(3, 0, new HTML(Jitavej.CONSTANTS.refer_to()));
		grid.setWidget(3, 1, to);
		
		grid.setWidget(4, 0, new HTML(Jitavej.CONSTANTS.refer_text1()));
		if(Jitavej.patient != null){
			grid.setWidget(4, 1, new HTML(Jitavej.patient.get("title").isString().stringValue() +" "+ Jitavej.patient.get("firstname").isString().stringValue() +" "+ Jitavej.patient.get("lastname").isString().stringValue()));		
			patient_id.setValue(Jitavej.patient.get("id").isNumber().toString());
		}
		grid.setWidget(4, 2, patient_id);		

		
		grid.setWidget(6, 0, new HTML(Jitavej.CONSTANTS.refer_examination2()));
		grid.setWidget(6, 1, examination);
		
		
		///////////////////////////////////////////////////////////////////////
      	try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/refer/lasticd10s");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + Jitavej.patient.get("id").isNumber().toString());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						JSONValue value = JSONParser.parse(response.getText());
						JSONArray icd10s = value.isArray();
						StringBuffer buff = new StringBuffer();
						for (int i = 0; i < icd10s.size(); i++) {
							JSONObject icd10 = icd10s.get(i).isObject();
							buff.append(icd10.get("code").isString().stringValue() +" "+icd10.get("name").isString().stringValue() +"\n");
						}
						diagnosis.setText(buff.toString());
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}    	
		///////////////////////////////////////////////////////////////////////
		
		grid.setWidget(10, 0, new HTML(Jitavej.CONSTANTS.refer_diagnosis2()));
		grid.setWidget(10, 1, diagnosis);
		
		///////////////////////////////////////////////////////////////////////
      	try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/refer/lastdrugs");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + Jitavej.patient.get("id").isNumber().toString());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						JSONValue value = JSONParser.parse(response.getText());
						JSONArray orderItems = value.isArray();
						StringBuffer buff = new StringBuffer();
						for (int i = 0; i < orderItems.size(); i++) {
							JSONObject orderItem = orderItems.get(i).isObject();
							buff.append(orderItem.get("drug").isString().stringValue() +" "+orderItem.get("dose").isString().stringValue()  +" "+orderItem.get("qty").isNumber().toString() +"\n");
						}
						treatment.setText(buff.toString());
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}    	
		///////////////////////////////////////////////////////////////////////
		
		grid.setWidget(11, 0, new HTML(Jitavej.CONSTANTS.refer_treatment2()));
		grid.setWidget(11, 1, treatment);
	
		grid.setWidget(13, 0, new HTML(Jitavej.CONSTANTS.refer_task()));
		grid.setWidget(13, 1, comment);

		///////////////////////////////////////////////////////////////////////
      	try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/refer/lastdoctor");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + Jitavej.patient.get("id").isNumber().toString());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						JSONValue value = JSONParser.parse(response.getText());
						JSONObject doctor = value.isObject();
						user_id.setValue(doctor.get("id").isNumber().toString());
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}    	
		///////////////////////////////////////////////////////////////////////
		
		submit = new Button("Submit", new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		});
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.add(submit);
		hor.add(cancel);
		
		grid.setWidget(15, 0, new HTML(""));
		grid.setWidget(15, 1, hor);
		
		setWidget(form);
		
		
	}
}