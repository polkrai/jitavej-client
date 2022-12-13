package com.neural.jitavej.client.record;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.MessageBox;
import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;

public class RemedPanel extends VerticalPanel {
	
	String data = null;
	Button save;
	public RemedOrderPanel remedOrderPanel;
	public RecordDrugPanel recordDrugPanel;
	
	public RemedPanel() {
		
		setSpacing(0);
		
		recordDrugPanel = new RecordDrugPanel("remed");
		remedOrderPanel = new RemedOrderPanel(recordDrugPanel);
		
		recordDrugPanel.setVisible(true);
		recordDrugPanel.remed.setVisible(false);
		//recordDrugPanel.save.setVisible(false);
		
		save = new Button("+" + Jitavej.CONSTANTS.record_save());
		
		save.addClickListener(new ClickListener() {
			
			public void onClick(com.google.gwt.user.client.ui.Widget sender) {
				
				if(recordDrugPanel.orderItems.size() == 0){
					
					return;
				}
				
				save.setEnabled(false);
				
				try {
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/auto");
					rb.setTimeoutMillis(30000);
					StringBuffer params = new StringBuffer();
					params.append("orderitems=" + URL.encodeComponent(recordDrugPanel.orderItems.toString()));
					params.append("&");
					params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
					params.append("&");
					params.append("remed=0");
					params.append("&");
					params.append("type=drug");
					params.append("&");
					params.append("mode=remed");					
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								MessageBox.setMinWidth(300);
								
								MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.record_complete());
								
								//Window.alert(Jitavej.CONSTANTS.record_complete());
								
								recordDrugPanel.orderItems = new JSONArray();
								recordDrugPanel.update();
							} 
							else {
								
								Window.alert("response.getText() "+response.getText());
							}
							
							save.setEnabled(true);
						}
					});

				} 
				catch (Exception e1) {
					
					Window.alert(e1.toString());
					save.setEnabled(true);
				}
				
			}

		});

		ActionBar bar = new ActionBar();
		bar.add(save);

		add(remedOrderPanel);
		add(recordDrugPanel);

	}
}
