package com.neural.jitavej.client.history;
  
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.PsychoResultDialog;

public class PsychoItemHistoryPanel extends VerticalPanel {  
	public static JSONArray psychos;	
	public static Store store1;
	public static JSONObject order;
	public static HTML doctor = new HTML("");
	
	public PsychoItemHistoryPanel() {
		add(doctor);
		add(new HTML("Item:"));
		add(getOrderItem());
	
	}

	public Widget getOrderItem(){
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
			new StringFieldDef("item", "item")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
              
        store1 = new Store(dataProxy,reader);

		// setup column model
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { 
			new ColumnConfig(Jitavej.CONSTANTS.old_item(), "item", 500, true)
		});

		GridPanel grid = new GridPanel();
		// grid.setTitle("Local Json Grid");
		grid.setStore(store1);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(520);
		grid.setHeight(400);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	//JSONObject order = psychos.isArray().get(rowIndex).isObject();
				PsychoResultDialog dialog = new PsychoResultDialog(order.get("id").isNumber().toString());
				dialog.show();
				dialog.center();	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
            
        });

		store1.load();
		
		return grid;
	}	

    public static void load(JSONObject order0){
    	
		try {
			//Window.alert(""+order.get("id").isNumber().toString());
			
			order = order0;
			
			doctor.setHTML("<b>" + order.get("usergroup").isString().stringValue() + order.get("doctor").isString().stringValue() + "</b>");
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/records");
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
						
						GWT.log(response.getText(), null);
						
						psychos = JSONParser.parse(response.getText()).isArray();
						
						store1.removeAll();
						
						store1.loadJsonData(psychos.toString(), true);
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

}
