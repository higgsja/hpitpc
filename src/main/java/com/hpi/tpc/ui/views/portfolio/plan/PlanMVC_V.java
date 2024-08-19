package com.hpi.tpc.ui.views.portfolio.plan;

import com.hpi.tpc.ui.views.portfolio.plan.charts.PlanChartsMVC_V;
import com.hpi.tpc.ui.views.portfolio.plan.sectors.PlanSectorsMVC_V;
import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/**
 * makes direct request for data from model to display the planning
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
@Route(value = PORTFOLIO_PLAN_VIEW, layout = MainLayout.class)
@PageTitle(TITLE_PORTFOLIO + ": " + TITLE_PORTFOLIO_PLAN)
@PermitAll
//@CssImport("./styles/portfolioPlanning.css")
public class PlanMVC_V
    extends FlexLayout
    implements BeforeEnterObserver, BeforeLeaveObserver
{

    @Autowired private TPCDAOImpl tpcService;
    @Autowired private PlanSectorsMVC_V planSectorsMVCView;
    @Autowired private PlanChartsMVC_V planChartsMVCView;

    private final Grid<FinVizEquityInfoModel> sectorGrid;

    public PlanMVC_V()
    {
        //hit
        this.addClassName("planningContentFL");
        this.setFlexDirection(FlexDirection.ROW);
        this.setFlexWrap(FlexWrap.WRAP);

        //flex takes all space
        this.setSizeFull();

        this.sectorGrid = new Grid<>();
    }

    @PostConstruct
    private void construct()
    {
        this.setId("PlanningContent");

        //singletons not available until postConstruct        
        this.doSetup();
    }

    private void doSetup()
    {
        this.add(this.planSectorsMVCView, this.planChartsMVCView);

        //let table take all necessary space
//        this.planSectorsMVCView.setSizeUndefined();
//        this.planChartsMVCView.setSizeUndefined();
//        this.setFlexGrow(1.0, this.planSectorsMVCView);
//        this.setFlexGrow(0, this.planChartsMVCView);   
    }

    @PreDestroy
    private void destruct()
    {
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //hit

        this.tpcService.AppTracking("TPC:Portfolio:Planning");
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event)
    {
    }
}
