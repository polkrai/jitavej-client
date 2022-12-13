package com.neural.jitavej.client.util;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class DateRenderer implements Renderer {

	private DateTimeFormat dateTimeFormat;

	public DateRenderer(String format) {

		this.dateTimeFormat = DateTimeFormat.getFormat(format);
		// Locale.setDefault(new Locale("th", "TH"));

	}

	public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {

		return value != null ? dateTimeFormat.format((Date) value) : "";
	}

}