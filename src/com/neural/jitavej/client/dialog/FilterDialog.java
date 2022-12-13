package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class FilterDialog extends FormDialog {

	public FilterDialog() {
		setHTML("<B>Filter</B>");

		HTML html = new HTML("<IFRAME SRC='/jvkk_filter/iframe_history.php?vn=" + Jitavej.visit.get("vn").isString().stringValue() + "' " + " WIDTH=560 HEIGHT=400 FRAMEBORDER=0></IFRAME>");

		ScrollPanel scrollPanel = new ScrollPanel(html);

		scrollPanel.setWidth("560");

		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(cancel);
		ver.add(new HTML(Jitavej.visit.get("vn").isString().stringValue()));
		ver.add(scrollPanel);
		
		setWidget(ver);

	}

}
