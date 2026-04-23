package com.hpi.tpc.ui.views.menus.data.validate.options;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

/**
 * makes direct request for data from model
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Route(value = ROUTE_DATA_VALIDATE_OPTIONS, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_DATA + ":" + TITLE_PAGE_DATA_VALIDATE + ":" + TITLE_PAGE_DATA_VALIDATE_OPTIONS)
@org.springframework.stereotype.Component
@PermitAll
@NoArgsConstructor
public class DataValidateOptionsView
    extends ViewControllerBaseFL
{

    @Getter private VerticalLayout titleVL;
    private DataValidateOptionsViewGridVL grid;

    private DataValidateOptionsViewControlsHL controlsHL;
//    private HorizontalLayout comboBoxesHL;
//    @Getter private ComboBox<EditAccountModel> comboAccounts;
//    @Getter private ComboBox<TickerModel> comboTickers;
//    private HorizontalLayout checkBoxesHL;
//    @Getter private Checkbox checkboxSkip;
//    @Getter private Checkbox checkboxValidated;
//    private HorizontalLayout buttonsHL;
//    @Getter private Button buttonSave;
//    @Getter private Button buttonCancel;

    @PostConstruct
    public void construct()
    {
        this.addClassName("dataValidateOptionsView");
        this.setMinWidth("320px");
        this.setSizeFull();

        this.add(this.titleFormat("Validate Options Transactions"));

        this.controlsHL = new DataValidateOptionsViewControlsHL();
        this.add(this.controlsHL);

        this.grid = new DataValidateOptionsViewGridVL();
        this.add(this.grid.getGridVL());
    }

//    public void doControls()
//    {
//        this.comboAccounts = new ComboBox<>();
//        this.comboTickers = new ComboBox<>();
//
//        this.checkboxSkip = new Checkbox("Skip");
//        this.checkboxValidated = new Checkbox("Validated");
//
//        this.buttonSave = new Button("Save");
//        this.buttonCancel = new Button("Cancel");
//
//        this.comboBoxesHL = new HorizontalLayout(
//            this.comboAccounts, this.comboTickers);
//        this.comboBoxesHL.setClassName("optionsValidateControlsComboboxes");
//
//        this.checkBoxesHL = new HorizontalLayout(
//            this.checkboxSkip, this.checkboxValidated);
//        this.checkBoxesHL.setClassName("optionsValidateControlsCheckboxes");
//
//        this.buttonsHL = new HorizontalLayout(
//            this.buttonSave, this.buttonCancel);
//        this.buttonsHL.setClassName("optionsValidateControlsButtons");
//
//        this.controlsHL = new HorizontalLayout(
//            this.comboBoxesHL, this.checkBoxesHL, this.buttonsHL);
//        this.controlsHL.setClassName("optionsValidateControls");
//
//        this.add(this.controlsHL);
//    }

//    public void doGrid()
//    {
//        this.grid = new Grid<>();
//
//        this.grid.addClassName("validate-options-grid");
//        this.grid.setId("validate-options-grid");
//
//        this.grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        this.grid.addThemeVariants(GridVariant.LUMO_COMPACT);
//        this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
////        this.grid.setHeightFull();
//        this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
////        this.grid.setHeightByRows(true);
//
//        this.footer = this.grid.appendFooterRow();
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getEquityId)
//            .setHeader("EquityId")
//            .setTextAlign(ColumnTextAlign.CENTER)
//            .setAutoWidth(true)
//            .setFrozen(true)
//            .setSortProperty("equityId");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getDtTrade)
//            .setHeader("Trade Date")
//            .setTextAlign(ColumnTextAlign.CENTER)
//            .setAutoWidth(true)
//            .setSortProperty("tradeDate");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getUnits)
//            .setHeader("Units")
//            .setTextAlign(ColumnTextAlign.END)
//            .setAutoWidth(true)
//            .setSortProperty("units")
//            .setKey("units");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getTradePrice)
//            .setHeader("Trade Price")
//            .setTextAlign(ColumnTextAlign.END)
//            .setAutoWidth(true)
//            .setSortProperty("tradePrice");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getLastPrice)
//            .setHeader("Last Price")
//            .setTextAlign(ColumnTextAlign.END)
//            .setAutoWidth(true)
//            .setSortProperty("lastPrice");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getFiTId)
//            .setHeader("FiTId")
//            .setTextAlign(ColumnTextAlign.END)
//            .setAutoWidth(true)
//            .setSortProperty("fiTId");
//
//        this.grid
//            .addColumn(ValidateOptionTransactionModel::getTransactionType)
//            .setHeader("TransType")
//            .setTextAlign(ColumnTextAlign.END)
//            .setAutoWidth(true)
//            .setSortProperty("transType");
//
//        this.grid.addComponentColumn(vstm ->
//        {
//            Checkbox checkBox = new Checkbox();
//            checkBox.setValue(vstm.getBSkip());
//            checkBox.addValueChangeListener(event ->
//            {
//                //make the change
//                vstm.setBSkip(event.getValue());
//                //inform listeners
//                optionsModel.getGridDataProvider().refreshItem(vstm);
//            });
//
//            return checkBox;
//        })
//            .setHeader("Skip")
//            .setAutoWidth(true)
//            .setTextAlign(ColumnTextAlign.CENTER);
//
//        this.grid.addComponentColumn(vstm ->
//        {
//            Checkbox checkBox = new Checkbox();
//            checkBox.setValue(vstm.getBValidated());
//            checkBox.addValueChangeListener(event ->
//            {
//                //make the change
//                vstm.setBValidated(event.getValue());
//                //inform listeners
//                optionsModel.getGridDataProvider().refreshItem(vstm);
//            });
//
//            return checkBox;
//        })
//            .setHeader("Valid")
//            .setAutoWidth(true)
//            .setTextAlign(ColumnTextAlign.CENTER);
//
//        //disallow client changes
//        this.grid.addComponentColumn(votm ->
//        {
//            Checkbox checkBox = new Checkbox();
//            checkBox.setValue(votm.getBComplete());
//
//            checkBox.setEnabled(false);
//
//            return checkBox;
//        })
//            .setHeader("Complete")
//            .setAutoWidth(true)
//            .setTextAlign(ColumnTextAlign.CENTER);
//
//        this.grid.setColumnReorderingAllowed(true);
//        this.grid.recalculateColumnWidths();
//
//        this.gridVL = new VerticalLayout(grid);
//        this.gridVL.setClassName("optionsValidateGrid");
//
//        this.add(gridVL);
//    }

//    public void setupViewer()
//    {
//        this.doTitle();
//
//        this.doControls();
//
//        this.doGrid();
//
//        this.comboAccounts.setItems(this.optionsModel.getAccountModels());
//
//        this.comboTickers.setItems(this.optionsModel.getTickerModels());
//
//        //set check boxes
//        this.checkboxSkip.setValue(this.optionsModel.getSelectedSkip());
//        this.checkboxValidated.setValue(this.optionsModel.getSelectedValidated());
//
//        //set buttons
//        this.buttonSave.setEnabled(false);
//        this.buttonCancel.setEnabled(false);
//
//        //filter as appropriate
//        this.optionsModel.filters(
//            this.optionsModel.getSelectedSkip(),
//            this.optionsModel.getSelectedValidated());
//    }

    public ComboBox<EditAccountModel> getComboAccounts()
    {
        return this.controlsHL.getComboAccounts();
    }

    public ComboBox<TickerModel> getComboTickers()
    {
        return this.controlsHL.getComboTickers();
    }

    public Checkbox getCheckboxSkip()
    {
        return this.controlsHL.getCheckboxSkip();
    }

    public void setCheckboxSkipValue(Boolean skip)
    {
        this.controlsHL.getCheckboxSkip().setValue(skip);
    }

    public Boolean getCheckboxSkipValue()
    {
        return this.controlsHL.getCheckboxSkip().getValue();
    }

    public Checkbox getCheckboxValidated()
    {
        return this.controlsHL.getCheckboxValidated();
    }

    public void setCheckboxValidatedValue(Boolean skip)
    {
        this.controlsHL.getCheckboxValidated().setValue(skip);
    }

    public Boolean getCheckboxValidatedValue()
    {
        return this.controlsHL.getCheckboxValidated().getValue();
    }

    public Button getButtonSave()
    {
        return this.controlsHL.getButtonSave();
    }

    public void setButtonSaveEnabled(Boolean enabled)
    {
        this.controlsHL.getButtonSave().setEnabled(enabled);
    }

    public Button getButtonCancel()
    {
        return this.controlsHL.getButtonCancel();
    }

    public void setButtonCancelEnabled(Boolean enabled)
    {
        this.controlsHL.getButtonCancel().setEnabled(enabled);
    }

    public Grid<ValidateOptionTransactionModel> getGrid()
    {
        return this.grid.getGrid();
    }

    public FooterRow getFooterRow()
    {
        return this.grid.getFooterRow();
    }

    @Override
    public void addMenuBarTabs()
    {
        //none
    }
}
