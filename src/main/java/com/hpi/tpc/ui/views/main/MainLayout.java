package com.hpi.tpc.ui.views.main;

import com.hpi.tpc.ui.views.notes.NotesModel;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.prefs.*;
import com.hpi.tpc.services.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.menubar.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.dom.*;
import com.vaadin.flow.spring.annotation.*;
import com.vaadin.flow.theme.lumo.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Provides the primary layout for the app
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@CssImport(value = "./styles/vaadin-app-layout.css", themeFor = "vaadin-app-layout")
public class MainLayout
    extends AppLayout
{

    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private MainDrawer mainDrawer;
    @Autowired private NotesModel notesModel;
    @Autowired private PrefsController prefsController;

    //globals
    @Getter private List<ClientSectorModel> clientAllSectorListModels;
    @Getter private List<ClientSectorModel> clientActiveSectorListModels;
    @Getter private HashMap<Integer, String> clientAllSectorHashMap;
    @Getter private HashMap<Integer, Double> clientSectorIdTgtPctHashMap;
    //clientEquityAttributes does not change often
    @Getter private List<ClientEquityModel> clientEquityAttributesModels;
    //top menu

    private final Icon noteIcon;
//    @Getter private MenuBar menuBar;
//    private HorizontalLayout pagePrefsHL;

    /**
     * Constructor
     */
    public MainLayout()
    {
        this.noteIcon = this.noteIcon();
    }

    @PostConstruct
    private void construct()
    {
        prefsController.readPrefsByPrefix("TPC");

        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK))
        {
        } else
        {
            themeList.add(Lumo.DARK);
        }

        this.getClientSectorLists();

        this.getClientEquityAttributesList();

        //construct the drawer
        this.addToDrawer(this.mainDrawer.getMainMenuTabs());
        this.mainDrawer.getMainMenuTabs().addSelectedChangeListener(event ->
        {
            UI.getCurrent().navigate(((MyTab) event.getSelectedTab()).getNavTargetTitle());
        });
        this.setPrimarySection(Section.DRAWER);

        //construct the navBar
        this.addToNavbar(true, new DrawerToggle(), this.noteIcon());
    }

    private Icon noteIcon()
    {
        Icon pencilIcon;

        pencilIcon = new Icon(VaadinIcon.PENCIL);
        pencilIcon.setColor("#169FF3");

        //add listener for new note
        pencilIcon.addClickListener(event ->
        {
//            this.notesModel.setIsAdd(true);
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_ADD);
        });

        return pencilIcon;
    }

    public void doNavBar(MenuBar menuBar, HorizontalLayout prefsPageHL)
    {
        this.removeNavBar();

        this.addToNavbar(true, new DrawerToggle(), this.noteIcon, menuBar);

        if (prefsPageHL != null)
        {
            this.addToNavbar(true, prefsPageHL);
        }
    }

    public void updatePagePrefsHL(HorizontalLayout prefsPageHL)
    {
        this.removePagePrefsHL();
        if (prefsPageHL != null)
        {
            this.addToNavbar(true, prefsPageHL);
        }
    }

    /**
     * Removes the existing navBar
     */
    public void removeNavBar()
    {
        Element mainLayoutElement;
        //get array of all elements in the layout; process those with attribute of 'slot'
        for (Object c : this.getElement().getChildren().toArray())
        {
            mainLayoutElement = (Element) c;
            //only want slot elements
            if (mainLayoutElement.getAttribute("slot") != null
                //and those that are 'navbar'
                && (mainLayoutElement.getAttribute("slot").equalsIgnoreCase("navbar")
                //or 'navbar touch-optimized'
                || mainLayoutElement.getAttribute("slot").equalsIgnoreCase("navbar touch-optimized")))
            {
                //then remove them from the navBar
                //should not really work: removing from array in the for statement
                this.getElement().removeChild((Element) c);
            }
        }
    }

    /**
     * Removes the existing gear icon if it is in the navBar
     */
    public void removePagePrefsHL()
    {
        Element mainLayoutElement;
        Element removeElement = null;

        //get array of all elements in the layout; process those with attribute of 'slot'
        for (Object c : this.getElement().getChildren().toArray())
        {
            mainLayoutElement = (Element) c;
            //only want slot elements
            if (mainLayoutElement.getAttribute("slot") != null
                //and those that are 'navbar'
                && (mainLayoutElement.getAttribute("slot").equalsIgnoreCase("navbar")
                //or 'navbar touch-optimized'
                || mainLayoutElement.getAttribute("slot").equalsIgnoreCase("navbar touch-optimized")))
            {
                //then remove them from the navBar
                if (mainLayoutElement.getAttribute("class") != null
                    && mainLayoutElement.getAttribute("class").equalsIgnoreCase("prefsicon"))
                {
                    removeElement = mainLayoutElement;
                }
            }
        }

        if (removeElement != null)
        {
            this.getElement().removeChild(removeElement);
        }
    }

    public void getClientSectorLists()
    {
        //query data from backend
        this.clientAllSectorListModels = this.serviceTPC.getClientSectorModels(
            ClientSectorModel.SQL_SELECT_ALL_SECTORS_BY_NAME);

        //put in the hashMaps
        this.clientAllSectorHashMap = new HashMap<>();
        this.clientSectorIdTgtPctHashMap = new HashMap<>();

        for (ClientSectorModel csm : this.clientAllSectorListModels)
        {
            this.clientAllSectorHashMap.put(csm.getClientSectorId(), csm.getCSecShort());
            this.clientSectorIdTgtPctHashMap.put(csm.getClientSectorId(), csm.getTgtPct());
        }

        //query data from backend
        this.clientActiveSectorListModels = this.serviceTPC.getClientSectorModels(
            ClientSectorModel.SQL_SELECT_ALL_ACTIVE_SECTORS_BY_NAME);

        //put in the hashMap
        this.clientAllSectorHashMap = new HashMap<>();

        for (ClientSectorModel csm : this.clientAllSectorListModels)
        {
            this.clientAllSectorHashMap.put(csm.getClientSectorId(), csm.getCSecShort());
        }
    }

    public void getClientEquityAttributesList()
    {
        //query clientEquityAttributes data from backend
        //need to pull in equityInfo.Company
        this.clientEquityAttributesModels = this.serviceTPC.getClientEquityAttributesModels();
    }
}
