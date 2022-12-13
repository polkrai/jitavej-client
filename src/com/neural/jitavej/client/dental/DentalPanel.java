package com.neural.jitavej.client.dental;

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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.gwtext.client.core.EventCallback;
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
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.autocomplete.ItemSuggestOracle;

public class DentalPanel extends VerticalPanel {
	String data = null;
	public static SuggestBox suggestBox;
	public static Store store;
	public static JSONArray orderItems = new JSONArray();
	public static TextBox tooth;
	public static TextBox detail;
	public static TextBox surfaces;
	public static TextBox qty;
	public static TextBox fee;
	public static TextBox sum;
	boolean opd;
	public static ListBox dentist;
	RecordDef recordDef;
	BaseColumnConfig[] columns;
	public int row, col;
	EditorGridPanel grid;
	
	public DentalPanel(final boolean opd) {
		this.opd = opd;
		setTitle(Jitavej.CONSTANTS.lab_order());

		recordDef = new RecordDef(new FieldDef[] {
            new StringFieldDef("name", "item.name"), 	
            new StringFieldDef("qty", "qty"), 	
            new StringFieldDef("tooth", "tooth"), 	
            new StringFieldDef("surfaces", "surfaces"), 	
            new StringFieldDef("fee", "fee"), 	
            new StringFieldDef("sum", "sum")
		});			

		
		JsonReader reader = new JsonReader(recordDef);
		// reader.setRoot("data");
		// reader.setTotalProperty("totalCount");
		String[][] itemList = new String[][] { { "", "" }, { "", "" } };

		DataProxy dataProxy = new MemoryProxy(itemList);
		store = new Store(dataProxy, reader);

		ColumnConfig commonCol0 = new ColumnConfig(Jitavej.CONSTANTS.record_number(), "qty", 75, true, null, "qty");
		ColumnConfig commonCol1 = new ColumnConfig(Jitavej.CONSTANTS.dental_tooth(), "tooth", 75, true, null, "tooth");
		ColumnConfig commonCol2 = new ColumnConfig(Jitavej.CONSTANTS.dental_surfaces(), "surfaces", 75, true, null, "surfaces");
		ColumnConfig commonCol3 = new ColumnConfig(Jitavej.CONSTANTS.dental_fee(), "fee", 75, true, null, "fee");
		
		final TextField text = new TextField();
		text.addListener(new TextFieldListenerAdapter(){
			@Override
			public void onFocus(Field field) {
				text.selectText();
			}
		});
		
		text.addListener(new TextFieldListenerAdapter(){
			
			public void onChange(Field field, Object newVal, Object oldVal) {
				try {

					if(col == 3){
						orderItems.get(row).isObject().put("tooth", new JSONString(newVal.toString()));
					}else if(col == 4){
						orderItems.get(row).isObject().put("surfaces", new JSONString(newVal.toString()));
					}else if(col == 5){
						orderItems.get(row).isObject().put("fee", new JSONNumber(Double.parseDouble(newVal.toString())));
						orderItems.get(row).isObject().put("sum", new JSONNumber(Double.parseDouble(newVal.toString())));
					}
					
					update();

				} catch (Exception e) {
					//Window.alert("row="+row+" >>> "+e.toString());
					update();
				}
				GWT.log("onChange on row = " + row  , null);
			}
		});
		text.addKeyPressListener(new EventCallback(){

			public void execute(EventObject e) {
				row = grid.getCellSelectionModel().getSelectedCell()[0];
				col = grid.getCellSelectionModel().getSelectedCell()[1];
			}
			
		});
		
		GridEditor editor = new GridEditor(text);
		commonCol1.setEditor(editor);
		commonCol2.setEditor(editor);
		commonCol3.setEditor(editor);
		
        ColumnConfig colType = new ColumnConfig(" ", "name", 24, false, new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                return Format.format("<img src='images/cross.gif'/>", value.toString());
            }
        });

        columns = new BaseColumnConfig[]{  
            new RowNumberingColumnConfig(), 
            new ColumnConfig(Jitavej.CONSTANTS.dental_treatment(), "name", 440, true),
            commonCol0,
            commonCol1,
            commonCol2,
            commonCol3,
            new ColumnConfig(Jitavej.CONSTANTS.dental_sum(), "sum", 100, true),
            colType,
					
         };         	

        ColumnModel columnModel = new ColumnModel(columns);  
         //columnModel.setColumnWidth(0, 30);
         
		grid = new EditorGridPanel();
		grid.setBorder(false);
		grid.setMargins(1);
		grid.setPaddings(1);
		// grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setClicksToEdit(1);
		grid.setFrame(false);
		grid.setBodyBorder(true);
		//grid.setHideBorders(true);
		
		grid.setWidth(900); 
        grid.setHeight(520); 
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

		GridView view = new GridView();
		view.setEmptyText(Jitavej.CONSTANTS.lab_no_item());
		grid.setView(view);

		grid.addGridCellListener(new GridCellListenerAdapter() {
			public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
				if (colindex == 7) {
					MessageBox.confirm("Confirm", "Confirm to delete?", new MessageBox.ConfirmCallback() {
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
			//	JSONValue list = JSONParser.parse((String) data);
			//	JSONArray jsonArray = list.isArray();
			//	JSONObject item = jsonArray.get(rowIndex).isObject(); 
			}

		});

		Button add = new Button("Add Item", new ClickListener() {
			public void onClick(Widget sender) {
				addItem();
			}
		});
		
		final Button clear = new Button("Clear All", new ClickListener() {
			public void onClick(Widget sender) {
				orderItems = new JSONArray();
				store.removeAll();
			}
		});

        ItemSuggestOracle oracle = new ItemSuggestOracle("dental");
        suggestBox = new SuggestBox(oracle);
        suggestBox.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
        suggestBox.setWidth("455px");
        suggestBox.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestBox.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
					if(!suggestBox.suggestionPopup.isAttached()){
						qty.setFocus(true);
					}
		        }

				super.onKeyPress(sender, keyCode, modifiers);
			}
        	
        });


        qty = new TextBox();
        qty.setWidth("65px");
        qty.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestBox.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	tooth.setFocus(true);
		        }

				super.onKeyPress(sender, keyCode, modifiers);
			}
        	
        }); 
        
        tooth = new TextBox();
        tooth.setWidth("65px");
        tooth.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestBox.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	surfaces.setFocus(true);
		        }

				super.onKeyPress(sender, keyCode, modifiers);
			}
        	
        });      
        
        surfaces = new TextBox();
        surfaces.setWidth("65px");
        surfaces.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestBox.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	addItem();
		        }

				super.onKeyPress(sender, keyCode, modifiers);
			}
        	
        });        
 

        
        detail = new TextBox();
      
		dentist = new ListBox();
		
		dentist.addItem(null);
		
		for (int i = 0; i < Jitavej.users.size(); i++) {
			
			JSONObject user = Jitavej.users.get(i).isObject();
			
			GWT.log(user.toString(), null);
			
			if(user.get("userGroup").isNumber().toString().equals("33")){
				
				dentist.addItem(user.get("firstname").isString().stringValue() +" "+user.get("lastname").isString().stringValue(), user.get("id").isNumber().toString());			
			}
		}
		
		dentist.setSelectedIndex(0);

    	Grid panel = new Grid(2,10);
		panel.setStyleName("dental-bar");
		
       	panel.setWidget(0, 0, new HTML(Jitavej.CONSTANTS.dental_treatment()));
       	panel.setWidget(1, 0, suggestBox);
  
       	panel.setWidget(0, 1, new HTML(Jitavej.CONSTANTS.record_number()));
       	panel.setWidget(1, 1, qty);
       	
       	panel.setWidget(0, 2, new HTML(Jitavej.CONSTANTS.dental_tooth()));
       	panel.setWidget(1, 2, tooth);
   
       	panel.setWidget(0, 3, new HTML(Jitavej.CONSTANTS.dental_surfaces()));
       	panel.setWidget(1, 3, surfaces);

       	panel.setWidget(1, 4, add);

        sum = new TextBox();
        sum.setStyleName("sum-black");
        sum.setWidth("100");
        sum.setEnabled(false);

		ActionBar bar2 = new ActionBar();
		bar2.setStyleName("dental-bar");
		bar2.add(new HTML(Jitavej.CONSTANTS.dentist()));
		bar2.add(dentist);		
		bar2.add(new HTML("<div style='width:300px'></div>"));
		bar2.add(clear);
		bar2.add(sum);

 		add(panel);
        add(grid);
        add(bar2);
        
	}
	
	public static void addItem(){
		try {	
			String name = suggestBox.getText();
	    //    record.put("recordDrugs", orders);
        
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/item/findByName");
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("name=" + URL.encodeComponent(name.trim()));
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
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
							orderItem.put("tooth", new JSONString(tooth.getText()));
							orderItem.put("surfaces", new JSONString(surfaces.getText()));
							try{
								orderItem.put("qty", new JSONNumber(Integer.parseInt(qty.getText().trim())));
							}catch(Exception ex){
								orderItem.put("qty", new JSONNumber(1));
							}
							
							orderItem.put("fee", item.get("price").isNumber());
							orderItem.put("sum", new JSONNumber(orderItem.get("qty").isNumber().doubleValue() * item.get("price").isNumber().doubleValue()));
							orderItems.set(orderItems.size(), orderItem);
							store.removeAll();
							GWT.log("recordLabs > "+ orderItems.toString(), null);
							store.loadJsonData(orderItems.toString(), true); 
							suggestBox.setText("");
							qty.setText("");
							tooth.setText("");
							surfaces.setText("");
							
							suggestBox.setFocus(true);
							update();
						}
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
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
					GWT.log("Error response received during authentication of user", exception);
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
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});
			

		} catch (RequestException e1) {
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
		}else{
		//	this.sum.setCls("sum-red");
		//	this.sum.setStyle("color: red;font-size: 20px;font-weight: bold;width:100px;height: 24px;");
		}
		sum.setText(""+sum2);
	//	LabHistoryPanel.update();
	}
	
	public static void setPatient(JSONObject patient){
		orderItems = new JSONArray();
		store.removeAll();
		dentist.setSelectedIndex(0);
	}
	
	
}
