package com.hpi.tpc.ui.views.notes.notesEdit;

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
@Route(value = ROUTE_NOTES_CONTROLLER_EDIT, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_NOTES + ": " + TITLE_PAGE_NOTES_ADD)
@org.springframework.stereotype.Component
@PermitAll
public class NotesEditControllerFL
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private NotesModel notesModel;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private NotesEditFormVL notesEditFormVL;

    public NotesEditControllerFL()
    {
        this.addClassName("notesAddControllerFL");
        this.setAlignItems(Alignment.STRETCH);
        this.setWidth("100%");
        this.setHeight("100%");
    }

    @PostConstruct
    private void construct()
    {
        this.add(this.notesEditFormVL);

        this.addListeners();
    }

    private void validateAndSave()
    {
        if (this.notesModel.getBinder().isValid())
        {
//            fireEvent(new SaveEvent(this, this.notesModel.getBinder().getBean()));
        }
    }

    private void addListeners()
    {
        this.notesEditFormVL.controlsHL.getButtonAddSave().addClickListener(click -> validateAndSave());

        this.notesModel.getBinder().addStatusChangeListener(evt ->
        {
            this.notesEditFormVL.controlsHL
                .getButtonAddSave().setEnabled(this.notesModel.getBinder().isValid());
        });

        this.notesEditFormVL.controlsHL.getButtonAddSave().addClickListener((ClickEvent<Button> e) ->
        {
            //set this so no error when clearing the ticker after a save
//            this.notesModel.setIsSave(true);
            this.notesModel.saveUpdate(false);
            //todo: how do you know the save succeeded?
            //todo: return to correct list
//            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_MINE);
            this.notesEditFormVL.controlsHL.getButtonAddSave().setEnabled(false);
//            this.notesEditFormVL.getTicker().focus();
//            this.notesEditFormVL.getTicker().setValue("");
        });

        this.notesEditFormVL.controlsHL.getButtonAddCancel().addClickListener(e ->
        {
            //todo: return to correct list
            //required to set the ticker to empty
            this.notesModel.getNoteModel().setTicker("");
            this.notesModel.getNoteModel().setDescription("");
            this.notesModel.getNoteModel().setUnits(100.0);
            this.notesModel.getNoteModel().setIPrice(0.0);
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_MINE);
        });

        this.notesEditFormVL.controlsHL.getButtonAddArchive().addClickListener(e ->
        {
            this.notesModel.getNoteModel().setActive(NoteModel.ACTIVE_NO);

            this.notesModel.saveUpdate(true);

            this.notesEditFormVL.controlsHL.getButtonAddSave().setEnabled(false);
            
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_MINE);

        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);
        
        this.notesModel.setIsAdd(false);

        //log feature use
        this.serviceTPC.AppTracking("TPC:Notes:Edit:Controller");

        this.notesModel.getBinder().setBean(this.notesModel.getNoteModel());

        //disallow changes for edit
        this.notesEditFormVL.ticker.setEnabled(false);
        this.notesEditFormVL.iPrice.setEnabled(false);
        this.notesEditFormVL.description.setEnabled(false);

        //initial button settings upon entry
//        this.notesEditFormVL.getControlsHL().getButtonAddSave().setVisible(true);
        this.notesEditFormVL.controlsHL.getButtonAddSave().setEnabled(false);

//        this.notesEditFormVL.controlsHL.getButtonAddCancel().setVisible(true);
        this.notesEditFormVL.controlsHL.getButtonAddCancel().setEnabled(true);

//        this.notesEditFormVL.controlsHL.getButtonAddArchive().setVisible(true);
        this.notesEditFormVL.controlsHL.getButtonAddArchive().setEnabled(true);

        //refresh gear (none)
        super.updateNavBarGear(null);
    }

    @Override
    public void addMenuBarTabs()
    {
        //not changing them
    }
}
