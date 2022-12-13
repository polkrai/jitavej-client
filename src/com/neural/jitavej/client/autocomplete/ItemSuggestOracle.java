package com.neural.jitavej.client.autocomplete;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.neural.jitavej.client.Jitavej;

public class ItemSuggestOracle extends SuggestOracle {
	String group;
	RequestBuilder rb;
	
	public ItemSuggestOracle(String group){
		this.group = group;
		if(group.equals("all")){
			rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/item/suggest");
		}else if(group.equals("lab")){
			rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/item/suggestlab");
		}else if(group.equals("dental")){
			rb = new RequestBuilder(RequestBuilder.POST, Jitavej.server + "/item/suggestdental");
		}
		
	}
	
	public boolean isDisplayStringHTML() {
		return true;
	}

	public void requestSuggestions(final SuggestOracle.Request req, final SuggestOracle.Callback callback) {
	//	JitavejService.getItemService().suggest(req.getQuery(), req.getLimit(), new ItemSuggestCallback(req, callback));
		try {		
			
			rb.setTimeoutMillis(30000);	
			StringBuffer params = new StringBuffer();
			params.append("key=" + URL.encodeComponent(req.getQuery().trim()));
			rb.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
			rb.sendRequest(params.toString(), new com.google.gwt.http.client.RequestCallback() {
				public void onError(Request request, Throwable exception) {
					GWT.log("Error response received during authentication of user", exception);
				}
				public void onResponseReceived(com.google.gwt.http.client.Request request, com.google.gwt.http.client.Response response) {
					if (response.getStatusCode() == 200) {
						List<Suggestion> suggestions = new ArrayList<Suggestion>(10);
						suggestions.clear();
						GWT.log(response.getText(), null);
						JSONValue list = JSONParser.parse(response.getText());
						JSONArray Items = list.isArray();
						
						for(int i=0;i<Items.size();i++){
							final JSONObject item = Items.get(i).isObject();
							GWT.log("ItemSuggestOracle>>>"+item.toString()+"<<<", null);
							if(!group.equals("dental")){
								suggestions.add(new ItemSuggestion("<table width=\"360px\"><tr><td width=\"360px\"><b>"+ item.get("name").isString().stringValue() +"</b></td></tr></table>", item.get("name").isString().stringValue()));
							}else{
								suggestions.add(new ItemSuggestion("<table width=\"600px\"><tr><td width=\"600px\"><b>"+ item.get("name").isString().stringValue() +"</b></td></tr></table>", item.get("name").isString().stringValue()));								
							}
							
						}

						SuggestOracle.Response resp = new SuggestOracle.Response();
						resp.setSuggestions(suggestions);
						callback.onSuggestionsReady(req, resp);
					} else {
						Window.alert("response.getText() "+response.getText());
					}
				}
				public void onError(com.google.gwt.http.client.Request request, Throwable exception) {
					// TODO Auto-generated method stub
					
				}
	

			});

		} catch (RequestException e1) {
			e1.printStackTrace();
		}
	}

}