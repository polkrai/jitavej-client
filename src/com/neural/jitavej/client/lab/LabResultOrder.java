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
import com.google.gwt.user.client.ui.DockPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.*;
import com.gwtext.client.data.Record;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridPanel;  
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.util.DateRenderer;
  
public class LabResultOrder extends DockPanel {  
	
	public static JSONArray orders = new JSONArray();
	public static Store store;
	//public EditorGridPanel gridResult;

	public LabResultOrder() {

		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
            new StringFieldDef("id", "id"),
            new DateFieldDef("time", "time"), 
            new StringFieldDef("vn", "vn"),
            new StringFieldDef("labout", "labout")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
              
        store = new Store(dataProxy,reader);
     
		//setup column model
        ColumnConfig printType = new ColumnConfig("Order", "name", 50, false, new Renderer() {
        	
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                
        		return Format.format("<img class=\"icon-center\" src='images/print.png'/>", "");
            }
        });
                    
        ColumnConfig printType2 = new ColumnConfig("Result", "name", 50, false, new Renderer() {
        	
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                
        		return Format.format("<img class=\"icon-center\" src='images/print.png'/>", "");
            }
        });
        
        ColumnConfig printScan = new ColumnConfig("LabOut", "name", 55, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
        		
        		if (orders.size() > 0) {
        			
	        		JSONObject scanlab = orders.get(rowIndex).isObject();
	        		
	        		if(scanlab.get("scanresult").toString() != "null"){
	        			
	        			return Format.format("<a class=\"icon-pointer-center\" href=" + scanlab.get("scanresult").toString() + " target=\"_blank\"><img src=\"images/view.png\"/></a>", "");
	        		}
	        		else {
	        			
	        			return Format.format("<img class=\"icon-center\" src='images/clear.png'/>", "");
	        		}
        		}
        		else {
        			
        			return Format.format("<img class=\"icon-center\" src='images/clear.png'/>", "");
        		}
             }
        });
        
        BaseColumnConfig[] columns = new BaseColumnConfig[]{
			new ColumnConfig("ID", "id", 70, true),
			new ColumnConfig("Date", "time", 70, true, new DateRenderer("dd/MM/yy")), 
			new ColumnConfig("VN", "vn", 120, true),
			printType,
			printType2,
			printScan
         };
        
        ColumnModel columnModel = new ColumnModel(columns); 
 
        EditorGridPanel grid = new EditorGridPanel();

        grid.setStore(store);
        grid.setColumnModel(columnModel);

	    GridView view = new GridView();
	    
	    view.setEmptyText("No visit here.");
	    
	    grid.setView(view); 
	    grid.setFrame(false);
	    grid.setBorder(false);
	    grid.setWidth(475);
	    grid.setHeight(515);
	    grid.stripeRows(true);

	    grid.setIconCls("grid-icon");
	    grid.setTrackMouseOver(false);

	    grid.addGridCellListener(new GridCellListenerAdapter() {
            
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	JSONObject order = orders.get(rowIndex).isObject();
            	
            	if(colindex == 3){
            		
            		//Window.alert("3");
					Window.open( "/jitavej/lab/printreport?order_id="+order.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=600,width=800" ); 
					//Window.open("/miracle/java_print_out/printreport.php?order_id="+order.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=900,width=1000");
	
            	}
            	
            	if(colindex == 4){
            		
            		//Window.alert("4");
					Window.open("/jitavej/lab/printreport2?order_id="+order.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=600,width=800");
					//Window.open("/miracle/java_print_out/printreport2.php?order_id="+order.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=900,width=1000");
	
            	}
            	else {
                	
                	LabResultPanel.labResultRecord.load(order);
                	
                	super.onCellClick(grid, rowIndex, colindex, e);
            	}

            }
        });
       
		store.load();

		add(grid, DockPanel.CENTER);

	}

	public static void update() {
		
		try {
			
			if(Jitavej.patient == null){
				
				orders = new JSONArray();
				store.removeAll();
				store.loadJsonData(orders.toString(), true);
				
				return;
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/lab/orders");
			
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

		} 
		catch (RequestException e1) {
			
			e1.printStackTrace();
		}
	}

}
