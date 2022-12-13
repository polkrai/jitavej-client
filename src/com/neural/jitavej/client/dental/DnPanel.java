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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;

public class DnPanel extends VerticalPanel  {
 
	public static TextBox dn;

	public static RadioButton topic1r1 = new RadioButton("topic1", Jitavej.CONSTANTS.no());
	public static RadioButton topic1r2 = new RadioButton("topic1", Jitavej.CONSTANTS.yes());
	
	public static TextBox topic1t1 = new TextBox();

	public static RadioButton topic2r1 = new RadioButton("topic2", Jitavej.CONSTANTS.no());
	public static RadioButton topic2r2 = new RadioButton("topic2", Jitavej.CONSTANTS.yes());
	
	public static RadioButton topic3r1 = new RadioButton("topic3", Jitavej.CONSTANTS.no());
	public static RadioButton topic3r2 = new RadioButton("topic3", Jitavej.CONSTANTS.yes());
	public static TextBox topic3t1 = new TextBox();
	
	public static RadioButton topic4r1 = new RadioButton("topic4", Jitavej.CONSTANTS.no());
	public static RadioButton topic4r2 = new RadioButton("topic4", Jitavej.CONSTANTS.yes());
	public static TextBox topic4t1 = new TextBox();
	
	public static RadioButton topic5r1 = new RadioButton("topic5", Jitavej.CONSTANTS.topic5r1());
	public static RadioButton topic5r2 = new RadioButton("topic5", Jitavej.CONSTANTS.topic5r2());
	public static TextBox topic5t1 = new TextBox();
	public static TextBox topic5t2 = new TextBox();
	
	public static RadioButton topic6r1 = new RadioButton("topic6", Jitavej.CONSTANTS.no());
	public static RadioButton topic6r2 = new RadioButton("topic6", Jitavej.CONSTANTS.yes());
	
	public static RadioButton topic7r1 = new RadioButton("topic7", Jitavej.CONSTANTS.topic5r1());
	public static RadioButton topic7r2 = new RadioButton("topic7", Jitavej.CONSTANTS.topic5r2());
	public static TextBox topic7t1 = new TextBox();
	
	public static RadioButton topic8r1 = new RadioButton("topic8", Jitavej.CONSTANTS.no());
	public static RadioButton topic8r2 = new RadioButton("topic8", Jitavej.CONSTANTS.yes());
	public static TextBox topic8t1 = new TextBox();
	
	public static RadioButton topic9r1 = new RadioButton("topic9", Jitavej.CONSTANTS.topic9r1());
	public static RadioButton topic9r2 = new RadioButton("topic9", Jitavej.CONSTANTS.topic9r2());
	public static TextBox topic9t1 = new TextBox();
	public static CheckBox topic9c1 = new CheckBox(Jitavej.CONSTANTS.topic9c1());
	public static CheckBox topic9c2 = new CheckBox(Jitavej.CONSTANTS.topic9c2());
	
	public static CheckBox topic10c1 = new CheckBox(Jitavej.CONSTANTS.topic10c1());
	public static CheckBox topic10c2 = new CheckBox(Jitavej.CONSTANTS.topic10c2());
	public static CheckBox topic10c3 = new CheckBox(Jitavej.CONSTANTS.topic10c3());
	public static CheckBox topic10c4 = new CheckBox(Jitavej.CONSTANTS.topic10c4());
	public static CheckBox topic10c5 = new CheckBox(Jitavej.CONSTANTS.topic10c5());
	public static CheckBox topic10c6 = new CheckBox(Jitavej.CONSTANTS.topic10c6());
	public static TextBox topic10t1 = new TextBox();
	
	public static RadioButton topic11r1 = new RadioButton("topic11", Jitavej.CONSTANTS.topic9r1());
	public static RadioButton topic11r2 = new RadioButton("topic11", Jitavej.CONSTANTS.topic9r2());
	public static TextBox topic11t1 = new TextBox();
	
	public DnPanel() {
	
		Button save = new Button(Jitavej.CONSTANTS.save());
		save.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/dental/savedn");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
					StringBuffer params = new StringBuffer();
					params.append("patient_id="+ URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
					params.append("&");
					params.append("user_id="+ URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
					params.append("&");	
					
					params.append("topic1r1="+ topic1r1.isChecked());
					params.append("&");	
					params.append("topic1t1="+ URL.encodeComponent(topic1t1.getText()));
					params.append("&");	
					
					params.append("topic2r1="+ topic2r1.isChecked());
					params.append("&");	
					
					params.append("topic3r1="+ topic3r1.isChecked());
					params.append("&");	
					params.append("topic3t1="+ URL.encodeComponent(topic3t1.getText()));
					params.append("&");	
					
					params.append("topic4r1="+ topic4r1.isChecked());
					params.append("&");	
					params.append("topic4t1="+ URL.encodeComponent(topic4t1.getText()));
					params.append("&");	
					
					params.append("topic5r1="+ topic5r1.isChecked());
					params.append("&");	
					params.append("topic5t1="+ URL.encodeComponent(topic5t1.getText()));
					params.append("&");	
					params.append("topic5t2="+ URL.encodeComponent(topic5t2.getText()));
					params.append("&");	
					
					params.append("topic6r1="+ topic6r1.isChecked());
					params.append("&");	
					
					params.append("topic7r1="+ topic7r1.isChecked());
					params.append("&");	
					params.append("topic7t1="+ URL.encodeComponent(topic7t1.getText()));
					params.append("&");	
					
					params.append("topic8r1="+ topic8r1.isChecked());
					params.append("&");	
					params.append("topic8t1="+ URL.encodeComponent(topic8t1.getText()));
					params.append("&");	
					
					params.append("topic9r1="+ topic9r1.isChecked());
					params.append("&");	
					params.append("topic9t1="+ URL.encodeComponent(topic9t1.getText()));
					params.append("&");	
					params.append("topic9c1="+ topic9c1.isChecked());
					params.append("&");	
					params.append("topic9c2="+ topic9c2.isChecked());
					params.append("&");	
					

					params.append("topic10c1="+ topic10c1.isChecked());
					params.append("&");	
					params.append("topic10c2="+ topic10c2.isChecked());
					params.append("&");	
					params.append("topic10c3="+ topic10c3.isChecked());
					params.append("&");	
					params.append("topic10c4="+ topic10c4.isChecked());
					params.append("&");	
					params.append("topic10c5="+ topic10c5.isChecked());
					params.append("&");	
					params.append("topic10c6="+ topic10c6.isChecked());
					params.append("&");	
					params.append("topic10t1="+ URL.encodeComponent(topic10t1.getText()));
					params.append("&");	
					
					params.append("topic11r1="+ topic11r1.isChecked());
					params.append("&");	
					params.append("topic11t1="+ URL.encodeComponent(topic11t1.getText()));
					params.append("&");	
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request,	Response response) {
							if (response.getStatusCode() == 200) {
								Window.alert("DN: " +response.getText());
								Jitavej.loadDn();
							} 
						}
					});

				} catch (RequestException e1) {
					Window.alert(e1.toString());
				}
			}
		});
		
		Button print = new Button("Print");
		print.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				try {					
					String url = Jitavej.server + "/report/dn?patient_id=" + Jitavej.patient.get("id").isNumber().toString();
					Window.open(url, "_new", "");
				} catch (Exception e1) {
					Window.alert(e1.toString());
				}
			}
		});
		
    	Grid panel = new Grid(26,7);
    	panel.setCellSpacing(8);
    	panel.setCellPadding(0);
		panel.setStyleName("dental-bar");
		panel.setWidth("90s0"); 
		//panel.setHeight("200"); 
		panel.getColumnFormatter().setStyleName(3, "dental-bar-right");
		panel.getColumnFormatter().setWidth(6, "100px");
		
       	panel.setWidget(0, 0, new HTML(Jitavej.CONSTANTS.topic1()));
       	panel.setWidget(0, 1, topic1r1);
       	panel.setWidget(0, 2, topic1r2);       	
       	panel.setWidget(0, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(0, 4, topic1t1);

       	panel.setWidget(2, 0, new HTML(Jitavej.CONSTANTS.topic2()));
       	panel.setWidget(2, 1, topic2r1);
       	panel.setWidget(2, 2, topic2r2);       	

       	panel.setWidget(4, 0, new HTML(Jitavej.CONSTANTS.topic3()));
       	panel.setWidget(4, 1, topic3r1);
       	panel.setWidget(4, 2, topic3r2);       	
       	panel.setWidget(4, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(4, 4, topic3t1);
       	
       	panel.setWidget(6, 0, new HTML(Jitavej.CONSTANTS.topic4()));
       	panel.setWidget(6, 1, topic4r1);
       	panel.setWidget(6, 2, topic4r2);       	
       	panel.setWidget(6, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(6, 4, topic4t1);
       	panel.setWidget(6, 5, new HTML("mmHg"));
       	
       	panel.setWidget(8, 0, new HTML(Jitavej.CONSTANTS.topic5()));
       	panel.setWidget(8, 1, topic5r1);
       	panel.setWidget(8, 2, topic5r2);       	
       	panel.setWidget(8, 3, new HTML(Jitavej.CONSTANTS.topic5t1()));
       	panel.setWidget(8, 4, topic5t1);
       	panel.setWidget(8, 5, new HTML(Jitavej.CONSTANTS.topic5t2()));
       	panel.setWidget(8, 6, topic5t2);
       	
       	panel.setWidget(10, 0, new HTML(Jitavej.CONSTANTS.topic6()));
       	panel.setWidget(10, 1, topic6r1);
       	panel.setWidget(10, 2, topic6r2); 
       	
       	panel.setWidget(12, 0, new HTML(Jitavej.CONSTANTS.topic7()));
       	panel.setWidget(12, 1, topic7r1);
       	panel.setWidget(12, 2, topic7r2);       	
       	panel.setWidget(12, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(12, 4, topic7t1);
       	
       	panel.setWidget(14, 0, new HTML(Jitavej.CONSTANTS.topic8()));
       	panel.setWidget(14, 1, topic8r1);
       	panel.setWidget(14, 2, topic8r2);       	
       	panel.setWidget(14, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(14, 4, topic8t1);
       	
       	panel.setWidget(16, 0, new HTML(Jitavej.CONSTANTS.topic9()));
       	panel.setWidget(16, 1, topic9r1);
       	panel.setWidget(16, 2, topic9r2);       	
       	panel.setWidget(16, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(17, 4, topic9t1);
       	panel.setWidget(17, 1, topic9c1);
       	panel.setWidget(17, 2, topic9c2);  
       	
       	panel.setWidget(19, 0, new HTML(Jitavej.CONSTANTS.topic10()));
       	panel.setWidget(19, 1, topic10c1);
       	panel.setWidget(19, 2, topic10c2); 	
       	panel.setWidget(20, 1, topic10c3);
       	panel.setWidget(20, 2, topic10c4); 	
       	panel.setWidget(21, 1, topic10c5);
       	panel.setWidget(21, 2, topic10c6); 	
       	panel.setWidget(21, 3, new HTML(Jitavej.CONSTANTS.topic10t1()));
       	panel.setWidget(21, 4, topic10t1);
       	
       	panel.setWidget(23, 0, new HTML(Jitavej.CONSTANTS.topic11()));
       	panel.setWidget(23, 1, topic11r1);
       	panel.setWidget(23, 2, topic11r2);       	
       	panel.setWidget(23, 3, new HTML(Jitavej.CONSTANTS.t()));
       	panel.setWidget(23, 4, topic11t1);
       	panel.setWidget(23, 5, new HTML(Jitavej.CONSTANTS.topic11t1()));
       
       	panel.setWidget(25, 0, save);
       	panel.setWidget(25, 1, print);
       	
		add(panel);
	}

	public void cleardata(){
		topic1r1.setChecked(true);
		topic2r1.setChecked(true);
		topic3r1.setChecked(true);
		topic4r1.setChecked(true);
		topic5r1.setChecked(true);
		topic6r1.setChecked(true);
		topic7r1.setChecked(true);
		topic8r1.setChecked(true);
		topic9r1.setChecked(true);
		topic11r1.setChecked(true);		
		
		topic1t1.setText("");
		
	} 
	
	public void setPatient(JSONObject patient){
		
		cleardata();
		
		if(Jitavej.dn != null && Jitavej.dn.get("id").isNumber() != null){
			if(!Jitavej.dn.get("topic1r1").isBoolean().booleanValue()){
				topic1r2.setChecked(true);
			}	
			topic1t1.setText(Jitavej.dn.get("topic1t1").isString().stringValue());
			/*	
			if(!Jitavej.dn.get("topic2r1").isBoolean().booleanValue()){
				topic1r2.setChecked(true);
			}	
			
			if(!Jitavej.dn.get("topic3r1").isBoolean().booleanValue()){
				topic3r2.setChecked(true);
			}	
			topic3t1.setText(Jitavej.dn.get("topic3t1").isString().stringValue());
			
			if(!Jitavej.dn.get("topic4r1").isBoolean().booleanValue()){
				topic4r1.setChecked(true);
			}	
			topic4t1.setText(Jitavej.dn.get("topic4t1").isString().stringValue());
			
			if(!Jitavej.dn.get("topic5r1").isBoolean().booleanValue()){
				topic5r2.setChecked(true);
			}	
			topic5t1.setText(Jitavej.dn.get("topic5t1").isString().stringValue());
			topic5t1.setText(Jitavej.dn.get("topic5t1").isString().stringValue());
		*/
		
		
		} 
				


	}

}
