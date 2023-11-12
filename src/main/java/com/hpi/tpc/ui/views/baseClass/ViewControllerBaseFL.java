package com.hpi.tpc.ui.views.baseClass;

import com.hpi.tpc.prefs.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.menubar.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/*
 * Abstract View Controller: 
 * common methods and fields for View Controllers
 */
@Component
public abstract class ViewControllerBaseFL
    extends FlexLayout
    implements BeforeEnterObserver
{

    @Autowired private MainLayout mainLayout;
    @Autowired private PrefsController prefsController;

    public MenuBar menuBar;
    public HorizontalLayout prefsPageHL;

    public ViewControllerBaseFL()
    {
        this.setClassName("viewControllerBaseFL");
        this.setFlexDirection(FlexDirection.ROW);
        this.setFlexWrap(FlexWrap.WRAP);
        this.menuBar = new MenuBar();
        this.menuBar.setOpenOnHover(true);

        this.setSizeFull();

        this.prefsPageHL = null;
    }

    /**
     * establish top navBar menu
     *
     * @param prefsPage: Route to preferences page
     */
    public void doNavBar(String prefsPage)
    {
        if (prefsPage != null)
        {
            this.prefsPageHL = this.createPreferencesTabHL(prefsPage);
        }

        //set the top menu        
        this.mainLayout.doNavBar(this.menuBar, prefsPageHL);
    }

    public void updateNavBarGear(String prefsPage)
    {
        if (prefsPage != null)
        {
            this.prefsPageHL = this.createPreferencesTabHL(prefsPage);
        }

        //update the top menu        
        this.mainLayout.updatePagePrefsHL(prefsPageHL);
    }

    /**
     * create top menuBar tabs and listeners
     */
    public abstract void addMenuBarTabs();

    /*
     * create the preferences icon and listener for the top menuBar
     */
    public HorizontalLayout createPreferencesTabHL(String prefsPage)
    {
        //hit
        Icon prefsIcon;
        HorizontalLayout prefsHorizontalLayout;

        prefsHorizontalLayout = new HorizontalLayout();
        prefsHorizontalLayout.setWidth("100%");
        prefsHorizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        prefsHorizontalLayout.setClassName("prefsIcon");

        if (prefsPage == null)
        {
            return prefsHorizontalLayout;
        }

        prefsIcon = new Icon(VaadinIcon.COG);
        prefsIcon.setColor("#169FF3");
        prefsHorizontalLayout.add(prefsIcon);

        //add listener to prefs page
        prefsIcon.addClickListener((ClickEvent<Icon> t) ->
        {
            UI.getCurrent().navigate(prefsPage);
        });

        return prefsHorizontalLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        if (this.prefsController.getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
        {
            this.mainLayout.setDrawerOpened(false);
        }
    }

    public final NativeLabel titleFormat(String title)
    {
        NativeLabel label = new NativeLabel(title);
        label.getElement().getStyle().set("font-size", "14px");
        label.getElement().getStyle().set("font-family", "Arial");
//        label.getElement().getStyle().set("color", "#169FF3");
        label.getElement().getStyle().set("margin-top", "0px");
        label.getElement().getStyle().set("margin-bottom", "0px");
        label.getElement().getStyle().set("margin-block-start", "0px");
        label.getElement().getStyle().set("margin-block-end", "0px");
        label.getElement().getStyle().set("line-height", "1em");

        return label;
    }
}
