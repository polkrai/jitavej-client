package com.neural.jitavej.client.old;

import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.Panel;  
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.neural.jitavej.client.Jitavej;
  
public class OldOrder extends Panel {  
  
    public OldOrder() {  
        setBorder(false);  
        setPaddings(5);  
  
        RecordDef recordDef = new RecordDef(  
                new FieldDef[]{  
                        new StringFieldDef("a"),  
                        new StringFieldDef("s"),  
                        new StringFieldDef("d"),  
                        new StringFieldDef("f"),  
                        new StringFieldDef("g"),  
                }  
        );  
  
        GridPanel grid = new GridPanel();  
  
        Object[][] data = getCompanyData();  
        MemoryProxy proxy = new MemoryProxy(data);  
  
        ArrayReader reader = new ArrayReader(recordDef);  
        Store store = new Store(proxy, reader);  
        store.load();  
        grid.setStore(store);  
  
  
        ColumnConfig[] columns = new ColumnConfig[]{  
                //column ID is company which is later used in setAutoExpandColumn  
                new ColumnConfig(Jitavej.CONSTANTS.old_id(), "a", 100, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.old_startdate(), "s", 120, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.old_enddate(), "d", 120, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.old_doctor(), "f", 180, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.old_comment(), "g", 100, true, null, "company"),  

        
        
        };  
  
        ColumnModel columnModel = new ColumnModel(columns);  
        grid.setColumnModel(columnModel);  
  
        grid.setFrame(true);  
        grid.setStripeRows(true);  
        grid.setAutoExpandColumn("company");  
  
        grid.setHeight(150);  
        grid.setWidth(640);  
        grid.setTitle(Jitavej.CONSTANTS.old_order());  
/*
        Toolbar bottomToolbar = new Toolbar();  
        bottomToolbar.addFill();  
        bottomToolbar.addButton(new ToolbarButton("Clear Sort", new ButtonListenerAdapter() {  
            public void onClick(Button button, EventObject e) {  
                grid.clearSortState(true);  
            }  
        }));  
        grid.setBottomToolbar(bottomToolbar);  
 */
        add(grid);  

    }  
  
    private Object[][] getCompanyData() {  
        return new Object[][]{  
                new Object[]{"0001", "12/05/2551", "12/06/2551",  "doctor1", "12345667890"}, 
                new Object[]{"0002", "12/06/2551", "12/07/2551",  "doctor1", ""},                 
                new Object[]{"0003", "12/07/2551", "12/08/2551",  "doctor1", "12345667890"},                
                new Object[]{"0004", "12/08/2551", "12/09/2551",  "doctor1", "xxxxxxxxxxx"}, 
                new Object[]{"0005", "12/06/2551", "12/07/2551",  "doctor1", ""},                 
                new Object[]{"0006", "12/07/2551", "12/08/2551",  "doctor1", ""},                
                new Object[]{"0007", "12/08/2551", "12/09/2551",  "doctor1", ""},  
        };  
    }  
}  