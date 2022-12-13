package com.neural.jitavej.client.lab;

//import java.text.SimpleDateFormat;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;

import com.neural.jitavej.client.ActionBar;
import com.neural.jitavej.client.Jitavej;

public class LabReportPanel extends VerticalPanel {
	
	String data = null;
	public static Store store;
	public static JSONArray items = new JSONArray();
	public static TextBox sum;
	public static ListBox q;
	DateField startdate;
	DateField enddate;
	RecordDef recordDef;
	BaseColumnConfig[] columns;
	Button load, rtpcr_button, atk_button;
	
	public LabReportPanel() {
		
		setTitle(Jitavej.CONSTANTS.lab_order());

		recordDef = new RecordDef(new FieldDef[] {
				
            new StringFieldDef("name", "name"), 	
            new StringFieldDef("qty", "qty"), 	
		});			

		JsonReader reader = new JsonReader(recordDef);
		// reader.setRoot("data");
		// reader.setTotalProperty("total
		String[][] itemList = new String[][] { { "", "" }, { "", "" } };

		DataProxy dataProxy = new MemoryProxy(itemList);
		store = new Store(dataProxy, reader);

        columns = new BaseColumnConfig[]{  
			new RowNumberingColumnConfig(), 
			new ColumnConfig(Jitavej.CONSTANTS.lab_item(), "name", 560, true),
			new ColumnConfig(Jitavej.CONSTANTS.record_number(), "qty", 90, true)
					
        };         	
  
        ColumnModel columnModel = new ColumnModel(columns);  
        //columnModel.setColumnWidth(0, 30);
         
		GridPanel grid = new GridPanel();
		
		grid.setBorder(false);
		grid.setMargins(1);
		grid.setPaddings(1);
		// grid.setTitle("Local Json Grid");
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		
		grid.setFrame(false);
		grid.setBodyBorder(true);
		//grid.setHideBorders(true);
		
        grid.setWidth(920);  
        grid.setHeight(435); 
		grid.stripeRows(true);
		grid.setIconCls("grid-icon");
		grid.setTrackMouseOver(false);

		GridView view = new GridView();
		
		view.setEmptyText(Jitavej.CONSTANTS.lab_no_item());
		grid.setView(view);
		
		startdate = new DateField("startdate", "d/m/y");
		enddate = new DateField("enddate", "d/m/y");
		
		//enddate.setFormat(""); //setFormatPattern("yyyy-MM-dd"); 
		//startdate = new DateField("startdate", "y-m-d");
		//enddate = new DateField("enddate", "y-m-d");
		
		startdate.setValue(new Date());
		enddate.setValue(new Date());
		
		load = new Button(Jitavej.CONSTANTS.report(), new ClickListener() {
			
			public void onClick(Widget sender) {
				load();
			}
		});
		
		rtpcr_button  = new Button("RT-PCR Report", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				Window.open("/report_lab/rtpcr_report_excel.php?start_date=" + startdate.getValue().getTime() + "&end_date=" + enddate.getValue().getTime(), "_blank", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=600,width=800");
			}
		});
		
		atk_button = new Button("ATK Report", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				//startdate.getValue().getTime(); //.format('yyyy-MM-dd');
				
				//String start_date = startdate.getValue().getYear() + "-" + startdate.getValue().getMonth() + "-" + startdate.getValue().getDay();
				
				//Window.open("/jitavej/lab/printreport?order_id=", "*My Window*", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=600,width=800");
				
				Window.open("/report_lab/atk_report_excel.php?start_date=" + startdate.getValue().getTime() + "&end_date=" + enddate.getValue().getTime(), "_blank", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,height=600,width=800");
			}
		});

		q = new ListBox();
		q.addItem("Q 1 WK");
		q.addItem("Q 2 WK");
		q.addItem("Q 1 MO");
		q.addItem("Q 2 MO");
		q.addItem("Q 3 MO");
		q.addItem("Q 4 MO");
    
		ActionBar panel2 = new ActionBar();
		panel2.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		panel2.add(new HTML(Jitavej.CONSTANTS.date_from()));
		panel2.add(startdate);
		panel2.add(new HTML(Jitavej.CONSTANTS.date_to()));
		panel2.add(enddate);
    	panel2.add(load);
    	panel2.add(new HTML("&nbsp;&nbsp;"));
    	panel2.add(rtpcr_button);
    	panel2.add(new HTML("&nbsp;&nbsp;"));
    	panel2.add(atk_button);
    	panel2.add(new HTML("<div style='width:230px'></div>"));

        sum = new TextBox();
        sum.setStyleName("sum-black");
        sum.setWidth("100");
        sum.setEnabled(false);

		ActionBar bar2 = new ActionBar();
		//bar2.add(re);
		bar2.add(new HTML("<div style='width:340px'></div>"));
		bar2.add(sum);
	
 		add(panel2);
        add(grid);
        add(bar2);
        
	}
	
    public void load(){
    	
		try {
			
			load.setEnabled(false);
			
			store.removeAll();
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/lab/report");
			
			rb.setTimeoutMillis(60000);
			
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			StringBuffer params = new StringBuffer();
			
			params.append("startdate=" + startdate.getValue().getTime());
			params.append("&");
			params.append("enddate=" + enddate.getValue().getTime());
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					
					GWT.log("Error response received during authentication of user", exception);
					
					load.setEnabled(true);
				}

				public void onResponseReceived(Request request, Response response) {
					
					if (response.getStatusCode() == 200) {
						
						GWT.log(response.getText(), null);
						
						items = JSONParser.parse(response.getText()).isArray();
						
						store.removeAll();
						
						store.loadJsonData(items.toString(), true);
						
						sum.setText(""+items.size());
					} 
					else {
						
						Window.alert("response.getText() "+response.getText());
					}
					
					load.setEnabled(true);
				}
			});

		} 
		catch (RequestException e1) {
			
			e1.printStackTrace();
		}    	
    }

	public static void setPatient(JSONObject patient){
		
		if(patient == null){
			
		}else{
			
		}
		
		LabHistoryPanel.update();

	}
	
	
}
