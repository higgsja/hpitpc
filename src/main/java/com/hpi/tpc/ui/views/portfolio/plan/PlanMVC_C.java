package com.hpi.tpc.ui.views.portfolio.plan;

import com.hpi.tpc.ui.views.portfolio.plan.charts.PlanChartsMVC_C;
import com.hpi.tpc.ui.views.portfolio.plan.sectors.PlanSectorsMVC_V;
import com.hpi.tpc.ui.views.portfolio.plan.sectors.PlanSectorsMVC_C;
import com.hpi.tpc.ui.views.portfolio.plan.sectors.PlanSectorsMVC_M;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;

/*
 * Interface between Model and View to process business logic and incoming requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class PlanMVC_C {
    @Autowired private MainLayout mainLayout;
    @Autowired private TPCDAOImpl serviceTPC;
    //not used here but sets up the MVC
    @Autowired private PlanSectorsMVC_C planSectorsMVCController;
    @Autowired private PlanSectorsMVC_M planSectorsMVCModel;
    @Autowired private PlanSectorsMVC_V planSectorsMVCView;
    //not used here but sets up the MVC
    @Autowired private PlanChartsMVC_C planChartsMVCController;
    @Autowired private PlanMVC_M planMVCModel;
    @Autowired private PlanMVC_V planMVCView;

    private final ArrayList<Registration> listeners;
    private Registration gridDataProviderListener;

    public PlanMVC_C() {
        //hit
        this.listeners = new ArrayList<>();
    }

    @PostConstruct
    private void construct() {
        this.getData();

        this.doListeners();

        this.planChartsMVCController.chartUpdate();
    }

    @PreDestroy
    private void destruct() {
        this.removeListeners();
    }

    /**
     * called from postConstruct, listeners at the very top level
     */
    private void doListeners() {
        //hit
        //special case outside array
        this.addDataProviderListener();

        //adjust button; must be here to adjust charts on changes
        this.listeners.add(this.planSectorsMVCController.getPlanSectorsMVCView()
            .getAdjustButton().addClickListener(adjust -> {
                //adjust the table values
                this.planSectorsMVCModel.doAdjust(this.planSectorsMVCController.getPlanSectorsMVCView(),
                    this.planSectorsMVCController.getPlanSectorsMVCView().getPlanTableVL().getPlanningGrid(),
                    this.planSectorsMVCController.getPlanSectorsMVCView().getPlanTableVL().getFooter());
                //adjust changes the dataset in the grid
            }));

        //grid
        //save button: does not have to be here but is more logical
        this.listeners.add(this.planSectorsMVCController.getPlanSectorsMVCView()
            .getSaveButton().addClickListener(save -> {
                //save data
                this.planSectorsMVCModel.doSave(this.planSectorsMVCView.getPlanTableVL().getPlanningGrid());

                //pull data from database after save
                this.getData();

                this.planChartsMVCController.chartUpdate();

                this.planSectorsMVCController.getPlanSectorsMVCView()
                    .getSaveButton().setEnabled(false);
            }));
    }

    /**
     * Create ListDataProvider for grid
     * Also used in Tracking
     */
    public void getData() {
        //hit
        List<ClientSectorModel> clientSectorModels;

        //remove old listener
        this.removeDataProviderListener();
        
        //query data from backend
        clientSectorModels = this.serviceTPC.getClientSectorModels(ClientSectorModel
            .SQL_SELECT_ALL_SECTORS_BY_TGTPCT_D);

        //put the data in the grid
        this.planSectorsMVCView.getPlanTableVL().getPlanningGrid()
            .setItems(this.mainLayout.getClientAllSectorListModels());

        //calculate totals
        this.planSectorsMVCModel.doTotals(this.planSectorsMVCView.getPlanTableVL().getPlanningGrid(),
            this.planSectorsMVCView.getPlanTableVL().getFooter());

        //add new listener
        this.addDataProviderListener();
    }

    /**
     * called from preDestroy
     */
    private void removeListeners() {
        for (Registration r : this.listeners) {
            if (r != null) {
                r.remove();
            }

            this.listeners.clear();
        }

        this.removeDataProviderListener();
    }

    private void addDataProviderListener() {
        //change to table data, update charts
        this.gridDataProviderListener = this.planSectorsMVCView.getPlanTableVL().getPlanningGrid().getDataProvider()
            .addDataProviderListener(data -> {
                //update charts
                //why enable adjust button here?
                //this.planSectorsMVCView.getAdjustButton().setEnabled(true);
                this.planChartsMVCController.chartUpdate();
            });
    }

    private void removeDataProviderListener() {
        if (this.gridDataProviderListener != null) {
            this.gridDataProviderListener.remove();
        }
    }
}
