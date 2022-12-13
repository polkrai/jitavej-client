package com.neural.jitavej.client.history;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.neural.jitavej.client.Jitavej;

public class HisSocialPanel extends DockPanel {
	
	HTML html;
	
	public HisSocialPanel() {
		
		setStyleName("iframe");
		
			html = new HTML();
			html.setWidth("1120");
			html.setHeight("600");
			
			add(html, DockPanel.CENTER);

			update();
	}

	public void update(){
		
		if(Jitavej.patient != null){
			
			String url = "<IFRAME SRC='/miracle/ci3_hmvc/index.php/Icf/history_family?history=true&hn=" +
			Jitavej.patient.get("hn").isString().stringValue()+"&id_sess=" + Jitavej.session.get("sessionId").isString().stringValue() + "' "+
			" WIDTH=1200 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
			
			//Window.alert(url);
			
			html.setHTML(url);
		}
		else {
			
			html.setHTML("");
		}
	}
}
