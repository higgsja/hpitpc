package com.hpi.tpc.ui.views.main;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.app.security.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;
import org.springframework.stereotype.Component;

/**
 *  Provides code for app main drawer
 */
@UIScope
@VaadinSessionScope
@Getter
@Component
public class MainDrawer
{

    @Setter private Tabs mainMenuTabs;
    private final Tab data;
    private final Tab portfolio;
    private final Tab coaching;
    private final Tab tools;
    private final Tab exit;
    private final Tab setup;
    private final Tab notesTab;
    private final Tab admin;
    private final Tab about;

/**
 * Constructor
 */
    public MainDrawer()
    {
        //setup up the drawer tabs
        this.notesTab = new MyTab(ROUTE_NOTES_CONTROLLER,
            myHLayout((Icon) VaadinIcon.NOTEBOOK.create(), mySpan(TITLE_PAGE_NOTES)));
        
        this.data = new MyTab(ROUTE_DATA, myHLayout((Icon) VaadinIcon.DATABASE.create(),
            mySpan(TITLE_PAGE_DATA)));
        
        this.portfolio = new MyTab(ROUTE_PORTFOLIO, myHLayout((Icon) VaadinIcon.PIE_CHART.create(), 
            mySpan(ROUTE_PORTFOLIO)));
        
        this.coaching = new MyTab(ROUTE_COACHING_CONTROLLER, myHLayout(VaadinIcon.DOCTOR.create(), 
            mySpan(ROUTE_COACHING_CONTROLLER)));
        
        this.tools = new MyTab(ROUTE_TOOLS, myHLayout((Icon) VaadinIcon.TOOLBOX.create(),
            mySpan(ROUTE_TOOLS)));
        
        this.exit = new MyTab(ROUTE_EXIT, myHLayout((Icon) VaadinIcon.EXIT.create(), mySpan(ROUTE_EXIT)));
        
        this.setup = new MyTab(ROUTE_SETUP, myHLayout((Icon) VaadinIcon.TOOLS.create(), mySpan(ROUTE_SETUP)));
        
        this.admin = new MyTab(ROUTE_ADMIN, myHLayout((Icon) VaadinIcon.PUZZLE_PIECE.create(), 
            mySpan(ROUTE_ADMIN)));
        
        this.about = new MyTab(ROUTE_ABOUT, myHLayout((Icon) VaadinIcon.INFO_CIRCLE_O.create(), 
            mySpan(ROUTE_ABOUT)));

        this.mainMenuTabs = new Tabs();
        this.mainMenuTabs.setOrientation(Tabs.Orientation.VERTICAL);
        this.mainMenuTabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        this.mainMenuTabs.setId("drawerTabsID");
        this.mainMenuTabs.setClassName("drawerTabs");
        this.mainMenuTabs.add(this.notesTab, this.portfolio, this.coaching,
            this.tools, this.data, this.setup, this.about, this.exit);

        if (SecurityUtils.getUserId() != null
            && SecurityUtils.getUserId() == 816)
        {
//            this.mainMenuTabs.add(new Hr());
            this.mainMenuTabs.add(this.admin);
        }
    }

    private Span mySpan(String text)
    {
        Span aSpan;

        aSpan = new Span(text);
        aSpan.getStyle().set("font-family", "Arial");//may need face

        return aSpan;
    }

    private HorizontalLayout myHLayout(Icon anIcon, Span aSpan)
    {
        HorizontalLayout aLayout;

        aLayout = new HorizontalLayout(anIcon, aSpan);
        aLayout.getStyle().set("alignItems", "left");

        return aLayout;
    }
}
