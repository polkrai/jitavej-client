package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class SocialDialog extends FormDialog {
	public SocialDialog() {
		setHTML("<B>Psyco</B>");
		
		String url = "<IFRAME SRC='/wsgi/social/background?" +
		"pa_id=" +Jitavej.patient.get("id").isNumber().toString()+"&"+
		"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
		" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";
		
	//	Window.alert(url);
		
		HTML html = new HTML(url);
	//	html.setWidth("980");
	//	html.setHeight("600");

		ScrollPanel scrollPanel = new ScrollPanel(html);
		
		scrollPanel.setWidth("980");

		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(cancel);
		ver.add(scrollPanel);
		
		setWidget(ver);

	}


}
