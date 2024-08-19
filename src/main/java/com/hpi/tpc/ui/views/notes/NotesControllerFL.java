package com.hpi.tpc.ui.views.notes;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.hpi.tpc.ui.views.notes.mine.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.*;
import com.vaadin.flow.component.html.*;
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
@Route(value = ROUTE_NOTES_CONTROLLER, layout = MainLayout.class)
@RouteAlias(value = HOME_URL, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_NOTES)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PermitAll
public class NotesControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private NotesModel notesModel;
//    @Autowired private TPCDAOImpl noteService;
    
    @PostConstruct
    private void construct()
    {
        this.addClassName("notesControllerFL");
        
        this.addMenuBarTabs();
    }

    /*
     * create top menuBar tabs and listeners
     */
    @Override
    public void addMenuBarTabs()
    {
        MenuItem notesItem = this.menuBar.addItem(TAB_NOTES_VIEW_TITLE);
        SubMenu notesSubMenu = notesItem.getSubMenu();

        MenuItem notesAdd = notesSubMenu.addItem(TAB_NOTES_VIEW_ADD_TITLE);

        notesSubMenu.add(new Hr());
        MenuItem notesMineItem = notesSubMenu.addItem(TAB_NOTES_VIEW_MINE_TITLE);
        MenuItem notesAllItem = notesSubMenu.addItem(TAB_NOTES_VIEW_ALL_TITLE);

        notesSubMenu.add(new Hr());
        MenuItem notesArchivedItem = notesSubMenu.addItem(TAB_NOTES_VIEW_ARCHIVED_TITLE);

        notesAdd.addClickListener((ClickEvent<MenuItem> event) ->
        {
//            this.notesModel.setIsAdd(true);
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_ADD);
        });

        notesMineItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_MINE);
        });

        notesAllItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_ALL);
        });

        notesArchivedItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_ARCHIVED);
        });

        MenuItem infoItem = this.menuBar.addItem(TAB_NOTES_INFO);

        infoItem.addClickListener((ClickEvent<MenuItem> event) ->
        {
            UI.getCurrent().navigate(ROUTE_NOTES_INFO);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);
        
        //log feature use
        this.notesModel.serviceTPC.AppTracking("TPC:Notes:Mine:Controller");

        //set navBar for this menu
        super.doNavBar(null);

        //send to default view
        bee.forwardTo(NotesMineControllerFL.class);
    }
}
