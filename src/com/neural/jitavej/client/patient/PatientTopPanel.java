package com.neural.jitavej.client.patient;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;

public class PatientTopPanel extends HorizontalPanel {
	
	ListBox userBox;
	PatientAction patientAction = new PatientAction();
	HTML image_pt, hn, dn, name, age, sunchart, sasana, job, drugerror, mederror, risk, warning, address, sit,editmederror, editwarning, editdn;

	public PatientTopPanel() {

		VerticalPanel west = new VerticalPanel();
		//dock.setStyleName("cw-DockPanel");
		west.setSpacing(0);
		west.setWidth("100");
		west.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		//Image image = new Image("http://192.168.44.15/nano/"+Jitavej.patient.get("picture").isString().stringValue());
		Image image = Jitavej.images.nobody().createImage();

		image.setSize("100", "108");

		west.add(image);
		//west.add(new HTML("HN0015585"));

		//image_pt = new HTML("");
		//image_pt.setStyleName("");
		hn = new HTML("");
		hn.setStyleName("header3");
		
		dn = new HTML("");
		dn.setStyleName("header3");
		
		name = new HTML("");
		name.setStyleName("header3");
		
		age = new HTML("");
		age.setStyleName("header3");
		
		sunchart = new HTML("");
		sasana = new HTML("");		
		job = new HTML("");
		drugerror = new HTML("");		
		mederror = new HTML("");
		
		risk = new HTML("");		
		warning = new HTML("");
		
		editmederror = new HTML(Jitavej.images.edit().createImage().toString());
		editmederror.setStyleName("edit");
		
		editwarning = new HTML(Jitavej.images.edit().createImage().toString());	
		editwarning.setStyleName("edit");
		
		editdn = new HTML(Jitavej.images.edit().createImage().toString());	
		editdn.setStyleName("edit");
		
		editmederror.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				MedErrorDialog dialog = new MedErrorDialog();
				dialog.show();
				dialog.center();
			}
		});
		
		editwarning.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				WarningDialog dialog = new WarningDialog();
				dialog.show();
				dialog.center();
			}
		});
		
		editdn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				DnDialog dialog = new DnDialog();
				dialog.show();
				dialog.center();
			}
		});
		
		address = new HTML("");
		sit = new HTML("");
		//sit.setStyleName("header3");
		
		HorizontalPanel hor1 = new HorizontalPanel();
		hor1.setSpacing(3);
		
		if(Jitavej.mode.equals("dental")){
			hor1.add(new HTML("<b>DN</b>:"));
			hor1.add(dn);		
			hor1.add(editdn);	
			editdn.setVisible(false);
			hn.removeStyleName("header3");
			hn.setStyleName("header4");
		}
		
		hor1.add(new HTML("<b>"+Jitavej.CONSTANTS.info_hn()+"</b>:"));
		hor1.add(hn);		
		hor1.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_name()+"</b>:"));
		hor1.add(name);
		hor1.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_age()+"</b>:"));
		hor1.add(age);
		
		/*
		HorizontalPanel hor2 = new HorizontalPanel();
		hor2.setSpacing(2);
		hor2.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_sunchart()+"</b>:"));
		hor2.add(sunchart);	
		hor2.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_sasana()+"</b>:"));
		hor2.add(sasana);	
		hor2.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_job()+"</b>:"));
		hor2.add(job);		
		 */	
		
		HorizontalPanel hor3 = new HorizontalPanel();
		hor3.setSpacing(2);
		hor3.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_address()+"</b>:"));
		hor3.add(address);	
					
		HorizontalPanel hor4 = new HorizontalPanel();
		hor4.setSpacing(2);
		hor4.add(new HTML("<b>"+Jitavej.CONSTANTS.info_sit()+"</b>:"));
		hor4.add(sit);	
		
		HorizontalPanel hor5 = new HorizontalPanel();
		hor5.setSpacing(2);
		hor5.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_drugerror()+"</b>:"));
		hor5.add(drugerror);	
		
		HorizontalPanel hor6 = new HorizontalPanel();
		hor6.setSpacing(2);
		hor6.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_risk()+"</b>:"));
		hor6.add(risk);	
		
		HorizontalPanel hor7 = new HorizontalPanel();
		hor7.setSpacing(2);
		hor7.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_mederror()+"</b>:"));
		hor7.add(mederror);	
		hor7.add(editmederror);			

		
		HorizontalPanel hor8 = new HorizontalPanel();
		hor8.setSpacing(2);
		hor8.add(new HTML("<b>"+Jitavej.CONSTANTS.patient_warning()+"</b>:"));
		hor8.add(warning);	
		hor8.add(editwarning);	
		
		/*				
		try {
			Date birthdate = new Date((int)Jitavej.patient.get("birthdate").isNumber().doubleValue());
			DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy");
			Date now = new Date();
			int date1 = Integer.parseInt(dateTimeFormat.format(birthdate));
			int date2 = Integer.parseInt(dateTimeFormat.format(now));		
			
			hor2.add(new HTML(""+(date2-date1)));
		} 
		catch (Exception e) {
			//	e.printStackTrace();
		}	
		 */	
		
		userBox = new ListBox();
		//userBox.setEnabled(false);
		if(Jitavej.mode.equals("frontmed")){
			
			userBox.addItem(null);
			
			//userBox.addItem("", null);
			for (int i = 0; i < Jitavej.users.size(); i++) {
				JSONObject user = Jitavej.users.get(i).isObject();
				GWT.log(user.toString(), null);
				//userBox.addItem(user.get("id").isNumber().toString());
				userBox.addItem(user.get("firstname").isString().stringValue() +" "+user.get("lastname").isString().stringValue(), user.get("id").isNumber().toString());			
			}
			
			userBox.setSelectedIndex(0);

			userBox.addChangeListener(new ChangeListener(){

				public void onChange(Widget sender) {
					
					try {
						
						RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/patient/setowner");
						rb.setTimeoutMillis(30000);	
						StringBuffer params = new StringBuffer();
						params.append("patient_id=" + URL.encodeComponent(Jitavej.patient.get("id").isNumber().toString()));
						params.append("&");
						params.append("owner_id=" + userBox.getValue(userBox.getSelectedIndex()));
						rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
						rb.sendRequest(params.toString(), new RequestCallback() {
							
							public void onError(Request request, Throwable exception) {
								GWT.log("Error response received during authentication of user", exception);
							}
							
							public void onResponseReceived(Request request, Response response) {
								
								if (response.getStatusCode() == 200) {
									//Window.alert(Jitavej.CONSTANTS.record_edited());
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
				
			});			
		}
		
		HorizontalPanel hor9 = new HorizontalPanel();
		hor9.setSpacing(2);
		hor9.add(new HTML("<b>"+Jitavej.CONSTANTS.info_owner()+"</b>:"));
		hor9.add(userBox);	
		
		VerticalPanel ver1 = new VerticalPanel();
		ver1.add(hor1);
		//ver1.add(hor2);
		ver1.add(hor3);
		//ver1.add(hor4);
		ver1.add(hor5);
		ver1.add(hor6);
		ver1.add(hor7);		
		ver1.add(hor8);
		
	
		if(Jitavej.mode.equals("frontmed")){
			ver1.add(hor9);
		}
		ver1.setWidth("440");
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(0);
		hor.add(ver1);
		//hor.add(new PatientResult());	

        add(west);  
        add(hor);  
        add(patientAction);
        //add(new PatientResult(), DockPanel.EAST);  

        setHeight("108");

	}

	public void setPatient(JSONObject patient){
		
		if(patient != null){
			
			hn.setHTML(Jitavej.patient.get("hn").isString().stringValue());
			
			if(Jitavej.dn != null && Jitavej.dn.get("dn").isString() != null){
				
				dn.setHTML(Jitavej.dn.get("dn").isString().stringValue());	
				editdn.setVisible(true);
			}
			else{
				
				dn.setHTML("*");	
				editdn.setVisible(false);
			}
			
			
			name.setHTML(Jitavej.patient.get("title").isString().stringValue() +" "+ Jitavej.patient.get("firstname").isString().stringValue() +" "+ Jitavej.patient.get("lastname").isString().stringValue());	
			

			String tmp = Jitavej.patient.get("birthdate").toString();
			Date date = new Date(Long.parseLong(tmp));
			Date now = new Date();
			//Window.alert(date.toString());
			age.setHTML(""+(now.getYear()-date.getYear()));
			
			sunchart.setHTML(Jitavej.patient.get("sunchart").isString().stringValue());	
			
			sasana.setHTML(Jitavej.patient.get("sasana").isString().stringValue());	
			
			job.setHTML(Jitavej.patient.get("job").isString().stringValue());
			
			//Jitavej.patient.get("drugerror1").isString().stringValue();
			
			//Jitavej.patient.get("drugerror1").isString().stringValue().replace("/start[\s\S]*?<\/script>/", "");
			
			drugerror.setHTML("<FONT COLOR='RED'>"+Jitavej.patient.get("drugerror1").isString().stringValue() +" "+ Jitavej.patient.get("drugerror2").isString().stringValue()+"</FONT>");			
			
			mederror.setHTML("<FONT COLOR='RED'>"+Jitavej.patient.get("mederror").isString().stringValue()+"</FONT>");
			
			risk.setHTML("<FONT COLOR='RED'>"+Jitavej.patient.get("risk").isString().stringValue()+"</FONT>");
			
			warning.setHTML("<FONT COLOR='RED'>"+Jitavej.patient.get("warning").isString().stringValue()+"</FONT>");

			StringBuffer buffer = new StringBuffer();
			
			if(Jitavej.address.get("ad_ban") != null && Jitavej.address.get("ad_ban").isString() != null){
				buffer.append(Jitavej.address.get("ad_ban").isString().stringValue());
			}
			
			if(Jitavej.address.get("ad_moo") != null && Jitavej.address.get("ad_moo").isString() != null){
				buffer.append(" "+Jitavej.CONSTANTS.patient_moo());
				buffer.append("  "+Jitavej.address.get("ad_moo").isString().stringValue());
			}
			
			if(Jitavej.address.get("ad_tambol") != null && Jitavej.address.get("ad_tambol").isString() != null){
				buffer.append("  "+Jitavej.CONSTANTS.patient_tambon());
				buffer.append("  "+Jitavej.address.get("ad_tambol").isString().stringValue());
			}
			
			if(Jitavej.address.get("ad_ampher") != null && Jitavej.address.get("ad_ampher").isString() != null){
				buffer.append("  "+Jitavej.CONSTANTS.patient_amphor());
				buffer.append("  "+Jitavej.address.get("ad_ampher").isString().stringValue());
			}
			
			if(Jitavej.address.get("ad_province") != null && Jitavej.address.get("ad_province").isString() != null){
				buffer.append("  "+Jitavej.CONSTANTS.patient_province());
				buffer.append("  "+Jitavej.address.get("ad_province").isString().stringValue());
			}	

			address.setHTML(buffer.toString());
			
			if(Jitavej.patient.get("owner").isObject() != null){
				
				int index = 0;
				for (int i = 0; i < Jitavej.users.size(); i++) {
					
					JSONObject user = Jitavej.users.get(i).isObject();
					
					if(Jitavej.patient.get("owner").isObject().get("id").isNumber().doubleValue() == user.get("id").isNumber().doubleValue()){
						index = i;
						break;
					}
				}
				userBox.setSelectedIndex(index+1);
			}		
					
		}
		else{
			
			hn.setHTML("   ");	
			dn.setHTML("   ");	
			
			name.setHTML("    ");	
			sunchart.setHTML("    ");	
			sasana.setHTML("    ");	
			job.setHTML("    ");	
			drugerror.setHTML("    ");			
			mederror.setHTML("    ");	
			risk.setHTML("    ");			
			warning.setHTML("    ");	
			address.setHTML("    ");
			sit.setHTML("   ");
			userBox.setSelectedIndex(0);
		}
		
		patientAction.setPatient(patient);
		
		if(Jitavej.visit != null){
			sit.setHTML(""+Jitavej.visit.get("privilege").isObject().get("name").isString().stringValue());	
		}
		

	}


}
