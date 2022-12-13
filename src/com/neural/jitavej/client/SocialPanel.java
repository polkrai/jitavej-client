package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class SocialPanel extends DockPanel {
	HTML html;
	
	public SocialPanel() {
		setStyleName("iframe");
		
		//	Window.alert(Jitavej.session.get("session_id").isString().stringValue());
		
			html = new HTML();
			html.setWidth("970");
			html.setHeight("600");
			
			add(html, DockPanel.CENTER);

	}

	public void setPatient(JSONObject patient){
		if(patient != null){
			String url = "<IFRAME SRC='/wsgi/social/background?" +
			"pa_id=" +Jitavej.patient.get("id").isNumber().toString()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
			html.setHTML(url);
		}else{
			html.setHTML("");
		}
	}
}