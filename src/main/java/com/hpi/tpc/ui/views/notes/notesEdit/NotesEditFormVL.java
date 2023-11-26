package com.hpi.tpc.ui.views.notes.notesEdit;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.notes.notesAddEdit.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import java.util.*;

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
        this.addClassName("notesEditFormVL");
    }
    
    @PostConstruct
    private void construct(){
        super.buildForm("Edit a note ...");
        
//        this.controlsHL.getButtonAddArchive().setEnabled(false);
//        this.controlsHL.getButtonAddSave().setEnabled(false);
    }
    
    @Override
    public void doLayout()
    {
        super.doLayout();
        
        this.notesModel.getBinder()
            .forField(this.ticker)
            .withValidator(e ->
            {
                //on edit, nothing to validate on ticker
                //todo: can be case of editing old enough that the ticker no longer exists
//                List<NoteQuoteModel> quotes = this.notesModel.getTickerInfo(this.ticker.getValue());

//                if (quotes == null)
//                {
//                    this.controlsHL.getButtonAddSave().setEnabled(false);
//                    return false;
//                }

//                this.iPrice.setValue(quotes.get(0).getClose());
//                this.description.setValue(quotes.get(0).getCompany());
//
//                this.controlsHL.getButtonAddSave().setEnabled(true);

                return true;
            }, "Invalid", ErrorLevel.ERROR)
            .bind(NoteModel::getTicker, NoteModel::setTicker);
    }
}
