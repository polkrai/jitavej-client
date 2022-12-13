package com.neural.jitavej.client.dental;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.patient.PatientPanel;

public class DentalItemDialog extends DialogBox {
	public Button submit, cancel;
	public TextBox code, name, price;
	public JSONObject item;

	public DentalItemDialog(JSONObject item){
		this.item = item;
		setAnimationEnabled(false);
		setHTML("<b>Item</b>");

		code = new TextBox();

		name = new TextBox();
		name.setWidth("400px");
		
		price = new TextBox();
		price.setText("0");

		submit = new Button("Save");
		submit.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				submit();
			}
		});	
		
		cancel = new Button("Cancel");
		cancel.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				hide();
			}
		});
		
		Grid grid = new Grid(5,2);


		grid.setWidget(0, 0, new HTML("Code"));
		grid.setWidget(0, 1, code);
		
		grid.setWidget(1, 0, new HTML("Name"));
		grid.setWidget(1, 1, name);	

		grid.setWidget(2, 0, new HTML("Price"));
		grid.setWidget(2, 1, price);	

		HorizontalPanel hor = new HorizontalPanel();
		hor.add(submit);
		hor.add(cancel);
		
		grid.setWidget(4, 1, hor);	

		setWidget(grid);
		setWidth("440px");
		
		
		if(item != null){
			if(item.get("code") != null){
				code.setText(item.get("code").isString().stringValue());
			}
			if(item.get("name") != null){
				name.setText(item.get("name").isString().stringValue());
			}
			price.setText(item.get("price").isNumber().toString());
			
		}
	}
	
	public void submit(){
		try {
			RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,	Jitavej.server + "/item/putdental");
			rb.setTimeoutMillis(30000);
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			StringBuffer params = new StringBuffer();
			if(item != null){
				params.append("item_id=" + item.get("id").isNumber().toString());
				params.append("&");				
			}
			params.append("code=" + code.getText().trim());
			params.append("&");		
			params.append("name=" + name.getText().trim());
			params.append("&");	
			params.append("price=" + price.getText().trim());
			params.append("&");	
					
			rb.sendRequest(params.toString(), new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}

				public void onResponseReceived(Request request, Response response) {
					GWT.log(response.getText(), null);
					hide();
					PatientPanel.dentalItemPanel.loadData();
				}
			});
		} catch (RequestException e) {
			GWT.log("Error while checking for authentication of user", e);
		}
		
	}


}
