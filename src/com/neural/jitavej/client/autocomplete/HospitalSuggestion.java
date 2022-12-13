package com.neural.jitavej.client.autocomplete;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class HospitalSuggestion implements IsSerializable, Suggestion {
    private String display;
    private String replacement;
    public JSONObject hospital;

    // Required for IsSerializable to work
    public HospitalSuggestion() {}

    // Convenience method for creation of a suggestion
    public HospitalSuggestion(JSONObject hospital, String _disp, String _rep) {
        display = _disp;
        replacement = _rep;
        this.hospital = hospital;
    }

    public String getDisplayString() {
        return display;
    }

    public String getReplacementString() {
        return replacement;
    }

} 
