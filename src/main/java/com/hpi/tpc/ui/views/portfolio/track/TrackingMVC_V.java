package com.hpi.tpc.ui.views.portfolio.track;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.services.TPCDAOImpl;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.prefs.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.grid.editor.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

@UIScope
@VaadinSessionScope
@Route(value = PORTFOLIO_TRACK_VIEW, layout = MainLayout.class)
@PageTitle(TITLE_PORTFOLIO + ": " + TITLE_PORTFOLIO_TRACK)
@Component
@CssImport("./styles/portfolioTracking.css")
@NoArgsConstructor
@PermitAll
//@CssImport("./styles")
public class TrackingMVC_V
    extends MVCView2WideBase
    implements BeforeEnterObserver, BeforeLeaveObserver,
    MVCView2WideInterface
{

    @Autowired private MainLayout mainLayout;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private PrefsController prefsController;
    @Autowired private EquitiesGridVL equitiesGridVL;
    @Autowired private TrackingPositionGridVL trackingPositionGridVL;
    @Autowired private TrackingMVC_M trackingMVCModel;
    @Getter @Autowired private TrackingControlsHL1 trackingControlsHL1;
    @Getter @Autowired private TrackingControlsHL2 trackingControlsHL2;
    @Getter @Autowired private StatusHL statusHL;

    private Registration dataProviderListener;

    private final Binder<ClientEquityModel> binder = new Binder<>(ClientEquityModel.class);

    private Editor<ClientEquityModel> editor;

    @PostConstruct
    @Override
    public void construct()
    {
        super.construct();
        this.addClassName("trackingContentFL");
        this.setId("TrackingContent");

        this.trackingMVCModel.getPrefs();

        this.doInitSelections();

        this.setupLeftVL("Equity Holdings");
        this.trackingControlsHL1.getSaveButton().setEnabled(false);
        this.trackingControlsHL1.getAdjustButton().setEnabled(false);

        this.setupRightVL("Positions");

        this.doSelectionListenersAdd();

        //view component needs access to the actual view
        this.equitiesGridVL.setTrackingMVCView(this);
    }

    @PreDestroy
    @Override
    protected void destruct()
    {
        super.destruct();

    }

    @Override
    public void removeListeners()
    {
        super.removeListeners();

        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }
    }

    private void doInitSelections()
    {
        this.trackingMVCModel.setSelectedSector(ClientSectorModel.builder()
            .cSecShort("--All--")
            .build());
    }

    private void doSelectionListenersAdd()
    {
        this.listeners.add(this.trackingControlsHL1.getCbSectors().addValueChangeListener(event ->
        {
            //clear color highlight
//            this.trackingControlsHL2.getTopLeftControlsTgtPctLabel().getStyle().set("background", "#FFFFFF");

            //clear status message
            this.statusHL.getLabelStatus().setText("");

            //hold the clientSectorId            
            this.trackingMVCModel.setSelectedSectorId(event.getValue().getClientSectorId());

            //set sectorTargetPct
            this.trackingMVCModel.setSelectedSectorTgtPct(
                this.mainLayout.getClientSectorIdTgtPctHashMap().get(this.trackingMVCModel.getSelectedSectorId()));
//            if (this.trackingMVCModel.getSelectedSectorTgtPct().equals(0.0))
//            {
//                //todo: set background to yellow; foreground to black
//                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("background", "#FCE94F");
//                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("foreground", "black");
//            }

            //filter on the client sector
            this.equitiesGridVL.filterSector(this.trackingMVCModel.getSelectedSectorId());

            if (this.trackingMVCModel.getSelectedSectorId() == -1)
            {
                this.equitiesGridVL.setOnAll(true);
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().setText("100");
            } else
            {
                this.equitiesGridVL.setOnAll(false);
                this.trackingControlsHL2.getLeftControlsTgtPctLabel()
                    .setText(this.trackingMVCModel.getSelectedSectorTgtPct().toString());
            }

            //set to false on any sector change
            //todo: this is an issue? why? dipstick comment
            this.trackingControlsHL1.getSaveButton().setEnabled(false);
            this.trackingControlsHL1.getAdjustButton().setEnabled(false);
            this.trackingPositionGridVL.filterEquity(this.trackingMVCModel.getSelectedSectorId(), null);

            this.doTotals();
        }));

        this.listeners.add(this.trackingControlsHL1.getSaveButton().addClickListener(event ->
        {
            this.doSave();
        }));

        this.listeners.add(this.trackingControlsHL1.getAdjustButton().addClickListener(event ->
        {
            this.doAdjust();
        }));

        this.listeners.add(this.trackingControlsHL2.getCbOpenOnly().addClickListener(event ->
        {
            this.trackingMVCModel.setSelectedTrackOpen(event.getSource().getValue());
            this.equitiesGridVL.filterOpen(event.getSource().getValue());
            this.statusHL.getLabelStatus().setText("");
        }));

        this.listeners.add(this.trackingControlsHL2.getCbActiveOnly().addClickListener(event ->
        {
            this.trackingMVCModel.setSelectedTrackActive(event.getSource().getValue());
            this.equitiesGridVL.filterActive(event.getSource().getValue());
//            this.statusHL.getLabelStatus().setText("testing testing");
        }));
    }

    @Override
    public void setupLeftVL(String leftVLTitle)
    {
        this.leftVL.setMinWidth("360px");
        this.leftVL.setWidth("700px");
        this.leftVL.setMaxWidth("850px");
        this.leftVL.setHeight("100%");

        // grid refresh data
//        this.equitiesGridVL.getData();
        //title
        this.leftVL.add(this.titleFormat(leftVLTitle));

        //controls
        this.trackingControlsHL1.doLayout();
        this.trackingControlsHL2.doLayout();
        this.statusHL.doLayout();
        this.leftVL.add(this.trackingControlsHL1, this.trackingControlsHL2, this.statusHL);

        //grid
        this.leftVL.add(this.trackingMVCModel.getEquitiesGrid());

        //set initial values
        this.trackingControlsHL2.getCbOpenOnly().setValue(this.trackingMVCModel.getSelectedTrackOpen());
        this.trackingControlsHL2.getCbActiveOnly().setValue(this.trackingMVCModel.getSelectedTrackActive());
//        this.equitiesGridVL.filterOpen(this.trackingControlsHL2.getCbOpenOnly().getValue());
//        this.equitiesGridVL.filterActive(this.trackingControlsHL2.getCbActiveOnly().getValue());

        //grid editor
        this.editor = this.trackingMVCModel.getEquitiesGrid().getEditor();
        this.doEditor();

        //apply filters
//        this.equitiesGridVL.filterSector(this.trackingMVCModel.getSelectedSectorId());

        //establish filter listener
        this.listeners.add(this.trackingMVCModel.getEquitiesGrid().addItemClickListener(event ->
        {
            this.statusHL.getLabelStatus().setText("");

            ClientEquityModel cem = (ClientEquityModel) event.getItem();
            this.trackingPositionGridVL.filterEquity(this.trackingMVCModel.getSelectedSectorId(), cem.getTicker());
        }));

        //grid totals
        this.doTotals();

        this.setDataProviderListener();
    }

    /*
     * Calculate and display the table totals
     */
    private void doTotals()
    {
        Grid.Column aColumn;
        Double actTotal, lckTotal, tgtTotal, adjTotal;
        Double dTmp;
        Iterator itemsIterator;
        ClientEquityModel cem;

        this.statusHL.getLabelStatus().setText("");

        actTotal = lckTotal = tgtTotal = adjTotal = 0.0;

        itemsIterator = this.trackingMVCModel.getEquitiesGrid().getListDataView().getItems().iterator();
        while (itemsIterator.hasNext())
        {
            cem = (ClientEquityModel) itemsIterator.next();

            //only work with the selected sector
            if (!this.trackingMVCModel.getSelectedSectorId().equals(-1) && !cem
                .getClientSectorId().equals(this.trackingMVCModel.getSelectedSectorId()))
            {
                continue;
            }

            if (cem.getBTgtLocked())
            {
                lckTotal += cem.getTgtPct();
            }

            tgtTotal += cem.getTgtPct();
            adjTotal += cem.getAdjPct();
            actTotal += cem.getActPct();
        }

        aColumn = this.trackingMVCModel.getEquitiesGrid().getColumnByKey("actPct");
        dTmp = Math.round(actTotal * 10.0) / 10.0;
        this.equitiesGridVL.getFooter().getCell(aColumn).setText(dTmp.toString());
        this.trackingMVCModel.setTotalActPct(dTmp);

        aColumn = this.trackingMVCModel.getEquitiesGrid().getColumnByKey("tgtLocked");
        dTmp = Math.round(lckTotal * 10.0) / 10.0;
        this.equitiesGridVL.getFooter().getCell(aColumn).setText(dTmp.toString());
        this.trackingMVCModel.setTotalTgtLocked(dTmp);

        aColumn = this.trackingMVCModel.getEquitiesGrid().getColumnByKey("tgtPct");
        dTmp = Math.round(tgtTotal * 10.0) / 10.0;
        this.equitiesGridVL.getFooter().getCell(aColumn).setText(dTmp.toString());
        this.trackingMVCModel.setTotalTgtPct(dTmp);

        aColumn = this.trackingMVCModel.getEquitiesGrid().getColumnByKey("adjPct");
        dTmp = Math.round(adjTotal * 10.0) / 10.0;
        this.equitiesGridVL.getFooter().getCell(aColumn).setText(dTmp.toString());
        this.trackingMVCModel.setTotalAdjPct(dTmp);

        this.trackingMVCModel.getTotalTgtPct(); // table total

        Double dbl = (Double.parseDouble(this.trackingControlsHL2.getLeftControlsTgtPctLabel().getText()));

        switch (dbl.compareTo(this.trackingMVCModel.getTotalTgtPct()))
        {
            case -1:    //actual total less than target
                this.trackingControlsHL2.setSectorTgt();
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("background", "#fce94f");
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("color", "#000000");
                break;
            case 0:     //actual total equals target
                this.trackingControlsHL2.setSectorTgt();
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("background", "#73d216");
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("color", "#ffffff");
                break;
            case 1:     //actual total greater than total
                this.trackingControlsHL2.setSectorTgt();
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("background", "#fce94f");
                this.trackingControlsHL2.getLeftControlsTgtPctLabel().getStyle().set("color", "#000000");
                break;
            default:
                int i = 0;
        }

    }

    /*
     * Given the sector percentage, allocate to it based on requested
     * target percentages
     */
    private void doAdjust()
    {
        String s;
        Double lckTotal, tgtTotal, adjTotal, adjValue, allocFactor;
        Iterator itemsIterator;
        ClientEquityModel cem;

        this.statusHL.getLabelStatus().setText("");

        lckTotal = tgtTotal = adjTotal = 0.0;

        // sum the total of locked targets
        // sum the total of the other targets
        //iterator is on all, not filtered group
        itemsIterator = this.equitiesGridVL.getDataProvider().getItems().iterator();
        while (itemsIterator.hasNext())
        {
            cem = (ClientEquityModel) itemsIterator.next();

            //skip where sector does not match selected
            if (!cem.getClientSectorId().equals(this.trackingMVCModel.getSelectedSectorId()))
            {
                continue;
            }

            tgtTotal += cem.getTgtPct();

            if (cem.getBTgtLocked())
            {
                lckTotal += cem.getTgtPct();
            }
        }

        // calculate the factor
        if (tgtTotal.equals(lckTotal))
        {
            //no allocation required
            this.statusHL.getLabelStatus().setText("");
            this.trackingControlsHL1.getAdjustButton().setEnabled(false);
            this.trackingControlsHL1.getSaveButton().setEnabled(true);
            return;
        }

        if (lckTotal > 100.0)
        {
            //cannot allocate; give a message
            this.statusHL.getLabelStatus().setText(
                "Locked total exceeds 100%; unable to allocate.");
            this.statusHL.getLabelStatus().getElement().getStyle().set("color", "red");
            return;
        } else
        {
            allocFactor = (this.trackingMVCModel.getSelectedSectorTgtPct() - lckTotal) / (tgtTotal - lckTotal);
            this.statusHL.getLabelStatus().setText("");
        }

        itemsIterator = this.equitiesGridVL.getDataProvider().getItems().iterator();
        while (itemsIterator.hasNext())
        {
            cem = (ClientEquityModel) itemsIterator.next();

            //skip where sector does not match selected
            if (!cem.getClientSectorId().equals(this.trackingMVCModel.getSelectedSectorId()))
            {
                continue;
            }

            if (cem.getBTgtLocked())
            {
                // locked, set adj to tgt
                adjValue = cem.getTgtPct();

                cem.setAdjPct(adjValue);

                adjTotal += adjValue;
            } else
            {
                //not locked
                adjValue = Math.round(cem.getTgtPct() * allocFactor * 100.0) / 100.0;

                cem.setAdjPct(adjValue);

                adjTotal += adjValue;
            }
        }

        // array is set, update table
        this.equitiesGridVL.getDataProvider().refreshAll();
        this.trackingControlsHL1.getAdjustButton().setEnabled(false);
        this.trackingControlsHL1.getSaveButton().setEnabled(true);

        this.doTotals();
    }

    private void doEditor()
    {
        this.statusHL.getLabelStatus().setText("");

        this.editor.setBinder(binder);

        //tgtPct edit
        TextField tgtPct = new TextField();
        //close editor on leaving cell
        tgtPct.getElement()
            .addEventListener("keydown", event -> this.trackingMVCModel
            .getEquitiesGrid().getEditor().closeEditor())
            .setFilter("event.key === 'Tab' || event.key === 'Escape' "
                + "|| event.key === 'Esc' || event.key ==='Enter'");
        this.binder.forField(tgtPct)
            //.withvalidator
            .withConverter(new StringToDoubleConverter("Double required"))
            .bind("tgtPct");
        this.trackingMVCModel.getEquitiesGrid().getColumnByKey("tgtPct").setEditorComponent(tgtPct);

        //comment edit
        TextField comment = new TextField();
        //close editor on leaving cell
        comment.getElement()
            .addEventListener("keydown", event -> this.trackingMVCModel
            .getEquitiesGrid().getEditor().closeEditor())
            .setFilter("event.key === 'Tab' || event.key === 'Escape' "
                + "|| event.key === 'Esc' || event.key === 'Enter'");
        this.binder.forField(comment)
            //.withValidator(vldtr)
            .bind("comment");
        this.trackingMVCModel.getEquitiesGrid()
            .getColumnByKey("comment").setEditorComponent(comment);

        //listener for command to edit the row
        this.listeners.add(this.trackingMVCModel.getEquitiesGrid().addItemDoubleClickListener(event ->
        {
            if (this.trackingMVCModel.getSelectedSectorId() == -1)
            {
                this.statusHL.getLabelStatus().setText("Edit enabled only for single sector selection");
                return;
            }
            this.trackingMVCModel.getEquitiesGrid().getEditor().editItem(event.getItem());
            tgtPct.focus();
        }));

        //binder change update
        this.listeners.add(binder.addValueChangeListener(event ->
        {
            this.trackingMVCModel.getEquitiesGrid().getEditor().refresh();
        }));

        //editor closed
        this.listeners.add(this.trackingMVCModel.getEquitiesGrid().getEditor().addCloseListener(event ->
        {
            if (this.binder.getBean() != null)
            {
                this.trackingControlsHL1.getAdjustButton().setEnabled(true);
                //do not enable Save button until after an Adjust
//                    this.saveButton.setEnabled(true);

                //the data is changed in here
                this.doTotals();
            }
        }));
    }

    private void doSave()
    {
        String sql;
        Iterator itemsIterator;
        ClientEquityModel cem;

        this.statusHL.getLabelStatus().setText("");

        itemsIterator = this.trackingMVCModel.getEquitiesGrid().getListDataView().getItems().iterator();

        while (itemsIterator.hasNext())
        {
            cem = (ClientEquityModel) itemsIterator.next();

            sql = String.format(ClientEquityModel.SQL_UPDATE_ALLOCATION,
                cem.getBActive() ? "Yes" : "No",
                cem.getAdjPct(),
                cem.getComment(),
                cem.getBTgtLocked() ? "Yes" : "No",
                cem.getJoomlaId(),
                cem.getTicker());

            this.serviceTPC.executeSQL(sql);

            cem.setTgtPct(cem.getAdjPct());
            cem.setAdjPct(0.0);
        }

        this.equitiesGridVL.getDataProvider().refreshAll();
        this.doTotals();

        this.trackingControlsHL1.getSaveButton().setEnabled(false);
        this.trackingControlsHL1.getAdjustButton().setEnabled(false);
    }

    @Override
    public void setupRightVL(String rightVLTitle)
    {
        this.rightVL.addClassName("trackingPositionsVL");
        this.rightVL.setMinWidth("360px");
        this.rightVL.setWidth("850px");
        this.rightVL.setMaxWidth("850px");
        this.rightVL.setHeight("100%");

        this.rightVL.add(this.titleFormat(rightVLTitle));

        this.rightVL.add(this.trackingPositionGridVL);

//        this.trackingPositionGridVL.getData();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.serviceTPC.AppTracking("TPC:Portfolio:Tracking");

        if (this.prefsController.getPref("TPCDrawerClose").
            equalsIgnoreCase("yes"))
        {
            this.mainLayout.setDrawerOpened(false);
        }

        this.equitiesGridVL.getData();
        this.trackingPositionGridVL.getData();

        this.equitiesGridVL.filterOpen(this.trackingControlsHL2.getCbOpenOnly().getValue());
        this.equitiesGridVL.filterActive(this.trackingControlsHL2.getCbActiveOnly().getValue());
        
                this.equitiesGridVL.filterSector(this.trackingMVCModel.getSelectedSectorId());
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event)
    {
        this.trackingMVCModel.writePrefs();
    }

    private void setDataProviderListener()
    {
        if (this.dataProviderListener != null)
        {
            this.dataProviderListener.remove();
        }

        //not putting in array as it is dynamic
        this.dataProviderListener = this.trackingMVCModel.getEquitiesGrid()
            .getDataProvider().addDataProviderListener(
                dataEvent ->
            {
                this.equitiesGridVL.getOnAll();
                if (this.equitiesGridVL.getOnAll())
                {
                    //disallow changes to the grid when All
//                    this.statusHL.setStatus("Edit enabled only for single sector selection");
                } else
                {
                    //any change to the grid
                    this.trackingControlsHL1.getAdjustButton().setEnabled(true);

                    //do not enable Save button until after an adjustment
//                    this.saveButton.setEnabled(true);
                }
            });
    }
}
