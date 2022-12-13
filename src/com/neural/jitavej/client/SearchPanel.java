package com.neural.jitavej.client;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

public class SearchPanel extends DockPanel {

	public SearchPanel() {
		
		setStyleName("iframe2");
		
		HTML html = new HTML("<IFRAME SRC='http://localhost/nano/iframe/rec_iframe.php?opt=search'" + " WIDTH=970 HEIGHT=720 FRAMEBORDER=0></IFRAME>");
		html.setWidth("970");
	//	html.setHeight("100%");
		
		add(html, DockPanel.CENTER);

		
		
	}

}
