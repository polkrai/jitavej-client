package com.neural.jitavej.client.menu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.neural.jitavej.client.Jitavej;
import com.neural.jitavej.client.lab.LabItem;

public class TopMenuBar extends MenuBar {

	public TopMenuBar() {

	    setAutoOpen(true);
	    setWidth("200px");
	    setAnimationEnabled(true);

	    MenuBar roleMenu = new MenuBar(true);
	    roleMenu.addItem("", new Command(){
			public void execute() {
				Jitavej.show(new LabItem());
			}
	    });
 	    
	    roleMenu.addItem("", new Command(){
			public void execute() {
				Jitavej.show(new LabItem());
			}
	    }); 
	    
	    
	    addItem(new MenuItem("", true, roleMenu));

	}  

	
}
