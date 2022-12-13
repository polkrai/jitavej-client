package com.neural.jitavej.client.history;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.neural.jitavej.client.Jitavej;

public class IpdPanel extends DockPanel {

		HTML html;
		
		public IpdPanel() {
			
			setStyleName("iframe");
			
				html = new HTML();
				html.setWidth("1120");
				html.setHeight("600");
				
				add(html, DockPanel.CENTER);

				update();
		}

		public void update(){
			
			if(Jitavej.patient != null){
				
				String url = "<IFRAME SRC='/miracle/ci_jvkk/index.php/ci_iframe/iframe_ipd/" +
				Jitavej.patient.get("hn").isString().stringValue() + "?history=true&id_sess=" + Jitavej.session.get("sessionId").isString().stringValue() + "' "+
				" WIDTH=1200 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
				html.setHTML(url);
			}
			else{
				
				html.setHTML("");
			}
		}
}
