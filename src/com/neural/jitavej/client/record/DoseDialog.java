package com.neural.jitavej.client.record;

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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class DoseDialog extends FormDialog {
	ListBox time;
	TextBox am, noon, pm, hs, other;
	ListBox acpc;	
	TextBox comment;
	RecordDrugPanel recordDrugPanel;
	
	@SuppressWarnings("deprecation")
	public DoseDialog(RecordDrugPanel recordDrugPanel0) {
		//Window.alert(drug0.toString());
		this.recordDrugPanel = recordDrugPanel0;

		setHTML("<B>Dose</B>");

		Grid grid = new Grid(5,20);
		VerticalPanel ver = new VerticalPanel();
		
		//Hidden queue = new Hidden();
		//queue.setName("queue_id");
		//queue.setValue(Jitavej.queue.get("id").toString());

		am = new TextBox();
		noon = new TextBox();		
		pm = new TextBox();
		hs = new TextBox();
		other = new TextBox();
		
		am.setWidth("10");
		noon.setWidth("10");
		pm.setWidth("10");
		hs.setWidth("10");
		other.setWidth("10");
		
		time = new ListBox();
		time.addItem("");
		time.addItem("PRN");
		time.addItem("STAT");
		time.addItem("TODAY");
		time.addItem("Q 15 MIN");
		time.addItem("Q 30 MIN");
		time.addItem("Q 1 HR");
		time.addItem("Q 2 HR");
		time.addItem("Q 3 HR");
		time.addItem("Q 4 HR");
		time.addItem("Q 6 HR");
		time.addItem("Q 8 HR");
		time.addItem("Q 12 HR");		
		time.addItem("Q 1 WK");
		time.addItem("Q 2 WK");
		time.addItem("Q 1 MO");
		time.addItem("Q 2 MO");
		time.addItem("Q 3 MO");
		time.addItem("Q 4 MO");

					
		acpc = new ListBox();
		acpc.addItem("");
		acpc.addItem("AC");
		acpc.addItem("PC");		
					
		
		grid.setWidget(0, 0, am);
		grid.setWidget(0, 1, new HTML("")); 
		grid.setWidget(0, 2, new HTML("<b>AM</b>")); 
		grid.setWidget(0, 3, new HTML("<div style='width:20px;'></div>")); 
		grid.setWidget(0, 4, noon);
		grid.setWidget(0, 5, new HTML("")); 
		grid.setWidget(0, 6, new HTML("<b>NOON</b>")); 	
		grid.setWidget(0, 7, new HTML("<div style='width:20px;'></div>")); 
		grid.setWidget(0, 8, pm);
		grid.setWidget(0, 9, new HTML("")); 
		grid.setWidget(0, 10, new HTML("<b>PM</b>")); 		
		grid.setWidget(0, 11, new HTML("<div style='width:20px;'></div>")); 
		grid.setWidget(0, 12, hs);
		grid.setWidget(0, 13, new HTML("")); 
		grid.setWidget(0, 14, new HTML("<b>HS</b>")); 
		grid.setWidget(0, 15, new HTML("<div style='width:20px;'></div>")); 
		grid.setWidget(0, 16, acpc);
		
		
		grid.setWidget(1, 0, other);
		grid.setWidget(1, 1, new HTML("")); 
		grid.setWidget(1, 2, time); 

		comment = new TextBox();
		comment.setWidth("92%");
		
		submit = new Button("OK", new ClickListener() {
			public void onClick(Widget sender) {
				//form.submit();
				double alluse = 0;
				double frequency = 0;
				double pertime = 0;
				final StringBuffer buff = new StringBuffer();
				StringBuffer time = new StringBuffer();
				////////////////////////////////////////////////////////////////
				try{
					
					if(!am.getText().trim().equals("")){
						
						String amtext = am.getText().trim();
						
						buff.append(amtext);
						buff.append("*");
						buff.append("AM");
						buff.append(" ");
						
						time.append("08.00");
						/*		
						if(amtext.indexOf("/") == -1){
							alluse += Double.parseDouble(amtext);
						}else if(amtext.indexOf("+") == -1){
							int index = amtext.indexOf("/");
							alluse += Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;
						}else{
							int index = amtext.indexOf("+");
							double digit1 = Double.parseDouble(amtext.substring(0, index));
							amtext = amtext.substring(index+1, amtext.length());
							index = amtext.indexOf("/");
							alluse += digit1 + Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;							
						}
						frequency++;
					*/
					}

				}
				catch(Exception ex){
					
					Window.alert(ex.toString());
					return;
				}
				////////////////////////////////////////////////////////////////
				try{
					
					if(!noon.getText().trim().equals("")){
						
						String amtext = noon.getText().trim();
						
						if(buff.toString().length() > 0){
							buff.append(", ");
						}
						
						buff.append(amtext);
						buff.append("*");
						buff.append("NOON");
						buff.append(" ");
						if(time.toString().length() > 0){
							time.append(",");
						}							
						time.append("12.00");
						/*
						if(amtext.indexOf("/") == -1){
							alluse += Double.parseDouble(amtext);
						}else if(amtext.indexOf("+") == -1){
							int index = amtext.indexOf("/");
							alluse += Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;
						}else{
							int index = amtext.indexOf("+");
							double digit1 = Double.parseDouble(amtext.substring(0, index));
							amtext = amtext.substring(index+1, amtext.length());
							index = amtext.indexOf("/");
							alluse += digit1 + Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;							
						}
						frequency++;
						*/
					}

				}
				catch(Exception ex){
					
					Window.alert(ex.toString());
					return;
				}
				////////////////////////////////////////////////////////////////
				try{
					if(!pm.getText().trim().equals("")){
						String amtext = pm.getText().trim();
						if(buff.toString().length() > 0){
							buff.append(", ");
						}
						buff.append(amtext);
						buff.append("*");
						buff.append("PM");
						buff.append(" ");
						if(time.toString().length() > 0){
							time.append(",");
						}							
						time.append("18.00");
						/*
						if(amtext.indexOf("/") == -1){
							alluse += Double.parseDouble(amtext);
						}else if(amtext.indexOf("+") == -1){
							int index = amtext.indexOf("/");
							alluse += Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;
						}else{
							int index = amtext.indexOf("+");
							double digit1 = Double.parseDouble(amtext.substring(0, index));
							amtext = amtext.substring(index+1, amtext.length());
							index = amtext.indexOf("/");
							alluse += digit1 + Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;							
						}
						frequency++;
						*/
					}

				}
				catch(Exception ex){
					Window.alert(ex.toString());
					return;
				}
				////////////////////////////////////////////////////////////////
				try{
					if(!hs.getText().trim().equals("")){
						String amtext = hs.getText().trim();
						
						if(buff.toString().length() > 0){
							buff.append(", ");
						}
						buff.append(amtext);
						buff.append("*");
						buff.append("HS");
						buff.append(" ");
						
						if(time.toString().length() > 0){
							time.append(",");
						}						
						time.append("20.00");
						/*
						if(amtext.indexOf("/") == -1){
							alluse += Double.parseDouble(amtext);
						}else if(amtext.indexOf("+") == -1){
							int index = amtext.indexOf("/");
							alluse += Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;
						}else{
							int index = amtext.indexOf("+");
							double digit1 = Double.parseDouble(amtext.substring(0, index));
							amtext = amtext.substring(index+1, amtext.length());
							index = amtext.indexOf("/");
							alluse += digit1 + Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;							
						}
						frequency++;
						*/
					}

				}catch(Exception ex){
					Window.alert(ex.toString());
					return;
				}
				
				if(acpc.getSelectedIndex() != 0){
					buff.append(" ");
					buff.append(acpc.getValue(acpc.getSelectedIndex()));					
				}

				
				////////////////////////////////////////////////////////////////
				try{
					if(!other.getText().trim().equals("")){
						String amtext = other.getText().trim();
						if(buff.toString().trim().length() > 0){
							buff.append(", ");
						}
						buff.append(amtext);
						buff.append("*");
						buff.append(DoseDialog.this.time.getValue(DoseDialog.this.time.getSelectedIndex()));
						buff.append(" ");
						/*
						
						if(amtext.indexOf("/") == -1){
							alluse += Double.parseDouble(amtext);
						}else if(amtext.indexOf("+") == -1){
							int index = amtext.indexOf("/");
							alluse += Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;
						}else{
							int index = amtext.indexOf("+");
							double digit1 = Double.parseDouble(amtext.substring(0, index));
							amtext = amtext.substring(index+1, amtext.length());
							index = amtext.indexOf("/");
							alluse += digit1 + Double.parseDouble(amtext.substring(0, index)) / Double.parseDouble(amtext.substring(index+1, amtext.length())) ;							
						}
						frequency++;
						*/
					}

				}catch(Exception ex){
					Window.alert(ex.toString());
					return;
				}				
				////////////////////////////////////////////////////////////////
				/*	
				if(frequency > 0){
					pertime = alluse / frequency;
				}else{
					Window.alert("Frequency = 0");
					return;					
				}
				
				*/
				
				if(!comment.getText().trim().equals("")){
					buff.append(" (" +comment.getText().trim()+ ")");
				}

				////////////////////////////////////////////////////////////
				

				
				////////////////////////////////////////////////////////////
		    	try {
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/dose/put3");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					StringBuffer params = new StringBuffer();
					params.append("drug_id=" + URL.encodeComponent(recordDrugPanel.drug.get("id").isNumber().toString()));
					params.append("&");
					params.append("dosetext=" + URL.encodeComponent(buff.toString().trim()));
					params.append("&");
					params.append("time=" + URL.encodeComponent(time.toString().trim()));	
					params.append("&");
					params.append("frequency=" + 1);	
					params.append("&");
					params.append("pertime=" + 1);	
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								
								recordDrugPanel.suggestDose.setText(buff.toString());
								hide();
							} 
							else {
								
								Window.alert("response.getStatusCode() "+response.getStatusCode());
								Window.alert("response.getText() "+response.getText());
							}
						}
					});

				} catch (RequestException e1) {
					Window.alert(e1.toString());
				} 				
				
				///////////////////////////////////////////////////////////

				
				
			}
		});

		
		ver.add(grid);
		ver.add(new HTML("Remark"));
		ver.add(comment);
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(4);
		hor.add(submit);
		hor.add(cancel);
		ver.add(new HTML(""));
		ver.add(hor);
	
		setWidget(ver);

	}


}
