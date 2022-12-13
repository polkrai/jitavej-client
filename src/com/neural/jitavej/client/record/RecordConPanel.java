package com.neural.jitavej.client.record;

import java.util.Date;import com.google.gwt.core.client.GWT;import com.google.gwt.http.client.Request;import com.google.gwt.http.client.RequestBuilder;import com.google.gwt.http.client.RequestCallback;import com.google.gwt.http.client.RequestException;import com.google.gwt.http.client.Response;import com.google.gwt.http.client.URL;import com.google.gwt.json.client.JSONArray;import com.google.gwt.json.client.JSONBoolean;import com.google.gwt.json.client.JSONNumber;import com.google.gwt.json.client.JSONObject;import com.google.gwt.json.client.JSONParser;import com.google.gwt.json.client.JSONString;import com.google.gwt.json.client.JSONValue;import com.google.gwt.user.client.DeferredCommand;import com.google.gwt.user.client.Window;import com.google.gwt.user.client.ui.Button;import com.google.gwt.user.client.ui.ChangeListener;import com.google.gwt.user.client.ui.ClickListener;import com.google.gwt.user.client.ui.FocusListener;import com.google.gwt.user.client.ui.Grid;import com.google.gwt.user.client.ui.HTML;import com.google.gwt.user.client.ui.HorizontalPanel;import com.google.gwt.user.client.ui.KeyboardListenerAdapter;import com.google.gwt.user.client.ui.ListBox;import com.google.gwt.user.client.ui.SuggestBox;import com.google.gwt.user.client.ui.TextBox;import com.google.gwt.user.client.ui.VerticalPanel;import com.google.gwt.user.client.ui.Widget;import com.google.gwt.widgetideas.client.LazyPanel;import com.gwtext.client.core.EventCallback;import com.gwtext.client.core.EventObject;import com.gwtext.client.data.FieldDef;import com.gwtext.client.data.JsonReader;import com.gwtext.client.data.MemoryProxy;import com.gwtext.client.data.Record;import com.gwtext.client.data.RecordDef;import com.gwtext.client.data.Store;import com.gwtext.client.data.StringFieldDef;import com.gwtext.client.util.Format;import com.gwtext.client.widgets.MessageBox;import com.gwtext.client.widgets.form.Field;import com.gwtext.client.widgets.form.TextField;import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;import com.gwtext.client.widgets.grid.BaseColumnConfig;import com.gwtext.client.widgets.grid.CellMetadata;import com.gwtext.client.widgets.grid.ColumnConfig;import com.gwtext.client.widgets.grid.ColumnModel;import com.gwtext.client.widgets.grid.EditorGridPanel;import com.gwtext.client.widgets.grid.GridEditor;import com.gwtext.client.widgets.grid.GridPanel;import com.gwtext.client.widgets.grid.Renderer;import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;import com.neural.jitavej.client.ActionBar;import com.neural.jitavej.client.Jitavej;import com.neural.jitavej.client.autocomplete.DoseSuggestOracle;import com.neural.jitavej.client.autocomplete.DrugSuggestOracle;

public class RecordConPanel extends LazyPanel<VerticalPanel>  {
	
	SuggestBox suggestDrug, suggestDose;
	HTML drugText, doseText;
	TextBox qty;
	ListBox day, reason;
	JSONObject dose;	
	JSONArray doses;
	
	public TextBox sum;
	public JSONArray orderItems = new JSONArray();
	public JSONArray drugErrors = new JSONArray();	
	public Store store;
	public Button remed, clear;
	public JSONObject drug;
	public String mode;
	public EditorGridPanel grid;
	public ActionBar addBar;
	public Button ipdsave; 
	public StringBuffer errorMessage = new StringBuffer();
	public StringBuffer interactionIsorder = new StringBuffer();
	
	/*
	public static TextBox sum;
	public static JSONArray orderItems = new JSONArray();
	public JSONArray drugErrors = new JSONArray();	
	public static Store store;
	public Button remed, clear;
	public JSONObject drug;
	public static String mode;
	public EditorGridPanel grid;
	public static ListBox remedBox;
	public ActionBar addBar;
	public Button ipdsave; 
	public StringBuffer errorMessage = new StringBuffer();
	public StringBuffer interactionIsorder = new StringBuffer();
	*/
	
	public RecordConPanel(String mode) {
		
		this.mode = mode;
		
		//Window.alert("RecordCon => " + this.mode);

		/*		
 		remed = new Button("Remed", new ClickListener() {
			public void onClick(Widget sender) {
				DrugHistoryDialog drugHistoryDialog = new DrugHistoryDialog(RecordConPanel.this);
				drugHistoryDialog.show();
				drugHistoryDialog.center();					
			}
		});
		*/	

		ipdsave = new Button("<b><font style='color:blue;'>"+ Jitavej.CONSTANTS.record_save() + "</font></b>");
		
		if(mode.equals("confirm")){
			
			ipdsave.setHTML("<b><font style='color:blue;'>"+ Jitavej.CONSTANTS.record_conconfirm() + "</font></b>");
		}
		
		ipdsave.setPixelSize(180, 50);
		
		ipdsave.addClickListener(new ClickListener() {
			
			public void onClick(com.google.gwt.user.client.ui.Widget sender) {
				
				if (orderItems.size() > 0) {					
					if (RecordIcd10Panel.icd10Items.size() == 0) {
						
						Window.alert(Jitavej.CONSTANTS.record_alert_icd10());
						return;
					}
					
					final PinCodeConDialog dialog = new PinCodeConDialog(RecordConPanel.this);
					
					dialog.show();
					
					dialog.center();
					
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						
						public void execute() {
							
							dialog.pincode.setFocus(true);
						}
					});
				} 
			}
		});			

			
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("drug", "drugaccount"), 	
			new StringFieldDef("dose", "dose.code"), 
			new StringFieldDef("reason", "reason"), 
			new StringFieldDef("balance", "balance"), 	
			new StringFieldDef("return", "return"), 
			new StringFieldDef("remain", "remain"), 
			new StringFieldDef("qty", "qty"), 	
			new StringFieldDef("fee", "drug.item.price"), 	
			new StringFieldDef("sum", "sum")
		});
		
		JsonReader reader = new JsonReader(recordDef);
		//reader.setRoot("data");
		//reader.setTotalProperty("totalCount");
		String[][] itemList = new String[][] {};
		
		MemoryProxy dataProxy = new MemoryProxy(itemList);
		
		store = new Store(dataProxy, reader);
		
        sum = new TextBox();
        
        sum.setStyleName("sum-black");
        
        sum.setWidth("100");
        
        sum.setEnabled(false);

	}
	
	public int row;

	@Override
	public VerticalPanel createWidget() {

		ActionBar bar = new ActionBar();
		
		bar.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		//bar.add(hor);
		
		bar.add(new HTML("<div style='width:0px'></div>"));
		//bar.add(remed);
		
		bar.add(new HTML("<div style='width:200px'></div>"));
		
		bar.add(ipdsave);
		
		ColumnConfig commonCol = new ColumnConfig(Jitavej.CONSTANTS.record_number2(), "qty", 80, true, null, "qty");
		
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
					
					if(newVal != null){
						
						orderItems.get(row).isObject().put("qty", new JSONNumber(Double.parseDouble(newVal.toString())));
						
						update();						
					}
					else{
						
						orderItems.get(row).isObject().put("qty", new JSONNumber(0));
						
						update();
					}

				} 
				catch (Exception e) {
					
					update();
				}
				
				GWT.log("onChange on row = " + row  , null);
			}
		});
		
		text.addKeyPressListener(new EventCallback(){

			public void execute(EventObject e) {
				
				row = grid.getCellSelectionModel().getSelectedCell()[0];
			}
			
		});
		
		GridEditor editor = new GridEditor(text);
		
		commonCol.setEditor(editor);

		ColumnConfig commonCol2 = new ColumnConfig(Jitavej.CONSTANTS.record_return(), "return", 70, true, null, "return");
		
		final TextField text2 = new TextField();
		
		text2.addListener(new TextFieldListenerAdapter(){
			
			@Override
			public void onFocus(Field field) {
				
				text2.selectText();
			}
		});
		
		text2.addListener(new TextFieldListenerAdapter(){
			
			public void onChange(Field field, Object newVal, Object oldVal) {
				
				try {
					
					if(newVal != null){
						
						orderItems.get(row).isObject().put("return", new JSONNumber(Integer.parseInt(newVal.toString())));
						
						int balance = Integer.parseInt(orderItems.get(row).isObject().get("balance").isNumber().toString());
						
						int remain = balance - Integer.parseInt(newVal.toString());
						
						orderItems.get(row).isObject().put("remain", new JSONNumber(remain));
	
					}					else{
						
						orderItems.get(row).isObject().put("return", new JSONString(""));
						
						orderItems.get(row).isObject().put("remain", new JSONString(""));
					}
					//store.removeAll();
					//store.loadJsonData(orderItems.toString(), true); 
					update();
				} 				catch (Exception e) {
					
					update();
				}
				
				GWT.log("onChange on row = " + row  , null);
			}
		});
		
		text2.addKeyPressListener(new EventCallback(){

			public void execute(EventObject e) {
				
				row = grid.getCellSelectionModel().getSelectedCell()[0];
			}
			
		});
		
		GridEditor editor2 = new GridEditor(text2);
		
		commonCol2.setEditor(editor2);
			
        ColumnConfig colType = new ColumnConfig("ON/OFF", "drug", 75, false, new Renderer() {
           
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
        	   
        	   JSONObject item = orderItems.get(rowIndex).isObject();
        	   
        	   if(item.get("id") != null){
        		   
        		   if(item.get("stop").isBoolean().booleanValue()){
        			   
                       return Format.format("<img src='images/off.png'/>", "");
        		   }        		   else{
        			   
        			   return Format.format("<img src='images/on.png'/>", "");
        		   }
        	   }
        	   
               return Format.format("<img src='images/cross.gif'/>", "");
            }
        });
        
        ColumnConfig colHide = new ColumnConfig(null, "interaction", 70, true, null, "interaction");
        			 colHide.setHidden(true);

        BaseColumnConfig[] columns = new BaseColumnConfig[]{
        		 
            new RowNumberingColumnConfig(),
            new ColumnConfig(Jitavej.CONSTANTS.record_item(), "drug", 170, true),
            new ColumnConfig(Jitavej.CONSTANTS.record_dose(), "dose", 155, true),
            new ColumnConfig(Jitavej.CONSTANTS.record_reason(), "reason", 50, true),
            new ColumnConfig(Jitavej.CONSTANTS.record_balance(), "balance", 80, true),
            commonCol2,
            new ColumnConfig(Jitavej.CONSTANTS.record_remain(), "remain", 60, true),
            commonCol,
            new ColumnConfig(Jitavej.CONSTANTS.record_fee(), "fee", 70, true),
            new ColumnConfig(Jitavej.CONSTANTS.record_sum(), "sum", 70, true),
            colType,
            //colHide,
        }; 
         
        ColumnModel columnModel = new ColumnModel(columns);
         
        grid = new EditorGridPanel();
		grid.setBorder(false);
		grid.setMargins(1);
		grid.setPaddings(1);
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setClicksToEdit(1);
		grid.setStripeRows(true);
		grid.setFrame(false);
		grid.setBodyBorder(true);
		//grid.setHideBorders(true);
        //grid.setWidth(930);
        
        //MessageBox.setMinWidth(250);
		
        //MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.mode);
        
		//Window.alert(mode.toString());
		
		if(Jitavej.mode.equals("record_continuation")){
			
			//Window.alert("record_continuation");
			
			loadDrugError();
        }
        
        if(Jitavej.mode.equals("record_oneday") || Jitavej.mode.equals("record_unitdose") || Jitavej.mode.equals("record_continuation") || Jitavej.mode.equals("record_homemed")){
        	
        	//loadDrugError();
        	
        	grid.setHeight(202);
        }
        else if(Jitavej.mode.equals("remed")){
        	
        	grid.setHeight(302); 
        }
        else{
        	
        	grid.setHeight(282);
        }
        
        grid.setWidth(930);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);
		grid.addGridCellListener(new GridCellListenerAdapter() {
			
			public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
				
				if(Jitavej.mode.equals("backmed")){
					
					return;
				}
				
				if (colindex == 2) {
					
					final DoseEditConDialog dialog = new DoseEditConDialog(rowIndex, RecordConPanel.this);
					dialog.show();
					dialog.center();
					
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						
						public void execute() {
							
							dialog.suggestDose.setFocus(true);
						}
					});
					
				}
				
				if (colindex == 3) {
					
					final ReasonEditConDialog dialog = new ReasonEditConDialog(rowIndex, RecordConPanel.this);
					dialog.show();
					dialog.center();
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						public void execute() {
							dialog.reason.setFocus(true);
						}
					});
					
				}
				
				if (colindex == 10) {
					
	        	   JSONObject item = orderItems.get(rowIndex).isObject();

	        	   if(item.get("id") == null){
	        		   
						MessageBox.confirm("Confirm", "Confirm to delete?", new MessageBox.ConfirmCallback() {
							
							public void execute(String btnID) {
								
								if(btnID.equals("no")){
									
									return;
								}
								
								JSONArray recordDrugs0 = orderItems;
								
								orderItems = new JSONArray();
								
								for(int i=0;i<recordDrugs0.size();i++){
									
									JSONObject icd10 = recordDrugs0.get(i).isObject();
									
									if(i != rowIndex){
										
										orderItems.set(orderItems.size(), icd10);
									}
								}
								
								store.removeAll();
								
								store.loadJsonData(orderItems.toString(), true); 
								
								update();
							}
						});
	        	   }	        	   else if(item.get("stop").isBoolean().booleanValue()){
	        		   
	        		   item.put("stop", JSONBoolean.getInstance(false));
	        		   
	        		   update();
	        	   }	        	   else{
	        		   
	        		   item.put("stop", JSONBoolean.getInstance(true));
	        		   
	        		   try{
	        			   
		        		   if(item.get("return").isString() != null && item.get("return").isString().stringValue().equals("")){
		        			   
		        			   item.put("return", new JSONNumber(0));
		        		   }	        			   
	        		   }
	        		   catch(Exception ex){
	        			   
	        		   }
	        		   
	        		   update();
	        	   }

				}
			}
		});
		
		Button add = new Button(Jitavej.CONSTANTS.add_drug(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				addItem();
				
			}
		});
		
		add.setPixelSize(100, 55);		
		clear = new Button("Clear all", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				orderItems = new JSONArray();
				store.removeAll();
				suggestDrug.setText("");
				suggestDose.setText("");
				qty.setText("");
			}
		});
		
		final DrugSuggestOracle oracle = new DrugSuggestOracle();
        suggestDrug = new SuggestBox(oracle);
        suggestDrug.setLimit(5);   //Set the limit to 5 suggestions being shown at a time 
        suggestDrug.setPixelSize(300, 18);
        suggestDrug.setStyleName("drug");
        suggestDrug.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				
		        if (keyCode == (char) KEY_ENTER) {
		        	
					if(!suggestDrug.suggestionPopup.isAttached()){
						
						suggestDose.setFocus(true);
					}
		        }
		        
				super.onKeyPress(sender, keyCode, modifiers);
			}

        });
     
 
		final DoseSuggestOracle doseOracle = new DoseSuggestOracle();
        suggestDose = new SuggestBox(doseOracle);
        suggestDose.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
        suggestDose.setPixelSize(360, 18);
        suggestDose.setStyleName("drug");
        suggestDose.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				
		        if (keyCode == (char) KEY_ENTER) {
		        	
					if(!suggestDose.suggestionPopup.isAttached()){
						
						if(drug != null && drug.get("account").isString().stringValue().equals("NED")){
							
							reason.setFocus(true);
						}
						else{
							
			        		qty.setFocus(true);
						}
					}
		        }
		        
				super.onKeyPress(sender, keyCode, modifiers);
			}

        });
        
        suggestDose.addFocusListener(new FocusListener(){
        	
			public void onFocus(Widget sender) {
				
				GWT.log(suggestDrug.getText(), null);
				
				loadDrug();
			}

			public void onLostFocus(Widget sender) {
				//loadDose();
			}
        });
        
    	reason = new ListBox();
 		reason.addItem("");
 		reason.addItem("A");
 		reason.addItem("B");
 		reason.addItem("C");
 		reason.addItem("D");
 		reason.addItem("E");
 		reason.addItem("F");
 		reason.addItem("IPD");
 		
 		reason.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				
		        if (keyCode == (char) KEY_ENTER) {
		        	
		        	day.setFocus(true);
		        }
		        
				super.onKeyPress(sender, keyCode, modifiers);
			}

        });
     
        day = new ListBox();
        day.setStyleName("day");
        day.setPixelSize(80, 20);
        day.addItem("0", "0");
        day.addItem("1", "1");
        day.addItem("2", "2");
        day.addItem("3", "3");
        day.addItem("4", "4");
        day.addItem("5", "5");
        day.addItem("6", "6");
        day.addItem("7", "7");
        day.addItem("8", "8");
        day.addItem("9", "9");
        day.addItem("10", "10");
        day.addItem("11", "11");
        day.addItem("12", "12");
        day.addItem("13", "13");
        day.addItem("14", "14");
        
        if(Jitavej.building != null){
        	
            int period = (int)Jitavej.building.get("day").isNumber().doubleValue();
            
            //Window.alert(Jitavej.building.get("day").toString());
            
            if (period > 0) {
            	
	            Date date = new Date();
	            int now = date.getDay();
	            if(period <= now){
	            	period += 7;
	            }
	            
	            day.setSelectedIndex(period - now + 1);
            }
            else {
            	
            	day.setSelectedIndex(0);
            }
        }
        
        day.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestDrug.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	qty.setFocus(true);
		        }
				super.onKeyPress(sender, keyCode, modifiers);
			}
        });       
        day.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
		        calculateQty();
			}
        });
 
        qty = new TextBox();
        qty.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				String name = suggestDrug.getText().trim();
		        if (keyCode == (char) KEY_ENTER && !name.equals("")) {
		        	addItem();
		        }
				super.onKeyPress(sender, keyCode, modifiers);
			}
        });
        
        qty.addFocusListener(new FocusListener(){
        	
			public void onFocus(Widget sender) {
				//calculateQty();
				loadDrug();
				loadDose();
			}

			public void onLostFocus(Widget sender) {
				
			}
        });
        
        qty.setPixelSize(48, 18);
        qty.setStyleName("drug");
        drugText = new HTML();
		doseText = new HTML();
        addBar = new ActionBar();
        addBar.setStyleName("drug-bar");
        
       	Grid panel = new Grid(3,10);
       	panel.setWidget(0, 0, new HTML("Drug:"));
       	panel.setWidget(1, 0, suggestDrug);
    	panel.setWidget(2, 0, drugText);
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hor1.setSpacing(0);
		hor1.add(doseText);
		hor1.setPixelSize(360, 18);		
       	panel.setWidget(0, 1, new HTML("Dose:"));
       	panel.setWidget(1, 1, suggestDose);
       	panel.setWidget(2, 1, hor1); 	
    	panel.setWidget(0, 2, new HTML("<div style='width:10px'></div>"));
    	panel.setWidget(0, 3, new HTML("Reason"));
       	panel.setWidget(1, 3, reason);
       	
		if(mode.equals("continuation")){
			
	       	panel.setWidget(0, 4, new HTML("Day"));
	       	panel.setWidget(1, 4, day);
		}
		
       	panel.setWidget(0, 5, new HTML("Qty."));
       	panel.setWidget(1, 5, qty);
    	addBar.add(panel);
    	addBar.add(add);
    	
		ActionBar bar2 = new ActionBar();
        bar2.setStyleName("drug-bar");
        
        if(!Jitavej.mode.equals("remed")){
        	
        	bar2.add(clear); 
        	bar2.add(new HTML("<div style='width:40px'></div>"));
        	bar2.add(new HTML(Jitavej.CONSTANTS.returnhint1()+"<br>"+Jitavej.CONSTANTS.returnhint2()+"<br>"+Jitavej.CONSTANTS.returnhint3()));
        }
        
		bar2.add(new HTML("<div style='width:320px'></div>"));
		bar2.add(new HTML(Jitavej.CONSTANTS.record_sumall()));
		bar2.add(sum);		
		VerticalPanel ver = new VerticalPanel();
		ver.add(bar);
		
        if(!Jitavej.mode.equals("remed")){
        	
        	ver.add(addBar);	
        }	        
		ver.add(grid);
		ver.add(bar2);
		
		/*	
		if(mode.equals("continuation")){
			remedcon();
		}
		 */
		return ver;
	}
	
	public void addItem(){
		
		//Window.alert(drug.toString() + ">> addItem");
		
		try {
			
			if(drug == null){
				
				Window.alert(Jitavej.CONSTANTS.record_alert_drug());
				
				return;
			}	
			
			if(drug.get("account").isString().stringValue().equals("NED") && reason.getSelectedIndex() == 0){
				
				Window.alert(Jitavej.CONSTANTS.record_alert_reason());
				
				reason.setFocus(true);
				
				return;
			}	
			
			if(dose == null){
				
				Window.alert(Jitavej.CONSTANTS.record_alert_dose());
			}	
			
			if(drug.get("name") == null){
				
				return;
			}
			
			String name = suggestDrug.getText();
			
			String dose_id = suggestDose.getText();
			
			if(name.trim().equals("") || dose_id.trim().equals("") ){
				
				Window.alert(Jitavej.CONSTANTS.record_alert_add());
			}
			
			checkInteraction(drug.get("generic").isObject().get("name").isString().stringValue());
			
			checkDrugError(drug.get("generic").isObject().get("name").isString().stringValue());
			
			if(drug.get("generic").isObject().get("risk").isBoolean().booleanValue()){
				
				errorMessage.append(Jitavej.CONSTANTS.record_risk() + " >> " + drug.get("generic").isObject().get("name").isString().stringValue());
				
			}
			
			//Window.alert(interactionIsorder.toString());
			
			if(errorMessage.toString().length() > 0){								if (interactionIsorder.toString().equals("t")) {										MessageBox.alert(Jitavej.CONSTANTS.record_interaction(), errorMessage.toString() + "<br /> กรุณาเลือกหรือค้นหารายการยาใหม่");										suggestDrug.setText("");					suggestDose.setText("");					drugText.setText("");					doseText.setText("");					reason.setSelectedIndex(0);					qty.setText("");					drug = null;					dose = null;					doses = null;					suggestDrug.setFocus(true);									}				else {
					
					MessageBox.confirm(Jitavej.CONSTANTS.record_comfirm(), errorMessage.toString(), new MessageBox.ConfirmCallback() {	
						public void execute(String btnID) {	
							if(btnID.equals("no")){
								
								suggestDrug.setText("");
								suggestDose.setText("");
								drugText.setText("");
								doseText.setText("");
								reason.setSelectedIndex(0);
								qty.setText("");
								drug = null;
								dose = null;
								doses = null;
								suggestDrug.setFocus(true);	
								return;
							}
							
							checkStockWhenAdd();	
	   					}	
					});
				}
			} 			else {
				
				checkStockWhenAdd();
			}
			
			errorMessage = new StringBuffer();						interactionIsorder = new StringBuffer();
		} 		catch (Exception e1) {
			
			Window.alert(e1.toString());
		}		
	}

	public void addOrderItem(){
		
		try{
			
			GWT.log("drug.get('name') > "+ drug.get("name"), null);
			
			JSONObject orderItem = new JSONObject();
			
			orderItem.put("drug", drug);
			orderItem.put("drugaccount", new JSONString(drug.get("code").isString().stringValue() +" <b>(" + drug.get("account").isString().stringValue() + ")</b>"));
			orderItem.put("drugId", drug.get("id"));
			orderItem.put("genericId", drug.get("generic").isObject().get("name").isString());
			orderItem.put("dose", dose);
			
			if(reason.getSelectedIndex() > 0){
				
				orderItem.put("reason", new JSONString(reason.getValue(reason.getSelectedIndex())));	
			}
			
			orderItem.put("price", drug.get("item").isObject().get("price").isNumber());
			orderItem.put("balance", new JSONNumber(0));	
			orderItem.put("return", new JSONString(""));	
			orderItem.put("stop", JSONBoolean.getInstance(false));	
			orderItem.put("qty", new JSONNumber(Double.parseDouble(qty.getText())));
			orderItem.put("sum", new JSONNumber(Double.parseDouble(qty.getText())*drug.get("item").isObject().get("price").isNumber().doubleValue()));			
			orderItems.set(orderItems.size(), orderItem);
			
			GWT.log("recordDrugs > "+ orderItems.toString(), null);
			
			suggestDrug.setText("");
			suggestDose.setText("");
			drugText.setText("");
			doseText.setText("");
			reason.setSelectedIndex(0);
			qty.setText("");
			drug = null;
			dose = null;
			doses = null;
			suggestDrug.setFocus(true);
			
			update();	
		} 		catch (Exception e1) {
			
			Window.alert(e1.toString());
			qty.setFocus(true);
		} 

	}
	
	public void calculateQty(){
		
		if(dose == null){
			return;
		}
		int longday = Integer.parseInt(day.getValue(day.getSelectedIndex()));
		double number = dose.get("pertime").isNumber().doubleValue();
		double frequency = dose.get("frequency").isNumber().doubleValue();
		
		if(frequency != 0){
			
			qty.setText(""+((int)(longday*number*(24/frequency))));
		}		else{
			
			qty.setText("0");
		}
	}
	
	/*
	public void checkInteractionBeforeSave () {
		
		if(Jitavej.interactions == null){
			
			Jitavej.loadInteractions();
		}
		
		if(orderItems.size() == 0){
			
			return;
		}
		
		try {
			
			//Window.alert("nn");
		
			for(int i = 0; i < orderItems.size(); i++) {
				
				JSONObject orderItem = orderItems.get(i).isObject();
				
				String generic = orderItem.get("genericId").isString().stringValue();
				
				Window.alert(generic.toString());
				
				//checkInteraction(generic);
			}
		} 
		catch (Exception e) {
			
			GWT.log(e.toString(), null);
		}
	}
	*/
	
	public void checkInteraction(String generic1){
		
		//Window.alert(generic1);
		
		if(Jitavej.interactions == null){
			
			Jitavej.loadInteractions();
		}
		
		if(orderItems.size() == 0){
			
			return;
		}
		
		try {
			
			for(int i = 0; i < orderItems.size(); i++) {
				
				JSONObject orderItem = orderItems.get(i).isObject();
				
				try {
					
					String generic2 = orderItem.get("genericId").isString().stringValue();					//String notorder = orderItem.get("generic").isObject().get("name").isString().stringValue();
					
					if(!generic1.equals(generic2)){
						
						for(int k = 0; k < Jitavej.interactions.size(); k++) {
							
							JSONObject interaction = Jitavej.interactions.get(k).isObject();
							
							if((interaction.get("generic1").isString().stringValue().equals(generic1) && interaction.get("generic2").isString().stringValue().equals(generic2)) || ((interaction.get("generic1").isString().stringValue().equals(generic2) && interaction.get("generic2").isString().stringValue().equals(generic1)))) {																interactionIsorder.append(interaction.get("notorder").isString().stringValue());																errorMessage.append(Jitavej.CONSTANTS.record_interaction()+" >> " + generic1 + " && " +generic2 + " <br />");								return;
							}
						}
					}
				} 
				catch (Exception e) {
					
					e.printStackTrace();
				}

			}
		} 
		catch (Exception e) {
			
			GWT.log(e.toString(), null);
		}

	}
	
	public void checkDrugError(String generic1){
		
		if(Jitavej.interactions == null){
			
			Jitavej.loadInteractions();
		}
		
		try {
			
			for(int i = 0; i < drugErrors.size(); i++) {
				
				JSONObject drugError = drugErrors.get(i).isObject();
				
				String generic2 = drugError.get("generic").isObject().get("name").isString().stringValue();
				
				if(generic1.equals(generic2)){
					
					errorMessage.append(Jitavej.CONSTANTS.record_error()+" > " + generic1 + " <br />");
					
					return;
				}

			}
		} 
		catch (Exception e) {
			
			GWT.log(e.toString(), null);
		}
	}
	
	public void update(){
		
		store.removeAll();
		
		try {
			
			double sumtmp = 0;
			
			for(int i=0;i<orderItems.size();i++){
				
				JSONObject orderItem = orderItems.get(i).isObject();
				
				double qty = orderItem.get("qty").isNumber().doubleValue();
				
				double price = orderItem.get("price").isNumber().doubleValue();
				
				orderItem.put("drugaccount", new JSONString(orderItem.get("drug").isObject().get("code").isString().stringValue() +" <b>("+orderItem.get("drug").isObject().get("account").isString().stringValue() +")</b>"));
				
				orderItem.put("sum", new JSONNumber(qty * price));
				
				sumtmp += orderItem.get("sum").isNumber().doubleValue();
			}
			
			sum.setText(""+sumtmp);
		} 		catch (Exception e) {
			
		}
		
		store.loadJsonData(orderItems.toString(), true); 
	}
 
	public void remed(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;			
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/lastdrugs");
			
			rb.setTimeoutMillis(30000);
			
			StringBuffer params = new StringBuffer();
			
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						orderItems = value.isArray();
						
						for (int i = 0; i < orderItems.size(); i++) {
							
							final JSONObject orderItem = orderItems.get(i).isObject();
							
							double qty = orderItem.get("qty").isNumber().doubleValue();
							
							double price = orderItem.get("price").isNumber().doubleValue();
							
							orderItem.put("sum", new JSONNumber(qty * price));
						}	
						
						store.loadJsonData(orderItems.toString(), true); 
						
						update();
					} 					else {
						
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			GWT.log("Error > ", e1);
		}						
	}
	
	public void remedcon(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/remedcon");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						JSONValue value = JSONParser.parse(response.getText());
						
						orderItems = value.isArray();
						
						for (int i = 0; i < orderItems.size(); i++) {
							
							final JSONObject orderItem = orderItems.get(i).isObject();
							//GWT.log("orderItem id = " + orderItem.get("id").isNumber().toString(), null);
							
							//GWT.log("orderItem stop = " + orderItem.get("stop").isBoolean().booleanValue(), null);
							double qty = orderItem.get("qty").isNumber().doubleValue();
							
							double price = orderItem.get("price").isNumber().doubleValue();
							
							orderItem.put("qty", new JSONNumber(0));
							
							orderItem.put("sum", new JSONNumber(qty * price));
							
							orderItem.put("return", new JSONString(""));
						}
						
						store.loadJsonData(orderItems.toString(), true);
						
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

	public void remedconfirm(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;
				
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/remedconfirm");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						orderItems = value.isArray();
						
						for (int i = 0; i < orderItems.size(); i++) {
							
							final JSONObject orderItem = orderItems.get(i).isObject();
							
							double qty = orderItem.get("qty").isNumber().doubleValue();
							
							double price = orderItem.get("price").isNumber().doubleValue();
							
							orderItem.put("sum", new JSONNumber(qty * price));
						}
						
						store.loadJsonData(orderItems.toString(), true);
						
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
	
	public void remedhomemed(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;
				
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/remedhomemed");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("vn_id=" + URL.encodeComponent(Jitavej.visit.get("id").isNumber().toString()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						orderItems = value.isArray();
						
						for (int i = 0; i < orderItems.size(); i++) {
							
							final JSONObject orderItem = orderItems.get(i).isObject();
							
							double qty = orderItem.get("qty").isNumber().doubleValue();
							
							double price = orderItem.get("price").isNumber().doubleValue();
							
							orderItem.put("sum", new JSONNumber(qty * price));
						}
						
						store.loadJsonData(orderItems.toString(), true);
						
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
	
	public void reorder(int order_id){
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/reorder");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("order_id=" + order_id);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						orderItems = value.isArray();
						
						for (int i = 0; i < orderItems.size(); i++) {
							
							try {
								
								final JSONObject orderItem = orderItems.get(i).isObject();
								
								double qty = orderItem.get("qty").isNumber().doubleValue();
								
								double price = orderItem.get("item").isObject().get("price").isNumber().doubleValue();
								
								GWT.log("qty * price == " + qty * price, null);
								
								orderItem.put("sum", new JSONNumber(qty * price));
							} 
							catch (Exception e) {
								
							}
						}
						
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

    public void loadDrug(){
    	
    	try {
    		
    		if(suggestDrug.suggestionPopup.isAttached() || suggestDrug.getText().trim().length() == 0){
    			
    			return;
    		}
    		
    		if(drug != null && suggestDrug.getText().trim().equalsIgnoreCase(drug.get("name").isString().stringValue())){
    			
    			return;
    		}
    		
    		if(drug !=null){
    			
        		GWT.log("drug.get(name).isString().toString() " + drug.get("name").isString().stringValue(), null);
        		
        		GWT.log("loadDrug " + suggestDrug.getText(), null);   			
    		}

			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/drug/findByCode");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("code=" + URL.encodeComponent(suggestDrug.getText().trim()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert(exception.toString());
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						drug = value.isObject();
						
						GWT.log(drug.toString(), null);
						
						drugText.setHTML(drug.get("name").isString().stringValue()+" ("+Jitavej.CONSTANTS.record_price_per_unit() +" "+drug.get("item").isObject().get("price").isNumber().doubleValue()+" "+ Jitavej.CONSTANTS.bath()+")");
					}
					else{
						
						drugText.setHTML("");
						
						Window.alert(Jitavej.CONSTANTS.record_alert_drug());
						
						suggestDrug.setText("");
						
						suggestDrug.setFocus(true);

						//Window.alert("response.getStatusCode() " + response.getStatusCode());
						//Window.alert("response.getText() " + response.getText());
					}
				}
			});
		} 
    	catch (Exception e) {
    		
    		//Window.alert(e.toString());
		}
    }

    public void loadDose(){
    	
    	try {
    		
    		if(suggestDose.suggestionPopup.isAttached() || suggestDose.getText().trim().length() == 0){
    			
    			return;
    		}
    		
    		if(dose != null && suggestDose.getText().trim().equalsIgnoreCase(dose.get("code").isString().stringValue())){
    			
    			return;
    		}
    		
    		if(dose !=null){
    			
        		GWT.log("dose.get(code).isString().toString() " + dose.get("code").isString().stringValue(), null);
        		
        		GWT.log("loadDose " + suggestDose.getText(), null);   			
    		}

			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/dose/findByCode");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("code=" + URL.encodeComponent(suggestDose.getText().trim()));
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert(exception.toString());
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						JSONValue value = JSONParser.parse(response.getText());
						
						dose = value.isObject();
						
						doseText.setHTML(dose.get("name1").isString().stringValue());
						
						if(mode.equals("continuation")){
							
							double perday = dose.get("pertime").isNumber().doubleValue()*dose.get("frequency").isNumber().doubleValue();
							
							qty.setText(""+(int)(perday*day.getSelectedIndex()));
						}
					}					else{
						
						suggestDose.setText("");
						
						doseText.setHTML("");
						
						Window.alert(Jitavej.CONSTANTS.record_alert_dose());
						
						suggestDose.setFocus(true);	
						
						//Window.alert("response.getStatusCode() " + response.getStatusCode());
						//Window.alert("response.getText() " + response.getText());
					}
				}
			});
		} 
    	catch (Exception e) {
    		
			//Window.alert(e.toString());
		}
    }

    public void checkStockWhenAdd(){
    	
    	try {
    		
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/drug/checkStock");
			
			rb.setTimeoutMillis(30000);	
			
			StringBuffer params = new StringBuffer();
			
			params.append("drug_id=" + drug.get("id").isNumber().toString());
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					Window.alert(exception.toString());
				}
				
				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						int stock = Integer.parseInt(response.getText());
						
						if(stock < Double.parseDouble(qty.getText())){
							
							Window.alert(drug.get("name").isString().stringValue() + " Out of Stock!");
							
							qty.setFocus(true);
							
							return;
						}
						
						addOrderItem();
					}
				}
			});
		} 
    	catch (Exception e) {
    		
    		Window.alert(e.toString());
		}
    }
    
	public void setPatient(JSONObject patient){
		
		if(patient == null){
			
			remed.setEnabled(false);
			
			drugErrors = new JSONArray();
		}
		else{
			
			remed.setEnabled(true);
			
			loadDrugError();
		}
		
		if(suggestDrug != null){
			
			suggestDrug.setText("");
			
			suggestDose.setText("");
		}
		
		orderItems = new JSONArray();
		
		store.removeAll();
		
		if(Jitavej.visit != null){
			
			if(mode.equals("continuation")){
				
				remedcon();
			}
			else if(mode.equals("homemed")){
				
				remedhomemed();
			}
			else{
				
				remed();
			}
							
		}	
	}

	public void loadDrugError() {
		
		drugErrors = new JSONArray();
		
		try {
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/drug/drugError");
			
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
						
						GWT.log("loadDrugError => " + response.getText(), null);
						
						JSONValue list = JSONParser.parse(response.getText());
						
						//Window.alert(list.toString());
						
						drugErrors = list.isArray();
					}
				}
			});
		} 
		catch (RequestException e1) {
			
			GWT.log("loadComponents", e1);
		}			
	} 
}
