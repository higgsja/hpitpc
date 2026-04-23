package com.hpi.tpc.ui.views.notes.notesAdd;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.hpi.tpc.ui.views.notes.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
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
@Route(value = ROUTE_NOTES_CONTROLLER_ADD, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_NOTES + ": " + TITLE_PAGE_NOTES_ADD)
@org.springframework.stereotype.Component
@PermitAll
public class NotesAddControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private NotesModel notesModel;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private NotesAddFormVL notesAddFormVL;

    public NotesAddControllerFL()
    {
        this.addClassName("notesAddControllerFL");
        this.setAlignItems(Alignment.STRETCH);
        this.setWidth("100%");
        this.setHeight("100%");
    }

    @PostConstruct
    private void construct()
    {
        this.add(this.notesAddFormVL);

        this.addListeners();
    }

    private void addListeners()
    {
        this.notesAddFormVL.controlsHL.getButtonAddSave().addClickListener((ClickEvent<Button> e) ->
        {
            //set this so no error when clearing the ticker after a save
            this.notesModel.setIsSave(true);
            this.notesModel.saveAdd();
            //todo: how do you know the save succeeded?
            this.notesAddFormVL.controlsHL.getButtonAddSave().setEnabled(false);
            this.notesAddFormVL.ticker.focus();
            this.notesAddFormVL.ticker.setValue("");
            this.notesModel.setIsSave(false);
        });

        this.notesAddFormVL.controlsHL.getButtonAddCancel().addClickListener(e ->
        {
            //todo: return to correct list
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_MINE);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //log feature use
        this.serviceTPC.AppTracking("TPC:Notes:Add:Controller");
        
        this.notesModel.setIsAdd(true);

        //starter note template
        this.notesModel.setNoteModel(NoteModel.builder()
            .joomlaId(SecurityUtils.getUserId().toString())
            .action("Buy")
            .units(100.0)
            .triggerType("Price")
            .trigger("")
            .active("1")
            .build());

        this.notesModel.getBinder().setBean(this.notesModel.getNoteModel());

        //allow changes for Add
        this.notesAddFormVL.ticker.setEnabled(true);
//        this.notesAddFormVL.price.setEnabled(true);
        this.notesAddFormVL.description.setEnabled(false);

        //initial button settings upon entry
//        this.notesAddFormVL.getControlsHL().getButtonAddSave().setVisible(true);
        this.notesAddFormVL.controlsHL.getButtonAddSave().setEnabled(false);

//        this.notesAddFormVL.controlsHL.getButtonAddCancel().setVisible(true);
        this.notesAddFormVL.controlsHL.getButtonAddCancel().setEnabled(true);

        this.notesAddFormVL.controlsHL.getButtonAddArchive().setVisible(false);
//        this.notesAddFormVL.controlsHL.getButtonAddArchive().setEnabled(false);

        //refresh gear (none)
        super.updateNavBarGear(null);
    }

    @Override
    public void addMenuBarTabs()
    {
        //not changing them
    }
}
