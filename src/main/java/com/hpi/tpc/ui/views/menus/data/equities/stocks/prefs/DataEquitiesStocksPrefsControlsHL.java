package com.hpi.tpc.ui.views.menus.data.equities.stocks.prefs;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.orderedlayout.*;
import lombok.*;

public class DataEquitiesStocksPrefsControlsHL
    extends ControlsHLBase
{

    @Getter private Button equitiesStocksPrefsSave;
    @Getter private Button equitiesStocksPrefsCancel;

    private HorizontalLayout buttonsHL;

    public DataEquitiesStocksPrefsControlsHL()
    {
        this.doLayout();
    }

    @Override
    public final void doLayout()
    {
        this.addClassName("dataEquitiesStocksPrefsControlsHL");

        this.equitiesStocksPrefsSave = new Button("Save");
        this.equitiesStocksPrefsCancel = new Button("Cancel");

        this.buttonsHL = new HorizontalLayout(this.equitiesStocksPrefsSave, this.equitiesStocksPrefsCancel);
        this.buttonsHL.setClassName("dataEquitiesStocksPrefsControlsButtons");

        this.add(this.buttonsHL);
    }
}
