package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class PsycoPanel extends DockPanel {
	HTML html;
	
	public PsycoPanel() {
		setStyleName("iframe");
		
		//Window.alert(Jitavej.session.get("session_id").isString().stringValue());
	
		html = new HTML();
		html.setWidth("920");
		html.setHeight("500");
		
		add(html, DockPanel.CENTER);

	}
	
	public void setPatient(JSONObject patient){
		
		if(Jitavej.visit != null){
			
			String url = "<iframe src='/wsgi/psyco/req_test?qcalled=true&"+
			"vn_id=" +(int)Jitavej.visit.get("id").isNumber().doubleValue()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" width=920 height=500 frameborder=0></iframe>";
			
			html.setHTML(url);
		}
		else{
			
			html.setHTML("");
		}
	}
	


}
