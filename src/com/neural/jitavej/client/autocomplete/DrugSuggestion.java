package com.neural.jitavej.client.autocomplete;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class DrugSuggestion implements IsSerializable, Suggestion {
    private String display;
    private String replacement;

    // Required for IsSerializable to work
    public DrugSuggestion() {}

    // Convenience method for creation of a suggestion
    public DrugSuggestion(String _disp, String _rep) {
        display = _disp;
        replacement = _rep;
    }

    public String getDisplayString() {
        return display;
    }

    public String getReplacementString() {
        return replacement;
    }

} 
