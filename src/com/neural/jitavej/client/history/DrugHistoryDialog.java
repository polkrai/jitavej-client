package com.neural.jitavej.client.history;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;
import com.neural.jitavej.client.record.RecordDrugPanel;

public class DrugHistoryDialog extends FormDialog {
	
	public DrugHistoryPanel drugHistoryPanel = new DrugHistoryPanel();
	
	RecordDrugPanel recordDrugPanel;
	
	public DrugHistoryDialog(RecordDrugPanel recordDrugPanel0) {
		
		setHTML("<b>" + Jitavej.CONSTANTS.record_item() + "</b>");
		
		this.recordDrugPanel = recordDrugPanel0;
		
		Button remed = new Button("Remed", new ClickListener() {
			
			public void onClick(Widget sender) {
				
				recordDrugPanel.reorder((int)drugHistoryPanel.drugItemHistoryPanel.order.get("id").isNumber().doubleValue());
				
				hide();
			}
		});		
		
		ScrollPanel scrollPanel = new ScrollPanel(drugHistoryPanel);
		
		scrollPanel.setWidth("850");

		HorizontalPanel hor = new HorizontalPanel();
		hor.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		
		hor.add(remed);
		hor.add(new HTML("<div style='width:540px'></div>"));
		hor.add(cancel);
		
		VerticalPanel ver = new VerticalPanel();
		ver.add(hor);
		ver.add(scrollPanel);

		setWidget(ver);
	}

}
