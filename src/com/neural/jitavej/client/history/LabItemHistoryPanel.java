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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class LabItemHistoryPanel extends VerticalPanel {
	
	public static JSONArray records;	
	public static JSONArray recordvalues;
	public static Store store1;
	public static Store store2;
	public static JSONObject order;
	public static HTML doctor = new HTML("");
	public static Button ButtonlabScan;
	
	public LabItemHistoryPanel() {
		
		add(doctor);
		/*
		ButtonlabScan = new Button(Jitavej.CONSTANTS.view_lab_scan(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				LabScanDialog labScanDialog = new LabScanDialog(order.get("labscan").isString().stringValue());
				
				labScanDialog.show();
				
				labScanDialog.center();
			}
		});
		
		ButtonlabScan.setEnabled(false);
		*/
		add(new HTML(Jitavej.CONSTANTS.lab_item() + " :"));
		
		add(getOrderIcd10());
		
		add(new HTML(Jitavej.CONSTANTS.lab_record() + " :"));
		
		add(getOrderItem());
		
		/*
		DecoratorPanel panel = new DecoratorPanel();
		
		add(panel);
		
		add(ButtonlabScan);
		*/
	}

	public Widget getOrderIcd10(){
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("item", "item")
		});
		
        JsonReader reader = new JsonReader(recordDef);

        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
  
        store1 = new Store(dataProxy,reader);

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				
			new ColumnConfig(Jitavej.CONSTANTS.label_lab(), "item", 400, true)
		});

		GridPanel grid = new GridPanel();
		
		grid.setStore(store1);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(520);
		grid.setHeight(170);
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject record = records.isArray().get(rowIndex).isObject();
            	
            	loadrecordvalues(record);
            	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
            
        });

		store1.load();
		
		return grid;
	}	
	
	public Widget getOrderItem(){
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("id", "id"), 
			new StringFieldDef("item", "name"), 
			new StringFieldDef("standard", "standard"), 
			new StringFieldDef("result", "result"),
			new StringFieldDef("ckdstagas", "ckdstagas"),
			new StringFieldDef("postfix", "postfix")
		});

		EditorGridPanel grid = new EditorGridPanel();
		
		String[][] itemList = new String[][] {{"", ""}, {"", ""}};

		MemoryProxy proxy = new MemoryProxy(itemList);

		JsonReader reader = new JsonReader(recordDef);
		
		store2 = new Store(proxy, reader);
		
		store2.load();
		
		grid.setStore(store2);

		ColumnConfig commonCol = new ColumnConfig("Result", "result", 80, true, null, "result");
		
		ColumnConfig ckdstagas = new ColumnConfig("CKDStagas", "ckdstagas", 80, true, null, "ckdstagas");
		
		commonCol.setEditor(new GridEditor(new TextField()));

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				
			new ColumnConfig("id", "id", 50, true), 
			new ColumnConfig("Item", "item", 140, true), 
			new ColumnConfig("Reference", "standard", 80, true), 
			commonCol,
			ckdstagas,
			new ColumnConfig("Unit", "postfix", 80, true)
		};

		ColumnModel columnModel = new ColumnModel(columns);
		
		grid.setColumnModel(columnModel);

		grid.setFrame(false);
		grid.setStripeRows(true);
		grid.setClicksToEdit(1);
		grid.setWidth(520);
		grid.setHeight(230);
		
		//grid.setTitle(Jitavej.CONSTANTS.lab_item());

		store2.load();
		
		return grid;
	}

	
    public static void load(JSONObject order0){
    	
		try {
			
			order = order0;
			
			doctor.setHTML("<b>" + order.get("usergroup").isString().stringValue() + order.get("doctor").isString().stringValue() +"</b>");
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/order/records");
			
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
						
						store2.removeAll();
						
						records = JSONParser.parse(response.getText()).isArray();
						
						store1.removeAll();
						
						store1.loadJsonData(records.toString(), true);
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

	public void loadrecordvalues(JSONObject record) {
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/lab/results");
			
			rb.setTimeoutMillis(30000);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			StringBuffer params = new StringBuffer();
			
			params.append("record_id=" + record.get("id").isNumber().toString());
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						recordvalues = JSONParser.parse(response.getText()).isArray();
						
						store2.removeAll();
						
						store2.loadJsonData(recordvalues.toString(), true);
					} 
					else {
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			e1.printStackTrace();
		}
	}
}
