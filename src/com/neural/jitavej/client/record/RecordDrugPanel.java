package com.neural.jitavej.client.record;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.LazyPanel;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.DateField;
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
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.autocomplete.DoseSuggestOracle;
import com.neural.jitavej.client.autocomplete.DrugSuggestOracle;
import com.neural.jitavej.client.history.DrugHistoryDialog;
import com.neural.jitavej.client.history.DrugItemHistoryPanel;

public class RecordDrugPanel extends LazyPanel<VerticalPanel>  {
	
	SuggestBox suggestDrug, suggestDose;
	HTML drugText, doseText;
	TextBox qty;
	ListBox day, reason;
	JSONObject dose;	
	JSONArray doses;
	
	public static TextBox sum;
	public static JSONArray orderItems = new JSONArray();
	public JSONArray drugErrors = new JSONArray();	
	public static Store store;
	public Button remed, clear, x2, d2, x3, d3;
	public JSONObject drug;
	public static String mode;
	public EditorGridPanel grid;
	public static ListBox remedBox;
	public ActionBar addBar;
	public Button ipdsave; 
	public StringBuffer errorMessage = new StringBuffer();
	public StringBuffer interactionIsorder = new StringBuffer();
	
	public RecordDrugPanel(String mode) {
		
		this.mode = mode;

		remed = new Button("Remed", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				DrugHistoryDialog drugHistoryDialog = new DrugHistoryDialog(RecordDrugPanel.this);
				
				drugHistoryDialog.show();
				
				drugHistoryDialog.center();					
			}
		});
		
		if(!mode.equals("opd")){
			
			ipdsave = new Button("<b><FONT style='color:blue;'>"+ Jitavej.CONSTANTS.record_save() + "</font></b>");

			if(mode.equals("confirm")){
				
				ipdsave.setHTML("<b><FONT style='color:blue;'>"+ Jitavej.CONSTANTS.record_conconfirm() + "</font></b>");
			}
			
			ipdsave.setPixelSize(180, 50);
			
			ipdsave.addClickListener(new ClickListener() {
				
				public void onClick(com.google.gwt.user.client.ui.Widget sender) {
					
					if (orderItems.size() > 0) {

						if (RecordIcd10Panel.icd10Items.size() == 0) {
							
							Window.alert(Jitavej.CONSTANTS.record_alert_icd10());
							
							return;
						}

						final PinCodeDialog dialog = new PinCodeDialog(RecordDrugPanel.this);

						dialog.show();
						
						dialog.center();
						
						DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
							
							public void execute() {
								
								dialog.pincode.setFocus(true);
							}
						});
					}
					else {
						
						Window.alert(Jitavej.CONSTANTS.record_alert_drug_emty());
						
						return;
						
					}
				}
			});			
		}

		
		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				
			new StringFieldDef("drug", "drugaccount"), 	
			new StringFieldDef("dose", "dose.code"), 	
			new StringFieldDef("reason", "reason"), 	
			new StringFieldDef("qty", "qty"), 	
			new StringFieldDef("fee", "drug.item.price"), 	
			new StringFieldDef("sum", "sum"),
			new StringFieldDef("unit_dose", "unit_dose"),

		});
		
		JsonReader reader = new JsonReader(recordDef);

		String[][] itemList = new String[][]{{"", ""}, {"", ""}};

		MemoryProxy dataProxy = new MemoryProxy(itemList);
		
		store = new Store(dataProxy, reader);
        sum = new TextBox();
        
        sum.setStyleName("sum-black");
        sum.setWidth("100");
        sum.setEnabled(false);
		
        remedBox = new ListBox();
        
		remedBox.addItem(Jitavej.CONSTANTS.record_not_remed(), "0");
		remedBox.addItem("1 " + Jitavej.CONSTANTS.month(), "1");
		remedBox.addItem("2 " + Jitavej.CONSTANTS.month(), "2");
		remedBox.addItem("3 " + Jitavej.CONSTANTS.month(), "3");
		remedBox.addItem("4 " + Jitavej.CONSTANTS.month(), "4");
		remedBox.addItem("5 " + Jitavej.CONSTANTS.month(), "5");
		remedBox.addItem("6 " + Jitavej.CONSTANTS.month(), "6");
		remedBox.addItem("7 " + Jitavej.CONSTANTS.month(), "7");
		remedBox.addItem("8 " + Jitavej.CONSTANTS.month(), "8");
		remedBox.addItem("9 " + Jitavej.CONSTANTS.month(), "9");
		remedBox.addItem("10 " + Jitavej.CONSTANTS.month(), "10");
		remedBox.addItem("11 " + Jitavej.CONSTANTS.month(), "11");
		remedBox.addItem("12 " + Jitavej.CONSTANTS.month(), "12");
		
		x2 = new Button("x2", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				for(int i = 0; i < orderItems.size(); i++) {
					
					JSONObject orderItem = orderItems.get(i).isObject();
					
					orderItem.put("qty", new JSONNumber(orderItem.get("qty").isNumber().doubleValue()*2));
				}
				
				update();
			}
		});
		
		x3 = new Button("x3", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				for(int i = 0; i < orderItems.size(); i++) {
					
					JSONObject orderItem = orderItems.get(i).isObject();
					
					orderItem.put("qty", new JSONNumber(orderItem.get("qty").isNumber().doubleValue()*3));
				}
				
				update();
			}
		});
		
		d2 = new Button("%2", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				for(int i = 0; i < orderItems.size(); i++) {
					
					JSONObject orderItem = orderItems.get(i).isObject();
					
					orderItem.put("qty", new JSONNumber((int)(orderItem.get("qty").isNumber().doubleValue()/2)));
				}
				
				update();		
			}
		});
		
		d3 = new Button("%3", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				for(int i = 0; i < orderItems.size(); i++) {
					
					JSONObject orderItem = orderItems.get(i).isObject();
					
					orderItem.put("qty", new JSONNumber((int)(orderItem.get("qty").isNumber().doubleValue()/3)));
				}
				
				update();
			}
		});
		
	}
	
	public int row;

	@Override
	public VerticalPanel createWidget() {

		ActionBar bar = new ActionBar();
		
		bar.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		
		bar.add(new HTML("<div style='width:0px'></div>"));
		
		bar.add(remed);
		
		if(!mode.equals("opd") && !mode.equals("dental")){
			
			bar.add(new HTML("<div style='width:200px'></div>"));
			
			bar.add(ipdsave);
		}
		
		ColumnConfig commonCol = new ColumnConfig(Jitavej.CONSTANTS.record_number(), "qty", 72, true, null, "qty");
		
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
					
					orderItems.get(row).isObject().put("qty", new JSONNumber(Double.parseDouble(newVal.toString())));
					
					update();

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

        ColumnConfig colType = new ColumnConfig(Jitavej.CONSTANTS.record_delete(), "drug", 80, false, new Renderer() {
           
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
               
        		return Format.format("<img class=\"icon-center\" src='images/cross.gif'/>", "");
            }
        });
        
        ColumnConfig colUnitdose = new ColumnConfig("<center>UnitDose</center>", "unit_dose", 80, false, new Renderer() {
            
        	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
         	   
        		JSONObject item = orderItems.get(rowIndex).isObject();
         	   
        		if(item.get("id") != null){

         		   if(item.get("unit_dose").isBoolean().booleanValue()){
         			   
                        return Format.format("<center><img src='images/on.png'/></center>", "");
         		   }
         		   else{
         			   
         			   return Format.format("<center><img src='images/off.png'/></center>", "");
         		   }
         	   }
        		
               return Format.format("<center><img src='images/on.png'/></center>", "");
             }
        });
        
        if(Jitavej.mode.equals("backmed")) {
        	
        	colType.setHidden(true);
        }
        
        if(!mode.equals("unitdose")) {
        	
        	colUnitdose.setHidden(true);
        }
        
        BaseColumnConfig[] columns = new BaseColumnConfig[]{		
			new RowNumberingColumnConfig(), 
			new ColumnConfig(Jitavej.CONSTANTS.record_item(), "drug", 240, true),
			new ColumnConfig(Jitavej.CONSTANTS.record_dose(), "dose", 230, true),
			new ColumnConfig(Jitavej.CONSTANTS.record_reason(), "reason", 60, true),			
			commonCol,
			new ColumnConfig(Jitavej.CONSTANTS.record_fee(), "fee", 65, true),
			new ColumnConfig(Jitavej.CONSTANTS.record_sum(), "sum", 65, true),
			colType,
			colUnitdose,
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
		grid.setWidth(930);
		
		//MessageBox.setMinWidth(250);
		
        //MessageBox.alert(Jitavej.CONSTANTS.header_message_alert(), Jitavej.mode);
		
		//Window.alert("Mode => " + this.mode);
		
		if(!mode.equals("opd") && !mode.equals("remed")) {
			
			loadDrugError();
		}
		
        if(Jitavej.mode.equals("remed")){
        	
        	grid.setHeight(300);
        	//grid.setWidth(930);
        }
        else if(Jitavej.mode.equals("record_oneday") || Jitavej.mode.equals("record_continuation") || Jitavej.mode.equals("record_homemed")){
        	
        	//grid.setWidth(930);
        	grid.setHeight(270); 
        }
        else if(Jitavej.mode.equals("record_unitdose")){
        	
        	//grid.setWidth(930);
        	grid.setHeight(270); 
        }
        else if(Jitavej.mode.equals("backmed")){
        	
        	//grid.setWidth(930);
        	grid.setHeight(430); 
        }
        else {
        	
        	grid.setHeight(310); 
        }
        
		grid.setIconCls("grid-icon");
		
		grid.setTrackMouseOver(false);

		grid.addGridCellListener(new GridCellListenerAdapter() {
			
			public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
				
				if(Jitavej.mode.equals("backmed")){
					
					return;
				}

				if (colindex == 2) {
					
					final DoseEditDialog dialog = new DoseEditDialog(rowIndex, RecordDrugPanel.this);
					dialog.show();
					dialog.center();
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						public void execute() {
							dialog.suggestDose.setFocus(true);
						}
					});
				
				}
				
				if (colindex == 3) {
					
					final ReasonEditDialog dialog = new ReasonEditDialog(rowIndex, RecordDrugPanel.this);
					dialog.show();
					dialog.center();
					DeferredCommand.addCommand(new com.google.gwt.user.client.Command() {
						public void execute() {
							dialog.reason.setFocus(true);
						}
					});
					
				}
				
				if (colindex == 7) {
					
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
				}
				
				if (colindex == 8) {
					
					JSONObject item = orderItems.get(rowIndex).isObject();
					
					if(item.get("unit_dose").isBoolean().booleanValue()){
		        		   
	        		   item.put("unit_dose", JSONBoolean.getInstance(false));
	        		   
		        		   update();
		        	}
		        	else{
		        		   
	        		   item.put("unit_dose", JSONBoolean.getInstance(true));
	        		   
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
		
		add.setPixelSize(100, 60);

		clear = new Button(Jitavej.CONSTANTS.clear_item(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				orderItems = new JSONArray();
				store.removeAll();
				suggestDrug.setText("");
				suggestDose.setText("");
				qty.setText("");
			}
		});

		Button newdose = new Button(Jitavej.CONSTANTS.add_dose(), new ClickListener() {
			
			public void onClick(Widget sender) {
				
				if(drug == null){
					
					loadDrug();
				}
				
				DoseDialog dialog = new DoseDialog(RecordDrugPanel.this);
				dialog.show();
				dialog.center();
			}
		});		
		
		newdose.setEnabled(false);
		
		final DrugSuggestOracle oracle = new DrugSuggestOracle();
		
        suggestDrug = new SuggestBox(oracle);
        suggestDrug.setLimit(5);   // Set the limit to 5 suggestions being shown at a time 
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
						}else{
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
 		
 		if(Jitavej.mode.equals("record_oneday") || Jitavej.mode.equals("record_unitdose") || Jitavej.mode.equals("record_homemed")){
 	 		
 			reason.addItem("IPD");
        }
 		
 		reason.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		        if (keyCode == (char) KEY_ENTER) {
		        	qty.setFocus(true);
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
        
        if(Jitavej.building != null){
        	
            int period = (int)Jitavej.building.get("day").isNumber().doubleValue();
            
            Date date = new Date();
            
            int now = date.getDay();
            
            if(period <= now){
            	period += 7;
            }
            
            day.setSelectedIndex(period - now + 1);        	
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
		hor1.add(newdose);
		hor1.add(doseText);
		hor1.setPixelSize(360, 18);
		
       	panel.setWidget(0, 1, new HTML("Dose:"));
       	panel.setWidget(1, 1, suggestDose);
       	panel.setWidget(2, 1, hor1); 	

    	panel.setWidget(0, 2, new HTML("<div style='width:10px'></div>"));
   	
		if(mode.equals("continuation")){
			
	       	panel.setWidget(0, 4, new HTML("Day"));
	       	panel.setWidget(1, 4, day);
		}
		
	
    	panel.setWidget(0, 5, new HTML("Reason"));
       	panel.setWidget(1, 5, reason);
    	
       	panel.setWidget(0, 6, new HTML("Qty."));
       	panel.setWidget(1, 6, qty); 
       	
    	addBar.add(panel);
    	addBar.add(add);
		
		ActionBar bar2 = new ActionBar();
		
        bar2.setStyleName("drug-bar");
        
        if(!Jitavej.mode.equals("remed")){
        	
        	bar2.add(clear); 
        	bar2.add(new HTML("<div style='width:40px'></div>"));
        	bar2.add(new HTML(Jitavej.CONSTANTS.remed()));
        	bar2.add(remedBox);
        	
        }
        
        if(Jitavej.mode.equals("backmed")) {
        	
        	d2.setEnabled(false);
        	d3.setEnabled(false);
        	x2.setEnabled(false);
        	x3.setEnabled(false);
        }
        
        HorizontalPanel h = new HorizontalPanel();
        
        h.add(d2);
        h.add(d3);
        h.add(new HTML("<div style='width:5px'></div>"));
        h.add(x2);
        h.add(x3);
        
    	bar2.add(new HTML("<div style='width:180px'></div>"));
    	bar2.add(h);
    	
		bar2.add(new HTML("<div style='width:80px'></div>"));
		bar2.add(new HTML(Jitavej.CONSTANTS.record_sumall()));
		bar2.add(sum);
		
		VerticalPanel ver = new VerticalPanel();
		ver.add(bar);
		
        if(!Jitavej.mode.equals("remed")){
        	
        	ver.add(addBar);	
        }	
        
		ver.add(grid);
		ver.add(bar2);		

		return ver;
	}
	
	public void addItem(){
		
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
				
				return;
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
			
			if(errorMessage.toString().length() > 0){
				
				if (interactionIsorder.toString().equals("t")) {
					
					MessageBox.alert(Jitavej.CONSTANTS.record_interaction(), errorMessage.toString() + "<br /> กรุณาเลือกหรือค้นหารายการยาใหม่");
					
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
					
				}
				else {
				
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

			}
			else {
				
				checkStockWhenAdd();
			}
			
			errorMessage = new StringBuffer();
			
			interactionIsorder = new StringBuffer();
		} 
		catch (Exception e1) {
			
			Window.alert(e1.toString());
		}		
	}
	
	public void addOrderItem(){
		
		try{
			
			GWT.log("drug.get('name') > "+ drug.get("name"), null);
			
			JSONObject orderItem = new JSONObject();
			
			orderItem.put("drug", drug);
			orderItem.put("drugaccount", new JSONString(drug.get("code").isString().stringValue() +" <b>("+drug.get("account").isString().stringValue() +")</b>"));
			orderItem.put("drugId", drug.get("id"));
			orderItem.put("genericId", drug.get("generic").isObject().get("name").isString());
			orderItem.put("dose", dose);
			
			if(reason.getSelectedIndex() > 0){
				
				orderItem.put("reason", new JSONString(reason.getValue(reason.getSelectedIndex())));	
			}
			
			orderItem.put("price", drug.get("item").isObject().get("price").isNumber());
			orderItem.put("qty", new JSONNumber(Double.parseDouble(qty.getText())));
			orderItem.put("sum", new JSONNumber(Double.parseDouble(qty.getText())*drug.get("item").isObject().get("price").isNumber().doubleValue()));
			orderItem.put("unit_dose", JSONBoolean.getInstance(true));
					
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
		} 
		catch (Exception e1) {
			
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
		}
		else{
			
			qty.setText("0");
		}
	}

	public void checkInteraction(String generic1){
		
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
					
					String generic2 = orderItem.get("genericId").isString().stringValue();
					
					if(!generic1.equals(generic2)){
						
						for(int k = 0; k < Jitavej.interactions.size(); k++) {						

							JSONObject interaction = Jitavej.interactions.get(k).isObject();

							if((interaction.get("generic1").isString().stringValue().equals(generic1) &&  interaction.get("generic2").isString().stringValue().equals(generic2)) || ((interaction.get("generic1").isString().stringValue().equals(generic2) && interaction.get("generic2").isString().stringValue().equals(generic1)))) {

								interactionIsorder.append(interaction.get("notorder").isString().stringValue());
								
								errorMessage.append(Jitavej.CONSTANTS.record_interaction() + " >> " + generic1 + " && " + generic2 +" <br />");

								return;

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
					
					errorMessage.append(Jitavej.CONSTANTS.record_error() + " >> " + generic1 + " <br />");
					
					/*
					if(errorMessage.toString().length() > 0){
						
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
					}
					*/
					
					//Window.alert(errorMessage.toString());
					
					return;
				}

			}
		} 
		catch (Exception e) {
			
			GWT.log(e.toString(), null);
		}
	}
	
	public static void update(){
		
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
			
			sum.setText("" + sumtmp);
		} 
		catch (Exception e) {
			
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
						
						remedBox();
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
    
	public void remedBox(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/remedenddate");
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
						
						long remed = Long.parseLong(response.getText());
						
						if(remed != 0){
							
							Date date = new Date(remed);
							
							Date now = new Date();
							
							int month = (int)((date.getTime()-now.getTime())/1000/60/60/24/28);
							
							remedBox.setSelectedIndex(month);

						}
						else{
							
							remedBox.setSelectedIndex(0);
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
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			GWT.log("Error > ", e1);
		}						
	}
	
	public void remedunit(){
		
		try {
			
			if(Jitavej.patient == null){
				
				return;
				
			}
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/order/remedunit");
			
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
						
						Window.alert("response.getText() " + response.getText());
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
			
			if(Jitavej.patient == null) {
				
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
						
						Window.alert("response.getText() " + response.getText());
					}
				}
			});

		} 
		catch (RequestException e1) {
			
			GWT.log("Error > ", e1);
		}						
	}
	
	//function Set Drug to Grid for Remed
	
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
						
						Window.alert("response.getText() " + response.getText());
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
					}
				}
			});
		} 
    	catch (Exception e) {
    		
    		Window.alert(e.toString());
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
					}
					else{
						
						suggestDose.setText("");
						
						doseText.setHTML("");
						
						Window.alert(Jitavej.CONSTANTS.record_alert_dose());
						
						suggestDose.setFocus(true);
						
					}
				}
			});
		} 
    	catch (Exception e) {
    		
    		Window.alert(e.toString());
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
		
		DrugItemHistoryPanel.order = null;
		
		if(Jitavej.visit != null){
			
			if(mode.equals("continuation")){
				
				remedcon();
			}
			else if(mode.equals("homemed")){
				
				remedhomemed();
			}
			else if(mode.equals("dental")){
				
			}
			else{
				
				remed();
			}
							
		}
	
	}

	public void loadDrugError() {
		
		//Window.alert("loadDrugError");
		
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
