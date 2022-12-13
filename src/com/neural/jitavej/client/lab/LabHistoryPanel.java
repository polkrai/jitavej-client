package com.neural.jitavej.client.lab;
  
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
import com.gwtext.client.data.Record;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class LabHistoryPanel extends HorizontalPanel {
	
	public static JSONArray orders = new JSONArray();
	public static Store store;
	public static String print_img = "print.png";

	public LabHistoryPanel() {

		setSpacing(8);
		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
			//new StringFieldDef("id", "id"),
			new StringFieldDef("time", "time")
		});
		
        JsonReader reader = new JsonReader(recordDef); 
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
      
        store = new Store(dataProxy, reader);
        
        ColumnConfig colPrint = new ColumnConfig(Jitavej.CONSTANTS.print_lab(), "time", 40, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
        		
        		if (orders.size() > 0  && orders.get(rowIndex).isObject().get("is_approve").toString().equals("true")) {
        			
        			return Format.format("<img class=\"icon-center\" src=\"images/print.png\" />", "");
        		}
        		else {
        		
        			return Format.format("<img class=\"icon-center\" src=\"images/no-print-16x16.png\" />", "");
        		}
        	}
        });

        ColumnConfig colType = new ColumnConfig(Jitavej.CONSTANTS.delete_history_lab(), "time", 30, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                
        		return Format.format("<img class=\"icon-pointer-center\" src=\"images/cross.gif\" />", "");
            }
        });
        
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] {
		    new ColumnConfig(Jitavej.CONSTANTS.history_lab(), "time", 120, true),
		    colPrint,
		    colType
		});

		GridPanel grid = new GridPanel();
		grid.setStore(store);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(220);
		grid.setHeight(320);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            
            @Override
            public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
            	
            	if(colindex == 1){
            		
            		if(orders.get(rowIndex).isObject().get("is_approve").toString().equals("true")){
						
            			Window.open(Jitavej.server_name + "/miracle/print_lab/print_lab_er.php?order_id=" + URL.encodeComponent(orders.get(rowIndex).isObject().get("id").isNumber().toString()), "_blank", "toolbar=yes,menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=1280,width=1024");
					}
            		else {
            			
            			MessageBox.setMinWidth(300);
            			MessageBox.alert("แจ้งเตือน", "รายการนี้ยังไม่ได้ยืนยันผลตรวจ");
            			
            			return;
            		}
	
            	}
				else if (colindex == 2) {
            			
					MessageBox.confirm("ยืนยัน", Jitavej.CONSTANTS.confirm_delete(), new MessageBox.ConfirmCallback() {
						
						public void execute(String btnID) {
							
							if(btnID.equals("no")){
								
								return;
							}
							try {
								
								RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/order/delete");
								
								rb.setTimeoutMillis(30000);
								
								rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
								
								StringBuffer params = new StringBuffer();
								
								params.append("order_id=" + URL.encodeComponent(orders.get(rowIndex).isObject().get("id").isNumber().toString()));
								
								rb.sendRequest(params.toString(), new RequestCallback() {
									
									public void onError(Request request, Throwable exception) {
										
										GWT.log(Jitavej.CONSTANTS.error_response(), exception);
									}

									public void onResponseReceived(Request request, Response response) {
										
										if (response.getStatusCode() == 200) {
											
											GWT.log(response.getText(), null);
											
											update();
										}
									}
								});

							} 
							catch (RequestException e1) {
								
								e1.printStackTrace();
							}

						}
					});

				}
            	else{
					
					JSONObject order = orders.isArray().get(rowIndex).isObject();
					
	            	GWT.log(order.toString(), null);
	            	
	            	LabOrderPanel.load(order);
	            	
	            	super.onCellClick(grid, rowIndex, colindex, e);
				}
            	
            }
        });
        
		store.load();
		
		add(grid);
		
		update();

	}

	public static void update() {
		
		if(Jitavej.patient == null){
			
			return;
		}
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/history");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
			params.append("&type=lab");
			//params.append("type=lab");
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
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

		} 
		catch (RequestException e1) {
			e1.printStackTrace();
		}
	}

	public void onClick(Widget sender) {
		
		update();
	}
	
	
	
}
