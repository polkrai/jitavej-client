package com.neural.jitavej.client.record;

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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.autocomplete.HospitalSuggestOracle;
import com.neural.jitavej.client.autocomplete.HospitalSuggestion;

public class ReferMedPanel extends VerticalPanel {
	
	public static TextBox no = new TextBox();
	public static TextBox to = new TextBox();
	public static SuggestBox suggestBox;
	public static TextBox near = new TextBox();
	public static Label patienttext = new Label("...");
	
	public static CheckBox c1 = new CheckBox(Jitavej.CONSTANTS.refer_c1());
	public static CheckBox c2 = new CheckBox(Jitavej.CONSTANTS.refer_c2());
	public static CheckBox c3 = new CheckBox(Jitavej.CONSTANTS.refer_c3());
	public static CheckBox c4 = new CheckBox(Jitavej.CONSTANTS.refer_c4());
	
	public static CheckBox cc1 = new CheckBox(Jitavej.CONSTANTS.c1());
	public static CheckBox cc2 = new CheckBox(Jitavej.CONSTANTS.c2());
	public static CheckBox cc3 = new CheckBox(Jitavej.CONSTANTS.c3());
	
	public static TextArea historypass = new TextArea();
	public static TextArea historynow = new TextArea();
	public static TextArea examination = new TextArea();
	public static TextArea diagnosis = new TextArea();
	public static TextArea treatment = new TextArea();
	
	public static TextArea cause = new TextArea();
	public static TextArea comment = new TextArea();

	public static CheckBox police = new CheckBox(Jitavej.CONSTANTS.refer_police());
	public static CheckBox nopolice = new CheckBox(Jitavej.CONSTANTS.refer_nopolice());	
	public static CheckBox refer;

	public Grid grid;
	//public HTML html = new HTML();
	
	public ReferMedPanel() {

		refer = new CheckBox("Refer");
		refer.setChecked(true);
		
		//html.setWidth("920px");
		
		//refer.setWidth("920px");
		//refer.getElement().setPropertyString("style", "margin-top: 5px;");
		//refer.setStyleName("checkbox-top");
		/*
		if(refer.isChecked() && Jitavej.patient != null){
			
			html.setHeight("1px");
			
			grid.setVisible(true);
			
			patienttext.setText(Jitavej.patient.get("title").isString().stringValue() +" "+ Jitavej.patient.get("firstname").isString().stringValue() +" "+ Jitavej.patient.get("lastname").isString().stringValue());
			
			JSONArray icd10s = RecordIcd10Panel.icd10Items;
			
			StringBuffer buff = new StringBuffer();
			
			for (int i = 0; i < icd10s.size(); i++) {
				
				JSONObject icd10 = icd10s.get(i).isObject();
				
				buff.append(icd10.get("code").isString().stringValue() +" "+icd10.get("name").isString().stringValue() +"\n");
			}
			
			diagnosis.setText(buff.toString());
			
			JSONArray orderItems = RecordDrugPanel.orderItems;
			
			buff = new StringBuffer();
			
			for (int i = 0; i < orderItems.size(); i++) {
				
				JSONObject orderItem = orderItems.get(i).isObject();
				
				//Window.alert(orderItem.toString());
				buff.append(orderItem.get("drug").isObject().get("code").isString().stringValue() +" "+orderItem.get("dose").isObject().get("code").isString().stringValue()  +" "+orderItem.get("qty").isNumber().toString() +"\n");
			}
			
			treatment.setText(buff.toString());
			
		}
		else{
			
			html.setHeight("460px");
			grid.setVisible(false);
		}
		*/
		refer.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				
				//Window.alert("11");
				
				if(refer.isChecked() && Jitavej.patient != null){
					
					//html.setHeight("1px");
					
					grid.setVisible(true);
					
					patienttext.setText(Jitavej.patient.get("title").isString().stringValue() +" "+ Jitavej.patient.get("firstname").isString().stringValue() +" "+ Jitavej.patient.get("lastname").isString().stringValue());
					
					JSONArray icd10s = RecordIcd10Panel.icd10Items;
					
					StringBuffer buff = new StringBuffer();
					
					for (int i = 0; i < icd10s.size(); i++) {
						
						JSONObject icd10 = icd10s.get(i).isObject();
						
						buff.append(icd10.get("code").isString().stringValue() +" "+icd10.get("name").isString().stringValue() +"\n");
					}
					
					diagnosis.setText(buff.toString());
					
					JSONArray orderItems = RecordDrugPanel.orderItems;
					
					buff = new StringBuffer();
					
					for (int i = 0; i < orderItems.size(); i++) {
						
						JSONObject orderItem = orderItems.get(i).isObject();
						
						//Window.alert(orderItem.toString());
						buff.append(orderItem.get("drug").isObject().get("code").isString().stringValue() +" "+orderItem.get("dose").isObject().get("code").isString().stringValue()  +" "+orderItem.get("qty").isNumber().toString() +"\n");
					}
					
					treatment.setText(buff.toString());
					
				}
				else{
					
					//html.setHeight("460px");
					
					grid.setVisible(false);
				}
			}
		});
		
		grid = new Grid(19,3);
	
		//grid.setWidth("920");
		//grid.setCellPadding(8);
		grid.setCellSpacing(8);
		
        HospitalSuggestOracle oracle = new HospitalSuggestOracle();
        suggestBox = new SuggestBox(oracle);
        suggestBox.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
        suggestBox.setWidth("80px");
        suggestBox.addEventHandler(new SuggestionHandler() {
			public void onSuggestionSelected(SuggestionEvent event) {
				to.setText(((HospitalSuggestion)event.getSelectedSuggestion()).hospital.get("name").isString().stringValue());			
			}
		});
        
		to.setWidth("420px");
		near.setWidth("720");
		
		historypass.setPixelSize(720, 30);
		historynow.setPixelSize(720, 30);
		examination.setPixelSize(720, 30);
		diagnosis.setPixelSize(720, 40);
		treatment.setPixelSize(720, 80);
		
		cause.setPixelSize(720, 30);
		comment.setPixelSize(720, 30);

		no.setName("no");
		to.setName("to");
		near.setName("near");
		c1.setName("for1");
		c2.setName("for2");
		c3.setName("for3");
		c4.setName("for4");
		
		historypass.setName("historypass");
		historynow.setName("historynow");
		examination.setName("examination");
		diagnosis.setName("diagnosis");
		treatment.setName("treatment");
		cause.setName("cause");
		comment.setName("comment");		

		police.setName("police");
		nopolice.setName("nopolice");
		
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

		HorizontalPanel hor0 = new HorizontalPanel();
		hor0.add(suggestBox);
		hor0.add(to);
		
		grid.setWidget(3, 0, new HTML(Jitavej.CONSTANTS.refer_to()));
		grid.setWidget(3, 1, hor0);
		
		grid.setWidget(4, 0, new HTML(Jitavej.CONSTANTS.refer_text1()));
		grid.setWidget(4, 1, patienttext);		
		
		grid.setWidget(5, 0, new HTML(Jitavej.CONSTANTS.refer_near()));
		grid.setWidget(5, 1, near);
		
		HorizontalPanel hor4 = new HorizontalPanel();
		hor4.add(c1);
		hor4.add(c2);
		hor4.add(c3);
		hor4.add(c4);

		grid.setWidget(6, 0, new HTML(""));
		grid.setWidget(6, 1, hor4);	
		
		grid.setWidget(7, 0, new HTML(Jitavej.CONSTANTS.refer_historypass()));
		grid.setWidget(7, 1, historypass);
		
		grid.setWidget(8, 0, new HTML(Jitavej.CONSTANTS.refer_historynow()));
		grid.setWidget(8, 1, historynow);
		
		grid.setWidget(9, 0, new HTML(Jitavej.CONSTANTS.refer_examination()));
		grid.setWidget(9, 1, examination);
		
		grid.setWidget(10, 0, new HTML(Jitavej.CONSTANTS.refer_diagnosis()));
		grid.setWidget(10, 1, diagnosis);
		
		grid.setWidget(11, 0, new HTML(Jitavej.CONSTANTS.refer_treatment()));
		grid.setWidget(11, 1, treatment);
		
		grid.setWidget(12, 0, new HTML(Jitavej.CONSTANTS.refer_problem()));
		
		grid.setWidget(12, 1, cc1);
		
		grid.setWidget(13, 1, cc2);
		
		grid.setWidget(14, 1, cc3);
		
		grid.setWidget(15, 0, new HTML(Jitavej.CONSTANTS.refer_problem2()));
		grid.setWidget(15, 1, cause);

		grid.setWidget(16, 0, new HTML(Jitavej.CONSTANTS.refer_comment()));
		grid.setWidget(16, 1, comment);

		HorizontalPanel hor5 = new HorizontalPanel();
		hor5.add(police);
		hor5.add(nopolice);

		grid.setWidget(17, 0, new HTML(Jitavej.CONSTANTS.refer_connect()));
		grid.setWidget(17, 1, hor5);

		setWidth("920px");
		setPixelSize(920, 600);
		setSpacing(5);
		
		HTML html = new HTML();
		
		html.setWidth("920px");
		html.setHeight("1px");
		add(html);
		add(refer);
		add(grid);
		
		
		//HTML html = new HTML();
		
		//html.setWidth("920px");
		//html.setHeight("1px");
		//add(html);
		//add(refer);
		//add(grid);
		
		/*
		HTML html_body = new HTML();
		
		html_body.setWidth("920px");
		html_body.setHeight("460px");
		add(html_body);
		add(grid);
		*/
		
		grid.setVisible(false);
	
	}
	
	public void setPatient(JSONObject patient){
		
		grid.setVisible(false);

		if(patient == null){
			
			no.setText("");
			to.setText("");
			suggestBox.setText("");
			near.setText("");
		}
		
		refer.setChecked(false);

	}
	
	public void loadIcd10(){
		
      	try {
      		
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "//lasticd10s");
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
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} 
      	catch (RequestException e1) {
      		
			Window.alert(e1.toString());
		}  
	}
	
	public void loadDrug(){
		
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
	}
	
	public void loadDoctor(){
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
					//	user_id.setValue(doctor.get("id").isNumber().toString());
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