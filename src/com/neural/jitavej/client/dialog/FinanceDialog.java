package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class FinanceDialog extends FormDialog {
	public FinanceDialog() {
	
		//setStyleName("iframe");
		setHTML("<B>Finance Report</B>");
		
		
		HTML html = new HTML("<IFRAME SRC='/wsgi/finance/payment_page?" +
							"pa_id=" +(int)Jitavej.patient.get("id").isNumber().doubleValue()+"&"+
							"id_sess=" +Jitavej.session.get("sessionId").isString().stringValue()+"' "+
							" WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>");

		//html.setWidth("980");
		//html.setHeight("600");

		ScrollPanel scrollPanel = new ScrollPanel(html);
		
		scrollPanel.setWidth("980");
		
		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(cancel);
		ver.add(scrollPanel);
				
		setWidget(ver);

	}


}
