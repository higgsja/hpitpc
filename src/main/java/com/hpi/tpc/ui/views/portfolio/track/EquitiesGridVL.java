package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.text.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

//@CssImport(value = "./styles/tpc-grid-theme.css", id = "tpc-grid-theme", themeFor = "vaadin-grid")
@CssImport(value = "./styles/tpc-grid-theme.css", themeFor = "vaadin-grid")
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@NoArgsConstructor
public class EquitiesGridVL
    extends VerticalLayout
{

    @Autowired private MainLayout mainLayout;
    @Autowired private TrackingMVC_M trackingMVCModel;
//    @Autowired private TrackingMVCView trackingMVCview;

    @Getter private ListDataProvider<ClientEquityModel> dataProvider;

    //this is a sub-component of the view; needs access to the view
    @Setter private TrackingMVC_V trackingMVCView;

    private Registration equitiesGridListener;
    @Getter private FooterRow footer;

    @Getter @Setter private Boolean openEnabled = false;
    @Getter @Setter private Boolean activeEnabled = false;

    @Getter @Setter private Boolean onAll = true;

    private Boolean bInActiveCheckBox;
    private Boolean bInLockedCheckBox;

    @PostConstruct
    private void construct()
    {
        this.setClassName("holdingsGridVL");
        this.setHeight("100%");

        this.bInActiveCheckBox = false;
        this.bInLockedCheckBox = false;

        this.doGridSetup();

//        //change positions filter on click
//        this.equitiesGridListener = this.trackingMVCModel.getEquitiesGrid().addItemClickListener(
//            event ->
//        {
//            ClientEquityModel cem = (ClientEquityModel) event.getItem();
//            this.trackingMVCModel.getPositionsGrid().filterEquity(this.trackingMVCModel.getSelectedSectorId(), cem.getTicker());
//        });
    }

    @PreDestroy
    private void destruct()
    {
        this.removeListeners();
    }

    private void removeListeners()
    {
        this.equitiesGridListener.remove();
    }

    public void filterSector(Integer clientSectorId)
    {
        this.dataProvider.clearFilters();

        this.trackingMVCModel.setSelectedSectorId(clientSectorId);

        //todo: if -1 then all
        if (this.trackingMVCModel.getSelectedSectorId() != null
            && this.trackingMVCModel.getSelectedSectorId() != -1)
        {
            this.dataProvider.addFilter(cem -> Objects.equals(cem.getClientSectorId(),
                this.trackingMVCModel.getSelectedSectorId()));
        }

        if (openEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> Math.abs(clientEquityModel.getActPct()) > 0.0
                || clientEquityModel.getBTgtLocked());
        }

        if (activeEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> clientEquityModel.getBActive() || Math.abs(
                clientEquityModel.getActPct()) > 0.0 || clientEquityModel.getBTgtLocked());
        }
    }

    public void filterOpen(Boolean enable)
    {
        this.dataProvider.clearFilters();
        this.openEnabled = enable;

        if (this.trackingMVCModel.getSelectedSectorId() != null
            && this.trackingMVCModel.getSelectedSectorId() != -1)
        {
            this.dataProvider.addFilter(cem -> Objects.equals(cem.getClientSectorId(),
                this.trackingMVCModel.getSelectedSectorId()));
        }

        if (openEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> Math.abs(clientEquityModel.getActPct()) > 0.0
                || clientEquityModel.getBTgtLocked());
        }

        if (activeEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> clientEquityModel.getBActive() || Math.abs(
                clientEquityModel.getActPct()) > 0.0 || clientEquityModel.getBTgtLocked());
        }
    }

    public void filterActive(Boolean enable)
    {
        this.dataProvider.clearFilters();
        this.activeEnabled = enable;

        //filter on the sector selection
        if (this.trackingMVCModel.getSelectedSectorId() != null
            && this.trackingMVCModel.getSelectedSectorId() != -1)
        {
            this.dataProvider.addFilter(cem -> Objects.equals(cem.getClientSectorId(),
                this.trackingMVCModel.getSelectedSectorId()));
        }

        if (openEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> Math.abs(clientEquityModel.getActPct()) > 0.0
                || clientEquityModel.getBTgtLocked());
        }

        if (activeEnabled)
        {
            this.dataProvider.addFilter(clientEquityModel -> clientEquityModel.getBActive() || Math.abs(
                clientEquityModel.getActPct()) > 0.0 || clientEquityModel.getBTgtLocked());
        }
    }

    private void doGridSetup()
    {
        //this.addClassName("pt-grid-content");
//        this.trackingMVCModel.getEquitiesGrid().setId("equities-gridVL");
        this.trackingMVCModel.getEquitiesGrid().setThemeName("dense");

        this.trackingMVCModel.getEquitiesGrid().addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.trackingMVCModel.getEquitiesGrid().addThemeVariants(GridVariant.LUMO_COMPACT);
        this.trackingMVCModel.getEquitiesGrid().addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.trackingMVCModel.getEquitiesGrid().setSelectionMode(Grid.SelectionMode.SINGLE);
        this.trackingMVCModel.getEquitiesGrid().setAllRowsVisible(false);

        this.footer = this.trackingMVCModel.getEquitiesGrid().appendFooterRow();

        this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getJoomlaId)
            .setHeader("JoomlaId")
            .setWidth("150px")
            .setKey("joomlaid");
        this.trackingMVCModel.getEquitiesGrid().getColumnByKey("joomlaid").setVisible(false);

        this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getTicker)
            .setHeader("Equity")
            //.setWidth("80px")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("equity")
            .setSortProperty("equity");

        this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getPrice)
            .setHeader("Price")
            //.setWidth("80px")
            .setAutoWidth(true)
            .setKey("price")
            .setTextAlign(ColumnTextAlign.END)
            .setSortProperty("price");

        //active
        this.trackingMVCModel.getEquitiesGrid().addComponentColumn((clientEquityModel) ->
        {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(clientEquityModel.getBActive());
            
            checkBox.setEnabled(clientEquityModel.getActPct() > 0.0 || clientEquityModel.getTgtPct() > 0.0);

            checkBox.addValueChangeListener(event ->
            {
                if (this.bInActiveCheckBox)
                {
                    //avoid recursion
                    this.bInActiveCheckBox = false;
                } else
                {
                    //not currently in the ActiveCheckBox logic here
                    if (this.onAll)
                    {
                        //disallow editing unless on single sector
                        this.bInActiveCheckBox = true;
                        //reset to database value
                        checkBox.setValue(clientEquityModel.getBActive());

                        //show the error
                        this.trackingMVCView.getStatusHL()
                            .setStatus("Edit enabled only for single sector selection");
                    } else
                    {
                        //clear status message
                        this.trackingMVCView.getStatusHL().setStatus("");

                        this.bInActiveCheckBox = true;

                        clientEquityModel.setBActive(event.getValue());

                        //enable adjust
                        this.trackingMVCView.getTrackingControlsHL1().getAdjustButton().setEnabled(false);
                        
                        //enable/disable active checkBox
                        checkBox.setEnabled(clientEquityModel.getActPct() > 0.0 
                            || clientEquityModel.getTgtPct() > 0.0);
                    }
                }
            });
            return checkBox;
        })
            .setHeader("Active")
            //.setWidth("80px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("active")
            .setSortProperty("active");

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        this.trackingMVCModel.getEquitiesGrid().addColumn(new NumberRenderer<>(ClientEquityModel::getActPct, nf))
            .setHeader("Act%")
            .setAutoWidth(true)
            .setKey("actPct")
            .setTextAlign(ColumnTextAlign.END)
            .setSortProperty("actPct");

//        this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getActPct)
//            .setHeader("Act%")
//            //.setWidth("70px")
//            .setAutoWidth(true)
//            .setKey("actPct")
//            .setTextAlign(ColumnTextAlign.END)
//            .setSortProperty("actPct");
        this.trackingMVCModel.getEquitiesGrid().addColumn(new NumberRenderer<>(ClientEquityModel::getTgtPct, nf))
            //this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getTgtPct)
            .setHeader("Tgt%")
            //.setWidth("70px")
            .setAutoWidth(true)
            .setKey("tgtPct")
            .setTextAlign(ColumnTextAlign.END)
            .setClassNameGenerator(equityModel
                -> //>+-2%, >+-5%
                Math.abs(equityModel.getTgtPct() - equityModel.getActPct()) > 2.0 && Math.abs(equityModel
            .getTgtPct()
            - equityModel.getActPct()) < 5.0 ? "grid-cell-yel" : Math.abs(equityModel.getTgtPct() - equityModel
            .getActPct()) > 5.0 ? "grid-cell-neg" : "")
            .setSortProperty("tgtPct");

        this.trackingMVCModel.getEquitiesGrid().addColumn(new NumberRenderer<>(ClientEquityModel::getAdjPct, nf))
            //this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getAdjPct)
            .setHeader("Adj%")
            //.setWidth("70px")
            .setAutoWidth(true)
            .setKey("adjPct")
            .setTextAlign(ColumnTextAlign.END)
            .setSortProperty("adjPct");

        this.trackingMVCModel.getEquitiesGrid().addComponentColumn((clientequityModel) ->
        {
            Checkbox checkBox = new Checkbox();
            checkBox.setValue(clientequityModel.getBTgtLocked());
            checkBox.addValueChangeListener(event ->
            {
                if (this.bInLockedCheckBox)
                {
                    //avoid recursion
                    this.bInLockedCheckBox = false;
                } else
                {
                    if (this.onAll)
                    {
                        //disallow editing unless on single sector
                        this.bInLockedCheckBox = true;
                        //reset to the database value
                        checkBox.setValue(clientequityModel.getBTgtLocked());

                        //show the error
                        this.trackingMVCView.getStatusHL()
                            .setStatus("Edit enabled only for single sector selection");
                    } else
                    {
                        this.trackingMVCView.getStatusHL().setStatus("");

                        this.bInLockedCheckBox = true;

                        //allow the edit
                        clientequityModel.setBTgtLocked(event.getValue());

                        //enable adjust
                        this.trackingMVCView.getTrackingControlsHL1().getAdjustButton().setEnabled(false);
                    }
                }
            });

            return checkBox;
        })
            .setHeader("Lckd")
            //.setWidth("70px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("tgtLocked")
            .setSortProperty("tgtLocked");

        this.trackingMVCModel.getEquitiesGrid().addColumn(ClientEquityModel::getComment)
            .setHeader("Comment")
            .setWidth("150px")
            .setResizable(true)
            .setFlexGrow(1)
            .setKey("comment")
            .setSortProperty("comment");

        this.trackingMVCModel.getEquitiesGrid().setColumnReorderingAllowed(true);
        this.trackingMVCModel.getEquitiesGrid().recalculateColumnWidths();

        this.add(this.trackingMVCModel.getEquitiesGrid());
    }

    public void getData()
    {
        this.dataProvider = new ListDataProvider<>(this.mainLayout.getClientEquityAttributesModels());

        this.trackingMVCModel.getEquitiesGrid().setDataProvider(dataProvider);
    }
}
