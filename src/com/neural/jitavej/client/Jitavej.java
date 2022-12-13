package com.neural.jitavej.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.neural.jitavej.client.appointment.AppointmentPanel;
import com.neural.jitavej.client.history.HistoryPanel;
import com.neural.jitavej.client.history.LabHistoryPanel;
import com.neural.jitavej.client.lab.LabPanel;
import com.neural.jitavej.client.lab.LabResultPanel;
import com.neural.jitavej.client.patient.PatientPanel;
import com.neural.jitavej.client.record.RecordConPanel;
import com.neural.jitavej.client.record.RecordDrugPanel;
import com.neural.jitavej.client.record.RecordIcd10Panel;
import com.neural.jitavej.client.record.RecordTextPanel;
import com.neural.jitavej.client.refer.ReferReplyPanel;
import com.neural.jitavej.client.refer.ReferSendPanel;

public class Jitavej implements EntryPoint {
	
	public static DockPanel centerPanel;
	public static final JitavejImages images = (JitavejImages) GWT.create(JitavejImages.class);
	public static final JitavejConstants CONSTANTS = (JitavejConstants) GWT.create(JitavejConstants.class);
	public static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm"); 
	public static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy"); 	
	public static JSONObject queue, visit, patient, session, station, address, dn, building, queuereturn, component;	
	public static JSONArray components, stations, interactions, users;
	public static String server, server_name, mode, h4u_server, token, screen_width, screen_height, server_local;
	
	public void onModuleLoad() {
		
		Dictionary configure = Dictionary.getDictionary("configure");
		
		server = configure.get("server");
		server_name = configure.get("server_name");
		screen_width = configure.get("screen_width");
		screen_height = configure.get("screen_height");
		h4u_server = configure.get("q4u_server");
		token = configure.get("q4u_token");
		server_local = configure.get("server_local");

		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/mode");
			
			rb.setTimeoutMillis(30000);
			
			rb.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						mode = JSONParser.parse(response.getText()).isObject().get("mode").isString().stringValue();

						if(mode.equals("appointment")){
							
							loadBuilding();	
						}
						else if(mode.equals("refersend")){
							
							loadUsers();
						}
						else if(mode.equals("referreply")){
							
							loadUsers();
						}
						else{
							
							loadComponents();
						}
						
						//h4ujitavej.Requestqueue();
					} 
					else {
												
						Window.alert("Response URL =" + Jitavej.server + "/user/mode");
						Window.alert("/user/mode response.getStatusCode() = " + response.getStatusCode());
						Window.alert("/user/mode response.getText() = " + response.getText());
					}
				}
			});
			
		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}
	}
	
	public static void init() {
		
		DockPanel all = new DockPanel();
		all.setHeight("100%"); 
		all.setWidth("100%");  
		all.setSpacing(4);
		
		RootPanel.get().setStyleName("full");
		
		TopPanel topPanel = new TopPanel();
		Jitavej.centerPanel = new DockPanel();
				
		NavPanel navPanel = new NavPanel();

		all.add(topPanel, DockPanel.NORTH);
		all.add(Jitavej.centerPanel, DockPanel.CENTER);
		all.add(navPanel, DockPanel.WEST);
		
		PatientPanel patientPanel = new PatientPanel();
		 
		show(patientPanel);
		
		RootPanel.get().add(all);
	}
	
	public static void record() {
		
		RootPanel.get().setStyleName("iframe");
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/user/visit");
			
			rb.setTimeoutMillis(30000);
			
			rb.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						visit = value.isObject();
						
						patient = visit.get("patient").isObject();
						
						//Window.alert(visit.toString());
						//Window.alert(patient.toString());
						
						RecordIcd10Panel recordIcd10Panel = new RecordIcd10Panel();
						
						recordIcd10Panel.setVisible(true);
						
						RecordTextPanel recordTextPanel = new RecordTextPanel();
						
						recordTextPanel.setVisible(true);
						
						GWT.log("mode => " + Jitavej.mode, null);
						
						if(Jitavej.mode.equals("record_oneday")){
							
							RecordDrugPanel recordDrugPanel = new RecordDrugPanel("oneday");
							recordDrugPanel.setVisible(true);
							recordDrugPanel.remedBox.setVisible(false);
							
							RecordIcd10Panel.setPatient(Jitavej.patient);
							
							DecoratedTabPanel tabPanel = new DecoratedTabPanel();
							tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
							tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record() + "(One Day)");
							tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
							
							tabPanel.selectTab(1);
							
							RootPanel.get().add(tabPanel);							
						}
						else if (Jitavej.mode.equals("record_unitdose")) {
							
							RecordDrugPanel recordDrugPanel = new RecordDrugPanel("unitdose");
							recordDrugPanel.setVisible(true);
							recordDrugPanel.remedBox.setVisible(false);
							
							RecordIcd10Panel.setPatient(Jitavej.patient);
							
							recordDrugPanel.remedunit();
							
							DecoratedTabPanel tabPanel = new DecoratedTabPanel();
							
							tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
							tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record() + "(One Day Dose)");
							tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
							
							tabPanel.selectTab(1);
							
							RootPanel.get().add(tabPanel);
							
						}
						else if(Jitavej.mode.equals("record_continuation")){
							
							//	final RecordDrugPanel confirmDrugPanel = new RecordDrugPanel("confirm");
							//	confirmDrugPanel.setVisible(true);
							//	confirmDrugPanel.addBar.setVisible(false);
							//	confirmDrugPanel.remed.setVisible(false);
							//	confirmDrugPanel.clear.setVisible(false);
							//	confirmDrugPanel.remedBox.setVisible(false);
							//	confirmDrugPanel.remedconfirm();
							
							final RecordConPanel recordConPanel = new RecordConPanel("continuation");
							
							recordConPanel.setVisible(true);
							//	recordConPanel.remedBox.setVisible(false);
							recordConPanel.remedcon();
								
							RecordIcd10Panel.setPatient(Jitavej.patient);
							
							DecoratedTabPanel tabPanel = new DecoratedTabPanel();
							
							tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
							
							//tabPanel.add(confirmDrugPanel, Jitavej.CONSTANTS.record_conconfirm()+"(Confirm)");
							tabPanel.add(recordConPanel, Jitavej.CONSTANTS.tab_record()+"(Continuation)");
							tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
							
							tabPanel.selectTab(1);
							
							tabPanel.addTabListener(new TabListener(){

								public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
									// TODO Auto-generated method stub
									return true;
								}

								public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
									//	recordConPanel.remedcon();
									//	confirmDrugPanel.remedconfirm();
									//	RecordIcd10Panel.setPatient(Jitavej.patient);
								}
								
							});							
							
							RootPanel.get().add(tabPanel);
						}
						else if(Jitavej.mode.equals("record_homemed")){
							
							RecordDrugPanel recordDrugPanel = new RecordDrugPanel("homemed");
							recordDrugPanel.setVisible(true);
							recordDrugPanel.remedBox.setVisible(false);
							recordDrugPanel.remedhomemed();
							
							RecordIcd10Panel.setPatient(Jitavej.patient);
							
							DecoratedTabPanel tabPanel = new DecoratedTabPanel();
							tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
							tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record()+"(Home Med)");
							tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
							tabPanel.selectTab(1);
							RootPanel.get().add(tabPanel);							
						}
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}

	public static void lab_order() {
		
		RootPanel.get().setStyleName("iframe");
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/user/visit");
			
			rb.setTimeoutMillis(30000);
			
			rb.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						visit = value.isObject();
						
						patient = visit.get("patient").isObject();
						
						LabPanel labPanel = new LabPanel();
						
						labPanel.setVisible(true);
						
						LabPanel.update();
						
						//LabPanel.update_laborder_panel();
						
						//LabHistoryPanel.update();
						
						RootPanel.get().add(labPanel);
						
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}
	
	public static void history() {
		
		RootPanel.get().setStyleName("iframe");
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/user/patient");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						//Window.alert("11");
						
						if(response.getText() != null && !response.getText().equals("")){
							
							JSONValue value = JSONParser.parse(response.getText());
							
							patient = value.isObject();							
						}
						
						HistoryPanel historyPanel = new HistoryPanel();
						
						RootPanel.get().add(historyPanel);
						
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}

	public static void appointment() {
		
		RootPanel.get().setStyleName("iframe");
		
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/user/patient");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						if(response.getText() != null && !response.getText().equals("")){
							
							JSONValue value = JSONParser.parse(response.getText());
							
							patient = value.isObject();							
						}

						AppointmentPanel appointmentPanel = new AppointmentPanel();						
						
						appointmentPanel.setVisible(true);
						
						if(patient == null){
							
							appointmentPanel.add.setEnabled(false);
						}
						
						RootPanel.get().add(appointmentPanel);							

					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}

	public static void refersend() {
		
		RootPanel.get().setStyleName("iframe");

		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/user/patient");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						if(response.getText() != null && !response.getText().equals("")){
							
							JSONValue value = JSONParser.parse(response.getText());
							
							patient = value.isObject();							
						}

						ReferSendPanel referSendPanel = new ReferSendPanel();
						referSendPanel.setVisible(true);
						//referSendPanel.add.setEnabled(false);
						
						if(patient == null){
							
							referSendPanel.add.setEnabled(false);
						}
						
						RootPanel.get().add(referSendPanel);							

					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}
	
	public static void referreply() {
		
		RootPanel.get().setStyleName("iframe");

		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/user/patient");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					GWT.log(response.getText(), null);
					if (response.getStatusCode() == 200) {
						if(response.getText() != null && !response.getText().equals("")){
							JSONValue value = JSONParser.parse(response.getText());
							patient = value.isObject();							
						}

						ReferReplyPanel referReplyPanel = new ReferReplyPanel();
						referReplyPanel.setVisible(true);
						//referReplyPanel.add.setEnabled(false);
						if(patient == null){
							
							referReplyPanel.add.setEnabled(false);
						}
						
						RootPanel.get().add(referReplyPanel);							
							

					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}
	
	public static void show(Widget panel){
		centerPanel.clear();
		centerPanel.add(panel, DockPanel.CENTER);
	}

	public static void clear(){
		
		PatientPanel.setPatient(null);
	}
	
	public static void serviceQueue(JSONObject queue0){
		
		try {
			
			GWT.log("serviceQueue ", null);
			
			Jitavej.visit = null;
			
			PatientPanel.setPatient(null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/get2");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("queue_id=" + URL.encodeComponent(queue0.get("id").isNumber().toString()));
	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("json Response =" + response.getText(), null);
						
						queue = JSONParser.parse(response.getText()).isObject();
						
						visit 	= queue.get("visit").isObject();						
						patient = visit.get("patient").isObject();
						station	= queue.get("toStation").isObject();
						
						//GWT.log("json patient =" + patient.get("hn").isNumber().toString(), null);
						//h4ujitavej.Requestqueue();
						
						//h4ujitavej.Updatequeue();
						
						loadAddress();						
					}
				}
			});
		} 
		catch (RequestException e1) {
			
				GWT.log("loadComponents", e1);
		}			
	}

	public static void serviceQueue3(JSONObject queue0){
		
		try {
			
			GWT.log("serviceQueue ", null);
			
			Jitavej.visit = null;
			PatientPanel.setPatient(null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/get3");
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("queue_id=" + URL.encodeComponent(queue0.get("id").isNumber().toString()));
	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("xxx = " + response.getText(), null);
						queue = JSONParser.parse(response.getText()).isObject();
						visit = queue.get("visit").isObject();
						
						patient = visit.get("patient").isObject();
						loadAddress();						
					}
				}
			});
		} 
		catch (RequestException e1) {
			
			GWT.log("loadComponents", e1);
		}			
	}
	
	public static void servicePatient(JSONObject patient){
		
		try {
			
			Jitavej.patient = patient;
			Jitavej.queue = null;
			Jitavej.visit = null;
			loadAddress();
		} 
		catch (Exception e1) {
			
				GWT.log("loadComponents", e1);
		}			
	}
	
	public static void loadBuilding(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/user/building");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						if(response.getText().equals("")){
							
							building = null;
						}
						else{
							
							building = JSONParser.parse(response.getText()).isObject();
						}
						
						//Window.alert(building.toString());
						if(mode.equals("appointment")){
							
							loadComponents();
						}
						else{
							
							record();
						}
						
					} 
					else {
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}
	
	public static void loadComponent(){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/user/component");
			rb.setTimeoutMillis(30000);
			rb.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert("Error response received during authentication of user " + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						//Window.alert(response.getText());
						if(response.getText().equals("")){
							
							component = null;
						}
						else{
							
							component = JSONParser.parse(response.getText()).isObject();
						}
						
							
						if(mode.equals("appointment")){
							
							appointment();	
						}
						else{
							
							try {
								
								Window.setTitle(component.get("name").isString().stringValue());

							}
							catch(Exception ex){}
							
								loadStations();							
							}							
					} 
					else {
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});
		} 
		catch (RequestException e) {
			
			Window.alert(e.toString());
		}
	}

	public static void loadComponents() {
		
		try {
			
			GWT.log("loadComponents ", null);
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/component/list2");
			rb.setTimeoutMillis(30000);	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						JSONValue list = JSONParser.parse(response.getText());
						Jitavej.components = list.isArray();
						loadUsers();
					}
				}
			});
		} 
		catch (RequestException e1) {
			
				GWT.log("loadComponents", e1);
		}			
	} 		
	
	public static void loadStations() {
		
		try {
			
			GWT.log("loadStations ", null);
			//RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/station/findAllByComponent");
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/station/list2");
			rb.setTimeoutMillis(30000);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						JSONValue list = JSONParser.parse(response.getText());
						Jitavej.stations = list.isArray();
						
						loadSession();
					}
				}
			});
		} 
		catch (RequestException e1) {
			
				GWT.log("loadStations", e1);
		}			
	} 
	
	public static void loadInteractions() {  
		
		try {
			
			GWT.log("loadDrugInteractionlist ", null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/drug/interactionlist");
			rb.setTimeoutMillis(30000);	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue list = JSONParser.parse(response.getText());
						
						Jitavej.interactions = list.isArray();
						//Window.alert(Jitavej.interactions.toString());
					}
				}
			});
		} 
		catch (RequestException e1) {
			
			GWT.log("loadDrugInteractionlist", e1);
		}			
	} 		
	
	public static void loadUsers() {
		
		try {	
			
			GWT.log("loadUsers ", null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/list2");
			rb.setTimeoutMillis(30000);	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						
						JSONValue list = JSONParser.parse(response.getText());
						
						Jitavej.users = list.isArray();
						
						if(mode.equals("referresend")){
							
							refersend();	
						}
						else if(mode.equals("refersend")){
							
							refersend();	
						}
						else if(mode.equals("referreply")){
							
							referreply();	
						}
						else{
							
							loadComponent();
						}
						
					}
				}
			});
		} 
		catch (RequestException e1) {
			
				GWT.log("loadUsers", e1);
		}			
	} 					
									
	public static void loadSession() {
		
		try {
			
			GWT.log("loadSession", null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/jsession");
			rb.setTimeoutMillis(30000);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
					Window.alert(exception.getMessage());
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					//	Window.alert(response.getText());

					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						if(response.getText().equals("") && !mode.equals("history")){
							
							Window.alert(Jitavej.CONSTANTS.loginagain());
							
							try {
								
								RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/logout");
								rb.setTimeoutMillis(30000);	
								rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
								
								rb.sendRequest(null, new RequestCallback() {
									
									public void onError(Request request, Throwable exception) {
										
										GWT.log("Error response received during authentication of user", exception);
									}
									
									public void onResponseReceived(Request request, Response response) {
										
										if (response.getStatusCode() == 200) {
											
											Window.Location.replace("/nano/");
										}
										else{
											
											Window.alert("response.getText() "+response.getText());
										}
									}
								});
							} 
							catch (RequestException e1) {
								
									GWT.log("loadSession", e1);
							}
							
							return;
						}
						
						//Window.alert("0000");
						
						JSONValue value = JSONParser.parse(response.getText());
						
						session = value.isObject();
						
						//Window.alert(session.get("sessionId").isString().stringValue());

						if(mode.equals("history")){
						
							//Window.alert("1111");

							history();
							
							return;
						}
						
						if(mode.equals("record_oneday")){
							
							record();	
						}
						else if(mode.equals("record_unitdose")){
							
							record();	
						}
						else if(mode.equals("record_continuation")){
							
							loadBuilding();	
						}
						else if(mode.equals("record_homemed")){
							
							record();	
						}
						else if(mode.equals("lab_order")){
							
							lab_order();	
						}
						else {
							
							init();
						}
					}
				}
			});
		} 	
		catch (Exception e1) {
			
			Window.alert(e1.toString());
			
			GWT.log("", e1);
		}			
	} 	

	public static void loadAddress() {  
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/patient/address2");
			
			rb.setTimeoutMillis(30000);	
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			
			params.append("hn=" + URL.encodeComponent(patient.get("hn").isString().stringValue()));
				
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					GWT.log(response.getText(), null);
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						Jitavej.address = value.isObject();
						
						if(Jitavej.mode.equals("dental")){
							
							loadDn();
						}
						else{
							
							PatientPanel.setPatient(patient);
						}
						
					}
				}
			});
		} 
		catch (Exception e1) {
			
			GWT.log("", e1);
		}			
	} 
	
	public static void loadDn() {  
		try {	
		//	Jitavej.dn = null;
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/dental/dn");
			rb.setTimeoutMillis(30000);	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + URL.encodeComponent(patient.get("id").isNumber().toString()));
				
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					GWT.log(response.getText(), null);
					if (response.getStatusCode() == 200) {
						JSONValue value = JSONParser.parse(response.getText());
						Jitavej.dn = value.isObject();
						PatientPanel.setPatient(patient);
					}
				}
			});
		} catch (Exception e1) {
			GWT.log("", e1);
		}			
	} 
	
    public static void test123(String s, int i) {
    	Window.alert("computeLoanInterest " + s +"/"+i) ;   	
    //	show(new RecordPanel());
    }
    
	public static native void exportStaticMethod() /*-{
	    
    $wnd.test123 = function(s,i) { 
      @com.neural.jitavej.client.Jitavej::test123(Ljava/lang/String;I)(s,i);
    } 
	}-*/;
	
	
	public static void JRequestqueue(){
		
		try {
			
			GWT.log("Requestqueue ", null);

			RequestBuilder request = new RequestBuilder(RequestBuilder.POST, h4u_server + "/api/v1/api/call"); ///api/v1/api/call
	
			request.setTimeoutMillis(30000);
	
			StringBuffer senddata = new StringBuffer();
			
			senddata.append("&");
			
			senddata.append("queue_id="+ URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			senddata.append("hn=" + URL.encodeComponent(Jitavej.patient.get("hn").toString().replace('"', ' ').trim()));
			
			senddata.append("&");
			
			senddata.append("roomId=" + URL.encodeComponent(Jitavej.station.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			//senddata.append("servicePointId=10");
			senddata.append("servicePointId=" + URL.encodeComponent(Jitavej.station.get("id").isNumber().toString()));
			
			senddata.append("&");
			
			senddata.append("token=" + URL.encodeComponent(token.toString()));
			
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");

			request.sendRequest(senddata.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable e) {
	
					GWT.log("Error response received during authentication of user", e);
	
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("json Response =" + response.getText(), null);
						
						Jitavej.queuereturn = JSONParser.parse(response.getText()).isObject();
						
						//Jitavej.queuereturn = data;
						
						//Window.alert(Jitavej.queuereturn.toString());
						
						//if (queuereturn.get("statusCode").isNumber().toString() == "500"){
							
							//Window.alert(queuereturn.get("message").toString());
						//}
						
						//GWT.log("json Response =" + queuedatareturn.get("queueId"), null);
						//System.out.print(response.getText());
					}
					else if (response.getStatusCode() == 500) {
						
						Jitavej.queuereturn = JSONParser.parse(response.getText()).isObject();
						
						Window.alert(queuereturn.get("message").toString().replace('"', ' ').trim());
					}
				}
			
			});
		}
		catch (RequestException e) {
			
			Window.alert(h4u_server);
			Window.alert(e.getMessage());

		}
	}
	
	public static void JUpdatequeue(){
		
		try {
			
			//Window.alert(Jitavej.queuereturn.get("queueId").toString());
			
			GWT.log("Updatequeue ", null);
		
			RequestBuilder requestupdate = new RequestBuilder(RequestBuilder.POST, h4u_server + "/api/v1/api/pending");
	
			requestupdate.setTimeoutMillis(30000);
			
			StringBuffer updatedata = new StringBuffer();
			
			updatedata.append("&");
			
			updatedata.append("queueId="+ URL.encodeComponent(Jitavej.queuereturn.get("queueId").toString()));
			
			updatedata.append("&");
			
			updatedata.append("priorityId=" + URL.encodeComponent(Jitavej.queuereturn.get("priorityId").toString()));
			
			updatedata.append("&");
			
			updatedata.append("servicePointId=" + URL.encodeComponent("50"));
			
			updatedata.append("&");
			
			updatedata.append("token=" + URL.encodeComponent(token.toString()));
			
			requestupdate.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			//Window.alert(updatedata.toString());
			
			requestupdate.sendRequest(updatedata.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable e) {
	
					GWT.log("Error RequestBuilder Updatequeue ", e);
					
					Window.alert(e.getMessage());
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("json Update =" + response.getText(), null);
						
						//Window.alert("JUpdatequeue = " + response.getText());
						//System.out.print(response.getText());
					}
				}
			
			});
		}
		catch (RequestException e) {
			
			Window.alert(h4u_server);
			Window.alert(e.getMessage());

		}
	}

}
