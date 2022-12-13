package com.neural.jitavej.client.lab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.autocomplete.ItemSuggestOracle;

public class LabOrderPanel extends VerticalPanel {
	
	public static LabHistoryPanel labHistoryPanel = new LabHistoryPanel();
	
	String data = null;
	Button re;
	RecordDef recordDef;
	BaseColumnConfig[] columns;
	
	public static SuggestBox suggestBox;
	public static Store store;
	public static JSONArray orderItems = new JSONArray();
	public static TextBox sum;
	boolean opd;
	public static ListBox q;
	public Button save;
	
	public LabOrderPanel(final boolean opd) {
		
		this.opd = opd;
		
		setTitle(Jitavej.CONSTANTS.lab_order());

		if(opd){
			
			recordDef = new RecordDef(new FieldDef[] {
                new StringFieldDef("name", "item.name"), 	
                new StringFieldDef("qty", "qty"), 	
                new StringFieldDef("fee", "item.price"), 	
                new StringFieldDef("sum", "sum")
			});			
		}
		else{
			
			recordDef = new RecordDef(new FieldDef[] {
                new StringFieldDef("name", "item.name"), 	
                new StringFieldDef("qty", "q"), 	
                new StringFieldDef("fee", "item.price"), 	
                new StringFieldDef("sum", "sum")
			});
		}

		
		JsonReader reader = new JsonReader(recordDef);
		
		String[][] itemList = new String[][] {{"", "" }, {"", ""}};

		DataProxy dataProxy = new MemoryProxy(itemList);
		
		store = new Store(dataProxy, reader);

        ColumnConfig colType = new ColumnConfig(Jitavej.CONSTANTS.delete_history_lab(), "name", 30, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                
        		return Format.format("<img src='images/cross.gif'/>", value.toString());
            }
        });
        
        if(opd){
        	
            columns = new BaseColumnConfig[]{
                new RowNumberingColumnConfig(), 
                new ColumnConfig(Jitavej.CONSTANTS.lab_item(), "name", 420, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_number(), "qty", 55, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_fee(), "fee", 55, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_sum(), "sum", 55, true),
                colType,				
            };
            
        }
        else {
        	
            columns = new BaseColumnConfig[]{  
                new RowNumberingColumnConfig(), 
                new ColumnConfig(Jitavej.CONSTANTS.lab_item(), "name", 420, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_number(), "qty", 55, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_fee(), "fee", 55, true),
                new ColumnConfig(Jitavej.CONSTANTS.lab_sum(), "sum", 55, true),
                colType,		
             }; 
        }
        
        ColumnModel columnModel = new ColumnModel(columns);  
        //columnModel.setColumnWidth(0, 30);
         
		GridPanel grid = new GridPanel();
		grid.setBorder(false);
		grid.setMargins(1);
		grid.setPaddings(1);
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		
		grid.setFrame(false);
		grid.setBodyBorder(true);
		///grid.setHideBorders(true);
		//Jitavej.mode.equals("lab");
		
		if (opd) {
			
			grid.setWidth(670);  
	        grid.setHeight(355);
		}
		else {
			
			grid.setWidth(750);  
	        grid.setHeight(310);
		}
         
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

		GridView view = new GridView();
		
		view.setEmptyText(Jitavej.CONSTANTS.lab_no_item());
		
		grid.setView(view);

		grid.addGridCellListener(new GridCellListenerAdapter() {
			
			public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
				
				if (colindex == 5) {
					
					MessageBox.confirm(Jitavej.CONSTANTS.data_delete_confirm(), Jitavej.CONSTANTS.delete_confirm(), new MessageBox.ConfirmCallback() {
        			
						public void execute(String btnID) {
							
            				if(btnID.equals("no")){
            					
            					return;
            				}
        				
            				JSONArray orderItems0 = orderItems;
            				
            				orderItems = new JSONArray();
            				
            				for(int i=0;i<orderItems0.size();i++){
            					
            					JSONObject icd10 = orderItems0.get(i).isObject();
            					
            					if(i != rowIndex){
            						
            						orderItems.set(orderItems.size(), icd10);
            					}
            				}
        			
            				store.removeAll();
            				
            				store.loadJsonData(orderItems.toString(), true); 
						}
					});  
				}
            }

			public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
				
                JSONValue list = JSONParser.parse((String) data);
                
                JSONArray jsonArray = list.isArray();
                
                JSONObject item = jsonArray.get(rowIndex).isObject();
                
                MessageBox.alert("คุณคลิก  : " + item.get("username").toString());
			}
		});

		Button add = new Button(Jitavej.CONSTANTS.add_item_lab(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				addItem();
			}
		});
		
		final Button clear = new Button(Jitavej.CONSTANTS.clear_item(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				orderItems = new JSONArray();
				
				store.removeAll();
			}
		});

		Button elec = new Button("ELECTROLYTE", new ClickListener() {
			
			public void onClick(Widget sender) {
				//clear.click();
				suggestBox.setText("SODIUM");
				addItem();
				suggestBox.setText("POTASSIUM");
				addItem();
				suggestBox.setText("BICARBONATE");
				addItem();		
				suggestBox.setText("CHLORIDE");
				addItem();			
			}
		});
		
		Button lipid = new Button("LIPID PROFILE", new ClickListener() {
			
			public void onClick(Widget sender) {
				//clear.click();
				suggestBox.setText("CHOLESTEROL");
				addItem();
				suggestBox.setText("TRIGLYCERIDE");
				addItem();
				suggestBox.setText("HDL");
				addItem();		
				suggestBox.setText("LDL");
				addItem();			
			}
		});
		
		Button set1 = new Button("Set 1", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				clear.click();
				suggestBox.setText("CBC (+ diff. RBC morphology + Plt count) by automation");
				addItem();
				suggestBox.setText("URINE EXAM");
				addItem();
				suggestBox.setText("STOOL EXAM");
				addItem();								
			}
		});
		
		Button set2 = new Button("Set 2", new ClickListener() {
			
			public void onClick(Widget sender) {
				clear.click();
				suggestBox.setText("CBC (+ diff. RBC morphology + Plt count) by automation");
				addItem();
				suggestBox.setText("URINE EXAM");
				addItem();
				suggestBox.setText("STOOL EXAM");
				addItem();
				suggestBox.setText("NAF-BLOOD SUGAR");
				addItem();
				suggestBox.setText("BUN - CREATININE");
				addItem();
				suggestBox.setText("LIVER FUNCTION TEST");
				addItem();
				suggestBox.setText("CHOLESTEROL");
				addItem();
				suggestBox.setText("TRIGLYCERIDE");
				addItem();
				suggestBox.setText("URIC ACID");
				addItem();				
			}
		});
		
		Button cpg_alcohol = new Button("CPG-Alcohol", new ClickListener() {
			
			public void onClick(Widget sender) {
				clear.click();
				suggestBox.setText("CBC (+ diff. RBC morphology + Plt count) by automation");
				addItem();
				suggestBox.setText("BUN (Blood Urea Nitrogen)");
				addItem();
				suggestBox.setText("Creatinine (Enzymatic reaction)");
				addItem();
				suggestBox.setText("Electrolyte (Na, K, Cl, CO2)");
				addItem();
				suggestBox.setText("NAF-BLOOD SUGAR");
				addItem();
				suggestBox.setText("LIVER FUNCTION TEST");
				addItem();
				suggestBox.setText("Calcium");
				addItem();
				suggestBox.setText("Magnesium");
				addItem();
				suggestBox.setText("Phosphorus");
				addItem();				
			}
		});
		
		Button baseline = new Button("Baseline", new ClickListener() {
			
			public void onClick(Widget sender) {
				clear.click();
				suggestBox.setText("CBC (+ diff. RBC morphology + Plt count) by automation");
				addItem();
				suggestBox.setText("BUN (Blood Urea Nitrogen)");
				addItem();
				suggestBox.setText("Creatinine (Enzymatic reaction)");
				addItem();
				suggestBox.setText("Electrolyte (Na, K, Cl, CO2)");
				addItem();				
			}
		});
	
        ItemSuggestOracle oracle = new ItemSuggestOracle("lab");
        
        suggestBox = new SuggestBox(oracle);
        suggestBox.setLimit(5); 
        suggestBox.setWidth("347");
        
        suggestBox.addKeyboardListener(new KeyboardListenerAdapter(){
        	
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				
				String name = suggestBox.getText().trim();
				
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	
					if(!suggestBox.suggestionPopup.isAttached()){
						
						addItem();
					}
		        }

				super.onKeyPress(sender, keyCode, modifiers);
			}
        	
        });

		q = new ListBox();
		q.addItem("Q 1 WK");
		q.addItem("Q 2 WK");
		q.addItem("Q 1 MO");
		q.addItem("Q 2 MO");
		q.addItem("Q 3 MO");
		q.addItem("Q 4 MO");
        
		suggestBox.setPixelSize(200, 18);
		
		ActionBar panel2 = new ActionBar();
		panel2.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		panel2.setStyleName("lab-bar");
		panel2.add(new HTML("Lab:"));
		//panel2.add(new HTML(Jitavej.CONSTANTS.search_item_lab() + ":"));
		
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setSpacing(0);
		hor1.add(suggestBox);
		hor1.add(new HTML("<div style='width:2px'></div>"));
		
    	if(!opd){
    		
    		hor1.add(q);
    	}
    	else{

    	}
    	
    	hor1.add(add);
    	hor1.add(new HTML("<div style='width:100'></div>"));
    	//hor1.add(elec);
    	hor1.add(cpg_alcohol);
    	hor1.add(new HTML("<div style='width:4'></div>"));
    	//hor1.add(lipid);
    	hor1.add(baseline);
    	//hor1.add(new HTML("<div style='width:4'></div>"));
    	//hor1.add(set2);
    	
    	panel2.add(hor1);
		panel2.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
	
 		save = new Button("<b> + " + Jitavej.CONSTANTS.lab_button_save() + "</b>");
 		save.setPixelSize(130, 45);
 		save.addClickListener(new ClickListener(){
 			
			public void onClick(com.google.gwt.user.client.ui.Widget sender) {
 			
 				if(orderItems.size() == 0){
					
					MessageBox.setMinWidth(300);
					
					MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.order_isnulll());
					
					return;
				}
			
				try {
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/put");
					
					rb.setTimeoutMillis(30000);
					
					StringBuffer params = new StringBuffer();
					
					params.append("user_id=" + URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));	
					params.append("&orderitems=" + URL.encodeComponent(orderItems.toString()));
					params.append("&vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
					params.append("&type=lab");
					
					if(opd){
						
						params.append("&mode=opd");					
					}
					else{
						
						params.append("&mode=continuation");		
					}
					
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log(Jitavej.CONSTANTS.error_response(), exception);
						}
						
						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								MessageBox.setMinWidth(300);
								
								MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.CONSTANTS.lab_save());
								
								orderItems = new JSONArray();
								
								store.removeAll();
								store.loadJsonData(orderItems.toString(), true); 
								suggestBox.setText("");
								suggestBox.setFocus(true);
								
								LabHistoryPanel.update();
							} 
							else {
								
								Window.alert("response.getText() "+response.getText());
							}
						}
					});
		
				} 
				catch (Exception e1) {
					
					Window.alert(e1.toString());
				}			
			}
 			
 		});
 		
		re = new Button("ReMed", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				try {
					
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/lastdrugs");
					rb.setTimeoutMillis(30000);	
					StringBuffer params = new StringBuffer();
					
					params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					rb.sendRequest(params.toString(), new RequestCallback() {
						
						public void onError(Request request, Throwable exception) {
							
							GWT.log(Jitavej.CONSTANTS.error_response(), exception);
						}
						
						public void onResponseReceived(Request request, Response response) {
							
							if (response.getStatusCode() == 200) {
								
								JSONValue value = JSONParser.parse(response.getText());
								orderItems = value.isArray();
								update();
							} 
							else {
								
								Window.alert("response.getText() "+response.getText());
							}
						}
					});

				} 
				catch (RequestException e1) {
					
					GWT.log("Error > ", e1);
				}				
			}
		});	

        sum = new TextBox();
        sum.setStyleName("sum-black");
        sum.setWidth("100");
        sum.setEnabled(false);
        
        //if (opd) {
        	
    	ActionBar bar = new ActionBar();
    	bar.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    	bar.add(save);
    	
    	add(bar);
        /*}
        else {
        	
        	panel2.add(save);
        }*/
        
		ActionBar bar2 = new ActionBar();
		//bar2.add(re);
		bar2.add(clear);
		bar2.add(new HTML("<div style='width:600px'></div>"));
		bar2.add(sum);
		
 		add(panel2);
 		
 		HorizontalPanel hor = new HorizontalPanel();
 		hor.add(grid);
 		
 		DecoratorPanel cen = new DecoratorPanel();
 		cen.add(labHistoryPanel);
 		hor.add(cen);
 		
        add(hor);
        add(bar2);
        
	}
	
	public static void addItem(){
		
		try {
			
			String name = suggestBox.getText();
			//record.put("recordDrugs", orders);
        
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/item/findByName");
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("name=" + URL.encodeComponent(name.trim()));
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log(Jitavej.CONSTANTS.error_response(), exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log("response.getText() > "+ response.getText(), null);
						
						JSONValue value = JSONParser.parse(response.getText());
						
						JSONObject item = value.isObject();

						if(item.get("name") != null){
							
							GWT.log("item.get('name') > "+ item.get("name"), null);
							
							JSONObject orderItem = new JSONObject();
							
							orderItem.put("item", item);
							
							orderItem.put("qty", new JSONNumber(1));
							
							orderItem.put("sum", new JSONNumber(item.get("price").isNumber().doubleValue()));
							
							orderItem.put("q", new JSONString(q.getValue(q.getSelectedIndex())));
							
							orderItems.set(orderItems.size(), orderItem);
							
							store.removeAll();
							
							GWT.log("recordLabs > "+ orderItems.toString(), null);
							
							store.loadJsonData(orderItems.toString(), true);
							
							suggestBox.setText("");
							
							suggestBox.setFocus(true);
							
							update();
						}
					} 
					else {
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			GWT.log("Error > ", e1);
		}		
	}
		
    public static void load(JSONObject order){
    	
		try {

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
						
						JSONArray records = JSONParser.parse(response.getText()).isArray();
						
						for(int i=0; i<records.size(); i++){
							
							GWT.log(records.get(i).toString(), null);
							
							orderItems = new JSONArray();
							
							store.removeAll();
							
							suggestBox.setText(records.get(i).isObject().get("item").isString().stringValue());
							
							addItem();
						}
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
    
	public static void update(){
		
		store.removeAll();
		store.loadJsonData(orderItems.toString(), true);
		
		double sum2 = 0;
		
		for(int i=0;i<orderItems.size();i++){
			
			JSONObject recordDrug = orderItems.get(i).isObject();
			
			sum2 += recordDrug.get("sum").isNumber().doubleValue();
		}
		
		if(sum2 <= 500){
			
    		//	this.sum.setCls("sum-black");
    		//	this.sum.setStyle("color: black;font-size: 20px;font-weight: bold;width:100px;height: 24px;");
		}
		else{
			
    		//this.sum.setCls("sum-red");
    		//this.sum.setStyle("color: red;font-size: 20px;font-weight: bold;width:100px;height: 24px;");
		}
		
		sum.setText(""+sum2);
		//LabHistoryPanel.update();
	}
	
	public static void setPatient(JSONObject patient){
		
		if(patient == null){
			
		}
		else{}
		
		LabHistoryPanel.update();

	}
	
	
}
