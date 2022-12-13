package com.neural.jitavej.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
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
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.neural.jitavej.client.util.DateRenderer;

public class FrontMedPanel extends Panel {
	
	public static String data1, data2;
	Timer timer;
	public static Store store, store2;
	public static RequestBuilder rb1 ,rb2;
	String[][] ss = null;
	Window window;
	Button button9;
	ComboBox cb;
	Button button8;
	
	public FrontMedPanel() {
		
		//Dictionary dictionary = Dictionary.getDictionary("dictionary");
		//String timeoutgrid = dictionary.get("timeoutgrid");
		
		//setTitle("Patient Queue");
		setWidth(260);
		setCollapsible(true);

		rb1 = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/before");
		rb1.setTimeoutMillis(30000);	
		
		rb2 = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/q/after");
		rb2.setTimeoutMillis(30000);		
		
		timer = new Timer() {
			
			public void run() {
				
				update();
			}
		};
		
		timer.scheduleRepeating(10000);
		//timer.scheduleRepeating(Integer.parseInt(timeoutgrid));
		
		setLayout(new RowLayout());  
		setBorder(true);  

		button9 = new Button("reload");
		
		button8 = new Button(Jitavej.component.get("name").isString().stringValue());
		button8.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
			//	ss = getStates();
			}

		});
		
		add(button8, new RowLayoutData(30));  
		
		Panel onePanel = new Panel();
		
		onePanel.setBorder(false);
		onePanel.setBodyStyle("border-bottom-width: 1px;background-color: cccccc;");
		onePanel.setStyle("background-color: cccccc;");
		onePanel.setTitle(Jitavej.CONSTANTS.nav_queue0());  
		onePanel.add(getTable1()); 
	//	onePanel.setId("greedy1");  
		onePanel.setCollapsible(true);  
	//	onePanel.setAutoScroll(true);  
	//	onePanel.setBodyStyle("margin-bottom:10px"); 

		add(onePanel, new RowLayoutData(240));  
		
		Panel secondPanel = new Panel();
		
		secondPanel.setBorder(false);
		secondPanel.setBodyStyle("border-bottom-width: 1px;");		
		secondPanel.setTitle(Jitavej.CONSTANTS.nav_queue2());  
		secondPanel.add(getTable2());  
	//	secondPanel.setId("greedy2");  
		secondPanel.setCollapsible(true);  
	//	secondPanel.setAutoScroll(true);  
	//	secondPanel.setBodyStyle("margin-bottom:10px");  
		   
		add(secondPanel, new RowLayoutData(150));  

		Panel thirdPanel = new Panel();  
		thirdPanel.setBorder(false);	
		thirdPanel.setTitle(Jitavej.CONSTANTS.nav_queue2());  
		thirdPanel.setHtml("<br><br>");  
	//	thirdPanel.setId("greedy3");  
		thirdPanel.setCollapsible(true);  
		thirdPanel.setAutoScroll(true);  
	//	thirdPanel.setBodyStyle("margin-bottom:10px");  
		   
	//	add(thirdPanel, new RowLayoutData("20%"));  
		
		update();
	//	add(createAccordionPanel(), new RowLayoutData("30%")); 
		
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
  //      reader.setRoot("data");  
  //      reader.setTotalProperty("totalCount");  
  
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
        //grid.setTitle("Local Json Grid");  
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
	
	public Panel getTable2() {
		
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
        String[][] itemList = new String[][]{{"images/icons/audio_30x30.gif", "buuga"},{"images/icons/cd_30x30.gif", "buuga"}};

        DataProxy dataProxy = new MemoryProxy(itemList);
        store2 = new Store(dataProxy,reader); 
        store2.removeAll();
        
        //setup column model  
        ColumnModel columnModel = new ColumnModel(new ColumnConfig[]{  
                new ColumnConfig(Jitavej.CONSTANTS.nav_firstname(), "firstname", 70, true),  
                new ColumnConfig(Jitavej.CONSTANTS.nav_lastname(), "lastname", 100, true), 
                new ColumnConfig(Jitavej.CONSTANTS.nav_time(), "addDate", 70, true, new DateRenderer("hh:mm"))
        });  
  
        GridPanel grid = new GridPanel();  
        //grid.setTitle("Local Json Grid");  
        grid.setStore(store2);  
        grid.setColumnModel(columnModel);  
        grid.setHeader(false);
        grid.setFrame(false); 
        grid.setBorder(false);
        grid.setWidth(258);  
        grid.setHeight(120);   
        grid.stripeRows(true);  
        grid.setIconCls("grid-icon");  
        grid.setTrackMouseOver(false);  

        grid.addGridCellListener(new GridCellListenerAdapter() {  

            public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {  
           	
            		JSONValue list = JSONParser.parse((String)data2);
            		JSONArray jsonArray  = list.isArray();
            		final JSONObject item = jsonArray.get(rowIndex).isObject();
            		//    GWT.log(item.get("username").toString(), null);
                
            		MessageBox.prompt("ChangeRoom for VN " + item.get("vn").toString() , "Please enter Component Number:",  
                		new MessageBox.PromptCallback() {  
                		    public void execute(String btnID, String text) {  
                		    //	int id = Integer.parseInt(item.get("id").toString());
                		    //	int room = Integer.parseInt(text);
  
                		    }  
                		});
              
                
                
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
					} else {
						MessageBox.alert("Server Down!!!");
					}
				}
			});
			////////////////////////////////////////
			rb2.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
					//	GWT.log(response.getText(), null);
						data2 = response.getText();
						store2.removeAll();
						store2.loadJsonData(data2, true);  							
					} else {
						MessageBox.alert("Server Down!!!");
					}
				}
			});					

		} catch (RequestException e1) {
			e1.printStackTrace();
		}			
	}
	
	/*	
	private String[][] getStates() {  
		
		try {	
			GWT.log("getStates ", null);

			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/component/list2");
			rb.setTimeoutMillis(30000);	
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest("", new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {

						JSONValue list = JSONParser.parse(response.getText());
						Jitavej.components = list.isArray();
						ss = new String[Jitavej.components.size()][1];
						for(int i=0;i<Jitavej.components.size();i++){
							final JSONObject com = Jitavej.components.get(i).isObject();
							ss[i][0] = com.get("name").isString().stringValue();
						//	GWT.log("s[]" + ss[i][0], null);
							
						}

				         final Store store = new SimpleStore(new String[]{"component"}, ss);  
				         store.load();  
				   
				         cb = new ComboBox();  
				         cb.setForceSelection(true);  
				         cb.setMinChars(1);  
				         cb.setFieldLabel("Component");  
				         cb.setStore(store);  
				         cb.setDisplayField("component");  
				         cb.setMode(ComboBox.LOCAL);  
				         cb.setTriggerAction(ComboBox.ALL);  
				         cb.setEmptyText("Enter Component");  
				         cb.setLoadingText("Searching...");  
				         cb.setTypeAhead(true);  
				         cb.setSelectOnFocus(true);  
				         cb.setWidth(200);  
				   
				         cb.setHideTrigger(false);  
						
				         
				 		button9.addClickListener(new ClickListener() {
							public void onClick(Widget sender) {
								try {		
									String component = cb.getText().trim();
									Jitavej.component = component;
									button8.setText(component);
									RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/changeComponent");
									rb.setTimeoutMillis(30000);	
									StringBuffer params = new StringBuffer();
									params.append("component=" + URL.encodeComponent(component));
									rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
									rb.sendRequest(params.toString(), new RequestCallback() {
										public void onError(Request request, Throwable exception) {
											GWT.log("Error response received during authentication of user", exception);
										}
										public void onResponseReceived(Request request, Response response) {
											if (response.getStatusCode() == 200) {
												FrontMedPanel.update();
												Jitavej.show(new Panel());
											//	MessageBox.alert("Send Successful.");
											} else {
												MessageBox.alert("Server Down!!!");
											}
										}
									});
						
								} catch (RequestException e1) {
									e1.printStackTrace();
								}								
								window.hide();
							}
						});	
				 		
				        window = new Window();  
				        window.setTitle("Select Location");  
				        window.setClosable(true);  
				        window.setWidth(220);  
				        window.setHeight(150);  
				        window.setPlain(true);  
				        window.setLayout(new VerticalLayout(15));  
				        window.add(cb);  
				        window.add(button9);  
				        window.setCloseAction(Window.HIDE);  				
				         window.show();  						
						
						
					} else {
						MessageBox.alert("Server Down!!!");
					}
				}
			});
			return ss;
		} catch (RequestException e1) {
			GWT.log("", e1);
		}			
		
         return null;
     }  
     */
}
