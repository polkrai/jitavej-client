package com.neural.jitavej.client.history;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class OPDCardDialog extends FormDialog {
	
	public OPDCardDialog(String opdcard) {
		
		setHTML("<b>" + Jitavej.CONSTANTS.view_opd_scan() + "</b>");
		
	    final Image image = new Image();

	    image.setUrl("" + opdcard);
	    
	    image.setPixelSize(850, 1100);

		ScrollPanel scrollPanel = new ScrollPanel(image);
		
		scrollPanel.setPixelSize(870, 640);
	
		VerticalPanel ver = new VerticalPanel();
		
		ver.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		
		ver.add(cancel);
		
		ver.add(scrollPanel);
		
		setWidget(ver);

	}

}
