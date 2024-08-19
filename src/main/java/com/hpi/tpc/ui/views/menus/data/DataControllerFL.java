package com.hpi.tpc.ui.views.menus.data;

import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.hpi.tpc.ui.views.menus.data.equities.stocks.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

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
@Route(value = ROUTE_DATA, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_DATA)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PermitAll
public class DataControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

//    @Autowired private MainLayout mainLayout;
    
    @PostConstruct
    private void construct()
    {
        this.addClassName("dataControllerFL");
        
        this.addMenuBarTabs();
    }

    /*
     * create top menuBar tabs and listeners
     */
    @Override
    public void addMenuBarTabs()
    {
        //Validate
        MenuItem validateItem = this.menuBar.addItem(TAB_DATA_VALIDATE_TITLE);
        SubMenu validateSubMenu = validateItem.getSubMenu();

        //validate options
        MenuItem validateOptions = validateSubMenu.addItem(TAB_DATA_VALIDATE_OPTIONS_TITLE);
        validateOptions.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_DATA_VALIDATE_OPTIONS_CONTROLLER);
        });

        //validate stocks
        MenuItem validateStocks = validateSubMenu.addItem(TAB_DATA_VALIDATE_STOCKS_TITLE);
        validateStocks.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_DATA_VALIDATE_STOCKS_CONTROLLER);
        });

        MenuItem equitiesItem = this.menuBar.addItem(TAB_DATA_EQUITIES_TITLE);
        SubMenu equitiesSubMenu = equitiesItem.getSubMenu();

        //show options
        MenuItem showOptions = equitiesSubMenu.addItem(TAB_DATA_EQUITIES_OPTIONS_TITLE);
        showOptions.setEnabled(false);
        showOptions.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_DATA_EQUITIES_OPTIONS);
        });

        //show stocks
        MenuItem showStocks = equitiesSubMenu.addItem(TAB_DATA_EQUITIES_STOCKS_TITLE);
        showStocks.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_DATA_EQUITIES_STOCKS_CONTROLLER);
        });

        MenuItem infoItem = this.menuBar.addItem(TAB_DATA_INFO);

        infoItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_DATA_INFO);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //set navBar for this menu
        super.doNavBar(null);

        //send to default view
        bee.forwardTo(DataEquitiesStocksControllerFL.class);
    }
}
