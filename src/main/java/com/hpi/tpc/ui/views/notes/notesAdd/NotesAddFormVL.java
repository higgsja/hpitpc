package com.hpi.tpc.ui.views.notes.notesAdd;

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
public class NotesAddFormVL
    extends NotesAddEditFormAbstractVL
{

//    @Autowired private NotesModel notesModel;
    public NotesAddFormVL()
    {
        this.addClassName("notesEditFormVL");
    }

    @PostConstruct
    private void construct()
    {
        super.buildForm("Add a note ...");
    }

    @Override
    public void doLayout()
    {
        super.doLayout();
        
//        this.getControlsHL().getButtonAddArchive().setEnabled(false);
//        this.getControlsHL().getButtonAddArchive().setVisible(false);
    }
}
