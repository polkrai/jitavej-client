package com.neural.jitavej.client.refer;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.Jitavej;

public class ReferSendPanel extends LazyPanel<HorizontalPanel> {  
	static ListBox userBox;	
	DatePicker datePicker;
	public static JSONArray refers;
	public static Store store;
	public Button add  = new Button("Send");
	
    public ReferSendPanel() {  
   
    }  
    
	@Override
	public HorizontalPanel createWidget() {

    	String[] months = {Jitavej.CONSTANTS.m1(),
			    			Jitavej.CONSTANTS.m2(),
			    			Jitavej.CONSTANTS.m3(),
			    			Jitavej.CONSTANTS.m4(),
			    			Jitavej.CONSTANTS.m5(),
			    			Jitavej.CONSTANTS.m6(),
			    			Jitavej.CONSTANTS.m7(),
			    			Jitavej.CONSTANTS.m8(),
			    			Jitavej.CONSTANTS.m9(),
			    			Jitavej.CONSTANTS.m10(),
			    			Jitavej.CONSTANTS.m11(),
			    			Jitavej.CONSTANTS.m12(),
    	};
    	
        datePicker = new DatePicker();  
        datePicker.setValue(new Date());  
        datePicker.setTodayText("Now!!");  
        datePicker.setMonthNames(months);
    //  datePicker.setWidth("120");
        datePicker.addListener(new DatePickerListenerAdapter() {  
            public void onSelect(DatePicker dataPicker, Date date) {  
                setDate(date);
            }  
        });  

		userBox = new ListBox();
		userBox.setName("user_id");
		userBox.addItem("", "0");
		for (int i = 0; i < Jitavej.users.size(); i++) {
			JSONObject user = Jitavej.users.get(i).isObject();
			userBox.addItem(user.get("firstname").isString().stringValue(), user.get("id").isNumber().toString());
		}
		userBox.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				setDate(datePicker.getValue());
			}
		});		
		
    	VerticalPanel ver = new VerticalPanel();
    	ver.add(datePicker);
    	ver.add(new HTML("User:"));
    	ver.add(userBox);    	
    	
		final RecordDef recordDef = new RecordDef(new FieldDef[] { 
				new StringFieldDef("no", "no"), 
				new StringFieldDef("patient", "patient"), 
				new StringFieldDef("doctor", "doctor"),
				new StringFieldDef("comment", "comment")
		});
		
        JsonReader reader = new JsonReader(recordDef);  
        //      reader.setRoot("data");  
        //      reader.setTotalProperty("totalCount");  
        
        String[][] itemList = new String[][]{{"", ""},{"", ""}};

        DataProxy dataProxy = new MemoryProxy(itemList);
      
        store = new Store(dataProxy,reader);

        ColumnConfig colType = new ColumnConfig(" ", "name", 24, false, new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                 return Format.format("<img src='images/cross.gif'/>", " ");
             }
         });

        ColumnConfig printType = new ColumnConfig(" ", "name", 24, false, new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                 return Format.format("<img src='images/print.png'/>", " ");
             }
         });
        
        ColumnConfig editType = new ColumnConfig(" ", "name", 24, false, new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                 return Format.format("<img src='images/edit.png'/>", " ");
             }
         });
        
        
		// setup column model
		ColumnModel columnModel = new ColumnModel(new ColumnConfig[] { 
				new ColumnConfig("No.", "no", 100, true), 
				new ColumnConfig("Patient", "patient", 150, true), 
				new ColumnConfig("Doctor", "doctor", 150, true), 
				new ColumnConfig("Comment", "comment", 200, true),
				printType,
				editType,
				colType

		});

		GridPanel grid = new GridPanel();
		// grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);

	    GridView view = new GridView();  
	    view.setEmptyText("No visit here.");  
	    grid.setView(view); 

		grid.setFrame(false);
		grid.setBorder(false);
		grid.setWidth(720);
		grid.setHeight(480);
		grid.stripeRows(true);

		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

        grid.addGridCellListener(new GridCellListenerAdapter() {
            @Override
            public void onCellClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
            	final JSONObject refer = refers.isArray().get(rowIndex).isObject();
            //	labItemValue.load(item);
            //	Window.alert(""+colindex);
            	if (colindex == 4) {
            		//	Window.alert(refer.toString());
					//Window.open( "/jitavej/refer/printrefersend?refersend_id="+refer.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=720,width=1024");
					Window.open( "/miracle/refer_send/refer_print.php?id_refer="+refer.get("id").isNumber().toString(), "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=900,width=900");
					//
            	}
            	else if (colindex == 5) {      		
					final ReferSendDialog dialog = new ReferSendDialog(refer);
					dialog.form.addFormHandler(new FormHandler() {
						public void onSubmitComplete(FormSubmitCompleteEvent event) {
							dialog.hide();
							setDate(datePicker.getValue());
						}

						public void onSubmit(FormSubmitEvent event) {
						}
					});
					dialog.show();
					dialog.center();
				}else if (colindex == 6) {
					MessageBox.confirm("Confirm", "Confirm to delete?", new MessageBox.ConfirmCallback() {
						public void execute(String btnID) {
						   	try {
								if(btnID.equals("no")){
									return;
								}
								RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/refer/deleterefersend");
								rb.setTimeoutMillis(30000);
								rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
								
								StringBuffer params = new StringBuffer();
								params.append("refersend_id=" + refer.get("id").isNumber().toString());
								rb.sendRequest(params.toString(), new RequestCallback() {
									public void onError(Request request, Throwable exception) {
										GWT.log("Error response received during authentication of user", exception);
									}

									public void onResponseReceived(Request request, Response response) {
										if (response.getStatusCode() == 200) {
											setDate(datePicker.getValue());
										} else {
											Window.alert("response.getText() "+response.getText());
										}
									}
								});
							} catch (RequestException e1) {
								Window.alert(e1.toString());
							}    	
						}
					});

				}
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
        
		store.load();
    	
		add.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final ReferSendDialog dialog = new ReferSendDialog(null);
				dialog.form.addFormHandler(new FormHandler() {
					public void onSubmitComplete(FormSubmitCompleteEvent event) {
					//	Window.alert(event.getResults());
						dialog.hide();
						setDate(datePicker.getValue());
					}

					public void onSubmit(FormSubmitEvent event) {
					}
				});
				dialog.show();
				dialog.center();				
			}
		});
		
		VerticalPanel ver2 = new VerticalPanel();
		ver2.add(add);
		ver2.add(new ScrollPanel(grid));
    	
    	HorizontalPanel hor1 = new HorizontalPanel(); 
    	hor1.setSpacing(8);
    	hor1.add(ver);
    	hor1.add(ver2);
    	
    	setDate(datePicker.getValue());
    	
		return hor1;
	}
	
    public void setDate(Date date){
 
       	try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/refer/sendlist");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			params.append("user_id=" + ReferSendPanel.userBox.getValue(ReferSendPanel.userBox.getSelectedIndex()));
			params.append("&");			
			params.append("date=" + date.getTime());
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						JSONValue list = JSONParser.parse(response.getText());
						refers = list.isArray();
						store.removeAll();
						store.loadJsonData(refers.toString(), true);
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			Window.alert(e1.toString());
		}    	
    }

}  