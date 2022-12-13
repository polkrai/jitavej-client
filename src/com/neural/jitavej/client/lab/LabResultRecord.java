package com.neural.jitavej.client.lab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.Panel;  
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class LabResultRecord extends Panel {
	
	public static Store store;
	
	public JSONArray records = new JSONArray();
	
    public LabResultRecord() {
    	
        setBorder(false);
        
        setPaddings(4);  
  
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
            new StringFieldDef("item", "item"), 	
            new StringFieldDef("fee", "price"), 	
		});
  
        GridPanel grid = new GridPanel();  
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        MemoryProxy proxy = new MemoryProxy(itemList);  
  
        JsonReader reader = new JsonReader(recordDef);
        store = new Store(proxy, reader);  
        store.load();  
        grid.setStore(store);
        
        BaseColumnConfig[] columns = new BaseColumnConfig[]{  
			new ColumnConfig(Jitavej.CONSTANTS.record_item(), "item", 240, true),
			new ColumnConfig(Jitavej.CONSTANTS.record_fee(), "fee", 100, true),
        };  
  
        ColumnModel columnModel = new ColumnModel(columns);  
        grid.setColumnModel(columnModel);  
        grid.setFrame(true);  
        grid.setStripeRows(true);  
        grid.setWidth(440);
        grid.setHeight(515);
        grid.setTitle(Jitavej.CONSTANTS.lab_item());
        
        grid.addGridCellListener(new GridCellListenerAdapter() {  
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject record = records.get(rowIndex).isObject();
            	
				LabResultValueDialog dialog = new LabResultValueDialog(Integer.parseInt(record.get("id").isNumber().toString()));
				
				dialog.show();
				
				dialog.center();
				
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
        add(grid);  

    }  

    public void load(JSONObject order){
    	
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/lab/records");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			
			params.append("order_id=" + order.get("id").isNumber().toString());
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("pdate records" + response.getText(), null);
						
						records = JSONParser.parse(response.getText()).isArray();
						
						store.removeAll();
						
						store.loadJsonData(records.toString(), true);
						
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