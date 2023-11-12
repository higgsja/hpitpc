package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
public class TrackingControlsHL1
    extends ControlsHLBase
{

    @Autowired private MainLayout mainLayout;
    @Autowired public TrackingMVC_M trackingMVCModel;
    @Autowired public JdbcTemplate jdbcTemplate;

    private final HorizontalLayout sectorsHL = new HorizontalLayout();
    private final HorizontalLayout controlsBtnsHL = new HorizontalLayout();
    @Getter private final ComboBox<ClientSectorModel> cbSectors = new ComboBox();
    @Getter private final Button saveButton = new Button("Save");
    @Getter private final Button adjustButton = new Button("Adjust");

    @PostConstruct
    public void construct_a()
    {
        //docs indicate abstract postConstruct will not be hit but it is
        this.addClassName("trackingControlsHL1");
        this.setWidth("390px");
        this.setHeight("55px");
    }

    @Override
    public void doLayout()
    {
        setupSectors();
        setupButtons();
    }

    private void setupSectors()
    {
        this.cbSectors.setWidth("140px");
        this.sectorsHL.setClassName("trackingPanelSectorsHL");
        this.sectorsHL.setAlignItems(Alignment.CENTER);

        this.add(this.sectorsHL);

        this.setSector();
    }

    /**
     * Come here because of a change in the sector selection
     * just change to that selection
     */
    protected void setSector()
    {
        ClientSectorModel pseudoClientSectorModel;
        List<ClientSectorModel> sectorModels;

        sectorModels = new ArrayList<>();

        //get the sector list
        //  add pseudo sector for All
        this.trackingMVCModel.setSelectedSectorId(-1);
        this.trackingMVCModel.setSelectedSectorTgtPct(100.0);

        pseudoClientSectorModel = new ClientSectorModel(
            SecurityUtils.getUserId(),
            this.trackingMVCModel.getSelectedSectorId(),
            "", "-- All --", "Yes", true, 0.0,
            0.0, "", "No", false, 0.0, 0.0, 0.0, 1,
            ClientSectorModel.CHANGE_NONE);

        // place All first
        sectorModels.add(pseudoClientSectorModel);

        // add the rest of active sectors
        this.mainLayout
            .getClientActiveSectorListModels()
            .forEach(csm ->
            {
                sectorModels.add(csm);
            });
        this.cbSectors.setItems(sectorModels);
        this.cbSectors.setValue(pseudoClientSectorModel);

        this.sectorsHL.add(cbSectors);
    }

    private void setupButtons()
    {
        this.controlsBtnsHL.setClassName("trackingControlsBtnsHL");
        //vertical alignment
        this.controlsBtnsHL.setAlignItems(Alignment.CENTER);

        this.setButtons();
    }

    private void setButtons()
    {
        this.saveButton.setWidth("85px");
        this.adjustButton.setWidth("85px");
        this.controlsBtnsHL.add(saveButton, adjustButton);

        this.add(this.controlsBtnsHL);
    }
}
