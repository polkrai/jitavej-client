package com.neural.jitavej.client.patient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.NavPanel;
import com.neural.jitavej.client.dental.DentalPanel;
import com.neural.jitavej.client.dialog.FilterDialog;
import com.neural.jitavej.client.dialog.FinanceDialog;
import com.neural.jitavej.client.dialog.InformationDialog;
import com.neural.jitavej.client.dialog.SendDialog;
import com.neural.jitavej.client.dialog.SocialDialog;
import com.neural.jitavej.client.history.HistoryDialog;
import com.neural.jitavej.client.record.PinCodeDialog;

public class PatientMenuPanel extends HorizontalPanel {
	
	Jitavej jitavej = new Jitavej();
	
	public Button info, history, filter, social, finance, requestqueue, save, savedental, send, send1, sticker;
	
	PinCodeDialog dialog;

	public PatientMenuPanel() {
		
		setSpacing(1);

		dialog = new PinCodeDialog(PatientPanel.recordDrugPanel);

		info = new Button("<b>"+Jitavej.CONSTANTS.information()+"</b>");
		info.setPixelSize(100, 30);
		info.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				
				InformationDialog dialog = new InformationDialog();
				dialog.show();
				dialog.center();
			}
		});

		history = new Button("<b>"+Jitavej.CONSTANTS.history()+"</b>");
		history.setPixelSize(120, 30);
		history.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				HistoryDialog dialog = new HistoryDialog();
				dialog.show();
				dialog.center();
			}
		});

		filter = new Button("<b>"+Jitavej.CONSTANTS.filter()+"</b>");
		filter.setPixelSize(120, 30);
		filter.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				FilterDialog dialog = new FilterDialog();
				dialog.show();
				dialog.center();
			}
		});

		social = new Button("<b>"+Jitavej.CONSTANTS.social()+"</b>");
		social.setPixelSize(140, 30);
		social.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				SocialDialog dialog = new SocialDialog();
				dialog.show();
				dialog.center();
			}
		});

		finance = new Button("<b>"+Jitavej.CONSTANTS.finance()+"</b>");
		finance.setPixelSize(100, 30);
		finance.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				FinanceDialog dialog = new FinanceDialog();
				dialog.show();
				dialog.center();
			}
		});
		
		sticker = new Button("<b>"+Jitavej.CONSTANTS.print_sticker()+"</b>");
		sticker.setPixelSize(80, 30);
		sticker.addClickListener(new ClickListener() {

		
			public void onClick(Widget sender) {
				
				//Window.alert(Jitavej.patient.get("id").isNumber().toString());
				
				try {
		
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	 Jitavej.server_local + "/print_sticker/print_foot_notes/query_foot_notes_opd.php");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
					
					StringBuffer params = new StringBuffer();
					params.append("id_patient="+ URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));

					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								//Window.alert("response.getText() " + response.getText());

							} 
							else {
								
								Window.alert("response.getStatusCode() " + response.getStatusCode());
								Window.alert("response.getText() " + response.getText());
							}
						}
					});

				} 
				catch (RequestException e1) {
					
					Window.alert(e1.toString());
				}
			}
		});

		send = new Button("<b>"+Jitavej.CONSTANTS.send()+"</b>");
		send.setPixelSize(80, 50);
		send.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final SendDialog dialog = new SendDialog();
				dialog.form.addFormHandler(new FormHandler() {
					public void onSubmitComplete(FormSubmitCompleteEvent event) {
						dialog.hide();
						NavPanel.update();
						Jitavej.visit = null;
						Jitavej.patient = null;
						Jitavej.address = null;
						PatientPanel.setPatient(null);
					}

					public void onSubmit(FormSubmitEvent event) {}
				});
				
				dialog.show();
				dialog.center();
			}
		});

		send1 = new Button("<b>"+Jitavej.CONSTANTS.send1()+"</b>");
		send1.setPixelSize(140, 50);
		send1.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				
				try {
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	 Jitavej.server + "/q/financetolab");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
					StringBuffer params = new StringBuffer();
					params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));

					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request,	Response response) {
							
							if (response.getStatusCode() == 200) {
								
								NavPanel.update();
								
								PatientPanel.setPatient(null);
							} 
							else {
								
								Window.alert("response.getStatusCode() " + response.getStatusCode());
								Window.alert("response.getText() " + response.getText());
							}
						}
					});

				} 
				catch (RequestException e1) {
					
					Window.alert(e1.toString());
				}
			}
		});
		
		requestqueue = new Button("<b>"+Jitavej.CONSTANTS.request_queue()+"</b>");
		requestqueue.setPixelSize(120, 50);
		
		requestqueue.addClickListener(new ClickListener() {

			public void onClick(com.google.gwt.user.client.ui.Widget sender) {

				if(Jitavej.visit == null){
					
					MessageBox.setMinWidth(300);
					MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.alert_select_patient());
					
					//MessageBox.alert("Please select the patient before making the transaction.");
					
					return;
				}
				else {
					
					Jitavej.JRequestqueue();
					//save.setEnabled(true);
				}
			}
			
			
		});
		
		/*
		retrunqueue = new Button("<b><FONT style='color:blue;'>"+ Jitavej.CONSTANTS.return_queue() + "</font></b>");
		retrunqueue.setPixelSize(50, 50);
		
		retrunqueue.addClickListener(new ClickListener() {

			public void onClick(com.google.gwt.user.client.ui.Widget sender) {

				if(Jitavej.visit == null){
					
					MessageBox.alert("Please select the patient before making the transaction.");
					
					//Window.alert("Please select the patient before making the transaction.");
					
					return;
				}
				else {
					
					h4upatientmenu.Updatequeue();
				}
			}
			
			
		});*/
		
		save = new Button("<b><FONT style='color:blue;'>"+ Jitavej.CONSTANTS.record_medfinish() + "</font></b>");
		save.setPixelSize(180, 50);
		//save.setEnabled(false);
		save.addClickListener(new ClickListener() {
			
			public void onClick(com.google.gwt.user.client.ui.Widget sender) {
				
				if(Jitavej.visit == null){
					
					//Window.alert("Visit is NULL");
					MessageBox.setMinWidth(300);
					MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.alert_vn_isnull());
					
					return;
				}			
				
				if(Jitavej.mode.equals("dental") && DentalPanel.dentist.getSelectedIndex() == 0){
					
					//Window.alert(Jitavej.CONSTANTS.dentistnull());
					MessageBox.setMinWidth(300);
					MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.dentistnull());
					
					return;
				}
				
				if(Jitavej.mode.equals("dental")){
					
					dialog.show();
					
					dialog.center();
					
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						
						public void execute() {
							
							dialog.pincode.setText("");
							
							dialog.pincode.setFocus(true);
						}
					});
					
				}
				else if (PatientPanel.recordDrugPanel.orderItems.size() > 0) {

					if (PatientPanel.recordIcd10Panel.icd10Items.size() == 0) {
						
						//Window.alert(Jitavej.CONSTANTS.record_alert_icd10());
						MessageBox.setMinWidth(300);
						MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_alert_icd10());
						
						return;
					}

					dialog.show();
					
					dialog.center();
					
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						
						public void execute() {
							
							dialog.pincode.setText("");
							dialog.pincode.setFocus(true);
						}
					});
				} 
				else if (PatientPanel.recordIcd10Panel.icd10Items.size() > 0) {
					
					dialog.submit();
					//h4upatientmenu.Updatequeue();
					//save.setEnabled(false);
				} 
				else if (!PatientPanel.recordTextPanel.text.getText().trim().equals("")) {
					
					dialog.submit();
					//h4upatientmenu.Updatequeue();
					//save.setEnabled(false);
				}
				else {
					
					sendBackmed();
					//jitavej.Updatequeue();
				}

			}

		});

		
		HTML html = new HTML(" ");

		if (Jitavej.mode.equals("med")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(40, 18);
			add(html);
			add(requestqueue);
			add(save);
		}
		else if (Jitavej.mode.equals("dental")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(150, 18);
			add(html);
			add(save);
		}
		else if (Jitavej.mode.equals("frontmed")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(230, 18);
			add(html);
			add(send);
		} 
		else if (Jitavej.mode.equals("backmed")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			add(sticker);
			html.setPixelSize(25, 18);
			add(html);
			add(send1);
			add(send);
		} 
		else if (Jitavej.mode.equals("lab")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(260, 18);
			add(html);
			add(send);
		}
		else if (Jitavej.mode.equals("emergency")) {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(230, 18);
			add(html);
			add(send);
		}
		else {
			
			add(info);
			add(filter);
			add(history);
			add(social);
			add(finance);
			html.setPixelSize(260, 18);
			add(html);
			add(send);
		}

		setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

	}

	public void setEnabled(boolean enabled) {
		
		info.setEnabled(enabled);
		history.setEnabled(enabled);
		filter.setEnabled(enabled);
		social.setEnabled(enabled);
		finance.setEnabled(enabled);
		sticker.setEnabled(enabled);
		send.setEnabled(enabled);
		send1.setEnabled(enabled);
		requestqueue.setEnabled(enabled);
		save.setEnabled(enabled);
	}
	
	public static void sendBackmed(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/q/changeComponent");
			
			rb.setTimeoutMillis(30000);
			
			rb.setHeader("Content-Type","application/x-www-form-urlencoded");
			
			StringBuffer params = new StringBuffer();
			
			params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			params.append("&");
			params.append("component_id=27");
			params.append("&");
			params.append("action_id=26");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request,	Response response) {
					
					if (response.getStatusCode() == 200) {
						
						NavPanel.update();
						
						Jitavej.visit = null;
						Jitavej.patient = null;
						Jitavej.address = null;
						
						PatientPanel.setPatient(null);
						
						boolean isQueueId = Jitavej.queuereturn.containsKey("queueId");
						
						if (isQueueId) {
							
							Jitavej.JUpdatequeue();						
						}
						
					} 
					else {
						
						Window.alert("response.getStatusCode() "+ response.getStatusCode());
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}

	public static void sendConfirmdrug(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/q/changeComponent");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type","application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
			params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			params.append("&");
			params.append("component_id=16");
			params.append("&");
			params.append("action_id=22");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request,	Response response) {
					
					if (response.getStatusCode() == 200) {
						
						NavPanel.update();
						
						Jitavej.visit = null;
						
						Jitavej.patient = null;
						
						Jitavej.address = null;
						
						PatientPanel.setPatient(null);
					} 
					else {
						
						Window.alert("response.getStatusCode() "+ response.getStatusCode());
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}
	
	public static void sendFinance(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/q/changeComponent");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type","application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
			params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			params.append("&");
			params.append("component_id=13");
			params.append("&");
			params.append("action_id=13");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request,	Response response) {
					
					if (response.getStatusCode() == 200) {
						
						NavPanel.update();
						
						Jitavej.visit = null;
						
						Jitavej.patient = null;
						
						Jitavej.address = null;
						
						PatientPanel.setPatient(null);
					} 
					else {
						
						Window.alert("response.getStatusCode() "+ response.getStatusCode());
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}
	
	public static void sendMedrec(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/q/changeComponent");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type","application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
			params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			params.append("&");
			params.append("component_id=2");
			params.append("&");
			params.append("action_id=20");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request,	Response response) {
					
					if (response.getStatusCode() == 200) {
						
						NavPanel.update();
						
						Jitavej.visit = null;
						
						Jitavej.patient = null;
						
						Jitavej.address = null;
						
						PatientPanel.setPatient(null);
					} 
					else {
						
						Window.alert("response.getStatusCode() "+ response.getStatusCode());
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}
	
	public static void sendIpd(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/q/changeComponent");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type","application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
			params.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			params.append("&");
			params.append("component_id=20");
			params.append("&");
			params.append("action_id=27");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request,	Response response) {
					
					if (response.getStatusCode() == 200) {
						
						NavPanel.update();
						
						Jitavej.visit = null;
						
						Jitavej.patient = null;
						
						Jitavej.address = null;
						
						PatientPanel.setPatient(null);
					} 
					else {
						
						Window.alert("response.getStatusCode() "+ response.getStatusCode());
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}
	
}
