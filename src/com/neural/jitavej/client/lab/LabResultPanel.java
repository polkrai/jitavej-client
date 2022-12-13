package com.neural.jitavej.client.lab;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class LabResultPanel extends HorizontalPanel {

	public static LabResultOrder labResultOrder;
	
	public static LabResultRecord labResultRecord;

	public LabResultPanel() {
		
		labResultOrder = new LabResultOrder();
		labResultRecord = new LabResultRecord();
		
		//labResultOrder.setWidth('900');

        add(labResultOrder);
        add(labResultRecord);
	}
}
