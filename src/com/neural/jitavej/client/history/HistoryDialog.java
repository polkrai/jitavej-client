package com.neural.jitavej.client.history;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class HistoryDialog extends FormDialog {

	public static HistoryPanel historyPanel;
	
	ScrollPanel scrollPanel;
	
	public HistoryDialog() {
		
		setHTML("<b>" + Jitavej.CONSTANTS.history() + "</b>");
		
		setPixelSize(850, 620);
		
		historyPanel = new HistoryPanel();
		
		scrollPanel = new ScrollPanel(historyPanel);
		
		scrollPanel.setWidth("850");
		
		historyPanel.addTabListener(new TabListener() {

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				
				if(tabIndex >= 3){
					
					setPixelSize(1240, 600);
					historyPanel.setPixelSize(1220, 600);
					scrollPanel.setWidth("1220");
					center();
				}
				else {
					
					setPixelSize(850, 540);
					historyPanel.setPixelSize(850, 540);
					scrollPanel.setWidth("850");
					center();
				}
			}

			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		/*
		HorizontalPanel hor = new HorizontalPanel();
		//setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		hor.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		//hor.add(new HTML("<div style='width:80%;'></div>"));
		hor.add(cancel);
		
		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		ver.add(hor);
		ver.add(scrollPanel);
				
		setWidget(ver);
		*/

		VerticalPanel ver = new VerticalPanel();
		
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(new HTML("<p>"));
		ver.add(cancel);
		
		ver.add(new HTML("</p>"));
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		ver.add(scrollPanel);
				
		setWidget(ver);
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/visit/ipd");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			
			params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						//orders = JSONParser.parse(response.getText()).isArray();	
					} 
					else {
						
						Window.alert("response.getText() " +response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			e1.printStackTrace();
		}
		

	}
}
