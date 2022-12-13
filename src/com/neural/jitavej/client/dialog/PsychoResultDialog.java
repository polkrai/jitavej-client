package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;

public class PsychoResultDialog extends FormDialog {
	public PsychoResultDialog(String order_id) {
		setHTML("<B>Psyco</B>");
		// Window.alert(""+Jitavej.session);

		String url = "<IFRAME SRC='http://192.168.44.14/wsgi/psyco/test_result?" + "order_id=" + order_id + "' " + " WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";

		// Window.alert(url);

		HTML html = new HTML(url);
		// html.setWidth("980");
		// html.setHeight("600");

		ScrollPanel scrollPanel = new ScrollPanel(html);
		scrollPanel.setWidth("980");

		String url2 = "<IFRAME SRC='http://192.168.44.14/wsgi/psyco/cure_result?" + "order_id=" + order_id + "' " + " WIDTH=970 HEIGHT=600 FRAMEBORDER=0></IFRAME>";

		// Window.alert(url2);

		HTML html2 = new HTML(url2);
		// html.setWidth("980");
		// html.setHeight("600");

		ScrollPanel scrollPanel2 = new ScrollPanel(html2);
		scrollPanel2.setWidth("980");

		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.add(scrollPanel, Jitavej.CONSTANTS.psycho1());
		tabPanel.add(scrollPanel2, Jitavej.CONSTANTS.psycho2());
		tabPanel.setPixelSize(980, 600);
		tabPanel.selectTab(0);

		VerticalPanel ver = new VerticalPanel();
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		ver.add(cancel);
		ver.add(tabPanel);

		setWidget(ver);

	}

}
