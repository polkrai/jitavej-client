package com.neural.jitavej.client.history;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class LabScanDialog extends FormDialog {
	
	public LabScanDialog(String labscan) {
		
		Window.alert(labscan);
		
		setHTML("<b>" + Jitavej.CONSTANTS.view_lab_scan() + "</b>");
		
	    final Image image = new Image();

	    image.setUrl("" + labscan);
	    
	    image.setPixelSize(850, 1100);

		ScrollPanel scrollPanel = new ScrollPanel(image);
		
		scrollPanel.setPixelSize(870, 640);
		
		VerticalPanel verPanel = new VerticalPanel();
		
		verPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		
		verPanel.add(cancel);
		
		verPanel.add(scrollPanel);
		
		setWidget(verPanel);

	}

}
