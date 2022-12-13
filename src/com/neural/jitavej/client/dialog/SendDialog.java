package com.neural.jitavej.client.dialog;

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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;

public class SendDialog extends FormDialog {
	
	public FormPanel form;
	ListBox actionBox;
	
	@SuppressWarnings("deprecation")
	
	public SendDialog() {
		
		setHTML("<B>"+Jitavej.CONSTANTS.send()+"</B>");

		form = new FormPanel();
		form.setAction(Jitavej.server + "/q/changeComponent");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		Grid grid = new Grid(4,3);
		form.setWidget(grid);
		
		Hidden queue = new Hidden();
		queue.setName("queue_id");
		queue.setValue(Jitavej.queue.get("id").toString());

		final ListBox componentBox = new ListBox();
		componentBox.setName("component_id");
		componentBox.addItem("", "");
		for (int i = 0; i < Jitavej.components.size(); i++) {
			JSONObject component = Jitavej.components.get(i).isObject();
			String type = "";
			if(component.get("type").isString() != null){
				type = component.get("type").isString().stringValue();
			}
		
			if(Jitavej.mode.equals("frontmed")){
				
				if(type.equals("backmed") || type.equals("remed") || type.equals("social")){
					componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
				}
			}
			else if(Jitavej.mode.equals("backmed")){
				
				if(type.equals("lab") || type.equals("psycho") || type.equals("finance")|| type.equals("medrec")  || type.equals("drugconfirm") || type.equals("frontmed") || type.equals("admission") || type.equals("psychosocial") || type.equals("x-ray") || type.equals("ect") || type.equals("dental") || type.equals("medoption") || type.equals("recover")){
					componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
				}
			}
			else if(Jitavej.mode.equals("remed")){
				
				if(type.equals("drugconfirm") || type.equals("backmed") || type.equals("frontmed")){
					componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
				}
			}
			else if(Jitavej.mode.equals("lab")){
				
				if(type.equals("medrec") || type.equals("ipd") || type.equals("backmed") || type.equals("frontmed")  || type.equals("finance")){
					componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
				}
			}
			else if(Jitavej.mode.equals("emergency")){
				
				componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
		
			}
		}
		
		componentBox.addChangeListener(new ChangeListener(){
			
			public void onChange(Widget sender) {
				
				try {
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/action/list2");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					StringBuffer params = new StringBuffer();
					params.append("component_id=" + componentBox.getValue(componentBox.getSelectedIndex()));
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								GWT.log(response.getText(), null);
								JSONArray actions = JSONParser.parse(response.getText()).isArray();
								if(actions.size() > 0){
									actionBox.setEnabled(true);
									submit.setEnabled(true);
								}else{
									actionBox.setEnabled(false);
									submit.setEnabled(false);
								}
								actionBox.clear();
								for (int i = 0; i < actions.size(); i++) {
									final JSONObject action = actions.get(i).isObject();
									actionBox.addItem(action.get("name").isString().stringValue(), action.get("id").isNumber().toString());
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
		});
		
		grid.setWidget(0, 0, new HTML(Jitavej.CONSTANTS.component()));
		grid.setWidget(0, 1, componentBox); 
		grid.setWidget(0, 2, queue); 
		
		
		actionBox = new ListBox();
		actionBox.setName("action_id");
		actionBox.setEnabled(false);
		grid.setWidget(1, 0, new HTML(Jitavej.CONSTANTS.action()));
		grid.setWidget(1, 1, actionBox); 

		Hidden user_id= new Hidden();
		user_id.setName("user_id");
		user_id.setValue(Jitavej.session.get("user").isObject().get("id").isNumber().toString());
		
		grid.setWidget(2, 1, user_id); 
		
		submit = new Button(Jitavej.CONSTANTS.send(), new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		});
		submit.setEnabled(false);
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.add(submit);
		hor.add(cancel);
		
		grid.setWidget(3, 0, new HTML(""));
		grid.setWidget(3, 1, hor);
		setWidget(form);

	}


}
