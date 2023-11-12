package com.hpi.tpc.ui.views.portfolio.plan.sectors;

import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;

import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

/*
 * Interface between Model and View to process business logic and incoming requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 * Also establishes navBar tabs
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class PlanSectorsMVC_C
    extends VerticalLayout {
    @Getter @Autowired private PlanSectorsMVC_M planSectorsMVCModel;
    @Getter @Autowired private PlanSectorsMVC_V planSectorsMVCView;

    private final ArrayList<Registration> listeners;

    public PlanSectorsMVC_C() {
        //hit
        this.listeners = new ArrayList<>();
    }

    @PostConstruct
    private void construct() {
        //hit
        this.doListeners();
    }

    @PreDestroy
    private void destruct() {
        this.removeListeners();
    }

    /**
     * These listeners are those that are local to this mvc
     */
    private void doListeners() {
        //save button: handled in planMVCController
        //adjust button: handled in planMVCController

        //open only: filter or not
        this.listeners.add(this.planSectorsMVCView.getCbOpenOnly().addClickListener(cbOpen -> {
            this.planSectorsMVCView.getPlanTableVL().filterOpen(cbOpen.getSource().getValue());
        }));

        //active only: filter or not
        this.listeners.add(this.planSectorsMVCView.getCbActiveOnly().addClickListener(cbActive -> {
            this.planSectorsMVCView.getPlanTableVL().filterActive(cbActive.getSource().getValue());
        }));

        //sectors table row edit
        this.listeners.add(this.planSectorsMVCView.getPlanTableVL().getPlanningGrid()
            .addItemDoubleClickListener(event -> {
//            //required? set grid to single selection mode
//            this.planningTableVL.getPlanningGrid().setSelectionMode(Grid.SelectionMode.SINGLE);
//            SingleSelect<Grid<ClientSectorModel>, ClientSectorModel> sectorSelect =
//                this.planningTableVL.getPlanningGrid().asSingleSelect();

                if (!event.getItem().getBActive()) {
                    //inactive cannot be edited
                    //checkboxes remain enabled with this
                    return;
                }

                this.planSectorsMVCView.getEditor().editItem(event.getItem());
            }));

        //editor closed, reset adjust button and redo totals
        this.listeners.add(this.planSectorsMVCView.getPlanTableVL().getPlanningGrid()
            .getEditor().addCloseListener(editor -> {
                if (this.planSectorsMVCView.getBinder().getBean() != null) {
                    //after an edit, adjust button enabled
                    this.planSectorsMVCView.getAdjustButton().setEnabled(true);
                    //and, redo table totals
                    this.planSectorsMVCModel.doTotals(this.planSectorsMVCView
                        .getPlanTableVL().getPlanningGrid(),
                        this.planSectorsMVCView.getPlanTableVL().getFooter());
                }
            }));
    }

    /**
     * called from preDestroy
     */
    private void removeListeners() {
        for (Registration r : this.listeners) {
            if (r != null) {
                r.remove();
            }
        }

        this.listeners.clear();
    }
}
