package com.neural.jitavej.client.history;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.neural.jitavej.client.Jitavej;

public class PsychosocialPanel extends DockPanel {
	
	HTML html;
	
	public PsychosocialPanel() {
		
		setStyleName("iframe");
		
		//	Window.alert(Jitavej.session.get("session_id").isString().stringValue());
		
		html = new HTML();
		html.setWidth("1120");
		html.setHeight("600");
		
		add(html, DockPanel.CENTER);

		//setWidth("1120");
		//setHeight("600");
		
		update();
	}

	public void update(){
		
		if(Jitavej.patient != null){
			
			String url = "<IFRAME SRC='/miracle/ci3_hmvc/index.php/Ps/history_ps?history=true&hn=" +
			Jitavej.patient.get("hn").isString().stringValue()+"&id_sess=" + Jitavej.session.get("sessionId").isString().stringValue() + "' "+
			" WIDTH=1200 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
			html.setHTML(url);
		}
		else{
			
			html.setHTML("");
		}
	}

}
