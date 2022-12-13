package com.neural.jitavej.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.autocomplete.PatientSuggestOracle;
import com.neural.jitavej.client.lab.LabPanel;
import com.neural.jitavej.client.patient.PatientPanel;

public class TopPanel extends DockPanel {
	
	SuggestBox suggestPatient;
	
	Button search;
	
	public static String hn;
	
	//public static JSONObject queue;
	public static LabPanel labPanel = new LabPanel();
	
	public TopPanel() {
		
		setHeight("40");
		
		int width = Integer.parseInt(Jitavej.screen_width) -30;
		
		setWidth(String.valueOf(width));
		
		setBorderWidth(0);

		Image image = Jitavej.images.logo2().createImage();
		
		PatientSuggestOracle oracle = new PatientSuggestOracle();
		
        suggestPatient = new SuggestBox(oracle);
        
        suggestPatient.setStyleName("input-search-patient");
        
        suggestPatient.setLimit(5);
        
        suggestPatient.setWidth("240");
        
        suggestPatient.setFocus(true);
        
        String placeholder = " HN " + Jitavej.CONSTANTS.nav_firstname() + " " + Jitavej.CONSTANTS.nav_lastname();
        
        suggestPatient.getElement().setPropertyString("placeholder", placeholder);
        
        suggestPatient.addKeyboardListener(new KeyboardListenerAdapter(){
    		@Override
    		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    
    			String hn = suggestPatient.getText().trim();
    		        
    			if (keyCode == (char) KEY_ENTER && !hn.equals("")) {
    							
    				if(hn.length() > 1){}
    	        }
    			
    			super.onKeyPress(sender, keyCode, modifiers);
			}
			
        });
        
        Button search = new Button(Jitavej.CONSTANTS.search_patient());
				
		search.setWidth("90");
		
		search.addClickListener(new ClickListener(){
			
			public void onClick(Widget sender) {
				
				try {	
					
					Jitavej.clear();
					
					GWT.log("SearchFindByHn ", null);
					
					hn = suggestPatient.getText().trim();
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/patient/findByHn");
					
					rb.setTimeoutMillis(30000);	
					
					StringBuffer params = new StringBuffer();
					
					params.append("hn=" + URL.encodeComponent(hn));
					
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log("Error response received during authentication of user", exception);
						}
						
						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								GWT.log("SearchFindByHn >> " + response.getText(), null);
								
								JSONObject patient = JSONParser.parse(response.getText()).isObject();
								
								if(!Jitavej.mode.equals("lab")) {
						 			
									Jitavej.servicePatient(patient);
									
									//Window.alert(Jitavej.visit.toString());
									
									if (Jitavej.visit != null){//Jitavej.mode.equals("backmed")
										
										labPanel.labOrderPanel.save.setEnabled(true);
									}
									else {
										
										labPanel.labOrderPanel.save.setEnabled(false);
									}
						 		}
								else {
									
									getqueueId(patient);
								}
								
								
								//Jitavej.servicePatient(patient);
								
								suggestPatient.setText(null);
								
								//JSONObject queue = JSONParser.parse("{id:1294042}").isObject();
								
								//Jitavej.serviceQueue(queue);
							}
						}
					});
				} 
				catch (RequestException e1) {
					
					GWT.log("SearchFindByHn", e1);
				}	
			}
		});

		HorizontalPanel hor = new HorizontalPanel();
		
		hor.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hor.add(suggestPatient);
		hor.add(new HTML("&nbsp;"));
		hor.add(search);

		VerticalPanel ver = new VerticalPanel();
		
		ver.add(hor);
		
		add(image, DockPanel.CENTER);
		
		if(Jitavej.mode.equals("lab") || Jitavej.mode.equals("backmed") || Jitavej.mode.equals("dental") || Jitavej.mode.equals("med")) {
			
			add(ver, DockPanel.EAST);  
		}
		
	}
	
	public static void getqueueId(final JSONObject patient){
    	
		try {
			
			//Window.alert(URL.encodeComponent(hn));

			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server_name + "/ci_kios/index.php/jitavej/index");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			
			params.append("hn=" + URL.encodeComponent(hn));
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("getqueueId >> " + response.getText(), null);
						
						JSONObject queue = JSONParser.parse(response.getText()).isObject();
						
						//Window.alert(queue.get("id").toString());
						
						if (queue.get("id").toString() == "null") {
						
							Jitavej.servicePatient(patient);
						}
						else {
							
							Jitavej.serviceQueue(queue);
						}
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e1) {
			
			e1.printStackTrace();
		}    	
    }
}
