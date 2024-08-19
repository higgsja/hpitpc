package com.hpi.tpc.ui.views.about;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
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
@Route(value = ROUTE_ABOUT, layout = MainLayout.class)
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
public class AboutController
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private AboutModel aboutModel;

    @PostConstruct
    private void construct()
    {
        this.setClassName("aboutController");

        this.aboutModel.getPrefs("About");

        this.addMenuBarTabs();
    }

    /**
     * create the tabs and listeners; add to the menuBar
     *
     */
    @Override
    public void addMenuBarTabs()
    {
        final String[][] selectTab =
        {
            //title, @Route string nav target
            {
                TITLE_TAB_ABOUT_REGISTER, ABOUT_REGISTER_VIEW
            },
            {
                TITLE_TAB_ABOUT_ABOUT, ABOUT_VIEW
            },
            {
                TITLE_TAB_ABOUT_CREDITS, ABOUT_CREDITS_VIEW
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

    /**
     *
     * @param bee
     */
    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        this.doNavBar(ABOUT_VIEW);

        //could not make it work otherwise
        //navigate instead?
        bee.forwardTo(AboutView.class);
    }
}
