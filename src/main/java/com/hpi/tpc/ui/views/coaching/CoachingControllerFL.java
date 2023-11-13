package com.hpi.tpc.ui.views.coaching;

import com.hpi.tpc.ui.views.baseClass.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import com.hpi.tpc.ui.views.coaching.benchmark.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
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
@Route(value = ROUTE_COACHING_CONTROLLER, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_COACHING)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PermitAll
public class CoachingControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{
    @Autowired private CoachingModel coachingModel;

    @PostConstruct
    private void construct()
    {
        this.addClassName("coachingControllerFL");

        //get any preferences
        this.coachingModel.getPrefs();

        this.addMenuBarTabs();
    }
    
    /*
     * create the tabs and listeners; add to the menuBar
     */
    @Override
    public void addMenuBarTabs()
    {
        MenuItem benchmarkItem = this.menuBar.addItem(ROUTE_COACHING_BENCHMARK_CONTROLLER);

        benchmarkItem.addClickListener(
            (ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_COACHING_BENCHMARK_CONTROLLER);
        });

        MenuItem gainsItem = this.menuBar.addItem(ROUTE_COACHING_GAINS_CONTROLLER);

        gainsItem.addClickListener(
            (ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_COACHING_GAINS_CONTROLLER);
        });
        
//        MenuItem tacticsItem = this.menuBar.addItem(MENU_COACHING_PERFORMANCE_TACTICS);
//
//        this.listeners.add(tacticsItem.addClickListener(
//            (ClickEvent<MenuItem> event) ->
//        {
//            UI.getCurrent().navigate(MENU_COACHING_PERFORMANCE_TACTICS);
//        }));

        MenuItem infoItem = this.menuBar.addItem(ROUTE_COACHING_INFO);

        infoItem.addClickListener(
            (ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_COACHING_INFO);
        });

    }


    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);
        
        //set navBar for this menu
        super.doNavBar(null);

        //send to default view
        bee.forwardTo(BenchmarkControllerFL.class);
    }
}
