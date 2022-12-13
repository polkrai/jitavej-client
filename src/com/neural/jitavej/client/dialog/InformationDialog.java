package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class InformationDialog extends FormDialog {
	public InformationDialog() {
		setHTML("<B>Information</B>");
		
		HTML html = new HTML("<IFRAME SRC='/nano/iframe/rec_iframe.php?" + "hn=" +Jitavej.patient.get("hn").isString().stringValue()+"'"+" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>");
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
