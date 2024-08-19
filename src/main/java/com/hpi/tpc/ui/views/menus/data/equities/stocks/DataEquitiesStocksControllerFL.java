package com.hpi.tpc.ui.views.menus.data.equities.stocks;

import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
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
@Route(value = ROUTE_DATA_EQUITIES_STOCKS_CONTROLLER, layout = MainLayout.class)
@PermitAll
@Component
public class DataEquitiesStocksControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private DataEquitiesStocksModel dataEquitiesStocksModel;

    private final DataEquitiesStocksVL dataEquitiesStocksVL;
    private final DataEquitiesStocksTitleVL dataEquitiesStocksTitleVL;
    @Getter private final DataEquitiesStocksGridHL dataEquitiesStocksGridVL;

    public DataEquitiesStocksControllerFL()
    {
        this.addClassName("dataEquitiesStocksControllerFL");
        
        //content vertical layout
        this.dataEquitiesStocksVL = new DataEquitiesStocksVL();
        this.add(this.dataEquitiesStocksVL);

        //title in content
        this.dataEquitiesStocksTitleVL = new DataEquitiesStocksTitleVL("Stock Data");
        this.dataEquitiesStocksVL.add(this.dataEquitiesStocksTitleVL);

        //grid in content
        this.dataEquitiesStocksGridVL = new DataEquitiesStocksGridHL();
        this.dataEquitiesStocksVL.add(this.dataEquitiesStocksGridVL);
    }

    @PostConstruct
    public void construct()
    {
        this.dataEquitiesStocksModel.getPrefs("DataEquitiesStocks");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        //log feature use
        this.dataEquitiesStocksModel.serviceTPC.AppTracking("TPC:Data:Equities:Stocks");

        //grid layout may change based on preferences change; refresh on every entry
        this.dataEquitiesStocksGridVL.doLayout(this.dataEquitiesStocksModel.getStringColumns());
        
        //set listeners for filters on new layout
        this.doStocksGridListeners();

        //data may change; update data on every entry
        this.dataEquitiesStocksModel.finVizEquityInfoModelGridDataProviderSetup();

        //set data grid to display data provider
        this.dataEquitiesStocksGridVL.getFinVizEquityInfoModelGrid()
            .setItems(this.dataEquitiesStocksModel.getDataProvider());

        //change the preferences route
        this.updateNavBarGear(ROUTE_DATA_EQUITIES_STOCKS_PREFERENCES);
    }
    
    private void doStocksGridListeners(){
        this.dataEquitiesStocksGridVL.getFilterTicker().addValueChangeListener(event ->
        {
            this.dataEquitiesStocksModel.getGridFilter().setFilterTicker(event.getValue());
            
            this.dataEquitiesStocksGridVL.getFinVizEquityInfoModelGrid().getDataProvider().refreshAll();
        });
        
        this.dataEquitiesStocksGridVL.getFilterSector().addValueChangeListener(event ->
        {
            this.dataEquitiesStocksModel.getGridFilter().setFilterSector(event.getValue());
            
            this.dataEquitiesStocksGridVL.getFinVizEquityInfoModelGrid().getDataProvider().refreshAll();
        });
        
        this.dataEquitiesStocksGridVL.getFilterIndustry().addValueChangeListener(event ->
        {
            this.dataEquitiesStocksModel.getGridFilter().setFilterIndustry(event.getValue());
            
            this.dataEquitiesStocksGridVL.getFinVizEquityInfoModelGrid().getDataProvider().refreshAll();
        });

    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
    }
}
