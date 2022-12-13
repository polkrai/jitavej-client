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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class LabResultValueDialog extends FormDialog {
	public static Store store;
	public JSONArray results = new JSONArray();
	public int record_id;

	public LabResultValueDialog(int record_id) {
		this.record_id = record_id;
		setHTML("<B>Lab Result</B>");

		Button save = new Button("+" + Jitavej.CONSTANTS.lab_result_save());
		save.addClickListener(new ClickListener() {
			public void onClick(com.google.gwt.user.client.ui.Widget sender) {

				Record[] record = store.getRecords();
				JSONArray values = new JSONArray();
				for (Record rec : record) {
					JSONObject result = new JSONObject();
					result.put("id", new JSONNumber(rec.getAsInteger("id")));
					result.put("result", new JSONString(""+ rec.getAsString("result")));
					values.set(values.size(), result);
				}

				try {
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
							Jitavej.server + "/lab/putresult");
					rb.setTimeoutMillis(30000);
					StringBuffer params = new StringBuffer();
					params.append("values="+ URL.encodeComponent(values.toString()));
					params.append("&");
					params.append("type=lab");
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request,
								Response response) {
							if (response.getStatusCode() == 200) {
								Window.alert("Complete!!!");
								hide();
							} else {
								Window.alert("response.getText() "
										+ response.getText());
							}
						}
					});

				} catch (Exception e1) {
					Window.alert(e1.toString());
				}
			}

		});

		final RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("id", "id"),
				new StringFieldDef("item", "name"),
				new StringFieldDef("standard", "standard"),
				new StringFieldDef("result", "result"),
				new StringFieldDef("postfix", "postfix") });

		EditorGridPanel grid = new EditorGridPanel();
		String[][] itemList = new String[][] { { "", "" }, { "", "" } };

		MemoryProxy proxy = new MemoryProxy(itemList);

		JsonReader reader = new JsonReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
		grid.setStore(store);

		 RecordDef recordDef2 = new RecordDef( 
		         new FieldDef[]{ 
		                 new StringFieldDef("company")
		         } 
		 ); 
		
		
       	MemoryProxy proxy2 = new MemoryProxy(getCompanyData()); 
		ArrayReader reader2 = new ArrayReader(recordDef2); 
		Store store2 = new Store(proxy2, reader2); 
		store2.load(); 
		
	//	store2.add(record); 
	//	store2.commitChanges(); 
		
		ComboBox cb = new ComboBox();
		cb.setDisplayField("company");
		cb.setStore(store2);

    /*    cb.addListener(new ComboBoxListenerAdapter() {  
            public void onFocus(Field field) {  
            	GWT.log("ComboBox::onFocus()", null); 
            }  
        });  
	*/
	
		ColumnConfig commonCol = new ColumnConfig("Result", "result", 100, true, null, "result");
		commonCol.setEditor(new GridEditor(cb));

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				new ColumnConfig("id", "id", 60, true),
				new ColumnConfig("Item", "item", 150, true),
				new ColumnConfig("Reference", "standard", 100, true), 
				commonCol,
				new ColumnConfig("Unit", "postfix", 80, true) };

		ColumnModel columnModel = new ColumnModel(columns);
		grid.setColumnModel(columnModel);

		grid.setFrame(false);
		grid.setStripeRows(true);
		grid.setClicksToEdit(1);
		grid.setHeight(360);
		grid.setWidth(540);
		// grid.setTitle(Jitavej.CONSTANTS.lab_item());

        grid.addGridCellListener(new GridCellListenerAdapter() {  
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            //	JSONObject result = results.get(rowIndex).isObject();
			//	GWT.log(result.toString(), null);
            	super.onCellClick(grid, rowIndex, colindex, e);
            }
        });
		
		ActionBar bar = new ActionBar();
		bar.add(save);
		bar.add(cancel);
		bar.add(new HTML("<div style='width:240px'></div>"));

		VerticalPanel ver = new VerticalPanel();
		ver.add(bar);
		ver.add(grid);

		setWidget(ver);
		update();
	}

	public void update() {
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/lab/results");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
			params.append("record_id=" + record_id);
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user",exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						GWT.log(response.getText(), null);
						results = JSONParser.parse(response.getText()).isArray();
						store.removeAll();
						store.loadJsonData(results.toString(), true);
					} else {
						Window.alert("response.getText() "+ response.getText());
					}
				}
			});

		} catch (RequestException e1) {
			e1.printStackTrace();
		}
	}
	
    private Object[][] getCompanyData() { 
        return new Object[][]{  
 
        };  
    } 
}
