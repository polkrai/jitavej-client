package com.neural.jitavej.client.old;

import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class OldPanel extends Panel {
	String data = null;

	public OldPanel() {
		setBorder(false);
		setBodyBorder(false);
		onModuleLoad();
	}

	public void onModuleLoad() {

		TabPanel tabPanel = new TabPanel();
		tabPanel.setBodyBorder(false);
		tabPanel.setResizeTabs(true);
		tabPanel.setMinTabWidth(115);
		tabPanel.setTabWidth(100);
		tabPanel.setEnableTabScroll(true);
		// tabPanel.setWidth(450);
		// tabPanel.setHeight(300);
		tabPanel.setActiveTab(0);

		tabPanel.add(new OldRecordPanel());

		setLayout(new BorderLayout());
	//	setTopToolbar(new RecordToolbar());
		// add(new PatientControlPanel(), new
		// BorderLayoutData(RegionPosition.NORTH));
		add(tabPanel, new BorderLayoutData(RegionPosition.CENTER));
	}
}
