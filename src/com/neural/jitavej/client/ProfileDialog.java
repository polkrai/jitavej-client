package com.neural.jitavej.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.dialog.FormDialog;

public class ProfileDialog extends FormDialog {
	public PasswordTextBox password1, password2, password3;
	public PasswordTextBox pincode1, pincode2, pincode3;
	
	
	public ProfileDialog() {
		setHTML("<B>Profile</B>");
		
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();

		VerticalPanel passwordPanel = new VerticalPanel();
		password1 = new PasswordTextBox();
		password2 = new PasswordTextBox();
		password3 = new PasswordTextBox();

		Button submit1 = new Button("Change", new ClickListener() {
			public void onClick(Widget sender) {
				try {	
					if(!password2.getText().equals(password3.getText())){
						Window.alert("New Password dont match!");
						return;
						
					}
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/changepassword");
					rb.setTimeoutMillis(30000);	
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
					params.append("&");
					params.append("password1=" + URL.encodeComponent(password1.getText().trim()));
					params.append("&");
					params.append("password2=" + URL.encodeComponent(password2.getText().trim()));

					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}
						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								if(response.getText().equals("true")){
									Window.alert("Change Complete! ");
								}else{
									Window.alert("Old Password dont match! ");
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

		});
		
		Grid grid = new Grid(4,2);
		grid.setWidget(0, 0, new HTML("Old Password")); 
		grid.setWidget(0, 1, password1); 
		
		grid.setWidget(1, 0, new HTML("New Password")); 
		grid.setWidget(1, 1, password2); 
		
		grid.setWidget(2, 0, new HTML("Confirm New Password")); 
		grid.setWidget(2, 1, password3); 

		grid.setWidget(3, 1, submit1); 
		
		passwordPanel.add(grid);
		
		
		VerticalPanel pincodePanel = new VerticalPanel();
		pincode1 = new PasswordTextBox();
		pincode2 = new PasswordTextBox();
		pincode3 = new PasswordTextBox();
		
		Button submit2 = new Button("Change", new ClickListener() {
			public void onClick(Widget sender) {
				try {	
					if(!pincode2.getText().equals(pincode3.getText())){
						Window.alert("New Pincode dont match!");
						return;
						
					}
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/user/changepincode");
					rb.setTimeoutMillis(30000);	
					StringBuffer params = new StringBuffer();
					params.append("user_id=" + URL.encodeComponent(Jitavej.session.get("user").isObject().get("id").isNumber().toString()));
					params.append("&");
					params.append("pincode1=" + URL.encodeComponent(pincode1.getText().trim()));
					params.append("&");
					params.append("pincode2=" + URL.encodeComponent(pincode2.getText().trim()));

					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}
						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								if(response.getText().equals("true")){
									Window.alert("Change Complete! ");
								}else{
									Window.alert("Old Pincode dont match! ");
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

		});
		Grid grid2 = new Grid(4,2);
		grid2.setWidget(0, 0, new HTML("Old Pincode")); 
		grid2.setWidget(0, 1, pincode1); 
		
		grid2.setWidget(1, 0, new HTML("New Pincode")); 
		grid2.setWidget(1, 1, pincode2); 
		
		grid2.setWidget(2, 0, new HTML("Confirm New Pincode")); 
		grid2.setWidget(2, 1, pincode3); 

		grid2.setWidget(3, 1, submit2); 
		
		pincodePanel.add(grid2);
		
		tabPanel.add(passwordPanel, "Password");
		tabPanel.add(pincodePanel, "Pincode");
		
		tabPanel.selectTab(0);
		
		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(cancel);
		ver.add(tabPanel);
		
		setWidget(ver);

	}


}
