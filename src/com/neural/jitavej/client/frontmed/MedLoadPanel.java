package com.neural.jitavej.client.frontmed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.NavPanel;
import com.neural.jitavej.client.patient.PatientPanel;
import com.neural.jitavej.client.util.DateRenderer;

public class MedLoadPanel extends VerticalPanel {
	
	public String data1, data2;
	Timer timer;
	public Store store, store2;
	public JSONObject station;
	public Button send;
	public HTML label;
	
	public MedLoadPanel(JSONObject station0) {
		
		this.station = station0;
		setBorderWidth(1);
		
		timer = new Timer() {
			public void run() {
				update();
			}
		};
		
		timer.scheduleRepeating(30000);
		
		send = new Button(Jitavej.CONSTANTS.send(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
		    	try {
		    		
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/q/changeComponent");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					
					StringBuffer params = new StringBuffer();
					params.append("queue_id=" + URL.encodeComponent(Jitavej.queue.get("id").isNumber().toString()));
					params.append("&");
					params.append("component_id=10");
					params.append("&");
					params.append("station_id="+station.get("id").isNumber().toString());
					params.append("&");
					params.append("action_id=1");					
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								NavPanel.update();
								Jitavej.patient = null;
								PatientPanel.setPatient(null);
								for(MedLoadPanel medLoadPanel : MedLoadTab.list){
									medLoadPanel.update();
								}
								
							} else {
								Window.alert("response.getStatusCode() "+response.getStatusCode());
								Window.alert("response.getText() "+response.getText());
							}
						}
					});

				} 
		    	catch (RequestException e1) {
					Window.alert(e1.toString());
				} 		
			}
		});
		
		label = new HTML(station.get("name").isString().stringValue());
		add(label);
		add(getTable1());
		add(send);
		
		update();

	}

	public Panel getTable1() {  
        Panel panel = new Panel();  
        panel.setBorder(false);  
        panel.setPaddings(0);  
  
        final RecordDef recordDef = new RecordDef(new FieldDef[]{  
                new StringFieldDef("firstname", "firstname"),  
                new StringFieldDef("lastname", "lastname"),  
                new StringFieldDef("time", "time")
        });
        
        JsonReader reader = new JsonReader(recordDef);  
  //      reader.setRoot("data");  
  //      reader.setTotalProperty("totalCount");  
  
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
        
        store = new Store(dataProxy,reader); 
        store.removeAll();


        //setup column model  
        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{  
                new ColumnConfig(Jitavej.CONSTANTS.nav_firstname(), "firstname", 60, true),  
                new ColumnConfig(Jitavej.CONSTANTS.nav_lastname(), "lastname", 70, true), 
                new ColumnConfig(Jitavej.CONSTANTS.nav_time(), "time", 40, true)
        });  
  
        
        GridPanel grid = new GridPanel();  
    //    grid.setTitle("Local Json Grid");  
        grid.setStore(store);  
        grid.setColumnModel(columnModel);  
        grid.setFrame(false); 
        grid.setBorder(false);
        grid.setWidth(175);  
        grid.setHeight(220);  
        grid.stripeRows(true);  
        grid.setIconCls("grid-icon");  
        grid.setTrackMouseOver(false);  

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	JSONValue list = JSONParser.parse(data1);
            	JSONObject queue = list.isArray().get(rowIndex).isObject();
            	Jitavej.serviceQueue(queue);
            
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
        GridView view = new GridView();  
        view.setEmptyText("No visit here.");  
        grid.setView(view);  
        store.load();
      
        panel.add(grid);  

        return grid; 
    }  
	
	public void update(){
		try {		
		//	Window.alert("update");
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server+"/q/loadStation");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("station_id=" + station.get("id").isNumber().toString());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log("XXXXXXXX >> " +response.getText(), null);
						data1 = response.getText();
						store.removeAll();
						store.loadJsonData(data1, true);  							
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
			
			
			rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+"/q/stationcount");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			params = new StringBuffer();
			params.append("station_id=" + station.get("id").isNumber().toString());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						label.setHTML(station.get("name").isString().stringValue() + " &nbsp;&nbsp;&nbsp;<FONT color='red'><b>(" +response.getText()+ ")</b></FONT>");
					} else {
					//	Window.alert("response.getText() "+response.getText());
					}
				}
			});			
			
		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}			
	}

}
