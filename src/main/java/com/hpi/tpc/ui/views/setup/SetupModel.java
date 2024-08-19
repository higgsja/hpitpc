package com.hpi.tpc.ui.views.setup;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;
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
@NoArgsConstructor
public class SetupModel 
    extends MVCModelBase{

    @Getter @Setter private boolean selectedTrackActive;
    @Getter @Setter private boolean selectedTrackOpen;

    /**
     *
     * @param prefix
     */
    @Override
    public void getPrefs(String prefix) {
//        this.prefsController.readPrefsByPrefix("PortfolioTrack");
//        this.selectedTrackActive = this.prefsController
//            .getPref("PortfolioTrackActive").equals("Yes");
//        this.selectedTrackOpen = this.prefsController
//            .getPref("PortfolioTrackOpen").equals("Yes");
    }

    /**
     *
     * @param prefix
     */
    @Override
    public void writePrefs(String prefix) {
//        //preferences, update the hashmap, then write to database
//        this.prefsController.setPref("PortfolioTrackActive",
//            selectedTrackActive ? "Yes" : "No");
//        this.prefsController.setPref("PortfolioTrackOpen",
//            selectedTrackOpen ? "Yes" : "No");
//        
//        this.prefsController.writePrefsByPrefix("PortfolioTrack");
    }
}
