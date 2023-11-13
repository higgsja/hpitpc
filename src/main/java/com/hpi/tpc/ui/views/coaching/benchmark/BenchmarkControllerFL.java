package com.hpi.tpc.ui.views.coaching.benchmark;

import com.hpi.tpc.ui.views.coaching.*;
import com.hpi.tpc.ui.views.baseClass.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;

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
@Route(value = ROUTE_COACHING_BENCHMARK_CONTROLLER, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_COACHING)
@org.springframework.stereotype.Component
@PermitAll
public class BenchmarkControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private CoachingModel coachingModel;
    @Autowired private BenchmarkModel benchMarkModel;

    private final BenchmarkChartVL benchmarkChartVL;
    private final BenchmarkVL benchmarkVL;
    private final BenchmarkTitleVL benchmarkTitleVL;

    public BenchmarkControllerFL()
    {
        this.addClassName("coachingController");

        //content
        this.benchmarkVL = new BenchmarkVL();
        this.add(this.benchmarkVL);

        //title in content
        this.benchmarkTitleVL = new BenchmarkTitleVL("Performance");
        this.benchmarkVL.add(this.benchmarkTitleVL);

        //chart in content
        this.benchmarkChartVL = new BenchmarkChartVL();
        this.benchmarkVL.add(this.benchmarkChartVL);
    }

    @PostConstruct
    private void construct()
    {
        //get any preferences
        this.coachingModel.getPrefs();

        this.doLayout();
    }

    private void doLayout()
    {
        this.setMinWidth("320px");
        this.setWidth("600px");
        this.setMaxWidth("600px");
        this.setMinHeight("320px");
        this.setMaxHeight("600px");
        this.setHeight("600px");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        //log feature use
        this.benchMarkModel.serviceTPC.AppTracking("TPC:Coaching:Benchmark");

        //change the preferences route
        this.updateNavBarGear(null);

        //update data
        this.benchmarkChartVL.removeAll();
        //this.benchmarkChartVL.add((Collection<Component>) this.benchMarkModel.doChart());
        this.benchmarkChartVL.add(this.benchMarkModel.doChart());
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
    }
}
