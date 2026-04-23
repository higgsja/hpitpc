package com.hpi.tpc.ui.views.setup.equities;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
//@CssImport(value = "./styles/setupEquities-grid.css",
//           id = "setupEquities-grid", themeFor = "vaadin-grid")
/**
 * Provides the Equities table
 */
public class SetupEquitiesTableVL
    extends VerticalLayout {
    @Getter private final Grid<ClientEquityModel> setupEquitiesGrid = new Grid<>();
    //subset of view needs knowledge of full view
    private SetupEquitiesView setupEquitiesMVCView;

    public SetupEquitiesTableVL() {
        this.setClassName("setupEquitiesTable");
        this.setSizeFull();
    }

    @PostConstruct
    private void construct() {
        this.doGridSetup();        
    }

    @PreDestroy
    private void destruct() {
    }

    private void doGridSetup() {
        this.setupEquitiesGrid.setId("setupSectorsTableGrid");
        this.setupEquitiesGrid.setThemeName("dense");
        this.setupEquitiesGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.setupEquitiesGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.setupEquitiesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.setupEquitiesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.setupEquitiesGrid.setHeightFull();
        this.setupEquitiesGrid.setAllRowsVisible(true);

        this.setupEquitiesGrid.addColumn(ClientEquityModel::getJoomlaId)
            .setHeader("JoomlaId")
            .setWidth("150px")
            .setKey("joomlaid");
        this.setupEquitiesGrid.getColumnByKey("joomlaid").setVisible(false);
        
        this.setupEquitiesGrid.addColumn(ClientEquityModel::getTicker)
            .setHeader("Ticker")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("equityTkr")
            .setSortProperty("equityTkr");

        this.setupEquitiesGrid.addColumn(ClientEquityModel::getCompany)
            .setHeader("Company")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("equity")
            .setSortProperty("company");
        
        this.setupEquitiesGrid.addColumn(ClientEquityModel::getClientSectorId)
            .setHeader("Sector")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("cSecShortId")
            .setSortProperty("cSecShortId");
        this.setupEquitiesGrid.getColumnByKey("cSecShortId").setVisible(false);
        
        this.setupEquitiesGrid.addColumn(ClientEquityModel::getCSecShort)
            .setHeader("Sector")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("cSecShort")
            .setSortProperty("cSecShort");

        this.setupEquitiesGrid.addColumn(ClientEquityModel::getComment)
            .setHeader("Comment")
            .setAutoWidth(true)
            .setResizable(true)
            .setKey("comment")
            .setSortProperty("comment");

        this.setupEquitiesGrid.setColumnReorderingAllowed(true);
        this.setupEquitiesGrid.recalculateColumnWidths();

        this.add(this.setupEquitiesGrid);
    }
    
    public void setSetupEquitiesMVCView(SetupEquitiesView setupEquitiesMVCView){
        this.setupEquitiesMVCView = setupEquitiesMVCView;
    }
}
