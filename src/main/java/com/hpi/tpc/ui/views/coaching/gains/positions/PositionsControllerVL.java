package com.hpi.tpc.ui.views.coaching.gains.positions;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

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
@Component
public class PositionsControllerVL
    extends ViewControllerBaseVL
    implements BeforeEnterObserver
{

    @Autowired GainsPositionGridVL gainsPositionsGridVL;
    private GainsPositionsTitleVL gainsPositionsTitleVL;

    public PositionsControllerVL()
    {
        this.addClassName("gainsPositionsControllerVL");
        
        this.getStyle().set("padding", "0px 0px 16px 5px");
    }

    @PostConstruct
    private void construct()
    {

        //title
        this.gainsPositionsTitleVL = new GainsPositionsTitleVL("Positions");
        this.add(this.gainsPositionsTitleVL);

        //grid
        this.add(this.gainsPositionsGridVL);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.gainsPositionsGridVL.getData();
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
    }
}
