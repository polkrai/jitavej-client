package com.neural.jitavej.client.record;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class DoseDialog2 extends FormDialog {
	
	ListBox time1, time2, time3, time4;
	TextBox pertime1,pertime2,pertime3,pertime4;
	ListBox acpc1, acpc2, acpc3, acpc4;	
	TextBox comment;
	JSONObject drug;
	RecordDrugPanel recordDrugPanel;
	
	@SuppressWarnings("deprecation")
	public DoseDialog2(RecordDrugPanel recordDrugPanel0, JSONObject drug0) {
		
		//Window.alert(drug0.toString());
		this.recordDrugPanel = recordDrugPanel0;
		this.drug = drug0;
		
		setHTML("<b>Dose</b>");

		Grid grid = new Grid(5,4);
		VerticalPanel ver = new VerticalPanel();
		
		
		Hidden queue = new Hidden();
		queue.setName("queue_id");
		queue.setValue(Jitavej.queue.get("id").toString());

		pertime1 = new TextBox();
		pertime2 = new TextBox();		
		pertime3 = new TextBox();
		pertime4 = new TextBox();
		pertime1.setWidth("30");
		pertime2.setWidth("30");
		pertime3.setWidth("30");
		pertime4.setWidth("30");
		
		time1 = new ListBox();
		time1.addItem("");
		time1.addItem("AM");
		time1.addItem("NOON");
		time1.addItem("PM");
		time1.addItem("HS");
		time1.addItem("2");
		time1.addItem("3");
		time1.addItem("4");
		time1.addItem("PRN");
		time1.addItem("STAT");
		time1.addItem("TODAY");
		time1.addItem("Q 15 MIN");
		time1.addItem("Q 30 MIN");
		time1.addItem("Q 1 HR");
		time1.addItem("Q 2 HR");
		time1.addItem("Q 3 HR");
		time1.addItem("Q 4 HR");
		time1.addItem("Q 6 HR");
		time1.addItem("Q 8 HR");
		time1.addItem("Q 12 HR");		
		time1.addItem("Q 1 WK");
		time1.addItem("Q 2 WK");
		time1.addItem("Q 1 MO");
		time1.addItem("Q 2 MO");
		time1.addItem("Q 3 MO");
		time1.addItem("Q 4 MO");

		
		time2 = new ListBox();
		
		for(int i=0; i<time1.getItemCount(); i++){
			
			time2.addItem(time1.getItemText(i), time1.getValue(i));
		}

		time3 = new ListBox();
		
		for(int i=0; i<time1.getItemCount(); i++){
			
			time3.addItem(time1.getItemText(i), time1.getValue(i));
		}	
		
		time4 = new ListBox();
		
		for(int i=0; i<time1.getItemCount(); i++){
			
			time4.addItem(time1.getItemText(i), time1.getValue(i));
		}
			
		acpc1 = new ListBox();
		acpc1.addItem("");
		acpc1.addItem("AC");
		acpc1.addItem("PC");		
				
		acpc2 = new ListBox();
		acpc2.addItem("");
		acpc2.addItem("ac");
		acpc2.addItem("pc");		
		
		acpc3 = new ListBox();
		acpc3.addItem("");
		acpc3.addItem("ac");
		acpc3.addItem("pc");		
		
		acpc4 = new ListBox();
		acpc4.addItem("");
		acpc4.addItem("ac");
		acpc4.addItem("pc");			
		
		grid.setWidget(0, 0, new HTML("pertime"));
		grid.setWidget(0, 2, new HTML("times")); 
		grid.setWidget(0, 3, new HTML("ac/pc")); 
		
		grid.setWidget(1, 0, pertime1);
		grid.setWidget(1, 1, new HTML("*")); 		
		grid.setWidget(1, 2, time1); 
		grid.setWidget(1, 3, acpc1); 		
		
		grid.setWidget(2, 0, pertime2);
		grid.setWidget(2, 1, new HTML("*")); 		
		grid.setWidget(2, 2, time2); 
		//grid.setWidget(2, 3, acpc2); 		
		
		grid.setWidget(3, 0, pertime3);
		grid.setWidget(3, 1, new HTML("*")); 		
		grid.setWidget(3, 2, time3); 
		//grid.setWidget(3, 3, acpc3); 

		grid.setWidget(4, 0, pertime4);
		grid.setWidget(4, 1, new HTML("*")); 		
		grid.setWidget(4, 2, time4); 
		//grid.setWidget(4, 3, acpc4); 
		
		comment = new TextBox();
		comment.setWidth("92%");
		
		submit = new Button("OK", new ClickListener() {
			
			public void onClick(Widget sender) {
				//form.submit();
				
				final StringBuffer buff = new StringBuffer();
				try{
					
					double pertime = Double.parseDouble(pertime1.getText().trim());
					
					if(pertime > 0){
						
						buff.append((int)pertime);
						buff.append("*");
						buff.append(time1.getValue(time1.getSelectedIndex()));
						buff.append(" ");
						buff.append(acpc1.getValue(acpc1.getSelectedIndex()));						
					}

				}
				catch(Exception ex){
					
					Window.alert(ex.toString());
					return;
				}
				
				try{
					
					double pertime = Double.parseDouble(pertime2.getText().trim());
					
					if(pertime > 0){
						
						buff.append(" + ");
						buff.append((int)pertime);
						buff.append("*");
						buff.append(time2.getValue(time2.getSelectedIndex()));						
					}


				}
				catch(Exception ex){
					//Window.alert(ex.toString());
				}	
				
				try{
					double pertime = Double.parseDouble(pertime3.getText().trim());
					if(pertime > 0){
						buff.append(" + ");
						buff.append((int)pertime);
						buff.append("*");
						buff.append(time3.getValue(time3.getSelectedIndex()));						
					}


				}
				catch(Exception ex){
					//Window.alert(ex.toString());
				}					
				
				try{
					double pertime = Double.parseDouble(pertime4.getText().trim());
					if(pertime > 0){
						buff.append(" + ");
						buff.append((int)pertime);
						buff.append("*");
						buff.append(time4.getValue(time4.getSelectedIndex()));						
					}


				}catch(Exception ex){
				//	Window.alert(ex.toString());
				}					
				
				if(!comment.getText().trim().equals("")){
					buff.append(" (" +comment.getText().trim()+ ")");
				}

				////////////////////////////////////////////////////////////
				
				String dosetext = buff.toString().trim();
				GWT.log(dosetext, null);
				double pertime = 0;
				double frequency = 0;
				double timeuse = 0;
				double countuse = 0;
				dosetext = dosetext.replace("+", ",");
				String[] doseArray = dosetext.split(",");
				StringBuffer time = new StringBuffer();
				for(String dose : doseArray){
					dose = dose.replaceAll("AC", "");
					dose = dose.replaceAll("PC", "");
					if(dose.indexOf("(") != -1){
						dose = dose.substring(0, dose.indexOf("("));
					}
					
					dose = dose.trim();
					
					GWT.log("dose " + dose, null);
					int start = dose.indexOf("*");
					GWT.log("start " + start, null);
					double count = Double.parseDouble(dose.substring(0, start));
					GWT.log("count " + count, null);
					String key = dose.substring(start + 1, dose.length());

					GWT.log("key " + key, null);
					if(time.toString().length() > 0){
						time.append(",");
					}
					
					if(key.endsWith("AM")){
						time.append("08.00");
						timeuse++;
						countuse += count;
					}else if(key.endsWith("NOON")){
						time.append("12.00");
						timeuse++;
						countuse += count;
					}else if(key.endsWith("PM")){
						time.append("18.00");
						timeuse++;
						countuse += count;
					}else if(key.endsWith("HS")){
						time.append("20.00");
						timeuse++;
						countuse += count;
					}else if(key.endsWith("2")){
						time.append("08.00,18.00");
						timeuse += 2;
						countuse += count*2;
					}else if(key.endsWith("3")){
						time.append("08.00,12.00,18.00");
						timeuse += 3;
						countuse += count*3;
					}else if(key.endsWith("4")){
						time.append("08.00,12.00,18.00,20.00");
						timeuse += 4;
						countuse += count*4;
					}else if(key.endsWith("PRN")){
						time.append("PRN");
						timeuse += 1;
						countuse += count*1;
					}else if(key.endsWith("STAT")){
						time.append("STAT");
						timeuse += 1;
						countuse += count*1;
					}else if(key.endsWith("TODAY")){
						time.append("TODAY");
						timeuse += 1;
						countuse += count*1;
					}else if(key.endsWith("Q 15 MIN")){
						time.append("Q 15 MIN");
						timeuse += 96;
						countuse += count*96;
					}else if(key.endsWith("Q 30 MIN")){
						time.append("Q 30 MIN");
						timeuse += 48;
						countuse += count*48;
					}else if(key.endsWith("Q 1 HR")){
						time.append("08.00,09.00,10.00,11.00,12.00,13.00,14.00,15.00,16.00,17.00,18.00,19.00,20.00,21.00,22.00,23.00,24.00,01.00,02.00,03.00,04.00,05.00,06.00,07.00");
						timeuse += 24;
						countuse += count*24;
					}else if(key.endsWith("Q 2 HR")){
						time.append("08.00,10.00,12.00,14.00,16.00,18.00,20.00,22.00,24.00,02.00,04.00,06.00");
						timeuse += 12;
						countuse += count*12;
					}else if(key.endsWith("Q 3 HR")){
						time.append("08.00,11.00,14.00,17.00,20.00,23.00,02.00,05.00");
						timeuse += 8;
						countuse += count*8;
					}else if(key.endsWith("Q 4 HR")){
						time.append("08.00,12.00,16.00,20.00,24.00,04.00");
						timeuse += 6;
						countuse += count*6;
					}else if(key.endsWith("Q 6 HR")){
						time.append("08.00,14.00,20.00,02.00");
						timeuse += 4;
						countuse += count*4;
					}else if(key.endsWith("Q 8 HR")){
						time.append("08.00,16.00,16.00,24.00");
						timeuse += 3;
						countuse += count*3;
					}else if(key.endsWith("Q 12 HR")){
						time.append("08.00,20.00");
						timeuse += 2;
						countuse += count*2;
					}else if(key.endsWith("Q 1 WK")){
						time.append("Q 1 WK");
						timeuse += 1.0/7;
						countuse += count/7;
					}else if(key.endsWith("Q 2 WK")){
						time.append("Q 2 WK");
						timeuse += 1.0/14;
						countuse += count/14;
					}else if(key.endsWith("Q 1 MO")){
						time.append("Q 1 MO");
						timeuse += 1.0/30;
						countuse += count/30;
					}else if(key.endsWith("Q 2 MO")){
						time.append("Q 2 MO");
						timeuse += 1.0/60;
						countuse += count/60;
					}else if(key.endsWith("Q 3 MO")){
						time.append("Q 3 MO");
						timeuse += 1.0/90;
						countuse += count/90;
					}else if(key.endsWith("Q 4 MO")){
						time.append("Q 4 MO");
						timeuse += 1.0/120;
						countuse += count/90;
					}
				}
				
				if(timeuse == 0){
					frequency = 0;
					pertime = 0;
				}else{
					frequency = 24/timeuse;
					pertime = countuse/(24/frequency);
				}
				
				
				////////////////////////////////////////////////////////////
		    	try {
					RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/dose/put3");
					rb.setTimeoutMillis(30000);
					rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
					StringBuffer params = new StringBuffer();
					params.append("drug_id=" + URL.encodeComponent(drug.get("id").isNumber().toString()));
					params.append("&");
					params.append("dosetext=" + URL.encodeComponent(buff.toString().trim()));
					params.append("&");
					params.append("time=" + URL.encodeComponent(time.toString().trim()));	
					params.append("&");
					params.append("frequency=" + frequency);	
					params.append("&");
					params.append("pertime=" + pertime);	
					rb.sendRequest(params.toString(), new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							GWT.log("Error response received during authentication of user", exception);
						}

						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() == 200) {
								recordDrugPanel.suggestDose.setText(buff.toString());
								hide();
							} else {
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
