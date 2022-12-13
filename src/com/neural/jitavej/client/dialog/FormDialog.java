package com.neural.jitavej.client.dialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;
import com.neural.jitavej.client.Jitavej;

public class FormDialog extends DialogBox {
	
	GlassPanel glassPanel;
	
	public Button submit, cancel;

	public FormDialog(){
		
		super(false, true);
		
		cancel = new Button("<b><FONT style='color:red;'>"+Jitavej.CONSTANTS.close()+ "</font></b>", new ClickListener() {
			public void onClick(Widget sender) {
				hide();
			}
		});
		//cancel.setStyleName("cancel");
	}
	
	@Override
	public void show() {
		
		glassPanel = new GlassPanel(true);
		RootPanel.get().add(glassPanel, 0, 0);
		super.show();
	}

	@Override
	public void hide() {
		
		if(glassPanel.isVisible()){
			glassPanel.setVisible(false);
		}
		super.hide();
	}
	
	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
		/*case KeyboardListener.KEY_ENTER:
			submit.click();
			break;*/
		case KeyboardListener.KEY_ESCAPE:
			hide();
			break;
		}

		return true;
	}
	

}
