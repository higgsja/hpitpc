package com.hpi.tpc.ui.views.notes.notesEdit;

import com.hpi.tpc.ui.views.notes.notesAddEdit.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;

/**
 * makes direct request for data from model
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class NotesEditFormVL
    extends NotesAddEditFormAbstractVL
{

//    @Autowired private NotesModel notesModel;

    public NotesEditFormVL()
    {
        this.addClassName("notesAddFormVL");
    }
    
    @PostConstruct
    private void construct(){
        super.buildForm("Edit a note ...");
        
        this.getControlsHL().getButtonAddArchive().setEnabled(false);
        this.getControlsHL().getButtonAddSave().setEnabled(false);
    }
}
