package com.neural.jitavej.client.patient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
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
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.SearchPanel;
import com.neural.jitavej.client.dialog.ChangeComponentDialog;
import com.neural.jitavej.client.frontmed.MedLoadDialog;
import com.neural.jitavej.client.util.DateRenderer;

public class PatientListPanel extends Panel {
	
	Timer timer;
	public static String data1, data2;
	public static Store store, store2;
	public static RequestBuilder rb1 ,rb2;
	public static  Button changeComponent;
    
	public PatientListPanel() {
		
        setWidth(260);
        setCollapsible(true);
        
        rb1 = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/patient/list2");
        rb1.setTimeoutMillis(30000);
        
        setLayout(new RowLayout());  
        setBorder(true);  
        
        Button search = new Button(Jitavej.CONSTANTS.search_patient());
        
        search.addClickListener(new ClickListener(){
        	
        	public void onClick(Widget sender) {
        		
        		Jitavej.show(new SearchPanel());
        	}
        });
        
        Button medload = new Button(Jitavej.CONSTANTS.load());
        
        medload.addClickListener(new ClickListener() {
        	
        	public void onClick(Widget sender) {
        		
        		MedLoadDialog dialog = new MedLoadDialog();
        		dialog.show();
        		dialog.center();
        	}
        });
        
        changeComponent = new Button(Jitavej.component.get("name").isString().stringValue());
        
        changeComponent.addClickListener(new ClickListener() {
        	
        	public void onClick(Widget sender) {
        		
        		final ChangeComponentDialog dialog = new ChangeComponentDialog();
        		
        		dialog.form.addFormHandler(new FormHandler() {
        			
        			public void onSubmitComplete(FormSubmitCompleteEvent event) {
        				
        				dialog.hide();
        				
        				PatientListPanel.update();
        				
        				Jitavej.show(new DockPanel());
        			}
        
        			public void onSubmit(FormSubmitEvent event) {
        				
        			}
        		});
        		
        		dialog.show();
        		
        		dialog.center();
        	}
        });

        add(search, new RowLayoutData(30));  		
        add(medload, new RowLayoutData(30));  
        add(changeComponent, new RowLayoutData(30));  

		Panel onePanel = new Panel();  
		onePanel.setBorder(false);
		onePanel.setBodyStyle("border-bottom-width: 1px;background-color: cccccc;");
		onePanel.setStyle("background-color: cccccc;");
		onePanel.setTitle(Jitavej.CONSTANTS.nav_queue0());  
		onePanel.add(getTable1()); 
		//onePanel.setId("greedy1");  
		onePanel.setCollapsible(true);  
	
		update();
		
	}

	public Panel getTable1() {
		
        Panel panel = new Panel();  
        panel.setBorder(false);  
        panel.setPaddings(0);  
  
        final RecordDef recordDef = new RecordDef(new FieldDef[]{
            new StringFieldDef("firstname", "visit.patient.firstname"),  
            new StringFieldDef("lastname", "visit.patient.lastname"),  
            new DateFieldDef("addDate", "addDate")
        }); 
        
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
  
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
        
        store = new Store(dataProxy,reader); 
        store.removeAll();

        //setup column model  
        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
        		
            new ColumnConfig(Jitavej.CONSTANTS.nav_firstname(), "firstname", 70, true),  
            new ColumnConfig(Jitavej.CONSTANTS.nav_lastname(), "lastname", 100, true), 
            new ColumnConfig(Jitavej.CONSTANTS.nav_time(), "addDate", 70, true, new DateRenderer("hh:mm"))
        });  

        GridPanel grid = new GridPanel();  
 
        grid.setStore(store);  
        grid.setColumnModel(columnModel);  
        grid.setFrame(false); 
        grid.setBorder(false);
        grid.setWidth(258);  
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
	

	public static void update(){
		
		try {		
			
			rb1.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						data1 = response.getText();
						
						store.removeAll();
						
						store.loadJsonData(data1, true);  							
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
