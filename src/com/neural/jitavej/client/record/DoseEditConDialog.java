package com.neural.jitavej.client.record;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.autocomplete.DoseSuggestOracle;
import com.neural.jitavej.client.dialog.FormDialog;
import com.neural.jitavej.client.patient.PatientMenuPanel;
import com.neural.jitavej.client.patient.PatientPanel;

public class DoseEditConDialog extends FormDialog {
	SuggestBox suggestDose;
	int row;
	RecordConPanel recordDrugPanel;
	
	@SuppressWarnings("deprecation")
	public DoseEditConDialog(int rowIndex, RecordConPanel recordDrugPanel0) {
		row = rowIndex;
		this.recordDrugPanel = recordDrugPanel0;
		setHTML("<B>Dose Edit</B>");
		
		final DoseSuggestOracle doseOracle = new DoseSuggestOracle();
		suggestDose = new SuggestBox(doseOracle);
        suggestDose.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
        suggestDose.setPixelSize(360, 18);
        suggestDose.setStyleName("drug");
        suggestDose.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		        if (keyCode == (char) KEY_ENTER) {
		        	if(!suggestDose.suggestionPopup.isAttached()){
		        		submit();
					}
		        }
				super.onKeyPress(sender, keyCode, modifiers);
			}
			
        });


		cancel.setText(Jitavej.CONSTANTS.record_pincode_cancel());
		
		submit = new Button(Jitavej.CONSTANTS.record_pincode_confirm(), new ClickListener() {
			public void onClick(Widget sender) {
				submit();
			}
		});

		
		VerticalPanel ver = new VerticalPanel();
		ver.setSpacing(2);
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(2);
		hor.add(submit);
		hor.add(cancel);
		
		ver.add(suggestDose);
		ver.add(hor);

		setWidget(ver);
	
	}

	public void submit(){
	   	try {
    		if(suggestDose.getText().trim().length() == 0){
    			return;
    		}

			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/dose/findByCode");
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("code=" + URL.encodeComponent(suggestDose.getText().trim()));
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.toString());
				}
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						JSONValue value = JSONParser.parse(response.getText());
						JSONObject dose = value.isObject();
						recordDrugPanel.orderItems.get(row).isObject().put("dose", dose);
						recordDrugPanel.update();
						hide();
					}else{
						suggestDose.setText("");
						Window.alert(Jitavej.CONSTANTS.record_alert_dose());
						suggestDose.setFocus(true);						
					//	Window.alert("response.getStatusCode() " + response.getStatusCode());
					//	Window.alert("response.getText() " + response.getText());
					}
				}
			});
		} catch (Exception e) {
		//	Window.alert(e.toString());
		}	
	}
}
