package com.neural.jitavej.client.dental;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.Space;

public class DentalItemPanel extends Composite {
	
	private FlexTable table = new FlexTable();
	private FlexTable header = new FlexTable();
	private JSONArray items = new JSONArray();
	private ListBox type;
	public TextBox key;
	private Label sumrow;
	private Button search;

	public DentalItemPanel() {
		
		Button add = new Button("Add");
		add.setWidth("80px");
		add.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				DentalItemDialog dialog = new DentalItemDialog(null);
				dialog.show();
				dialog.center();
			}
		});
		
		key = new TextBox();
		//key.setWidth("100%");
		
		type = new ListBox();
		//type.setWidth("120px");
		type.addItem("");;
		type.addItem("Order", "order");
		type.addItem("Commercial", "commercial");
	
		search = new Button("Search");
		search.setWidth("90px");
		search.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				loadData();
			}
		});
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		
		hor.setSpacing(4);
		hor.add(add);
		hor.add(new Space("10px"));		
		hor.add(new HTML("Key"));		
		hor.add(key);
    	//hor.add(new HTML("Type"));		
    	//hor.add(type);
    	//hor.add(new Space("4px"));		
		hor.add(search);

		//hor.setCellVerticalAlignment(type, HorizontalPanel.ALIGN_MIDDLE);
		
		sumrow = new Label();

		HorizontalPanel bar = new HorizontalPanel();
		
		bar.setWidth("100%");
		
		bar.add(hor);
		bar.add(sumrow);

		bar.setCellHorizontalAlignment(hor, HorizontalPanel.ALIGN_LEFT);
		bar.setCellHorizontalAlignment(sumrow, HorizontalPanel.ALIGN_RIGHT);
		
		bar.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);	
		bar.setCellVerticalAlignment(sumrow, HorizontalPanel.ALIGN_MIDDLE);
		
		bar.setStyleName("bar");

		header.setCellPadding(0);
		header.setCellSpacing(0);
		header.setBorderWidth(0);
		header.setStyleName("header");
		header.setWidth("100%");

		header.getColumnFormatter().setWidth(0, "8%");
		header.getColumnFormatter().setWidth(1, "12%");
		header.getColumnFormatter().setWidth(2, "70%");
		header.getColumnFormatter().setWidth(3, "10%");
		
		header.setText(0, 0, "ID");
		header.setText(0, 1, "Code");
		header.setText(0, 2, "Name");
		header.setText(0, 3, "Price");

		// Setup the table.
		table.setCellSpacing(0);
		table.setCellPadding(0);
		table.setBorderWidth(0);
		table.setStyleName("grid");
		table.setWidth("100%");

		table.getColumnFormatter().setWidth(0, "5%");
		table.getColumnFormatter().setWidth(1, "10%");
		table.getColumnFormatter().setWidth(2, "75%");
		table.getColumnFormatter().setWidth(3, "10%");

		table.addTableListener(new TableListener() {
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				JSONObject item = items.get(row).isObject();
				DentalItemDialog dialog = new DentalItemDialog(item);
				dialog.show();
				dialog.center();
			}
		});

	//	ScrollPanel scrollPanel = new ScrollPanel(table);

		VerticalPanel ver = new VerticalPanel();
		ver.add(bar);
		ver.add(header);
		ver.add(table);
		ver.setWidth("100%");

		initWidget(ver);
	}

	public void update() {
		for (int i = 0; i < items.size(); ++i) {

			JSONObject item = items.get(i).isObject();
			
			table.setText(i, 0, item.get("id").isNumber().toString());
			if(item.get("code") != null){
				table.setText(i, 1, item.get("code").isString().stringValue());
			}else{
				table.setText(i, 1, "");
			}
			
			table.setText(i, 2, item.get("name").isString().stringValue());
			table.setText(i, 3, item.get("price").isNumber().toString());


		}
		
		sumrow.setText(items.size() +" Rows");

	}

	public void loadData() {
		try {
			for(int i=table.getRowCount()-1; i>=0;i--){
				table.removeRow(i);				
			}
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/item/list3");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
			StringBuffer params = new StringBuffer();
	
			params.append("type=dental");
			params.append("&");
			params.append("key=" + URL.encode(key.getText().trim()));
			params.append("&");
			
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					GWT.log(response.getText(), null);
					JSONValue value = JSONParser.parse(response.getText());
					items = value.isArray();
					update();
				}
			});
		} catch (RequestException e) {
			GWT.log("Error while checking for authentication of user", e);
		}

	}

}
