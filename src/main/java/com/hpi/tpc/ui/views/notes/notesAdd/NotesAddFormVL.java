package com.hpi.tpc.ui.views.notes.notesAdd;

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
public class NotesAddFormVL
    extends NotesAddEditFormAbstractVL
{

//    @Autowired private NotesModel notesModel;
    public NotesAddFormVL()
    {
        this.addClassName("notesAddFormVL");
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

        this.notesModel.getBinder()
            .forField(this.ticker)
            .withValidator(e ->
            {
                List<NoteQuoteModel> quotes = this.notesModel.getTickerInfo(this.ticker.getValue());

                if (quotes == null)
                {
                    this.controlsHL.getButtonAddSave().setEnabled(false);
                    return false;
                }

                this.iPrice.setValue(quotes.get(0).getClose());
                this.description.setValue(quotes.get(0).getCompany());

                this.controlsHL.getButtonAddSave().setEnabled(true);

                return true;
            }, "Invalid", ErrorLevel.ERROR)
            .bind(NoteModel::getTicker, NoteModel::setTicker);

    }
}
