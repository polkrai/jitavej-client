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
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class LabValue extends DockPanel {  
	public static JSONArray values;
	public static Store store;

	public LabValue() {

		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
				
				new StringFieldDef("name", "name"), 
				new StringFieldDef("type", "type"), 
				new StringFieldDef("standard", "standard"),
				new StringFieldDef("postfix", "postfix"),
				new StringFieldDef("chioce", "chioce")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
        
              String[][] itemList = new String[][]{{"", ""},{"", ""}};

              DataProxy dataProxy = new MemoryProxy(itemList);
              
              store = new Store(dataProxy,reader);

		// setup column model
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { 
				new ColumnConfig("name", "name", 200, true), 
				new ColumnConfig("type", "type", 60, true), 
				new ColumnConfig("standard", "standard", 100, true), 
				new ColumnConfig("Unit", "postfix", 100, true), 				
				new ColumnConfig("chioce", "chioce", 100, true)});

		GridPanel grid = new GridPanel();
		// grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(970);
		grid.setHeight(480);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	JSONObject order = values.isArray().get(rowIndex).isObject();
            	LabResultPanel.labResultRecord.load(order);
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
        
        
        
		store.load();

		add(grid, DockPanel.CENTER);

		update();

	}

	public static void update() {
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/lab/valuelist");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();

			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						values = JSONParser.parse(response.getText()).isArray();
						store.removeAll();
						store.loadJsonData(values.toString(), true);
					} else {
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
