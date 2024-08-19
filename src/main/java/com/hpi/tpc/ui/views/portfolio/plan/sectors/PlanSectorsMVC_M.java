package com.hpi.tpc.ui.views.portfolio.plan.sectors;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@Component
public class PlanSectorsMVC_M {
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private MainLayout main;

    public PlanSectorsMVC_M() {
        //hit
    }

    @PostConstruct
    private void construct() {
        //hit
    }

    /**
     * Handle allocating portfolio sectors for 100%
     *
     * @param planSectorsMVCView
     * @param planningGrid
     * @param footerRow
     */
    public void doAdjust(PlanSectorsMVC_V planSectorsMVCView,
        Grid<ClientSectorModel> planningGrid, FooterRow footerRow) {
        String s;
        Double lckTotal, tgtTotal, adjTotal, adjValue, allocFactor;
        NativeLabel statusMsg;
        Iterator itemsIterator;
        ClientSectorModel csm;
        
        planSectorsMVCView.getStatusMsgHL().removeAll();

        lckTotal = tgtTotal = adjTotal = 0.0;
        statusMsg = new NativeLabel();

        // sum the total of locked targets
        // sum the total of the other targets
        itemsIterator = ((ListDataProvider)planningGrid.getDataProvider()).getItems().iterator();

        while (itemsIterator.hasNext()) {
            csm = (ClientSectorModel) itemsIterator.next();

            //todo: must lock sectors that are held short for now
            //this may never happen
            if (csm.getActPct() < 0) {
                csm.setBTgtLocked(true);
            }

            tgtTotal += csm.getTgtPct();

            if (csm.getBTgtLocked()) {
                lckTotal += csm.getTgtPct();
            }
        }

        // calculate the factor
        // if they are equal, allocation is complete
        if (tgtTotal.equals(lckTotal)) {
            //no allocation required
            planSectorsMVCView.getAdjustButton().setEnabled(false);
            planSectorsMVCView.getSaveButton().setEnabled(true);
            return;
        }

        if (lckTotal > 100.0) {
            statusMsg.setText("Locked total exceeds 100%; unable to allocate.");
            statusMsg.getElement().getStyle().set("color", "red");
            planSectorsMVCView.getStatusMsgHL().add(statusMsg);
            return;
        }
        else {
            allocFactor = (100.0 - lckTotal) / (tgtTotal - lckTotal);
        }

        itemsIterator = ((ListDataProvider) planningGrid.getDataProvider()).getItems().iterator();
        while (itemsIterator.hasNext()) {
            csm = (ClientSectorModel) itemsIterator.next();

            if (csm.getBTgtLocked()) {
                // locked, set adj to tgt
                adjValue = csm.getTgtPct();

                csm.setAdjPct(adjValue);

                adjTotal += adjValue;
            }
            else {
                //not locked
                adjValue = Math.round(csm.getTgtPct() * allocFactor * 10.0) / 10.0;

                csm.setAdjPct(adjValue);

                adjTotal += adjValue;
            }
        }

        // array is set, update table
        //refreshAll and refreshItem seem to screw things up
        ((ListDataProvider) planningGrid.getDataProvider()).refreshAll();
        planSectorsMVCView.getAdjustButton().setEnabled(false);
        planSectorsMVCView.getSaveButton().setEnabled(true);

        this.doTotals(planningGrid, footerRow);
    }

    /**
     * Save edited data
     * @param planningGrid
     */
    public void doSave(Grid <ClientSectorModel> planningGrid) {
        //dataProvider has edited data
        String sql;
        Iterator itemsIterator;
        ClientSectorModel csm;

        itemsIterator = ((ListDataProvider) planningGrid.getDataProvider()).getItems().iterator();

        while (itemsIterator.hasNext()) {
            csm = (ClientSectorModel) itemsIterator.next();

            sql = String.format(ClientSectorModel.SQL_UPDATE_ALLOCATION,
                csm.getActive(),
                csm.getAdjPct(), //replace target with adjusted
                csm.getComment(), csm.getTgtLocked(),
                csm.getJoomlaId(), csm.getClientSectorId());

            this.serviceTPC.executeSQL(sql);
        }
        
        //after save, refresh main array
        this.main.getClientSectorLists();
    }

    /**
     * Calculate the table column totals
     *
     * @param planningGrid
     * @param footerRow
     */
    public void doTotals(Grid<ClientSectorModel> planningGrid, FooterRow footerRow) {
        Column aColumn;
        Double actTotal, lckTotal, tgtTotal, adjTotal;
        Double dTmp;
        Iterator itemsIterator;
        ClientSectorModel csm;

        itemsIterator = ((ListDataProvider) planningGrid.getDataProvider()).getItems().iterator();
        
        actTotal = lckTotal = tgtTotal = adjTotal = 0.0;

        while (itemsIterator.hasNext()) {
            csm = (ClientSectorModel) itemsIterator.next();
            actTotal += csm.getActPct();

            if (csm.getBTgtLocked()) {
                lckTotal += csm.getTgtPct();
            }

            tgtTotal += csm.getTgtPct();
            adjTotal += csm.getAdjPct();

            aColumn = planningGrid.getColumnByKey("actPct");
            dTmp = Math.round(actTotal * 1.0) / 1.0;
            footerRow.getCell(aColumn).setText(dTmp.toString());

            aColumn = planningGrid.getColumnByKey("tgtLocked");
            dTmp = Math.round(lckTotal * 1.0) / 1.0;
            footerRow.getCell(aColumn).setText(dTmp.toString());

            aColumn = planningGrid.getColumnByKey("tgtPct");
            dTmp = Math.round(tgtTotal * 1.0) / 1.0;
            footerRow.getCell(aColumn).setText(dTmp.toString());

            aColumn = planningGrid.getColumnByKey("adjPct");
            dTmp = Math.round(adjTotal * 1.0) / 1.0;
            footerRow.getCell(aColumn).setText(dTmp.toString());
        }
    }
}
