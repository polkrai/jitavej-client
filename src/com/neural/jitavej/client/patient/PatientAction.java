package com.neural.jitavej.client.patient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class PatientAction extends VerticalPanel {
	
	HTML vn = new HTML();
	HTML action = new HTML();
	HTML from = new HTML();
	HTML sit = new HTML();	
	
	public PatientAction() {
		
		vn.setStyleName("header1");
		from.setStyleName("header1");
		action.setStyleName("header4");
		sit.setStyleName("header4");
		
		add(vn);
		add(sit);
		add(from);
		add(action);		
		
	}

	public void setPatient(JSONObject patient){
		
		if(Jitavej.visit != null){
			
			vn.setHTML("VN: "+Jitavej.visit.get("vn").isString().stringValue());
			
			if(Jitavej.visit != null){
				
				sit.setHTML(Jitavej.visit.get("privilege").isObject().get("name").isString().stringValue());	
			}
			
			from.setHTML("From : "+Jitavej.queue.get("fromComponent").isObject().get("name").isString().stringValue());
			
			GWT.log(""+Jitavej.queue.get("com1").isString().stringValue(), null);
			
			if(Jitavej.queue.get("actiontext3").isString() != null && !Jitavej.queue.get("actiontext3").isString().stringValue().equals("")){
				from.setHTML("From : "+Jitavej.queue.get("fromComponent").isObject().get("name").isString().stringValue() +" (" + Jitavej.queue.get("actiontext3").isString().stringValue() + ")");
			}
			
			action.setHTML(""+Jitavej.queue.get("actiontext").isString().stringValue());		
		}
		else{
			
			vn.setHTML("");
			sit.setHTML("");
			from.setHTML("");
			action.setHTML("");
		}

	}
}
