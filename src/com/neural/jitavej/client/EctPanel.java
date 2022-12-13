package com.neural.jitavej.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class EctPanel extends DockPanel {
	HTML html;
	
	public EctPanel() {
		setStyleName("iframe");
		
	//	Window.alert(Jitavej.session.get("session_id").isString().stringValue());
	
		html = new HTML();
		html.setWidth("970");
		html.setHeight("600");
		
		add(html, DockPanel.CENTER);

	}
	
	public void setPatient(JSONObject patient){
		
		if(Jitavej.visit != null){
			String url = "<IFRAME SRC='/nano/index2.php?option=com__ect&task=request_display&qcalled=true&iframe=iframe&"+
			"vn_id=" +(int)Jitavej.visit.get("id").isNumber().doubleValue()+"&"+
			"vn=" + Jitavej.visit.get("vn").isString().stringValue()+"&"+
			"pa_id=" +Jitavej.patient.get("id").isNumber().toString()+"&"+
			"hn=" + Jitavej.patient.get("hn").isString().stringValue()+"&"+
			"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
			" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
			html.setHTML(url);
		}else{
			html.setHTML("");
		}
		
	}

}
