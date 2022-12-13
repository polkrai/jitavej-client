package com.neural.jitavej.client.record;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;import com.neural.jitavej.client.H4uQueue;import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dental.DentalPanel;
import com.neural.jitavej.client.dialog.FormDialog;
import com.neural.jitavej.client.patient.PatientMenuPanel;
public class PinCodeDialog extends FormDialog {	
	public PasswordTextBox pincode;	//public static H4uQueue h4upincode = new H4uQueue();
	RecordDrugPanel recordDrugPanel;
	@SuppressWarnings("deprecation")
	public PinCodeDialog(RecordDrugPanel recordDrugPanel0) {		
		setHTML("<B>PinCode</B>");
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
														pincode.setEnabled(true);							pincode.setFocus(true);							
							submit.setEnabled(true);
						}						
						public void onResponseReceived(Request request, Response response) {							
							if (response.getStatusCode() == 200) {								
								if(response.getText().equals("true")){									
									hide();
									submit();
								}								else{																		//MessageBox.setMinWidth(300);									//MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_alert_pincode());									
									Window.alert(Jitavej.CONSTANTS.record_alert_pincode());									
									pincode.setEnabled(true);									pincode.setText("");									pincode.setFocus(true);									
									submit.setEnabled(true);									
									return;
								}
							} 							else {								
								Window.alert("response.getText() "+response.getText());
																pincode.setEnabled(true);
																submit.setEnabled(true);
							}
						}
					});

				} 				catch (RequestException e1) {					
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
		try {						//h4upincode.Requestqueue();
			//Window.alert("0");
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/auto");
						rb.setTimeoutMillis(30000);			
			StringBuffer params = new StringBuffer();			
			params.append("user_id="+URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
			params.append("&");	
			params.append("orderitems=" + URL.encodeComponent(RecordDrugPanel.orderItems.toString()));
			params.append("&");			
			if(Jitavej.mode.equals("dental")){				
				params.append("dentist_id="+DentalPanel.dentist.getValue(DentalPanel.dentist.getSelectedIndex()));
				params.append("&");					
				params.append("dentalitems="+URL.encodeComponent(DentalPanel.orderItems.toString()));
				params.append("&");	
			}			
			params.append("icd10s=" + URL.encodeComponent(RecordIcd10Panel.icd10Items.toString()));
			params.append("&");	
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			params.append("&");
			params.append("remed=" + RecordDrugPanel.remedBox.getValue(RecordDrugPanel.remedBox.getSelectedIndex()));
			params.append("&");
			params.append("medtext=" + RecordTextPanel.text.getText());
			params.append("&");		
			params.append("type=drug");
			params.append("&");
			params.append("mode="+RecordDrugPanel.mode);	
			params.append("&");			
			if(ReferMedPanel.refer != null && ReferMedPanel.refer.isChecked()){				
				params.append("refer=true");
				params.append("&");	
				params.append("no=" + URL.encode(ReferMedPanel.no.getText().trim()));
				params.append("&");
				params.append("to=" + URL.encode(ReferMedPanel.to.getText().trim()));
				params.append("&");		
				params.append("near=" + URL.encode(ReferMedPanel.to.getText().trim()));
				params.append("&");					
				if(ReferMedPanel.c1.isChecked()){					
					params.append("for1=true");
					params.append("&");					
				}				
				if(ReferMedPanel.c2.isChecked()){
					params.append("for2=true");
					params.append("&");					
				}				
				if(ReferMedPanel.c3.isChecked()){
					params.append("for3=true");
					params.append("&");					
				}				
				if(ReferMedPanel.c4.isChecked()){
					params.append("for4=true");
					params.append("&");					
				}				
				params.append("patient_id=" + URL.encode(Jitavej.patient.get("id").isNumber().toString()));
				params.append("&");		
				params.append("historypass=" + URL.encode(ReferMedPanel.historypass.getText().trim()));
				params.append("&");		
				params.append("historynow=" + URL.encode(ReferMedPanel.historynow.getText().trim()));
				params.append("&");		
				params.append("examination=" + URL.encode(ReferMedPanel.examination.getText().trim()));
				params.append("&");		
				params.append("diagnosis=" + URL.encode(ReferMedPanel.diagnosis.getText().trim()));
				params.append("&");		
				params.append("treatment=" + URL.encode(ReferMedPanel.treatment.getText().trim()));
				params.append("&");				
				StringBuffer cause = new StringBuffer();				
				if(ReferMedPanel.cc1.isChecked()){					
					cause.append(ReferMedPanel.cc1.getText());
					cause.append("\n");
				}				
				if(ReferMedPanel.cc2.isChecked()){
					cause.append(ReferMedPanel.cc2.getText());
					cause.append("\n");
				}				
				if(ReferMedPanel.cc3.isChecked()){
					cause.append(ReferMedPanel.cc3.getText());
					cause.append("\n");
				}				
				cause.append(ReferMedPanel.cause.getText());
				params.append("cause=" + URL.encode(cause.toString()));
				params.append("&");		
				params.append("comment=" + URL.encode(ReferMedPanel.comment.getText().trim()));
				params.append("&");		
				params.append("police=" + URL.encode(ReferMedPanel.police.getText().trim()));
				params.append("&");		
				params.append("nopolice=" + URL.encode(ReferMedPanel.nopolice.getText().trim()));
				params.append("&");		
			}						GWT.log("Params POST order/auto >> " + URL.encode(params.toString()), null);			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			rb.sendRequest(params.toString(), new RequestCallback() {				
				public void onError(Request request, Throwable exception) {					
					GWT.log("Error response received during authentication of user", exception);
					pincode.setEnabled(true);
					submit.setEnabled(true);
				}				
				public void onResponseReceived(Request request, Response response) {
										pincode.setEnabled(true);
					pincode.setText("");		
					submit.setEnabled(true);					MessageBox.setMinWidth(300);					
					if (response.getStatusCode() == 200) {
												if(response.getText().indexOf("out of stock") != -1){														MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), response.getText());
							//Window.alert(response.getText());	
							return;
						}												//Window.alert(Jitavej.queuereturn.toString());												/*boolean isQueueId = Jitavej.queuereturn.containsKey("queueId");												if (isQueueId) {														//Window.alert("HAS QUEUE");							Jitavej.JUpdatequeue();													} else {														//Window.alert("NO QUEUE");						}*/																		MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_complete() + " " + response.getText());						
						//Window.alert(Jitavej.CONSTANTS.record_complete() + " " + response.getText());											
						if(Jitavej.mode.equals("dental")){							
							if(Jitavej.visit.get("an").isString() != null){																MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), "sendIpd");
								//Window.alert("sendIpd");
								PatientMenuPanel.sendIpd();
							}							else if(recordDrugPanel.orderItems.size() > 0){																MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), "sendConfirmdrug");
								//Window.alert("sendConfirmdrug");
								PatientMenuPanel.sendConfirmdrug();
							}														else if(DentalPanel.orderItems.size() > 0){																MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), "sendFinance");
								//Window.alert("sendFinance");
								PatientMenuPanel.sendFinance();
							}							else{																MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), "sendMedrec");
								//Window.alert("sendMedrec");
								PatientMenuPanel.sendMedrec();
							}
							
						}						
						RecordDrugPanel.orderItems = new JSONArray();
						RecordDrugPanel.update();						
						if(Jitavej.mode.equals("dental")){							
							DentalPanel.orderItems = new JSONArray();
							DentalPanel.update();
						}						
						RecordIcd10Panel.icd10Items = new JSONArray();
						RecordIcd10Panel.store.removeAll();	
						RecordDrugPanel.remedBox.setSelectedIndex(0);						
						if(Jitavej.mode.equals("med") && recordDrugPanel.mode.equals("opd")){							
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
					} 					else {						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 		catch (Exception e1) {			
			Window.alert(e1.toString());
		}	
	}
}