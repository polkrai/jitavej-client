package com.neural.jitavej.client.lab;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.widgetideas.client.LazyPanel;
import com.neural.jitavej.client.Jitavej;

public class LabPanel extends LazyPanel<SimplePanel> {
	
	String data = null;
	
	public static LabOrderPanel labOrderPanel = new LabOrderPanel(true);
	
	public LabPanel() {
	}

	@Override
	public SimplePanel createWidget() {
		
		SimplePanel panel = new SimplePanel(); 
		/*	
	 	if(Jitavej.mode.equals("lab_order")){
			DecoratedTabPanel tab = new DecoratedTabPanel();
			tab.setStyleName("gwt-TabPanelBottom2");
			tab.add(labOrderPanel, Jitavej.CONSTANTS.tab_lab()+" (One Day)");
			tab.add(new LabOrderPanel(false), Jitavej.CONSTANTS.tab_lab()+" (Continuation)");
			tab.selectTab(0);
			panel.add(tab);
			
		}else{
		*/	panel.add(labOrderPanel);
        /*}
        */
		
		return panel;
	}
	
	public static void update(){
		
		LabOrderPanel.setPatient(Jitavej.patient);
	}
}
