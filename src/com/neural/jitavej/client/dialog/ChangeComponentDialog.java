package com.neural.jitavej.client.dialog;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.NavPanel;
public class ChangeComponentDialog extends FormDialog {
	public FormPanel form;
	@SuppressWarnings("deprecation")
	public ChangeComponentDialog() {
		setHTML("<B>Change Component</B>");
		form = new FormPanel();
		form.setAction(Jitavej.server + "/user/changeComponent");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		Grid grid = new Grid(7,3);
		form.setWidget(grid);

		final ListBox componentBox = new ListBox();
		componentBox.setName("component_id");
		for (int i = 0; i < Jitavej.components.size(); i++) {
			JSONObject component = Jitavej.components.get(i).isObject();
			componentBox.addItem(component.get("name").isString().stringValue(), component.get("id").isNumber().toString());
		}
		grid.setWidget(0, 0, new HTML("Component"));
		grid.setWidget(0, 1, componentBox); 
		
		submit = new Button("Change", new ClickListener() {
			public void onClick(Widget sender) {
			//	Jitavej.component = componentBox.getValue(componentBox.getSelectedIndex());
				NavPanel.changeComponent.setText(Jitavej.component.get("name").isString().stringValue());
				form.submit();
			}
		});
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.add(submit);
		hor.add(cancel);
		
		grid.setWidget(2, 0, new HTML(""));
		grid.setWidget(2, 1, hor);
		setWidget(form);

	}


}
