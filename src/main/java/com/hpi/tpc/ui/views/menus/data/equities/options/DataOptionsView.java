package com.hpi.tpc.ui.views.menus.data.equities.options;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.prefs.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/**
 * makes direct request for data from model
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
@Route(value = ROUTE_DATA_EQUITIES_OPTIONS, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_DATA + ":" + TITLE_PAGE_DATA_EQUITIES_OPTIONS)
@PermitAll
public class DataOptionsView
    extends VerticalLayout
    implements BeforeEnterObserver, BeforeLeaveObserver {
    @Autowired private MainLayout mainLayout;
    @Autowired private TPCDAOImpl tpcService;
    @Autowired private PrefsController prefsController;

    private final Grid<FinVizEquityInfoModel> equityInfoGrid = new Grid<>();

    public DataOptionsView() {
        this.addClassName("dataStocksView");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.tpcService.AppTracking("TPC:Data:Equities:Options");

        if (prefsController.getPref("TPCDrawerClose").equalsIgnoreCase("yes")) {
            this.mainLayout.setDrawerOpened(false);
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
    }
}
