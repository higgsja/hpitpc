package com.hpi.tpc.ui.views.menus.data.validate.stocks;

import com.hpi.tpc.ui.views.baseClass.ViewBaseHL;
import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.grid.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

public class DataValidateStocksGridVL
    extends ViewBaseHL
{

    @Autowired private DataValidateStocksControllerFL dataValidateStocksControllerFL;
    @Autowired private DataValidateStocksModel dataValidateStocksModel;

    @Getter private final Grid<ValidateStockTransactionModel> validateStocksGrid;
    @Getter private final FooterRow validateStocksGridFooterRow;

    public DataValidateStocksGridVL()
    {
        this.addClassName("dataValidateStocksGridVL");

        this.validateStocksGrid = new Grid();
        this.validateStocksGridFooterRow = this.validateStocksGrid.appendFooterRow();
        this.add(this.validateStocksGrid);
        
        this.getElement().getStyle().set("padding", "0");

        this.doLayout();
    }

    private void doLayout()
    {
        this.validateStocksGrid.setColumnReorderingAllowed(true);
        this.validateStocksGrid.recalculateColumnWidths();
        
        this.validateStocksGrid.setAllRowsVisible(false);

        this.validateStocksGrid.addClassName("dataValidateStocksGrid");
        this.validateStocksGrid.setThemeName("dense");

        this.validateStocksGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.validateStocksGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.validateStocksGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        this.validateStocksGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getEquityId)
            .setHeader("EquityId")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setAutoWidth(true)
            .setFrozen(true)
            .setSortProperty("equityId");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getDtTrade)
            .setHeader("Trade Date")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setAutoWidth(true)
            .setSortProperty("tradeDate");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getUnits)
            .setHeader("Units")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("units")
            .setKey("units");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getTradePrice)
            .setHeader("Trade Price")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("tradePrice");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getLastPrice)
            .setHeader("Last Price")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("lastPrice");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getFiTId)
            .setHeader("FiTId")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("fiTId");

        this.validateStocksGrid.addColumn(ValidateStockTransactionModel::getTransactionType)
            .setHeader("TransType")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("transType");

        this.validateStocksGrid.addComponentColumn(vstm ->
        {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(vstm.getBSkip());
            checkBox.addValueChangeListener(event ->
            {
                //make the change
                vstm.setBSkip(event.getValue());
                //inform listeners
                this.dataValidateStocksModel.getGridDataProvider().refreshItem(vstm);
                //Enable Save
                this.dataValidateStocksControllerFL.setButtonSaveEnabled(true);
            });

            return checkBox;
        })
            .setHeader("Skip")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);

        this.validateStocksGrid.addComponentColumn(vstm ->
        {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(vstm.getBValidated());
            checkBox.addValueChangeListener(event ->
            {
                //make the change
                vstm.setBValidated(event.getValue());
                //inform listeners
                this.dataValidateStocksModel.getGridDataProvider().refreshItem(vstm);
                //enable Cancel
                this.dataValidateStocksControllerFL.setButtonCancelEnabled(true);
            });

            return checkBox;
        })
            .setHeader("Valid")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);

        //disallow client changes
        this.validateStocksGrid.addComponentColumn(votm ->
        {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(votm.getBComplete());

            checkBox.setEnabled(false);

            return checkBox;
        })
            .setHeader("Complete")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);
    }
}
