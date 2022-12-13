package com.neural.jitavej.client;

import com.google.gwt.user.client.ui.HTML;

public class Space extends HTML{
	
	public Space(){
		setWidth("4px");
		setHeight("20px");
	}
	
	public Space(int width){
		setWidth(width+"px");
	}	
	
	public Space(String width){
		setWidth(width);
	}
}
