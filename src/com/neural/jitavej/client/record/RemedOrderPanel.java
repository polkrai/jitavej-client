package com.neural.jitavej.client.record;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class RemedOrderPanel extends DockPanel {
	
	RecordDrugPanel recordDrugPanel;
	public static JSONArray orders;
	public static Store store;

	public RemedOrderPanel(RecordDrugPanel recordDrugPanel0) {

		this.recordDrugPanel = recordDrugPanel0;
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
            new StringFieldDef("time1", "time1"), 
            new StringFieldDef("time2", "time2"), 
            new StringFieldDef("doctor", "doctor"), 
            new StringFieldDef("vn", "vn"), 
            new StringFieldDef("type", "type"), 
            new StringFieldDef("order", "order")});
		
        JsonReader reader = new JsonReader(recordDef);  

        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
      
        store = new Store(dataProxy,reader);

		// setup column model
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { 
            new ColumnConfig("StartDate", "time1", 100, true), 
            new ColumnConfig("EndDate", "time2", 100, true), 
            new ColumnConfig("Doctor", "doctor", 160, true), 
            new ColumnConfig("VN", "vn", 100, true), 
            new ColumnConfig("Type", "type", 100, true), 
            new ColumnConfig("Order", "order", 100, true)});

		GridPanel grid = new GridPanel();
		//grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(920);
		grid.setHeight(100);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
             	JSONObject order = orders.get(rowIndex).isObject();
             	GWT.log(""+order, null);
            	recordDrugPanel.reorder((int)order.get("id").isNumber().doubleValue());
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
		store.load();

		add(grid, DockPanel.CENTER);

	}

	public void update() {
		try {
			
			if(Jitavej.patient == null){
				orders = new JSONArray();
				store.removeAll();
				store.loadJsonData(orders.toString(), true);
				recordDrugPanel.setPatient(null);
				return;
			}
			
			recordDrugPanel.orderItems = null;
			recordDrugPanel.store.removeAll();
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/order/remed");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
	
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						orders = JSONParser.parse(response.getText()).isArray();
						store.removeAll();
						store.loadJsonData(orders.toString(), true);
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			e1.printStackTrace();
		}
	}

	public void onClick(Widget sender) {
		update();
	}
}
