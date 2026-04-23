package com.hpi.tpc.ui.views.portfolio.plan.sectors;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class PlanSectorTableVL
    extends VerticalLayout {
    @Getter private final Grid<ClientSectorModel> planningGrid = new Grid<>();
    @Getter private FooterRow footer;
    //subset of view needs knowledge of full view
    private PlanSectorsMVC_V planSectorsMVCView;

    private Boolean openEnabled = false;
    private Boolean activeEnabled = false;

    public PlanSectorTableVL() {
    }

    @PostConstruct
    private void construct() {
        this.addClassName("planningContentSectorsTableVL");
        this.setHeight("100%");
        this.doGridSetup();
    }

    @PreDestroy
    private void destruct() {
    }

    public void filterOpen(Boolean enable) {
        ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider()).clearFilters();

        this.openEnabled = enable;

        if (openEnabled) {
            ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider()).addFilter(
                clientSectorModel -> Math.abs(clientSectorModel.getActPct()) > 0.0
                                         || clientSectorModel.getBTgtLocked());
        }

        if (activeEnabled) {
            ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider()).addFilter(
                clientSectorModel -> clientSectorModel.getBActive() || Math.abs(
                clientSectorModel.getTgtPct()) > 0.0 || clientSectorModel.getBTgtLocked());
        }
    }

    public void filterActive(Boolean enable) {
        ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider()).clearFilters();

        this.activeEnabled = enable;

        if (openEnabled) {
            ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider())
                .addFilter(clientSectorModel -> Math.abs(clientSectorModel.getActPct()) > 0.0
                                                    || clientSectorModel.getBTgtLocked());
        }

        if (activeEnabled) {
            ((ListDataProvider<ClientSectorModel>) this.planningGrid.getDataProvider())
                .addFilter(clientSectorModel -> clientSectorModel.getBActive() || Math.abs(
                clientSectorModel.getTgtPct()) > 0.0 || clientSectorModel.getBTgtLocked());
        }
    }

    private void doGridSetup() {
        this.planningGrid.setClassName("planningContentSectorsTable");
        this.planningGrid.setThemeName("dense");
        this.planningGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.planningGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        this.footer = this.planningGrid.appendFooterRow();

        this.planningGrid.addColumn(ClientSectorModel::getJoomlaId)
            .setHeader("JoomlaId")
            .setWidth("150px")
            .setKey("joomlaid");
        this.planningGrid.getColumnByKey("joomlaid").setVisible(false);

        this.planningGrid.addColumn(ClientSectorModel::getClientSector)
            .setHeader("Sector")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("sector")
            .setSortProperty("sector");

        this.planningGrid.addComponentColumn((clientSectorModel) -> {
            Checkbox activeCheckBox = new Checkbox();
            activeCheckBox.setValue(clientSectorModel.getBActive());

            if (!clientSectorModel.getActPct().equals(0.0) || clientSectorModel.getBTgtLocked()
                    || !clientSectorModel.getTgtPct().equals(0.0)) {
                //disallow deactivating
                activeCheckBox.setEnabled(false);
            }
            activeCheckBox.addValueChangeListener(event -> {
                clientSectorModel.setBActive(event.getValue());

                //need to enable the adjust button on change
                this.planSectorsMVCView.getAdjustButton().setEnabled(true);
            });
            return activeCheckBox;
        })
            .setHeader("Active")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("active")
            .setSortProperty("active");

        this.planningGrid.addColumn(ClientSectorModel::getActPct)
            .setHeader("Act%")
            .setAutoWidth(true)
            .setKey("actPct")
            .setTextAlign(ColumnTextAlign.END)
            .setSortProperty("actPct");

        this.planningGrid.addColumn(ClientSectorModel::getTgtPct)
            .setHeader("Tgt%")
            .setAutoWidth(true)
            .setKey("tgtPct")
            .setTextAlign(ColumnTextAlign.END)
            .setClassNameGenerator(sectorModel -> //>+-2%, >+-5%
                Math.abs(sectorModel.getTgtPct() - sectorModel.getActPct()) > 2.0
                    && Math.abs(sectorModel.getTgtPct() - sectorModel
                .getActPct()) < 5.0 ? "grid-cell-yel" :
                Math.abs(sectorModel.getTgtPct() - sectorModel.getActPct()) > 5.0 ? "grid-cell-neg" : "")
            .setSortProperty("tgtPct");

        this.planningGrid.addColumn(ClientSectorModel::getAdjPct)
            .setHeader("Adj%")
            .setAutoWidth(true)
            .setKey("adjPct")
            .setTextAlign(ColumnTextAlign.END)
            .setSortProperty("adjPct");

        this.planningGrid.addComponentColumn(clientSectorModel -> {
            Checkbox lockedCheckBox = new Checkbox();
            lockedCheckBox.setValue(clientSectorModel.getBTgtLocked());

            if (!clientSectorModel.getBActive()) {
                //inactive sector, disallow editing
                //need to check Active; Adjust; Save to re-enable
                lockedCheckBox.setEnabled(false);
            }

            lockedCheckBox.addValueChangeListener(event -> {
                clientSectorModel.setBTgtLocked(event.getValue());

                //need to enable the adjust button on change
                this.planSectorsMVCView.getAdjustButton().setEnabled(true);
            });
            return lockedCheckBox;
        })
            .setHeader("Lckd")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("tgtLocked")
            .setSortProperty("tgtLocked");

        this.planningGrid.addColumn(ClientSectorModel::getComment)
            .setHeader("Comment")
            .setWidth("150px")
            .setResizable(true)
//            .setFlexGrow(1)
            .setKey("comment")
            .setSortProperty("comment");

        this.planningGrid.setColumnReorderingAllowed(true);
        this.planningGrid.recalculateColumnWidths();

        this.add(this.planningGrid);
    }

    public void setPlanSectorsMVCView(PlanSectorsMVC_V planSectorsMVCView) {
        this.planSectorsMVCView = planSectorsMVCView;
    }
}
