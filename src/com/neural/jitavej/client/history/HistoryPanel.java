package com.neural.jitavej.client.history;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.neural.jitavej.client.Jitavej;


public class HistoryPanel extends DecoratedTabPanel {
	
	public static JSONObject iframe_url;
	
	DrugHistoryPanel drugHistoryPanel = new DrugHistoryPanel();
	LabHistoryPanel labHistoryPanel = new LabHistoryPanel();
	PsychoHistoryPanel psychoHistoryPanel = new PsychoHistoryPanel();
	MedOptionPanel medOptionPanel = new MedOptionPanel();
	RestorePanel restorePanel = new RestorePanel();
	HisSocialPanel hissocialPanel = new HisSocialPanel();
	PsychosocialPanel psychosocialPanel = new PsychosocialPanel();
	
	public HistoryPanel() {
		
		add(drugHistoryPanel, Jitavej.CONSTANTS.label_drug());
		add(labHistoryPanel, Jitavej.CONSTANTS.label_lab());		
		add(psychoHistoryPanel, Jitavej.CONSTANTS.label_psycho());
		add(medOptionPanel, Jitavej.CONSTANTS.medoption());	
		add(restorePanel, Jitavej.CONSTANTS.restore());
		add(hissocialPanel, Jitavej.CONSTANTS.his_social());
		add(psychosocialPanel, Jitavej.CONSTANTS.his_psychosocial());
		
		selectTab(0);
		
		setPixelSize(650, 540);
	}

}
