package com.neural.jitavej.client.history;
  
import java.util.Date;

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
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class DrugItemHistoryPanel extends VerticalPanel {
	
	public static JSONArray icd10s;	
	public static JSONArray records;
	public static Store store1;
	public static Store store2;
	public static JSONObject order;
	public static HTML doctor = new HTML("");
	public static HTML text = new HTML("");
	public static HTML remed = new HTML("");
	public static Button scan;
	
	public DrugItemHistoryPanel() {
		
		add(doctor);
		
		scan = new Button(Jitavej.CONSTANTS.view_opd_scan(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				OPDCardDialog dialog = new OPDCardDialog(order.get("opdcard").isString().stringValue());
				
				dialog.show();
				
				dialog.center();
			}
		});
		
		scan.setEnabled(false);
		
		add(new HTML(Jitavej.CONSTANTS.record_icd10() + " :"));
		
		add(getOrderIcd10());
		
		add(new HTML(Jitavej.CONSTANTS.record_item() + " :"));
		
		add(getOrderItem());
		
		DecoratorPanel panel = new DecoratorPanel();
		
		add(remed);
		
		panel.add(text);
		
		add(panel);

		add(scan);
	}

	public Widget getOrderIcd10(){
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("priority", "priority"), 
			new StringFieldDef("code", "code"), 
			new StringFieldDef("name", "name")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
              
        store1 = new Store(dataProxy,reader);

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
			new ColumnConfig("Priority", "priority", 60, true), 
			new ColumnConfig("Icd-10", "code", 80, true), 
			new ColumnConfig("Name", "name", 360, true)
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
		grid.setHeight(150);
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject order = records.isArray().get(rowIndex).isObject();
            	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
            
        });

		store1.load();
		
		return grid;
	}	
	
	public Widget getOrderItem(){
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("drug", "drug"), 
			new StringFieldDef("dose", "dose"), 
			new StringFieldDef("reason", "reason"), 
			new StringFieldDef("qty", "qty")
		});
		
        JsonReader reader = new JsonReader(recordDef);

        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
              
        store2 = new Store(dataProxy,reader);

		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
				
			new ColumnConfig("Drug", "drug", 200, true), 
			new ColumnConfig("Dose", "dose", 120, true), 
			new ColumnConfig("Reason", "reason", 60, true), 
			new ColumnConfig("Qty", "qty", 50, true)
		});

		GridPanel grid = new GridPanel();

		grid.setStore(store2);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		
		grid.setWidth(520);
		grid.setHeight(250);
		
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject order = records.isArray().get(rowIndex).isObject();
            	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
            
        });

		store2.load();
		
		return grid;
	}

	
    public static void load(JSONObject order0){
    	
		try {
			
			order = order0;
			
			doctor.setHTML("<b>" + order.get("usergroup").isString().stringValue() + order.get("doctor").isString().stringValue() + "</b>");
			
			text.setHTML(Jitavej.CONSTANTS.tab_text() + " : " + order.get("text").isString().stringValue());
			
			if(order.get("remed") != null){
				
				Date date = new Date(Long.parseLong(order.get("remed").isNumber().toString()));
				
				Date now = new Date();
				
				int month = (int)((date.getTime() - now.getTime())/1000/60/60/24/28);
				
				remed.setHTML(Jitavej.CONSTANTS.remed() +" "+ month +" "+ Jitavej.CONSTANTS.month());
			}
			else{
				
				remed.setHTML(Jitavej.CONSTANTS.record_not_remed());
			}
			
			if(!order.get("opdcard").toString().equals("null")){
				
				scan.setEnabled(true);
			}
			else{
				
				scan.setEnabled(false);
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/icd10s");
			
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
						
						icd10s = JSONParser.parse(response.getText()).isArray();
						
						store1.removeAll();
						
						store1.loadJsonData(icd10s.toString(), true);
					} 
					
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
			
			rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/records");
			
			rb.setTimeoutMillis(30000);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			params = new StringBuffer();
			
			params.append("order_id=" + order.get("id").isNumber().toString());
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						records = JSONParser.parse(response.getText()).isArray();
						
						store2.removeAll();
						
						store2.loadJsonData(records.toString(), true);
						
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
