package com.neural.jitavej.client.patient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class DnDialog extends FormDialog {
	public TextBox text;
	
	
	public DnDialog() {
		
		setHTML("<b>DN</b>:");
		
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();

		text = new TextBox();
		text.setPixelSize(150, 18);
		try{
			text.setText(Jitavej.dn.get("dn").isString().stringValue());
		}catch(Exception ex){}
		
		Button submit1 = new Button(Jitavej.CONSTANTS.appointment_save(), new ClickListener() {
			public void onClick(Widget sender) {
				try {	
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/dental/setdn");
					rb.setTimeoutMillis(30000);	
					StringBuffer params = new StringBuffer();
					params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
					params.append("&");
					params.append("dn=" + URL.encodeComponent(text.getText().trim()));
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}
						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
							//	Jitavej.patient.put("mederror", new JSONString(text.getText().trim()));
								Jitavej.dn.put("dn", new JSONString(text.getText().trim()));
								PatientPanel.patientTopPanel.dn.setHTML(Jitavej.dn.get("dn").isString().stringValue());	

							//	PatientPanel.patientTopPanel.setPatient(Jitavej.patient);
							//	Window.alert("Change Complete! ");
								cancel.click();
							} else {
								Window.alert("response.getText() "+response.getText());
							}
						}
					});
				} catch (RequestException e1) {
					GWT.log("Error > ", e1);
				}	
			}

		});
	//	submit1.setStyleName("save");
		
		Grid grid = new Grid(2,1);
		grid.setWidget(0, 0, text); 
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.add(submit1);
		hor.add(cancel);
	
		grid.setWidget(1, 0, hor); 		
		setWidget(grid);

	}


}
