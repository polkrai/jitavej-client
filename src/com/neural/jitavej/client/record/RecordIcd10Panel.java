package com.neural.jitavej.client.record;

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
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.LazyPanel;
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
import com.neural.jitavej.client.autocomplete.Icd10SuggestOracle;

public class RecordIcd10Panel extends LazyPanel<VerticalPanel>  {
	
	SuggestBox suggestBox;
	Button re;
	TextBox textBox;
	public static Store store;
	
	public static JSONArray icd10Items = new JSONArray();

	public RecordIcd10Panel() {
	}

	@Override
	public VerticalPanel createWidget() {

		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
            new StringFieldDef("code", "code"), 
            new StringFieldDef("name", "name"), 				
			
		});
		
		JsonReader reader = new JsonReader(recordDef);
		//reader.setRoot("data");
		//reader.setTotalProperty("totalCount");
		String[][] itemList = new String[][] {{"", ""}, {"", ""}};

		DataProxy dataProxy = new MemoryProxy(itemList);
		store = new Store(dataProxy, reader);
		
        ColumnConfig colType = new ColumnConfig(" ", "name", 24, false, new Renderer() {
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
        		return Format.format("<img src='images/cross.gif'/>", "");
        	}
        });
        
        BaseColumnConfig[] columns = new BaseColumnConfig[]{  
            new RowNumberingColumnConfig(), 
            new ColumnConfig(Jitavej.CONSTANTS.record_code(), "code", 150, true),
            new ColumnConfig(Jitavej.CONSTANTS.record_icd10(), "name", 600, true),
            colType,			
        }; 
         
         ColumnModel columnModel = new ColumnModel(columns);  
         //columnModel.setColumnWidth(0, 30);
         
		GridPanel grid = new GridPanel();
		
        //grid.setWidth(850);
		grid.setWidth(930);
		//grid.setHeight(440);
		
		//MessageBox.setMinWidth(250);
		
		//MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.mode);
        
        if(Jitavej.mode.equals("record_ipd") || Jitavej.mode.equals("record_oneday") || Jitavej.mode.equals("record_unitdose") || Jitavej.mode.equals("record_homemed")){
        	
        	 grid.setHeight(360); 
        }
        else if(Jitavej.mode.equals("record_continuation")){
        	
        	grid.setHeight(340); 
        }
        else {
        	
        	grid.setHeight(410); 
        }
        
		grid.setBorder(false);
		grid.setMargins(1);
		grid.setPaddings(1);
		//grid.setTitle("Local Json Grid");
		
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		
		grid.setFrame(false);
		//grid.setBodyBorder(true);
		//grid.setHideBorders(true);
		
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

		GridView view = new GridView();
		view.setEmptyText(Jitavej.CONSTANTS.record_no_icd10());
		grid.setView(view);

		grid.addGridCellListener(new GridCellListenerAdapter() {
			
			public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
				
				if (colindex == 3) {
					
					MessageBox.confirm("Confirm", Jitavej.CONSTANTS.delete_confirm(), new MessageBox.ConfirmCallback() {
						
						public void execute(String btnID) {
							
							if(btnID.equals("no")){
								
								return;
							}
							
							JSONArray icd10Items0 = icd10Items;
							
							icd10Items = new JSONArray();
							
							for(int i=0;i<icd10Items0.size();i++){
								
								JSONObject icd10 = icd10Items0.get(i).isObject();
								
								if(i != rowIndex){
									
									icd10Items.set(icd10Items.size(), icd10);
								}
								
							}
							
							store.removeAll();
							
							store.loadJsonData(icd10Items.toString(), true); 
						}
					});
				}
			}

		});

		Button add = new Button(Jitavej.CONSTANTS.add_icd10(), new ClickListener() {
			public void onClick(Widget sender) {
				addItem();	
			}
		});
		

		Button clear = new Button(Jitavej.CONSTANTS.clear_item(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				JSONArray icd10Items0 = icd10Items;
				
				icd10Items = new JSONArray();
				
				for(int i=0;i<icd10Items0.size();i++){
					
					JSONObject icd10 = icd10Items0.get(i).isObject();
					
					if(i != i){
					
						icd10Items.set(icd10Items.size(), icd10);
					}
					
				}
				
				store.removeAll();
				
				store.loadJsonData(icd10Items.toString(), true); 
			}
		});
		
        textBox = new TextBox(); 
        textBox.addKeyboardListener(new KeyboardListenerAdapter(){ 
            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		        if (keyCode == (char) KEY_ENTER && !textBox.getText().equals("")) {
		        	if(!suggestBox.suggestionPopup.isAttached()){
		        		addItem();
		        	}
		        }
            } 
        }); 
        
        Icd10SuggestOracle oracle = new Icd10SuggestOracle();
        suggestBox = new SuggestBox(oracle, textBox);
        suggestBox.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
        suggestBox.setWidth("250");

        ActionBar panel2 = new ActionBar();
        
		panel2.add(new HTML("Icd10:"));
		panel2.add(suggestBox);
		panel2.add(add);
		panel2.add(new HTML("<div style='width:400px'></div>"));
		panel2.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

		ActionBar bar = new ActionBar();
		//bar.add(re);
		bar.add(clear);
		bar.add(new HTML("<div style='width:400px'></div>"));
	
		VerticalPanel ver = new VerticalPanel();
		ver.add(panel2);	
		ver.add(grid);
		ver.add(bar);

		return ver;
	}
	
	public void addItem(){
		
		try {	
			
			String code = textBox.getText().trim();
			
			GWT.log(" suggestBox.suggestionPopup.isVisible(); " + suggestBox.suggestionPopup.isAttached(), null);
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/icd10/findByCode");
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("code=" + URL.encodeComponent(code));
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						JSONObject icd10 = value.isObject();
						
						if(icd10.get("code") != null){
							
							icd10Items.set(icd10Items.size(), icd10);
							store.removeAll();
							store.loadJsonData(icd10Items.toString(), true); 
							suggestBox.setText("");
						}
						else {
							
							MessageBox.alert(Jitavej.CONSTANTS.add_icd10_alert());
							
							//Window.alert(Jitavej.CONSTANTS.add_icd10_alert());
						}
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
	
	public static void setPatient(JSONObject patient){
		
		icd10Items = new JSONArray();
		
		if(store != null){
			
			store.removeAll();	
		}
		
		
		if(patient != null && !Jitavej.mode.equals("dental")){
			
			try {
				
				RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/lasticd10s");
				rb.setTimeoutMillis(30000);
				
				StringBuffer params = new StringBuffer();
				
				params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
				
				rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
				
				rb.sendRequest(params.toString(), new RequestCallback() {
					
					public void onError(Request request, Throwable exception) {
						
						GWT.log("Error response received during authentication of user", exception);
					}
					
					public void onResponseReceived(Request request, Response response) {
						
						if (response.getStatusCode() == 200) {
							
							JSONValue value = JSONParser.parse(response.getText());
							
							icd10Items = value.isArray();
							
							store.loadJsonData(icd10Items.toString(), true); 
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
		else if(patient != null && Jitavej.mode.equals("dental")){
			
			try {	
				RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/lastdentalicd10s");
				rb.setTimeoutMillis(30000);	
				StringBuffer params = new StringBuffer();
				params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
				rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
				rb.sendRequest(params.toString(), new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						GWT.log("Error response received during authentication of user", exception);
					}
					public void onResponseReceived(Request request, Response response) {
						if (response.getStatusCode() == 200) {
							JSONValue value = JSONParser.parse(response.getText());
							icd10Items = value.isArray();
							store.loadJsonData(icd10Items.toString(), true); 
						} else {
							Window.alert("response.getText() "+response.getText());
						}
					}
				});

			} 
			catch (RequestException e1) {
				GWT.log("Error > ", e1);
			}			
		}
	}

}
