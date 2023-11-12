package com.hpi.tpc.ui.views.menus.data.validate.stocks;

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
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/*
 * Controller: Interface between Model and View to process business logic and incoming
 * requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 * Target for navigation from appDrawer
 */
@UIScope
@VaadinSessionScope
@Route(value = ROUTE_DATA_VALIDATE_STOCKS_CONTROLLER, layout = MainLayout.class)
@PermitAll
@Component
public class DataValidateStocksControllerFL
    extends ViewControllerBaseFL //flexLayout
    implements BeforeEnterObserver
{

    //data model singleton as used in multiple places
    @Autowired private DataValidateStocksModel dataValidateStocksModel;

    private final DataValidateStocksVL dataValidateStocksVL;
    private final DataValidateStocksTitleVL dataValidateStocksTitleVL;
    private final DataValidateStocksControlsHL dataValidateStocksControlsHL;
    private final DataValidateStocksGridVL dataValidateStocksGridVL;

    private Registration dataProviderListener = null;

    public DataValidateStocksControllerFL()
    {
        this.addClassName("dataValidateStocksController");

        this.dataValidateStocksVL = new DataValidateStocksVL();
        this.add(this.dataValidateStocksVL);

        this.dataValidateStocksTitleVL = new DataValidateStocksTitleVL("Data Validate");
        this.dataValidateStocksVL.add(this.dataValidateStocksTitleVL);

        this.dataValidateStocksControlsHL = new DataValidateStocksControlsHL();
        this.dataValidateStocksVL.add(this.dataValidateStocksControlsHL);

        this.dataValidateStocksGridVL = new DataValidateStocksGridVL();
        this.dataValidateStocksVL.add(this.dataValidateStocksGridVL);
    }

    @PostConstruct
    public void construct()
    {

        //there are none
        //this.dataValidateStocksModel.getPrefs("DataValidateStocks");

        this.setCheckboxSkipValue(this.dataValidateStocksModel.getSelectedSkip());

        this.setCheckboxValidatedValue(this.dataValidateStocksModel.getSelectedValidated());

        this.setButtonSaveEnabled(false);
        this.setButtonCancelEnabled(true);

        this.setListeners();
    }

    private ComboBox<EditAccountModel> getComboAccounts()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksComboAccounts();
    }

    private ComboBox<TickerModel> getComboTickers()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksComboTickers();
    }

    private Checkbox getCheckboxSkip()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksCheckboxSkip();
    }

    private void setCheckboxSkipValue(Boolean skip)
    {
        this.getCheckboxSkip().setValue(skip);
    }

//    private Boolean getCheckboxSkipValue()
//    {
//        return this.getCheckboxSkip().getValue();
//    }
    private Checkbox getCheckboxValidated()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksCheckboxValidated();
    }

    private void setCheckboxValidatedValue(Boolean skip)
    {
        this.getCheckboxValidated().setValue(skip);
    }

//    private Boolean getCheckboxValidatedValue()
//    {
//        return this.getCheckboxValidated().getValue();
//    }
    private Button getButtonSave()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksButtonSave();
    }

    public void setButtonSaveEnabled(Boolean enabled)
    {
        this.getButtonSave().setEnabled(enabled);
    }

    private Button getButtonCancel()
    {
        return this.dataValidateStocksControlsHL.getValidateStocksButtonCancel();
    }

    public void setButtonCancelEnabled(Boolean enabled)
    {
        this.getButtonCancel().setEnabled(enabled);
    }

    public Grid<ValidateStockTransactionModel> getGrid()
    {
        return this.dataValidateStocksGridVL.getValidateStocksGrid();
    }

    public FooterRow getFooterRow()
    {
        return this.dataValidateStocksGridVL.getValidateStocksGridFooterRow();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        //log feature use
        this.dataValidateStocksModel.serviceTPC.AppTracking("WTPC:Data:Validate:Stocks");

        //refresh data on every entry
        this.updateDataOnEnter();

        //change the preferences route (none)
        this.updateNavBarGear(null);
    }

    private void updateDataOnEnter()
    {
        /**
         * update data on every enter as data may have changed
         */
        this.updateDataOnEnterAccounts();
        this.updateDataOnEnterTickers();
        
        //set the buttons
        this.setButtonSaveEnabled(false);
        this.setButtonCancelEnabled(false);
    }

    private void updateDataOnEnterAccounts()
    {
        /**
         * update accounts data from database into the view
         */
        //update data
        this.dataValidateStocksModel.updateAccountModels();

        //update view
        this.getComboAccounts().setItems(this.dataValidateStocksModel.getAccountModels());
        this.getComboAccounts().setValue(this.dataValidateStocksModel.getAccountModels().get(0));

        //update the data model
        this.dataValidateStocksModel.setSelectedAccountModel(this.getComboAccounts().getValue());
    }

    private void updateDataOnEnterTickers()
    {
        /**
         * update tickers from database into the view
         */
        //update data
        this.dataValidateStocksModel.updateTickerModels();

        //update view
        this.getComboTickers().setItems(this.dataValidateStocksModel.getTickerModels());
        this.getComboTickers().setValue(this.dataValidateStocksModel.getTickerModels().get(0));

        //update the data model
        this.dataValidateStocksModel.setSelectedTickerModel(this.getComboTickers().getValue());
    }

    /**
     * pull new data set on any grid change
     */
    private void updateGridOnChange()
    {
        Iterator<ValidateStockTransactionModel> iterator;
        ValidateStockTransactionModel tmpModel;
        Double unitsTotal;

        unitsTotal = 0.0;

        if (this.dataValidateStocksModel.getSelectedAccountModel() == null
            || this.dataValidateStocksModel.getSelectedTickerModel() == null)
        {
            //no update until both lists are complete with a selection
            //this is an issue on entering as each is populated independently and fires the onChange
            return;
        }

        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }

        this.dataValidateStocksModel.updateGridData();

        this.getGrid().setDataProvider(this.dataValidateStocksModel.getGridDataProvider());
        this.setGridDataProviderListener();

        //set the totals
        iterator = this.dataValidateStocksModel.getGridDataProvider().getItems().iterator();

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

    private void setListeners()
    {
        this.getComboAccounts().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateStocksModel.setSelectedAccountModel((vcEvent.getValue()));

            this.updateGridOnChange();
        });

        this.getComboTickers().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateStocksModel.setSelectedTickerModel(vcEvent.getValue());

            this.updateGridOnChange();
        });

        this.getCheckboxSkip().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateStocksModel.filterChange(vcEvent.getValue(), null);
        });

        this.getCheckboxValidated().addValueChangeListener(
            vcEvent ->
        {
            this.dataValidateStocksModel.filterChange(null, vcEvent.getValue());
        });

        this.getButtonSave().addClickListener(vcEvent -> this.doSave());

        this.getButtonCancel().addClickListener(vcEvent -> this.doCancel());
    }

    private void setGridDataProviderListener()
    {
        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }

        this.dataProviderListener = this.dataValidateStocksModel
            .getGridDataProvider().addDataProviderListener(dataEvent ->
            {
                //only enable if not a simple filter change in data provider
                if (!this.dataValidateStocksModel.getBInFilterChange())
                {
                    this.getButtonSave().setEnabled(true);
                }
            });
    }

    private void doCancel()
    {
        //cancel all changes and restart fresh from the database
        this.getUI().get().navigate(ROUTE_DATA_VALIDATE_STOCKS_CONTROLLER);
    }

    private void doSave()
    {
        //write to database
        this.dataValidateStocksModel.doSave();

        /**
         * after save, pull from database and reset grid to be sure have the right data
         */
        this.updateGridOnChange();
        
        //fix buttons
        this.getButtonSave().setEnabled(false);
        this.getButtonCancel().setEnabled(false);
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
        //todo: change top menu prefs icon route
    }
}
