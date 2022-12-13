package com.neural.jitavej.client.frontmed;

import java.util.Vector;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.LazyPanel;
import com.neural.jitavej.client.Jitavej;

public class MedLoadTab extends LazyPanel<VerticalPanel> {
	public static Vector<MedLoadPanel> list = new Vector<MedLoadPanel>();
	
	public MedLoadTab() {
	}
	
	@Override
	public VerticalPanel createWidget() {
		VerticalPanel ver = new VerticalPanel();
		
		HorizontalPanel hor = new HorizontalPanel();
		ver.add(hor);
		for (int i = 0; i < Jitavej.stations.size(); i++) {
			final JSONObject station = Jitavej.stations.get(i).isObject();
		//	GWT.log(station.get("mode").isString().stringValue(), null);
			if(station.get("mode").isString().stringValue().equals("med")){
				MedLoadPanel medLoadPanel = new MedLoadPanel(station);
				list.add(medLoadPanel);
				if(hor.getWidgetCount() < 5){
					hor.add(medLoadPanel);
				}else{
					hor = new HorizontalPanel();
					ver.add(hor);
					hor.add(medLoadPanel);
				}
			}
		}
		setSize("970", "600");
		return ver;
	}
	
	public void setEnabled(boolean enabled){
		for(MedLoadPanel medLoadPanel : list){
			medLoadPanel.send.setEnabled(enabled);
		}
	}



}
