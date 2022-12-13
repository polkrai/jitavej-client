package com.neural.jitavej.client.record;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.dialog.FormDialog;

public class ReasonEditConDialog extends FormDialog {
	ListBox reason;
	int row;
	RecordConPanel recordConPanel;
	
	@SuppressWarnings("deprecation")
	public ReasonEditConDialog(int rowIndex, RecordConPanel recordConPanel0) {
		row = rowIndex;
		this.recordConPanel = recordConPanel0;
		setHTML("<B>Reason Edit</B>");
		
		reason = new ListBox();
 		reason.addItem("");
 		reason.addItem("A");
 		reason.addItem("B");
 		reason.addItem("C");
 		reason.addItem("D");
 		reason.addItem("E");
 		reason.addItem("F");
 		reason.addItem("IPD");
 		
		reason.addKeyboardListener(new KeyboardListenerAdapter(){
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		        if (keyCode == (char) KEY_ENTER) {
		        	submit();
		        }
			}

        });

		cancel.setText(Jitavej.CONSTANTS.record_pincode_cancel());
		
		submit = new Button(Jitavej.CONSTANTS.record_pincode_confirm(), new ClickListener() {
			public void onClick(Widget sender) {
				submit();
			}
		});

		
		VerticalPanel ver = new VerticalPanel();
		ver.setSpacing(2);
		
		HorizontalPanel hor = new HorizontalPanel();
		hor.setSpacing(2);
		hor.add(submit);
		hor.add(cancel);
		
		ver.add(reason);
		ver.add(hor);

		setWidget(ver);
	
	}

	public void submit(){
	   	try {
    	
			recordConPanel.orderItems.get(row).isObject().put("reason", new JSONString(reason.getValue(reason.getSelectedIndex())));
			recordConPanel.update();
						hide();
		} catch (Exception e) {
		//	Window.alert(e.toString());
		}	
	}
}
