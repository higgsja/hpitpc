package com.hpi.tpc.ui.views.menus.data.validate.options;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import lombok.*;
import org.springframework.stereotype.*;

@Getter
@Component
public class DataValidateOptionsViewControlsHL
    extends ControlsHLBase
{

    private ComboBox<EditAccountModel> comboAccounts;
    private ComboBox<TickerModel> comboTickers;

    private Checkbox checkboxSkip;
    private Checkbox checkboxValidated;

    private Button buttonSave;
    private Button buttonCancel;

    private HorizontalLayout comboBoxesHL;
    private HorizontalLayout checkBoxesHL;
    private HorizontalLayout buttonsHL;

    public DataValidateOptionsViewControlsHL()
    {
        this.doLayout();
    }

    @Override
    public final void doLayout()
    {
        this.addClassName("stocksValidateControls");

        this.comboAccounts = new ComboBox<>();
        this.comboTickers = new ComboBox<>();

        this.checkboxSkip = new Checkbox("Skip");
        this.checkboxValidated = new Checkbox("Validated");

        this.buttonSave = new Button("Save");
        this.buttonCancel = new Button("Cancel");

        this.comboBoxesHL = new HorizontalLayout(
            this.comboAccounts, this.comboTickers);
        this.comboBoxesHL.setClassName("stocksValidateControlsComboboxes");

        this.checkBoxesHL = new HorizontalLayout(
            this.checkboxSkip, this.checkboxValidated);
        this.checkBoxesHL.setClassName("stocksValidateControlsCheckboxes");

        this.buttonsHL = new HorizontalLayout(
            this.buttonSave, this.buttonCancel);
        this.buttonsHL.setClassName("stocksValidateControlsButtons");

        this.add(this.comboBoxesHL, this.checkBoxesHL, this.buttonsHL);
    }

    public void setCheckboxSkipValue(Boolean check)
    {
        this.checkboxSkip.setValue(check);
    }

    public Boolean getCheckgboxSkipValue()
    {
        return this.checkboxSkip.getValue();
    }

    public void setCheckboxValidatedValue(Boolean validated)
    {
        this.checkboxValidated.setValue(validated);
    }

    public Boolean getCheckboxValidatedValue()
    {
        return this.checkboxValidated.getValue();
    }

    public void setButtonSaveEnabled(Boolean enabled)
    {
        this.buttonSave.setEnabled(enabled);
    }

    public void setButtonCancelEnabled(Boolean enabled)
    {
        this.buttonCancel.setEnabled(enabled);
    }
}
