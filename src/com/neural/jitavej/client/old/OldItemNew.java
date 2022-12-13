package com.neural.jitavej.client.old;

 
import com.gwtext.client.data.*;  
import com.gwtext.client.widgets.Panel;  
import com.gwtext.client.widgets.grid.ColumnConfig;  
import com.gwtext.client.widgets.grid.ColumnModel;  
import com.gwtext.client.widgets.grid.GridPanel;  
import com.neural.jitavej.client.Jitavej;
  
public class OldItemNew extends Panel {  
  
    public OldItemNew() {  
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
                new ColumnConfig(Jitavej.CONSTANTS.record_item(), "a", 100, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.record_dose(), "s", 120, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.record_number(), "d", 120, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.record_fee(), "f", 180, true, null, "company"),  
                new ColumnConfig(Jitavej.CONSTANTS.record_sum(), "g", 100, true, null, "company"),  

   
        };  
  
        ColumnModel columnModel = new ColumnModel(columns);  
        grid.setColumnModel(columnModel);  
  
        grid.setFrame(true);  
        grid.setStripeRows(true);  
        grid.setAutoExpandColumn("company");  
  
        grid.setHeight(200);  
        grid.setWidth(720);  
        grid.setTitle(Jitavej.CONSTANTS.old_new());  
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
                new Object[]{"Paracetamol 200mg", "3x3", "20",  "5", "100"}, 
                new Object[]{"Paracetamol 500mg", "3x3", "20",  "5", "100"}, 
                new Object[]{"Paracetamol 1000mg", "3x3", "20",  "5", "100"}, 
    };  
    }  
}  