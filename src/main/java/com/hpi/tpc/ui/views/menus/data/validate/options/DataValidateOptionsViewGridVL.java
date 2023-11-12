package com.hpi.tpc.ui.views.menus.data.validate.options;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class DataValidateOptionsViewGridVL
{

    @Autowired private DataValidateOptionsModel dataValidateOptionsModel;

    @Getter private final VerticalLayout gridVL;
    @Getter private final Grid<ValidateOptionTransactionModel> grid;
    @Getter private final FooterRow footerRow;

    public DataValidateOptionsViewGridVL()
    {
        this.grid = new Grid<>();
        this.footerRow = this.grid.appendFooterRow();
        this.gridVL = new VerticalLayout(grid);
        this.doLayout();
    }

    private void doLayout()
    {
        this.grid.addClassName("validate-options-grid");

        this.grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        this.grid
            .addColumn(ValidateOptionTransactionModel::getEquityId)
            .setHeader("EquityId")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setAutoWidth(true)
            .setFrozen(true)
            .setSortProperty("equityId");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getDtTrade)
            .setHeader("Trade Date")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setAutoWidth(true)
            .setSortProperty("tradeDate");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getUnits)
            .setHeader("Units")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("units")
            .setKey("units");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getTradePrice)
            .setHeader("Trade Price")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("tradePrice");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getLastPrice)
            .setHeader("Last Price")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("lastPrice");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getFiTId)
            .setHeader("FiTId")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("fiTId");

        this.grid
            .addColumn(ValidateOptionTransactionModel::getTransactionType)
            .setHeader("TransType")
            .setTextAlign(ColumnTextAlign.END)
            .setAutoWidth(true)
            .setSortProperty("transType");

        this.grid.addComponentColumn(vstm -> {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(vstm.getBSkip());
            checkBox.addValueChangeListener(event -> {
                //make the change
                vstm.setBSkip(event.getValue());
                //inform listeners
                this.dataValidateOptionsModel.getGridDataProvider().refreshItem(vstm);
            });

            return checkBox;
        })
            .setHeader("Skip")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);

        this.grid.addComponentColumn(vstm -> {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(vstm.getBValidated());
            checkBox.addValueChangeListener(event -> {
                //make the change
                vstm.setBValidated(event.getValue());
                //inform listeners
                this.dataValidateOptionsModel.getGridDataProvider().refreshItem(vstm);
            });

            return checkBox;
        })
            .setHeader("Valid")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);

        //disallow client changes
        this.grid.addComponentColumn(votm -> {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(votm.getBComplete());

            checkBox.setEnabled(false);

            return checkBox;
        })
            .setHeader("Complete")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER);

        this.grid.setColumnReorderingAllowed(true);
        this.grid.recalculateColumnWidths();

        this.gridVL.setClassName("optionsValidateGridVL");
    }
}
