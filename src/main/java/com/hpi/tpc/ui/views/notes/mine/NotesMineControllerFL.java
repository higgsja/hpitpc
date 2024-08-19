package com.hpi.tpc.ui.views.notes.mine;

import com.hpi.tpc.ui.views.notes.NotesModel;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
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
@Route(value = ROUTE_NOTES_CONTROLLER_MINE, layout = MainLayout.class)
@org.springframework.stereotype.Component
@PermitAll
public class NotesMineControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private NotesMineVL notesMineVL;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private NotesModel notesModel;

    public NotesMineControllerFL()
    {
        this.addClassName("notesMineControllerFL");
    }

    @PostConstruct
    public void construct()
    {
        this.notesModel.getPrefs("NotesMine");

        this.add(this.notesMineVL);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //log feature use
        this.serviceTPC.AppTracking("TPC:Notes:Mine:Controller");

        //grid layout may change based on preferences change; refresh on every entry
        //none for now
        //this.notesMineVL.doLayout(this.notesModel.getStringColumns());
        //set listeners on new layout if necessary
        //data may change; update data on every entry
        this.notesModel.getData("mine");

        //set data grid to display data provider data
        this.notesMineVL.getNotesGrid().setItems(this.notesModel.getDataProvider());
        

        //refresh gear (none)
        super.updateNavBarGear(null);
    }

    @Override
    public void addMenuBarTabs()
    {
        //not changing them
    }
}
