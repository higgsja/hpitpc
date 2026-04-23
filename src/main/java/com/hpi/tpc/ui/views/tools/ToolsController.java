package com.hpi.tpc.ui.views.tools;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.hpi.tpc.ui.views.tools.hedge.*;
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
@Route(value = ROUTE_TOOLS, layout = MainLayout.class)
@PermitAll
/**
 * Tools layout, the playing field for the rest of the tools gui
 */
public class ToolsController
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private ToolsModel toolsModel;
    
    @PostConstruct
    private void construct(){
        this.setClassName("toolsController");

        this.toolsModel.getPrefs("Tools");

        this.addMenuBarTabs();        
    }

    /**
     * create the tabs and listeners; add to the menuBar
     */
    @Override
    public void addMenuBarTabs()
    {
        final String[][] selectTab =
        {
            //title, @Route string nav target
            {
                TITLE_TAB_HEDGE, ROUTE_TOOLS_HEDGE_PORTFOLIO
            }
        };

        for (String[] selectTab1 : selectTab)
        {
            this.menuBar.addItem(selectTab1[0], (ClickEvent<MenuItem> event) ->
            {
                UI.getCurrent().navigate(selectTab1[1]);
            });

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //refresh navBar for this view
        this.doNavBar(ROUTE_TOOLS_PREFERENCES);

        //navigate instead?
        bee.forwardTo(HedgePortfolioView.class);
    }
}
