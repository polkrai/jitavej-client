package com.neural.jitavej.client.history;
  
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;
public class PsychoHistoryPanel extends HorizontalPanel {
	
	public static JSONArray orders;
	public static Store store;
	PsychoItemHistoryPanel psychoItemHistoryPanel = new PsychoItemHistoryPanel();
	
	public PsychoHistoryPanel() {

		setSpacing(8);
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
			new StringFieldDef("time", "time"),
			new StringFieldDef("vn", "vn")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
      
        store = new Store(dataProxy,reader);

		// setup column model
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { 
			new ColumnConfig("VN", "vn", 110, true),
			new ColumnConfig(Jitavej.CONSTANTS.label_labdate(), "time", 170, true),
		});

		GridPanel grid = new GridPanel();
		// grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(300);
		grid.setHeight(400);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject order = orders.isArray().get(rowIndex).isObject();
            	
            	GWT.log(order.toString(), null);
            	
            	PsychoItemHistoryPanel.load(order);
            	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
		store.load();

		add(grid);
		
		add(psychoItemHistoryPanel);
		
		update();

	}

	public static void update() {
		
		if(Jitavej.patient == null){
			
			return;
		}
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/order/history");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
			params.append("&type=psycho");
			//params.append("type=psycho");
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						orders = JSONParser.parse(response.getText()).isArray();
						//Window.alert(""+Jitavej.patient);
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
