package com.hpi.tpc.ui.views.coaching.gains;

import com.hpi.tpc.ui.views.coaching.gains.chart.GainsControllerVL;
import com.hpi.tpc.ui.views.baseClass.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import com.hpi.tpc.ui.views.coaching.gains.positions.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Route(value = ROUTE_COACHING_GAINS_CONTROLLER, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_COACHING + ": " + TITLE_COACHING_GAINS)
@Component
@PermitAll
public class GainsControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private GainsVLModel gainsVLModel;
    @Autowired private GainsControllerVL gainsControllerVL;
    @Autowired private PositionsControllerVL positionsControllerVL;

    public GainsControllerFL()
    {
        this.addClassName("gainsControllerFL");
    }

    @PostConstruct
    protected void construct()
    {
        this.gainsVLModel.getPrefs("Gains");
        
        //gains chart
        this.gainsControllerVL.setWidth("550px");
        this.add(this.gainsControllerVL);

        //positions
        this.positionsControllerVL.setWidth("850px");
        this.add(this.positionsControllerVL);
    }
        

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        this.gainsVLModel.serviceTPC.AppTracking("TPC:Coaching:Gains");

        //change the preferences route
        super.updateNavBarGear(null);

//        event.forwardTo(GainsControllerVL.class);
    }

    @Override
    public void addMenuBarTabs()
    {
        //none
    }
}
