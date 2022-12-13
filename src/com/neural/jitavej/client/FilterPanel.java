package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class FilterPanel extends DockPanel {
	HTML html;
	public FilterPanel() {
		setStyleName("iframe");
		
		//	Window.alert(Jitavej.session.get("session_id").isString().stringValue());
		
			html = new HTML();
			html.setWidth("970");
			html.setHeight("600");
			
			add(html, DockPanel.CENTER);
		
	}

	public void setPatient(JSONObject patient){
		if(patient != null){
			String url = "<IFRAME SRC='/jvkk_filter/iframe_filter.php?"+
			"vn=" +Jitavej.visit.get("vn").isString().stringValue()+"&"+
			"hn=" +Jitavej.patient.get("hn").isString().stringValue()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
			html.setHTML(url);
		}else{
			html.setHTML("");
		}
	}
	
}
