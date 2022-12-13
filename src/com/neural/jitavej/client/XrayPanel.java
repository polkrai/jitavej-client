package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class XrayPanel extends DockPanel {
	HTML html;
	
	public XrayPanel() {
		
		setStyleName("iframe");
		
		html = new HTML();
		html.setWidth("920");
		html.setHeight("500");
		
		add(html, DockPanel.CENTER);

	}
	

	public void setPatient(JSONObject patient){
		
		if(Jitavej.visit != null){
			String url = "<iframe src='/wsgi/xray/xray?"+
			"vn_id=" + Jitavej.visit.get("id").isNumber().toString()+"&"+
			"pa_id=" + Jitavej.patient.get("id").isNumber().toString()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" width=920 height=500 frameborder=0></iframe>";
			
			html.setHTML(url);
		}
		else{
			
			html.setHTML("");
		}
	}

}
