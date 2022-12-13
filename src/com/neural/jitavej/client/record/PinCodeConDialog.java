package com.neural.jitavej.client.record;
import com.google.gwt.core.client.GWT;import com.google.gwt.http.client.Request;import com.google.gwt.http.client.RequestBuilder;import com.google.gwt.http.client.RequestCallback;import com.google.gwt.http.client.RequestException;import com.google.gwt.http.client.Response;import com.google.gwt.http.client.URL;import com.google.gwt.json.client.JSONArray;import com.google.gwt.user.client.Window;import com.google.gwt.user.client.ui.Button;import com.google.gwt.user.client.ui.ClickListener;import com.google.gwt.user.client.ui.HorizontalPanel;import com.google.gwt.user.client.ui.KeyboardListenerAdapter;import com.google.gwt.user.client.ui.PasswordTextBox;import com.google.gwt.user.client.ui.VerticalPanel;import com.google.gwt.user.client.ui.Widget;import com.gwtext.client.widgets.MessageBox;
import com.neural.jitavej.client.H4uQueue;import com.neural.jitavej.client.Jitavej;import com.neural.jitavej.client.dialog.FormDialog;import com.neural.jitavej.client.patient.PatientMenuPanel;

public class PinCodeConDialog extends FormDialog {
	
	public PasswordTextBox pincode;	public static H4uQueue h4upincodecon = new H4uQueue();
	
	RecordConPanel recordDrugPanel;
	
	@SuppressWarnings("deprecation")
	public PinCodeConDialog(RecordConPanel recordDrugPanel0) {
		
		setHTML("<b>PinCode</b>");
		this.recordDrugPanel = recordDrugPanel0;
		//Window.alert(recordDrugPanel.mode);
		pincode = new PasswordTextBox();
		pincode.addKeyboardListener(new KeyboardListenerAdapter(){
			
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				
		        if (keyCode == (char) KEY_ENTER) {
		        	
		        	submit.click();
		        	return;
		        }
		        
				super.onKeyPress(sender, keyCode, modifiers);
			}
        });
		
		cancel.setText(Jitavej.CONSTANTS.record_pincode_cancel());
		
		submit = new Button(Jitavej.CONSTANTS.record_pincode_confirm(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				pincode.setEnabled(false);
				
				submit.setEnabled(false);
				
				try {	
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/verifypincode");
					rb.setTimeoutMillis(30000);	
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
					params.append("&");
					params.append("pincode=" + URL.encodeComponent(pincode.getText().trim()));
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}
						
						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								if(response.getText().equals("true")){
									
									hide();
									
									submit();
								}								else{
									
									//MessageBox.setMinWidth(300);
									//MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_alert_pincode());
									
									Window.alert(Jitavej.CONSTANTS.record_alert_pincode());
									
									pincode.setEnabled(true);
									pincode.setText("");
									pincode.setFocus(true);
									
									submit.setEnabled(true);
									
									return;
								}
							} 							else {
								
								Window.alert("response.getText() "+response.getText());
							}
						}
					});

				} 
				catch (RequestException e1) {
					
					GWT.log("Error > ", e1);
				}	
	
			}
		});

		VerticalPanel ver = new VerticalPanel();
		ver.setSpacing(2);
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(2);
		hor.add(submit);
		hor.add(cancel);
		ver.add(pincode);
		ver.add(hor);
		setWidget(ver);
	}
	
	public void submit(){
		
		try {			
			//Thread.sleep(200);
			RequestBuilder rb;
			
			if(!recordDrugPanel.mode.equals("confirm")){
				
				rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/auto");
			}			else{
				
				rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/confirm");
			}

			rb.setTimeoutMillis(30000);
			
			StringBuffer params = new StringBuffer();
			
			//Window.alert(recordDrugPanel.orderItems.toString());
			
			if(recordDrugPanel.mode.equals("confirm")){
				
				params.append("order_id=" + URL.encodeComponent(recordDrugPanel.orderItems.get(0).isObject().get("order").isObject().get("id").isNumber().toString()));
				
				params.append("&");	
			}

			params.append("user_id=" + URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
			params.append("&");		
			params.append("orderitems=" + URL.encodeComponent(recordDrugPanel.orderItems.toString()));
			params.append("&");						
			params.append("icd10s=" + URL.encodeComponent(RecordIcd10Panel.icd10Items.toString()));
			params.append("&");	
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			params.append("&");
			params.append("remed=" + 0);
			params.append("&");
			params.append("medtext=" + RecordTextPanel.text.getText());
			params.append("&");		
			params.append("type=drug");
			params.append("&");
			params.append("mode=" + recordDrugPanel.mode);
			
			//Window.alert(RecordDrugPanel.mode);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						MessageBox.setMinWidth(300);
						
						if(response.getText().indexOf("out of stock") != -1){
							
							//Window.alert(response.getText());
							
							MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), response.getText());
							
							return;
						}
						
						MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_complete() + " " + response.getText());
						
						//Window.alert(Jitavej.CONSTANTS.record_complete() + " " + response.getText());												//h4upincodecon.Updatequeue();						
						recordDrugPanel.orderItems = new JSONArray();
						recordDrugPanel.update();
						
						RecordIcd10Panel.icd10Items = new JSONArray();
						RecordIcd10Panel.store.removeAll();												//monitor_queue();
						//recordDrugPanel.remedBox.setSelectedIndex(0);
						//hide();
						if(recordDrugPanel.mode.equals("opd")){
							
							PatientMenuPanel.sendBackmed();						
						}
						
						if(recordDrugPanel.mode.equals("continuation")){
							
							recordDrugPanel.remedcon();
							RecordIcd10Panel.setPatient(Jitavej.patient);
						}
						
						if(recordDrugPanel.mode.equals("homemed")){
							
							recordDrugPanel.remedhomemed();
							RecordIcd10Panel.setPatient(Jitavej.patient);
						}
						
						pincode.setEnabled(true);
						
						pincode.setText("");
						
						submit.setEnabled(true);
						
					} 					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} 
		catch (Exception e1) {
			
			Window.alert(e1.toString());
		}	
	}
}
