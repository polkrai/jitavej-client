package com.neural.jitavej.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.neural.jitavej.client.dialog.ChangeComponentDialog;

public class NavPanel extends Panel {
	
	public static JSONArray queues1 = new JSONArray();
	public static JSONArray queues2 = new JSONArray();
	Timer timer;
	public static Store store, store2;
	public static RequestBuilder rb1 ,rb2;
	Window window;
	public static Button changeComponent;
	public static GridPanel grid, grid2;
	 
    public native void aaa() /*-{ 
    	$wnd.setPatient('xxxx'); 
	}-*/; 
    
	public NavPanel() {
		
		//setTitle("Patient Queue");
		setWidth(300);
		setCollapsible(true);

		rb1 = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/before");
		rb1.setTimeoutMillis(90000);
		
		rb2 = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/after");
		rb2.setTimeoutMillis(120000);		
		
		timer = new Timer() {
			public void run() {
				update();
			}
		};
		
		timer.scheduleRepeating(60000);
		
		setLayout(new RowLayout());  
		setBorder(true);  

		Button logout = new Button(Jitavej.CONSTANTS.logout());
		
		logout.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				
				try {
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/logout");
					rb.setTimeoutMillis(30000);	
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					rb.sendRequest(null, new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}
						
						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								Window.Location.replace("/nano/");
							}
							else{
								
								Window.alert("response.getText() "+response.getText());
							}
						}
					});
				} 
				catch (RequestException e1) {
					GWT.log("", e1);
				}	
			}
		});
		
		logout.setWidth("70%");
		
		Button profile = new Button("Profile");
		profile.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				
				ProfileDialog dialog = new ProfileDialog();
				dialog.show();
				dialog.center();
			}
		});
		
		profile.setWidth("80%");
		
		
		/*Button medload = new Button(Jitavej.CONSTANTS.load());
		medload.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				MedLoadDialog dialog = new MedLoadDialog();
				dialog.show();
				dialog.center();
			}
		});
	 	*/

		//medload.setEnabled(false);
		changeComponent = new Button(Jitavej.component.get("name").isString().stringValue());
		changeComponent.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				
				final ChangeComponentDialog dialog = new ChangeComponentDialog();
				
				dialog.form.addFormHandler(new FormHandler() {
					
					public void onSubmitComplete(FormSubmitCompleteEvent event) {
						
						dialog.hide();
						
						NavPanel.update();
					}

					public void onSubmit(FormSubmitEvent event) {}
				});
				
				dialog.show();
				
				dialog.center();
			}
		});
		
		Panel loginPanel = new Panel();
		
		loginPanel.setBorder(false);
		loginPanel.setBodyStyle("border-bottom-width: 1px;background-color: ffffff;");
		loginPanel.setStyle("background-color: ffffff;");
		
		//loginPanel.setTitle(Jitavej.CONSTANTS.action());
		
		HTML html = new HTML("<h1>" + Jitavej.component.get("name").isString().stringValue() + "</h1>");
		
		html.setStyleName("header1");
		
		loginPanel.add(html);
		
		if(Jitavej.session != null){
			
			loginPanel.add(new HTML("<b>" + Jitavej.session.get("user").isObject().get("firstname").isString().stringValue() + " " + Jitavej.session.get("user").isObject().get("lastname").isString().stringValue() + "</b>")); 
			
		}

		//onePanel.setId("greedy1");  
		loginPanel.setCollapsible(true);
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(0);
		hor.add(profile);
		hor.add(logout);
		
		add(loginPanel, new RowLayoutData(50));  
		add(hor, new RowLayoutData(30));  
		//add(medload, new RowLayoutData(30));  
		add(changeComponent, new RowLayoutData(30));  

		Panel onePanel = new Panel();  
		onePanel.setBorder(false);
		onePanel.setBodyStyle("border-bottom-width: 1px;background-color: cccccc;");
		onePanel.setStyle("background-color: cccccc;");
		onePanel.setTitle(Jitavej.CONSTANTS.nav_queue0());  
		onePanel.add(getTable1()); 
		//onePanel.setId("greedy1");  
		onePanel.setCollapsible(true);  
		//onePanel.setAutoScroll(true);  
		//onePanel.setBodyStyle("margin-bottom:10px"); 

		add(onePanel, new RowLayoutData(320));  
		
		Panel secondPanel = new Panel(); 
		secondPanel.setBorder(false);
		secondPanel.setBodyStyle("border-bottom-width: 1px;");		
		secondPanel.setTitle(Jitavej.CONSTANTS.nav_queue2());  
		secondPanel.add(getTable2());  
	    //secondPanel.setId("greedy2");  
		secondPanel.setCollapsible(true);  
	    //secondPanel.setAutoScroll(true);  
	    //secondPanel.setBodyStyle("margin-bottom:10px");  
		   
		add(secondPanel, new RowLayoutData(320));  

		Panel thirdPanel = new Panel();  
		thirdPanel.setBorder(false);	
		thirdPanel.setTitle(Jitavej.CONSTANTS.nav_queue2());  
		thirdPanel.setHtml("<br /><br />");  
	    //thirdPanel.setId("greedy3");  
		thirdPanel.setCollapsible(true);  
		thirdPanel.setAutoScroll(true);  
	    //thirdPanel.setBodyStyle("margin-bottom:10px");  
		   
	    //add(thirdPanel, new RowLayoutData("20%"));  
		
		update();
	    //add(createAccordionPanel(), new RowLayoutData("30%")); 
		
	}

	public Panel getTable1() {
		
        Panel panel = new Panel();
        
        panel.setBorder(false);  
        panel.setPaddings(0);  
  
        final RecordDef recordDef = new RecordDef(new FieldDef[]{  
        		
                new StringFieldDef("firstname", "firstname"),  
                new StringFieldDef("lastname", "lastname"),  
                new StringFieldDef("from", "from"),  
                new StringFieldDef("action_text", "action_text"),  
                new StringFieldDef("time", "time"),
                new BooleanFieldDef("receive", "receive"),       
        });
        
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
  
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
        
        store = new Store(dataProxy, reader); 
        store.removeAll();

        ColumnConfig receive = new ColumnConfig(" ", "receive", 25, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
            	
               	if(!(Boolean)value){
               		
            		return Format.format("", "");
            	}
               	else{
               		
            		return Format.format("<img src='images/on.png'/>", "");
            	}  
               	//return Format.format("", "");
                
             }

         });
        
        //setup column model  
        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{
			new ColumnConfig(Jitavej.CONSTANTS.nav_firstname(), "firstname", 60, true),  
			new ColumnConfig(Jitavej.CONSTANTS.nav_lastname(), "lastname", 70, true), 
			new ColumnConfig(Jitavej.CONSTANTS.action(), "action_text", 70, true),                
			new ColumnConfig(Jitavej.CONSTANTS.nav_time(), "time", 50, true),
			receive
        });  

        grid = new GridPanel();  
        //grid.setTitle("Local Json Grid");  
        grid.setStore(store);  
        grid.setColumnModel(columnModel);  
        grid.setFrame(false); 
        grid.setBorder(false);
        //grid.setWidth(258);  
        grid.setHeight(320);  
        grid.stripeRows(true);  
        grid.setIconCls("grid-icon"); 
        grid.setTrackMouseOver(false);  
        grid.addGridCellListener(new GridCellListenerAdapter() {  
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	Jitavej.queue = queues1.get(rowIndex).isObject();
            	
            	//Jitavej.queue.put("receive", JSONBoolean.getInstance(true));
            	grid.getStore().getAt(rowIndex).set("receive", true);
            	
            	//store.removeAll();
            	//store.loadJsonData(queues1.toString(), true); 
            	Jitavej.serviceQueue(Jitavej.queue);
            	
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
	
	public Panel getTable2() {
		
        Panel panel = new Panel();  
        panel.setBorder(false);  
        panel.setPaddings(0);  
  
        final RecordDef recordDef = new RecordDef(new FieldDef[]{
        		
                new StringFieldDef("firstname", "firstname"),  
                new StringFieldDef("lastname", "lastname"),  
                new StringFieldDef("from", "from"),  
                new StringFieldDef("action_text", "action_text"),  
                new StringFieldDef("time", "time"),
                new DateFieldDef("receive", "receive"),
                
        });
        
        JsonReader reader = new JsonReader(recordDef);  
        //reader.setRoot("data");  
        //reader.setTotalProperty("totalCount");  
  
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
        
        store2 = new Store(dataProxy,reader); 
        store2.removeAll();

        ColumnConfig apppointment = new ColumnConfig(" ", "receive", 25, false, new Renderer() {
           
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
            	
            	if(rowIndex < queues1.size()){
            		
                	JSONObject queue = queues1.get(rowIndex).isObject();

                	return Format.format("", "");
                         		
            	}
            	
            	return Format.format("", "");
                
             }

         });
        
        //setup column model  
        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{  
			new ColumnConfig(Jitavej.CONSTANTS.nav_firstname(), "firstname", 60, true),  
			new ColumnConfig(Jitavej.CONSTANTS.nav_lastname(), "lastname", 70, true), 
			new ColumnConfig(Jitavej.CONSTANTS.action(), "action_text", 70, true),                
			new ColumnConfig(Jitavej.CONSTANTS.nav_time(), "time", 50, true),
			apppointment
        });
        
        grid2 = new GridPanel();  
        //grid.setTitle("Local Json Grid");  
        grid2.setStore(store2);  
        grid2.setColumnModel(columnModel);  
        grid2.setFrame(false); 
        grid2.setBorder(false);
        //grid.setWidth(258);  
        grid2.setHeight(320);  
        grid2.stripeRows(true);  
        grid2.setIconCls("grid-icon");  
        grid2.setTrackMouseOver(false);  

        grid2.addGridCellListener(new GridCellListenerAdapter() {  
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            	
            	Jitavej.queue = queues2.get(rowIndex).isObject();
            	
            	Jitavej.serviceQueue3(Jitavej.queue);
            	
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
        GridView view = new GridView();
        
        view.setEmptyText("No visit here.");
        
        grid2.setView(view);
        
        store2.load();
      
        panel.add(grid2);  

        return grid2; 
    }  	

	public static void update(){
		
		try {
			
			rb1.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						//GWT.log(response.getText(), null);
						queues1 = JSONParser.parse(response.getText()).isArray();
						store.removeAll();
						store.loadJsonData(queues1.toString(), true);  	
						
					    for(int i=1; i<queues1.size(); i++){
					    	
					    	JSONObject queue = queues1.get(i).isObject();
					    	
					    	if(Jitavej.queue != null && Jitavej.queue.get("id").isNumber().toString().equals(queue.get("id").isNumber().toString())){
					    		
					    		grid.getSelectionModel().selectRow(i);
					    	}
					    }
						
					} 
					else {
						
						//Window.alert("response.getStatusCode() "+response.getStatusCode());
						//Window.alert("response.getText() "+response.getText());
					}
				}
			});
			
			rb2.sendRequest(null, new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						//GWT.log(response.getText(), null);
						queues2= JSONParser.parse(response.getText()).isArray();
						store2.removeAll();
						store2.loadJsonData(queues2.toString(), true);  							
					} 
					else {
						
						//Window.alert("response.getStatusCode() "+response.getStatusCode());
						//Window.alert("response.getText() "+response.getText());
					}
				}
			});					

		} 
		catch (RequestException e1) {
			
			Window.alert(e1.toString());
		}			
	}
}