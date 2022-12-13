package com.neural.jitavej.client.patient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.neural.jitavej.client.EctPanel;
import com.neural.jitavej.client.EegPanel;
import com.neural.jitavej.client.EkgPanel;
import com.neural.jitavej.client.FilterPanel;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.PsycoPanel;
import com.neural.jitavej.client.XrayPanel;
import com.neural.jitavej.client.SocialPanel;
import com.neural.jitavej.client.ZoneReferPanel;
import com.neural.jitavej.client.ZoneRequestPanel;
import com.neural.jitavej.client.appointment.AppointmentPanel;
import com.neural.jitavej.client.dental.DentalFilterPanel;
import com.neural.jitavej.client.dental.DentalItemPanel;
import com.neural.jitavej.client.dental.DentalPanel;
import com.neural.jitavej.client.dental.DnPanel;
import com.neural.jitavej.client.frontmed.MedLoadTab;
import com.neural.jitavej.client.history.LabHistoryPanel;
import com.neural.jitavej.client.history.LabItemHistoryPanel;
//import com.neural.jitavej.client.history.LabHistoryPanel;
import com.neural.jitavej.client.lab.LabItem;
import com.neural.jitavej.client.lab.LabPanel;
import com.neural.jitavej.client.lab.LabReportPanel;
import com.neural.jitavej.client.lab.LabResultPanel;
import com.neural.jitavej.client.lab.LabValue;
import com.neural.jitavej.client.record.RecordDrugPanel;
import com.neural.jitavej.client.record.RecordIcd10Panel;
import com.neural.jitavej.client.record.RecordTextPanel;
import com.neural.jitavej.client.record.ReferMedPanel;
import com.neural.jitavej.client.record.RemedPanel;
import com.neural.jitavej.client.refer.ReferReplyPanel;
import com.neural.jitavej.client.refer.ReferSendPanel;

public class PatientPanel extends VerticalPanel {
	
	public static DecoratedTabPanel tabPanel;
	public static RecordIcd10Panel recordIcd10Panel;
	public static ReferMedPanel referMedPanel;
	public static RecordDrugPanel recordDrugPanel = new RecordDrugPanel("opd"); 
	public static DentalPanel dentalPanel;
	public static DentalFilterPanel dentalFilterPanel;
	public static DnPanel dnPanel;
	public static DentalItemPanel dentalItemPanel;
	public static RecordTextPanel recordTextPanel; 	
	public static PatientTopPanel patientTopPanel = new PatientTopPanel();
	public static MedLoadTab medLoadTab = new MedLoadTab();
	public static AppointmentPanel appointmentPanel  = new AppointmentPanel(); 
	public static LabPanel labPanel = new LabPanel(); 
	public static ReferSendPanel referSendPanel = new ReferSendPanel(); 
	public static ReferReplyPanel referReplyPanel = new ReferReplyPanel(); 	
	public static PatientMenuPanel patientMenuPanel;
	public static PsycoPanel psycoPanel = new PsycoPanel();
	public static ZoneReferPanel zoneReferPanel = new ZoneReferPanel();
	public static ZoneRequestPanel zoneRequestPanel = new ZoneRequestPanel();
	
	public static EctPanel ectPanel = new EctPanel();
	public static XrayPanel xrayPanel = new XrayPanel();
	public static EegPanel eegPanel = new EegPanel();
	public static EkgPanel ekgPanel = new EkgPanel();
	public static FilterPanel filterPanel;
	public static SocialPanel socialPanel;
	public static RemedPanel remedPanel;
	public static LabResultPanel labResultPanel;
	public static LabReportPanel labReportPanel;	
	public static LabItem labItem;
	public static LabValue labValue;
	
	
	public PatientPanel() {
		
		setWidth("900");
		
		setSpacing(0);
		
		tabPanel = new DecoratedTabPanel();

        if(Jitavej.mode.equals("med")){
        
            try {
            
            	RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server+ "/user/station");
            
            	rb.setTimeoutMillis(30000);
            
            	rb.sendRequest(null, new RequestCallback() {
            	
            		public void onError(Request request, Throwable exception) {
            		
            			Window.alert("Error response received during authentication of user " +exception);
            		}
            	
            		public void onResponseReceived(Request request, Response response) {
            		
            			if (response.getStatusCode() == 200) {
            			
            				GWT.log(response.getText(), null);
            			
            				Jitavej.station = JSONParser.parse(response.getText()).isObject();
            
            				recordIcd10Panel = new RecordIcd10Panel();
            			
                			if(Jitavej.station.get("name").isString().stringValue().equalsIgnoreCase("ECT")){
                				
                				tabPanel.add(ectPanel, "ECT");	
                			}
            			
                			recordDrugPanel = new RecordDrugPanel("opd"); 
                			recordTextPanel = new RecordTextPanel();
                			referMedPanel = new ReferMedPanel();
                			
                			tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
                			tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record());
                			tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
                			tabPanel.add(referMedPanel, Jitavej.CONSTANTS.tab_refer());
                			tabPanel.selectTab(0);
                		} 
                		else {
            			
                			Window.alert("/user/station response.getText() = " + response.getText());
            			}
            		}
            	});
            } 
            catch (RequestException e1) {
            	
            	Window.alert(e1.toString());
            }

		}
		else if(Jitavej.mode.equals("dental")){
			
			recordIcd10Panel = new RecordIcd10Panel();
			dentalPanel = new DentalPanel(true);
			recordDrugPanel = new RecordDrugPanel("dental"); 
			recordTextPanel = new RecordTextPanel();
			
			dnPanel = new DnPanel();
			dentalFilterPanel = new DentalFilterPanel();
			dentalItemPanel = new DentalItemPanel();
			
			tabPanel.add(dnPanel, Jitavej.CONSTANTS.dnpanel());
			tabPanel.add(dentalFilterPanel, Jitavej.CONSTANTS.dental_filter());
			tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
			tabPanel.add(dentalPanel, Jitavej.CONSTANTS.tab_dental());
			tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record());
			tabPanel.add(recordTextPanel, Jitavej.CONSTANTS.tab_text());
			tabPanel.add(appointmentPanel, Jitavej.CONSTANTS.tab_appointment());
			tabPanel.add(dentalItemPanel, Jitavej.CONSTANTS.dental_item());
			
			//tabPanel.add(labPanel, Jitavej.CONSTANTS.tab_lab());		
			//tabPanel.add(psycoPanel, "Psyco");		
			tabPanel.selectTab(1);
		}
		else if(Jitavej.mode.equals("frontmed")){
			
			tabPanel.add(medLoadTab, "Med Load");
			
		}
		else if(Jitavej.mode.equals("backmed")){
			
			recordDrugPanel = new RecordDrugPanel("opd");
			
			tabPanel.add(recordDrugPanel, Jitavej.CONSTANTS.tab_record());
			//recordDrugPanel = new RecordDrugPanel("o"); 
			recordDrugPanel.setVisible(true);
			recordDrugPanel.remed.setVisible(false);
			//recordDrugPanel.save.setVisible(false);
			recordDrugPanel.addBar.setVisible(false);
			//recordDrugPanel.grid.setDisabled(true);
			recordDrugPanel.grid.addGridCellListener(new GridCellListenerAdapter(){});
			recordDrugPanel.remed.setEnabled(false);
			recordDrugPanel.remedBox.setEnabled(false);
			recordDrugPanel.clear.setEnabled(false);
			
			tabPanel.add(labPanel, Jitavej.CONSTANTS.tab_lab());		
			tabPanel.add(psycoPanel, Jitavej.CONSTANTS.tab_psyco());		
			
			DecoratedTabPanel tab = new DecoratedTabPanel();
			tab.add(xrayPanel, "X-ray");
			tab.add(eegPanel, "EEG");
			tab.add(ekgPanel, "EKG");
			tab.selectTab(0);
			
			tabPanel.add(tab, "X-ray");		
			tabPanel.add(referSendPanel, Jitavej.CONSTANTS.tab_refer());	
			tabPanel.add(referReplyPanel, Jitavej.CONSTANTS.tab_referr());	
			tabPanel.add(appointmentPanel, Jitavej.CONSTANTS.tab_appointment());
			tabPanel.add(zoneReferPanel, Jitavej.CONSTANTS.tab_zonerefer());	
			tabPanel.add(zoneRequestPanel, Jitavej.CONSTANTS.tab_zonerequest());
			
			
		}
		else if(Jitavej.mode.equals("remed")){
			
			remedPanel = new RemedPanel();
			recordIcd10Panel = new RecordIcd10Panel();
			recordTextPanel = new RecordTextPanel();
			
			tabPanel.add(recordIcd10Panel, Jitavej.CONSTANTS.tab_icd10());
			tabPanel.add(remedPanel, Jitavej.CONSTANTS.tab_remed());
			tabPanel.selectTab(1);
		}
		else if(Jitavej.mode.equals("lab")){
			
			labResultPanel = new LabResultPanel();
			labReportPanel = new LabReportPanel();
				
			tabPanel.add(labResultPanel, Jitavej.CONSTANTS.tab_lab_result());
			tabPanel.add(labPanel, Jitavej.CONSTANTS.tab_lab());		
			tabPanel.add(appointmentPanel,Jitavej.CONSTANTS.tab_appointment());
			tabPanel.add(labReportPanel,Jitavej.CONSTANTS.report());
			
			//tabPanel.add(labItem,"LabItem");
			//tabPanel.add(labValue,"LabValue");
		}
		else if(Jitavej.mode.equals("emergency")){
			
			filterPanel = new FilterPanel();
			socialPanel = new SocialPanel();
			
			tabPanel.add(filterPanel, Jitavej.CONSTANTS.tab_filter());
			tabPanel.add(socialPanel, Jitavej.CONSTANTS.tab_social());
			tabPanel.add(medLoadTab, Jitavej.CONSTANTS.tab_medload());	
			tabPanel.add(labPanel, Jitavej.CONSTANTS.tab_lab());
			tabPanel.add(psycoPanel, Jitavej.CONSTANTS.tab_psyco());	
			tabPanel.add(appointmentPanel, Jitavej.CONSTANTS.tab_appointment());			
		}
				
		if(tabPanel.getTabBar().getTabCount() > 0){
			
			tabPanel.selectTab(0);
		}
		
		
		patientMenuPanel = new PatientMenuPanel();
		
		add(patientTopPanel);
		
		add(patientMenuPanel);
		
        add(tabPanel);

        setPatient(null);
	}

	public static void setPatient(JSONObject patient){
		
		Jitavej.patient = patient;
		
		if(patient == null){
			
			Jitavej.visit = null;
		}
		
		if(dnPanel != null){
			
			dnPanel.setPatient(patient);
			
			dentalPanel.setPatient(patient);
		}
		
		if(tabPanel.getTabBar().getTabCount() > 0){
			
			tabPanel.selectTab(0);
		}
		
		patientTopPanel.setPatient(patient);
		
		//if(Jitavej.mode.equals("backmed")){
		psycoPanel.setPatient(patient);
		zoneReferPanel.setPatient(patient);
		zoneRequestPanel.setPatient(patient);
		ectPanel.setPatient(patient);
		xrayPanel.setPatient(patient);
		eegPanel.setPatient(patient);
		ekgPanel.setPatient(patient);	
		//}
		
		LabPanel.update();
	
		if(recordIcd10Panel != null){
			
			recordIcd10Panel.setPatient(patient);
		}		
		
		if(recordDrugPanel != null){
			
			recordDrugPanel.setPatient(patient);
		}
		
		if(recordTextPanel != null){
			
			recordTextPanel.setPatient(patient);			
		}

		if(referMedPanel != null){
			
			referMedPanel.setPatient(patient);			
		}
		
		if(remedPanel != null){
			
			remedPanel.remedOrderPanel.update();
		}
		
		if(labResultPanel != null){
			
			labResultPanel.labResultOrder.update();
		}
		
		if(filterPanel != null){
			
			filterPanel.setPatient(patient);
		}
		
		if(socialPanel != null){
			
			socialPanel.setPatient(patient);
		}		
		
		if(patient != null){
			
			medLoadTab.setEnabled(true);
			//labPanel.labOrderPanel.save.setEnabled(true);
			if (Jitavej.visit != null) {
				
				labPanel.labOrderPanel.save.setEnabled(true);
			}
			else {
				
				labPanel.labOrderPanel.save.setEnabled(false);
			}
			
			patientMenuPanel.setEnabled(true);
			appointmentPanel.add.setEnabled(true);
			referSendPanel.add.setEnabled(true);
			referReplyPanel.add.setEnabled(true);
		}
		else {
			
			medLoadTab.setEnabled(false);
			patientMenuPanel.setEnabled(false);
			labPanel.labOrderPanel.save.setEnabled(false);
			appointmentPanel.add.setEnabled(false);
			referSendPanel.add.setEnabled(false);
			referReplyPanel.add.setEnabled(false);
		}
	
	}
}