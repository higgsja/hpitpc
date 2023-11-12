package com.hpi.tpc.ui.views.menus.data.validate.options;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

/**
 * translates user requests to actions, selects the appropriate view
 * minimal as possible; just translate requests in to Model actions and
 * selecting View
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Route(value = ROUTE_DATA_VALIDATE_OPTIONS_CONTROLLER, layout = MainLayout.class)
@NoArgsConstructor
@PermitAll
public class DataValidateOptionsController
    extends ViewControllerBaseFL //flexlayout
    implements BeforeEnterObserver
{
    @Autowired private DataValidateOptionsModel dataValidateOptionsModel;
    @Autowired private DataValidateOptionsView dataValidateOptionsView;

    private Registration dataProviderListener;

    @PostConstruct
    private void construct() {
        this.addClassName("dataValidateOptionsController");

        this.dataValidateOptionsModel.getPrefs("OptionValidate");

        this.setCheckboxSkipValue(this.dataValidateOptionsModel.getSelectedSkip());

        this.setCheckboxValidatedValue(this.dataValidateOptionsModel.getSelectedValidated());

        this.setButtonSaveEnabled(false);
        this.setButtonCancelEnabled(false);

        this.setListeners();
    }
    
        
private ComboBox<EditAccountModel> getComboAccounts(){
        return this.dataValidateOptionsView.getComboAccounts();
    }
    
    private ComboBox<TickerModel> getComboTickers(){
        return this.dataValidateOptionsView.getComboTickers();
    }

    private Checkbox getCheckboxSkip(){
        return this.dataValidateOptionsView.getCheckboxSkip();
    }
    
    private void setCheckboxSkipValue(Boolean skip)
    {
        this.dataValidateOptionsView.setCheckboxSkipValue(skip);
    }

    private Boolean getCheckboxSkipValue()
    {
        return this.dataValidateOptionsView.getCheckboxSkipValue();
    }

    private Checkbox getCheckboxValidated(){
        return this.dataValidateOptionsView.getCheckboxValidated();
    }
    
    private void setCheckboxValidatedValue(Boolean skip)
    {
        this.dataValidateOptionsView.setCheckboxValidatedValue(skip);
    }

    private Boolean getCheckboxValidatedValue()
    {
        return this.dataValidateOptionsView.getCheckboxValidatedValue();
    }

    private Button getButtonSave(){
        return this.dataValidateOptionsView.getButtonSave();
    }
    
    private void setButtonSaveEnabled(Boolean enabled)
    {
        this.dataValidateOptionsView.setButtonSaveEnabled(enabled);
    }
    
    private Button getButtonCancel(){
        return this.dataValidateOptionsView.getButtonCancel();
    }

    private void setButtonCancelEnabled(Boolean enabled)
    {
        this.dataValidateOptionsView.setButtonCancelEnabled(enabled);
    }

    public Grid<ValidateOptionTransactionModel> getGrid(){
        return this.dataValidateOptionsView.getGrid();
    }
    
    public FooterRow getFooterRow(){
        return this.dataValidateOptionsView.getFooterRow();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.dataValidateOptionsModel.serviceTPC.AppTracking("WTPC:Data:Validate:Options");

        this.updateViewOnEnter();

        //transfer to viewer
        event.forwardTo(DataValidateOptionsView.class);
    }

    private void updateViewOnEnter()
    {
        /**
         * update data on every enter as data may have changed
         */
        this.updateViewOnEnterAccounts();
        this.updateViewOnEnterTickers();
    }

    private void updateViewOnEnterAccounts()
    {
        /**
         * update accounts data from database into the view
         */
        //update data
        this.dataValidateOptionsModel.updateAccountModels();
        //update view
        this.getComboAccounts().setItems(this.dataValidateOptionsModel.getAccountModels());
        this.getComboAccounts().setValue(this.dataValidateOptionsModel.getAccountModels().get(0));
        //update the data model
        this.dataValidateOptionsModel.setSelectedAccountModel(this.getComboAccounts().getValue());
    }

    private void updateViewOnEnterTickers()
    {
        /**
         * update tickers from database into the view
         */
        //update data
        this.dataValidateOptionsModel.updateTickerModels();
        //update view
        this.getComboTickers().setItems(this.dataValidateOptionsModel.getTickerModels());
        this.getComboTickers().setValue(this.dataValidateOptionsModel
            .getTickerModels().get(0));
        //update the data model
        this.dataValidateOptionsModel.setSelectedTickerModel(this.getComboTickers().getValue());
    }

    private void updateGridOnChange()
    {
        /**
         * any change to accounts or tickers pull new data set
         */
        Iterator<ValidateOptionTransactionModel> iterator;
        ValidateOptionTransactionModel tmpModel;
        Double unitsTotal;

        unitsTotal = 0.0;

        if (this.dataValidateOptionsModel.getSelectedAccountModel() == null
            || this.dataValidateOptionsModel.getSelectedTickerModel() == null)
        {
            //no update until both lists are complete with a selection
            //this is an issue on entering as each is populated independently and fires the onChange
            return;
        }

        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }

        this.dataValidateOptionsModel.updateGridData();

        this.getGrid().setDataProvider(
            this.dataValidateOptionsModel.getGridDataProvider());
        this.setGridDataProviderListener();

        //set the totals
        iterator = this.dataValidateOptionsModel.getGridDataProvider().getItems().iterator();

        //exclude skips from the total
        //todo: need to change to account for filtering
        while (iterator.hasNext())
        {
            tmpModel = iterator.next();
            if (!tmpModel.getBSkip())
            {
                unitsTotal += tmpModel.getUnits();
            }
        }

        Grid.Column aColumn = this.getGrid().getColumnByKey("units");

        this.getFooterRow().getCell(aColumn).setText(unitsTotal.toString());

    }

//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.serviceTPC.AppTracking("TPC:Data:Validate:Options");
//
//        this.optionsModel.getPrefs();
//
//        //ensure page is clear
//        this.removeAll();
//
//        //ensure listeners removed
//        this.removeListeners();
//
//        //data
//        this.optionsModel.doAccountModels();
//
//        //todo: use prefs to return to the last settings
//        //  for now, first account, first ticker
//        this.optionsModel.setSelectedAccountModel(this.optionsModel
//            .getAccountModels().get(0));
//
//        this.optionsModel.doTickerModels();
//
//        //todo: use prefs to return to the last settings
//        //  for now, first account, first ticker
//        this.optionsModel.setSelectedTickerModel(this.optionsModel.getTickerModels().get(0));
//
//        if (this.dataProviderListener != null) {
//            this.dataProviderListener.remove();
//        }
//
//        this.optionsViewer.setupViewer();
//
//        //todo: use preferences so come in to where left off
//        this.optionsViewer.getComboAccounts()
//            .setValue(this.optionsModel.getSelectedAccountModel());
//
//        //todo: use preferences so come in to where left off
//        this.optionsViewer.getComboTickers()
//            .setValue(this.optionsModel.getSelectedTickerModel());
//
//        this.doGridData();
//
//        this.setListeners();
//
//        //transfer to viewer
//        event.forwardTo(DataValidateOptionsView.class);
//    }

//    public void removeListeners() {
//        //general
//        for (Registration r : this.listeners) {
//            if (r != null) {
//                r.remove();
//            }
//
//            this.listeners.clear();
//        }
//
//        //special
//        if (this.dataProviderListener != null) {
//            this.dataProviderListener.remove();
//        }
//    }

//    private void doGridData() {
//        Iterator<ValidateOptionTransactionModel> iterator;
//        ValidateOptionTransactionModel tmpModel;
//        Double unitsTotal;
//
//        unitsTotal = 0.0;
//
//        if (this.dataProviderListener != null) {
//            this.dataProviderListener.remove();
//        }
//
//        this.optionsModel.doGridData();
//        this.optionsViewer.getGrid().setDataProvider(
//            this.optionsModel.getGridDataProvider());
//        this.setDataProviderListener();
//
//        //set the totals
//        iterator = this.optionsModel.getGridDataProvider()
//            .getItems().iterator();
//
//        //todo: need to change this to account for filtering
//        while (iterator.hasNext()) {
//            tmpModel = iterator.next();
//            if (!tmpModel.getBSkip()) {
//                unitsTotal += tmpModel.getUnits();
//            }
//        }
//
//        Grid.Column aColumn = this.optionsViewer.getGrid().getColumnByKey(
//            "units");
//
//        this.optionsViewer.getFooter().getCell(aColumn)
//            .setText(unitsTotal.toString());
//    }
    
//    private void updateGridOnChange()
//    {
//        /**
//         * any change to accounts or tickers pull new data set
//         */
//        Iterator<ValidateStockTransactionModel> iterator;
//        ValidateStockTransactionModel tmpModel;
//        Double unitsTotal;
//
//        unitsTotal = 0.0;
//
//        if (this.dataValidateOptionsModel.getSelectedAccountModel() == null
//            || this.dataValidateOptionsModel.getSelectedTickerModel() == null)
//        {
//            //no update until both lists are complete with a selection
//            //this is an issue on entering as each is populated independently and fires the onChange
//            return;
//        }
//
//        if (this.dataProviderListener != null)
//        {
//            this.dataProviderListener.remove();
//        }
//
//        this.dataValidateOptionsModel.updateGridData();
//
//        this.getGrid().setDataProvider(
//            this.dataValidateOptionsModel.getGridDataProvider());
//        this.setGridDataProviderListener();
//
//        //set the totals
//        iterator = this.dataValidateOptionsModel.getGridDataProvider().getItems().iterator();
//
//        //exclude skips from the total
//        //todo: need to change to account for filtering
//        while (iterator.hasNext())
//        {
//            tmpModel = iterator.next();
//            if (!tmpModel.getBSkip())
//            {
//                unitsTotal += tmpModel.getUnits();
//            }
//        }
//
//        Grid.Column aColumn = this.getGrid().getColumnByKey("units");
//
//        this.getFooterRow().getCell(aColumn).setText(unitsTotal.toString());
//
//    }

    private void setListeners()
    {
        this.getComboAccounts().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateOptionsModel.setSelectedAccountModel((vcEvent.getValue()));

            this.updateGridOnChange();
        });

        this.getComboTickers().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateOptionsModel.setSelectedTickerModel(vcEvent.getValue());

            this.updateGridOnChange();
        });

        this.getCheckboxSkip().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateOptionsModel.filterChange(vcEvent.getValue(), null);
        });

        this.getCheckboxValidated().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateOptionsModel.filterChange(null, vcEvent.getValue());
        });

        this.getButtonSave().addClickListener(vcEvent -> this.doSave());

        this.getButtonCancel().
            addClickListener(vcEvent -> this.doCancel());
    }

//    private void setDataProviderListener() {
//        if (this.dataProviderListener != null) {
//            this.dataProviderListener.remove();
//        }
//
//        this.dataProviderListener = this.dataValidateOptionsModel.getGridDataProvider().addDataProviderListener(
//            dataEvent -> {
//            this.dataValidateOptionsView.getButtonSave().setEnabled(true);
////                this.optionsViewer.getButtonCancel().setEnabled(true);
//        });
//    }
    
private void setGridDataProviderListener()
    {
        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }

        this.dataProviderListener = this.dataValidateOptionsModel
            .getGridDataProvider().addDataProviderListener(dataEvent ->
            {
                //do not want enabled on simple filter change in data provider
                if (!this.dataValidateOptionsModel.getBInFilterChange())
                {
                    this.getButtonSave().setEnabled(true);
                }
            });
    }

    private void doCancel() {
        int i = 0;
    }

    private void doSave() {
        //write to database
        this.dataValidateOptionsModel.doSave();
        //fix buttons
        this.getButtonSave().setEnabled(false);
        this.getButtonCancel().setEnabled(false);

        /**
         * after save, pull from database and reset grid to be sure have the right data
         */
        this.updateGridOnChange();
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
        //todo: change top menu prefs icon route
    }
}
