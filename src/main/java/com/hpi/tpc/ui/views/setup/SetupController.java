package com.hpi.tpc.ui.views.setup;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.hpi.tpc.ui.views.setup.equities.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Route(value = ROUTE_SETUP, layout = MainLayout.class)
@PermitAll
/*
 * Controller: Interface between Model and View to process business logic and incoming
 * requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 * Target for navigation from appDrawer
 */
public class SetupController
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private SetupModel setupModel;

    @PostConstruct
    private void construct()
    {
        this.setClassName("setupController");

        //get any preferences
        this.setupModel.getPrefs("Setup");

        //create top menu tabs
        this.addMenuBarTabs();
    }

    /*
     * create the tabs and listeners; add to the menuBar
     */
    @Override
    public void addMenuBarTabs()
    {

        final String[][] selectTab =
        {
            //title, @Route string nav target
            {
                TITLE_TAB_SETUP_EQUITIES, SETUP_EQUITIES_VIEW
            },
            {
                TITLE_TAB_SETUP_SECTORS, SETUP_SECTORS_VIEW
            },
            {
                TITLE_TAB_SETUP_ACCOUNTS, ROUTE_SETUP_ACCOUNTS_VIEW
            }
        };

        for (String[] selectTab1 : selectTab)
        {
            menuBar.addItem(selectTab1[0], (ClickEvent<MenuItem> event) ->
            {
                UI.getCurrent().navigate(selectTab1[1]);
            });

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //set navBar for this menu
        this.doNavBar(ROUTE_SETUP_PREFERENCES);

        //send to default view
        bee.forwardTo(SetupEquitiesView.class);
    }
}
