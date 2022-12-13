package com.neural.jitavej.client.old;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.SuggestBox;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.neural.jitavej.client.Jitavej;

public class OldRecordPanel extends Panel {

	String data = null;
	SuggestBox suggestBox;
	JSONArray items = new JSONArray();
	Store store;
	
	public OldRecordPanel() {
		setBorder(false);
		setTitle(Jitavej.CONSTANTS.old_record());
		onModuleLoad();
	}

	public void onModuleLoad() {

		setLayout(new VerticalLayout(0));

        add(new OldOrder());
        add(new OldItemNew());
	}
}
