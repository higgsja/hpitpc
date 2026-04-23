package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.prefs.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@NoArgsConstructor
public class TrackingMVC_M
{

    @Autowired private PrefsController prefsController;

    @Getter @Setter private Integer selectedSectorId = -1;
    @Getter @Setter private Double selectedSectorTgtPct;
    @Getter @Setter private ClientSectorModel selectedSector;

    @Getter @Setter private Boolean selectedTrackActive;
    @Getter @Setter private Boolean selectedTrackOpen;
   
    @Getter private Grid<ClientEquityModel> equitiesGrid;
    @Getter private Grid<PositionModel> positionsGrid;
    
    @Getter @Setter private Double totalActPct;
    @Getter @Setter private Double totalTgtLocked;
    @Getter @Setter private Double totalTgtPct;
    @Getter @Setter private Double totalAdjPct;
    
    @PostConstruct
    private void construct(){
        this.equitiesGrid = new Grid<>();
        this.positionsGrid = new Grid<>();
    }
    
    public void getPrefs()
    {
        this.prefsController.readPrefsByPrefix("PortfolioTrack");
        this.selectedTrackActive = this.prefsController
            .getPref("PortfolioTrackActive").equals("Yes");
        this.selectedTrackOpen = this.prefsController
            .getPref("PortfolioTrackOpen").equals("Yes");
    }

    public void writePrefs()
    {
        //preferences, update the hashmap, then write to database
        this.prefsController.setPref("PortfolioTrackActive",
            selectedTrackActive ? "Yes" : "No");
        this.prefsController.setPref("PortfolioTrackOpen",
            selectedTrackOpen ? "Yes" : "No");

        this.prefsController.writePrefsByPrefix("PortfolioTrack");
    }
}
