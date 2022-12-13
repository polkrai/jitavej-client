package com.neural.jitavej.client.frontmed;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class MedLoadDialog extends FormDialog {

	public MedLoadDialog() {
		setHTML("<B>"+Jitavej.CONSTANTS.load()+"</B>");

		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(4);

		for (int i = 0; i < Jitavej.components.size(); i++) {
			final JSONObject component = Jitavej.components.get(i).isObject();
			hor.add(new MedLoadPanel(component));
		}

		cancel = new Button(Jitavej.CONSTANTS.close(), new ClickListener() {
			public void onClick(Widget sender) {
				MedLoadDialog.this.clear();
				hide();
			}
		});
		
		DockPanel dock = new DockPanel();
		dock.setSpacing(4);
		dock.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		ScrollPanel scroller = new ScrollPanel(hor);
		scroller.setSize("980px", "265px");
		dock.add(cancel, DockPanel.NORTH);
		dock.add(scroller, DockPanel.CENTER);

		setWidget(dock);
	}

}
