package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class ZoneRequestPanel extends DockPanel {
	HTML html;
	
	public ZoneRequestPanel() {
		setStyleName("iframe");
		
	//	Window.alert(Jitavej.session.get("session_id").isString().stringValue());
	
		html = new HTML();
		html.setWidth("920");
		html.setHeight("500");
		
		add(html, DockPanel.CENTER);

	}
	
	public void setPatient(JSONObject patient){
		
		//Window.alert(""+Jitavej.patient);
		if(Jitavej.patient != null){
			
			String url = "<iframe src='/wsgi/zone/visit_request?"+
			"pa_id=" +(int)Jitavej.patient.get("id").isNumber().doubleValue()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" width=970 height=600 frameborder=0></iframe>";
			
			html.setHTML(url);
		}
		else{
			
			html.setHTML("");
		}
	}

}
