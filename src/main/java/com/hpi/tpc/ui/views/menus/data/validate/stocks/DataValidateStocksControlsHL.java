package com.hpi.tpc.ui.views.menus.data.validate.stocks;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import lombok.*;

public class DataValidateStocksControlsHL
    extends ControlsHLBase
{

    @Getter private ComboBox<EditAccountModel> validateStocksComboAccounts;
    @Getter private ComboBox<TickerModel> validateStocksComboTickers;

    @Getter private Checkbox validateStocksCheckboxSkip;
    @Getter private Checkbox validateStocksCheckboxValidated;

    @Getter private Button validateStocksButtonSave;
    @Getter private Button validateStocksButtonCancel;

    private HorizontalLayout comboBoxesHL;
    private HorizontalLayout checkBoxesHL;
    private HorizontalLayout buttonsHL;

    public DataValidateStocksControlsHL()
    {
        this.doLayout();
    }

    @Override
    public final void doLayout()
    {
        this.addClassName("dataValidateStocksControlsHL");

        this.validateStocksComboAccounts = new ComboBox<>();
        this.validateStocksComboTickers = new ComboBox<>();

        this.validateStocksCheckboxSkip = new Checkbox("Skip");
        this.validateStocksCheckboxValidated = new Checkbox("Validated");

        this.validateStocksButtonSave = new Button("Save");
        this.validateStocksButtonCancel = new Button("Cancel");

        this.comboBoxesHL = new HorizontalLayout(
            this.validateStocksComboAccounts, this.validateStocksComboTickers);
        this.comboBoxesHL.setClassName("stocksValidateControlsComboboxes");

        this.checkBoxesHL = new HorizontalLayout(
            this.validateStocksCheckboxSkip, this.validateStocksCheckboxValidated);
        this.checkBoxesHL.setClassName("stocksValidateControlsCheckboxes");

        this.buttonsHL = new HorizontalLayout(
            this.validateStocksButtonSave, this.validateStocksButtonCancel);
        this.buttonsHL.setClassName("stocksValidateControlsButtons");

        this.add(this.comboBoxesHL, this.checkBoxesHL, this.buttonsHL);
    }
}
