package com.hpi.tpc.ui.views.portfolio;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.hpi.tpc.ui.views.portfolio.track.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

/*
 * Controller: Interface between Model and View to process business logic and incoming
 * requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 * Target for navigation from appDrawer
 */
@UIScope
@VaadinSessionScope
@Route(value = ROUTE_PORTFOLIO, layout = MainLayout.class)
@PageTitle(TITLE_PORTFOLIO)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PermitAll
public class PortfolioController
    extends ViewControllerBaseFL
    implements BeforeEnterObserver, BeforeLeaveObserver
{

    @Autowired private PortfolioModel portfolioModel;

    @PostConstruct
    private void construct()
    {
        this.setClassName("portfolioMenu");
        this.menuBar.setId("portfolioMenuId");

        //get any preferences
        this.portfolioModel.getPrefs("Portfolio");

        this.addMenuBarTabs();
    }

    /*
     * create the tab and listeners; add to the menuBar
     */
    @Override
    public void addMenuBarTabs()
    {
        MenuItem planItem = this.menuBar.addItem(TITLE_PORTFOLIO_PLAN);
        MenuItem trackItem = this.menuBar.addItem(TITLE_PORTFOLIO_TRACK);

        planItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(PORTFOLIO_PLAN_VIEW);
        });

        trackItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(PORTFOLIO_TRACK_VIEW);
        });
    }


    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
//        if (this.getPrefsController().getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
//        {
//            this.getMainLayout().setDrawerOpened(false);
//        }
        
        super.beforeEnter(bee);

        //navBar refresh on entry to each view as they are different
        this.doNavBar(ROUTE_PORTFOLIO_PREFERENCES);

        bee.forwardTo(TrackingMVC_V.class);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent ble)
    {
    }
}
