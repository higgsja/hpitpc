package com.hpi.tpc.ui.views.menus.data.equities.stocks.prefs;

import com.flowingcode.vaadin.addons.twincolgrid.*;
import com.hpi.tpc.ui.views.menus.data.equities.stocks.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.hpi.tpc.ui.views.menus.data.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
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
@Route(value = ROUTE_DATA_EQUITIES_STOCKS_PREFERENCES, layout = MainLayout.class)
@PermitAll
@Component
public class DataEquitiesStocksPrefsControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private MainLayout mainLayout;
    @Autowired private DataEquitiesStocksModel dataEquitiesStocksModel;

    private final DataEquitiesStocksPrefsVL prefsVL;
    private final DataEquitiesStocksPrefsTitleVL title;
    private TwinColGrid<Attribute> twinColGrid;
    private final DataEquitiesStocksPrefsControlsHL controls;

    private Registration selectedListener;
    private Registration dataProviderListener;

    public DataEquitiesStocksPrefsControllerFL()
    {
        this.addClassName("dataEquitiesStocksPrefs");
        this.setWidth("380px");

        //title vertical layout
        this.title = new DataEquitiesStocksPrefsTitleVL("Stock Data Attributes");

        //content vertical layout
        this.prefsVL = new DataEquitiesStocksPrefsVL();

        //controls
        this.controls = new DataEquitiesStocksPrefsControlsHL();

        //listeners
        this.doButtonListeners();
    }

    @PostConstruct
    public void construct()
    {
        this.twinColGrid = new TwinColGrid<Attribute>()
            .withAvailableGridCaption("Available")
            .withSelectionGridCaption("Selected")
            .withSelectionGridReordering()
            .withSizeFull()
            .withDragAndDropSupport()
//            .createFirstHeaderRow(false)
            .selectRowOnClick();

//        this.twinColGrid.addColumn(Attribute::getAttribute)
//            .setComparator(Attribute::getAttribute);
//            .setHeader("Attributes");

//        this.twinColGrid.setCaption("This is the caption");

        this.add(this.prefsVL);
        this.prefsVL.add(this.title);
        this.prefsVL.add(this.title);
        this.prefsVL.add(this.twinColGrid);
        this.prefsVL.add(this.controls);
    }

    private void doButtonListeners()
    {
        this.controls.getEquitiesStocksPrefsSave().addClickListener(vcEvent -> this.doSave());
        this.controls.getEquitiesStocksPrefsCancel().addClickListener(vcEvent -> this.doCancel());
    }

    private void doDataListeners()
    {
        this.selectedListener = this.twinColGrid.getSelectionGrid().getDataProvider()
            .addDataProviderListener(e ->
            {
                this.doSelectionChanged();
            });
    }

    private void removeDataListeners()
    {
        if (this.selectedListener != null)
        {
            this.selectedListener.remove();
        }

        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }
    }

    private void doCancel()
    {
        UI.getCurrent().navigate(DataEquitiesStocksControllerFL.class);
    }

    private void doSave()
    {
        //write the changes
        this.dataEquitiesStocksModel.writePrefs(this.twinColGrid);

        this.controls.getEquitiesStocksPrefsSave().setEnabled(false);
    }

    private void doSelectionChanged()
    {
        this.controls.getEquitiesStocksPrefsSave().setEnabled(true);
        this.controls.getEquitiesStocksPrefsCancel().setEnabled(true);
    }

    private void updateData()
    {
        this.removeDataListeners();
        this.dataEquitiesStocksModel.initAttributeData();

        this.twinColGrid.getAvailableGrid().setItems(this.dataEquitiesStocksModel.getAvailableAttributes());
        this.twinColGrid.getSelectionGrid().setItems(this.dataEquitiesStocksModel.getSelectedAttributes());

        this.doDataListeners();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        this.dataEquitiesStocksModel.serviceTPC.AppTracking("TPC:Data:Equities:Stocks:Preferences");

        //update the data
        this.updateData();

        //update buttons
        this.controls.getEquitiesStocksPrefsSave().setEnabled(false);
        this.controls.getEquitiesStocksPrefsCancel().setEnabled(true);

        //update the gear
        this.mainLayout.updatePagePrefsHL(null);
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
    }
}
