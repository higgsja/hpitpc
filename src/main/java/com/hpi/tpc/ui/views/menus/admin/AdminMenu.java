package com.hpi.tpc.ui.views.menus.admin;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

@UIScope
@VaadinSessionScope
@Route(value = ROUTE_ADMIN, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_ADMIN)
@org.springframework.stereotype.Component
@NoArgsConstructor
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
public class AdminMenu
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private AdminModel adminModel;

    @PostConstruct
    private void construct()
    {
        //hit
        this.setClassName("adminMenu");

        //get any preferences associated with this feature
        this.adminModel.getPrefs("Admin");

        this.addMenuBarTabs();
    }

    /*
     * create the tabs and listeners; add to the menuBar
     */
    @Override
    public void addMenuBarTabs()
    {
        MenuItem menuItem;
        ArrayList<SubMenu> subMenus;

        subMenus = new ArrayList<>();

        //top tabs
        final String[][] selectTab =
        {
            //title, nav target
            {
                TITLE_TAB_ADMIN_DOWNLOAD, ROUTE_ADMIN_DOWNLOAD_VIEW
            }
        };

        //top tab submenu for download; title, nav target, enabled
        final String[][] subDownloadSelect =
        {
            {
                TITLE_TAB_ADMIN_DOWNLOAD_OFXHOME, ROUTE_ADMIN_DOWNLOAD_OFXHOME_CONTROLLER, "yes"
            },
            {
                TITLE_TAB_ADMIN_DOWNLOAD_FINVIZ, ROUTE_ADMIN_DOWNLOAD_FINVIZ_CONTROLLER, "yes"
            },
            {
                TITLE_TAB_ADMIN_DOWNLOAD_OPTIONHISTORY, ROUTE_ADMIN_DOWNLOAD_OPTIONHISTORY_VIEW, "no"
            },
            {
                TITLE_TAB_ADMIN_DOWNLOAD_IEX, ROUTE_ADMIN_DOWNLOAD_IEX_VIEW, "no"
            }
        };

        //add top tabs
        //todo: eliminate refresh data: add that functionality to finViz
        //then, there will only be download
        for (String[] selectTab1 : selectTab)
        {
            menuItem = menuBar.addItem(selectTab1[0]);
            subMenus.add(menuItem.getSubMenu());

            //no listeners on the top tabs
//            this.listeners.add(menuItem.addClickListener(
//                new ComponentEventListener<ClickEvent<MenuItem>>() {
//                @Override
//                public void onComponentEvent(ClickEvent<MenuItem> event) {
//                    UI.getCurrent().navigate(selectTab1[1]);
//                }
//            }));
        }

        //add submenu
        //download
//        for (String[] selectTab1 : subDownloadSelect)
//        {
//            MenuItem subItem = subMenus.get(0).addItem(selectTab1[0]);
//            subItem.setEnabled(selectTab1[2].equalsIgnoreCase("yes"));
//
//            subItem.addClickListener(
//                new ComponentEventListener<ClickEvent<MenuItem>>()
//            {
//                @Override
//                public void onComponentEvent(ClickEvent<MenuItem> event)
//                {
//                    UI.getCurrent().navigate(selectTab1[1]);
//                }
//            });
//        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
//        if (this.getPrefsController().getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
//        {
//            this.getMainLayout().setDrawerOpened(false);
//        }

        super.beforeEnter(bee);
        //refresh navBar for this view
        super.doNavBar(ROUTE_ADMIN_PREFERENCES);

        //send to the controller
//        bee.forwardTo(FinVizMVCController.class);
        bee.forwardTo(ROUTE_ADMIN_VIEW);
    }
}
