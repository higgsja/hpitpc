package com.hpi.tpc.ui.views.portfolio.plan.charts;

import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;

/*
 * Interface between Model and View to process business logic and incoming
 * requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class PlanChartsMVC_C {
    @Autowired private PlanChartsMVC_M portfolioPlanChartsMVCModel;
    @Autowired private PlanChartsMVC_V portfolioPlanChartsMVCView;

    private final ArrayList<Registration> listeners;

    public PlanChartsMVC_C() {
        this.listeners = new ArrayList<>();
    }

    @PostConstruct
    private void construct() {
        this.doListeners();
    }

    @PreDestroy
    private void destruct() {
        this.removeListeners();
    }

    /**
     * Listeners specific to this mvc; others in planMVCController
     */
    private void doListeners() {
        //planSectorsMVCModel dataProvider?
        //no, do that in sectors and call here for update
    }

    public void chartUpdate() {
        this.portfolioPlanChartsMVCModel.doPieData();
        this.portfolioPlanChartsMVCView.doCharts(
            this.portfolioPlanChartsMVCModel.getDoublesTargetValues(),
            this.portfolioPlanChartsMVCModel.getStringsTargetLabels(),
            this.portfolioPlanChartsMVCModel.getDoublesActualValues(),
            this.portfolioPlanChartsMVCModel.getStringsActualLabels());
    }

    /**
     * Called from preDestroy
     */
    private void removeListeners() {
        //general
        for (Registration r : this.listeners) {
            if (r != null) {
                r.remove();
            }
        }

        this.listeners.clear();
    }
}
