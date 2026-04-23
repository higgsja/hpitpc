package com.hpi.tpc.ui.views.notes.notesAddEdit;

import com.hpi.tpc.ui.views.notes.NotesAddEditFormTitleVL;
import com.hpi.tpc.ui.views.notes.NotesAddEditControlsHL;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.notes.*;
//import com.studerw.tda.model.quote.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.value.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

/**
 * makes direct request for data from model
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public abstract class NotesAddEditFormAbstractVL
    extends ViewBaseVL
    implements BeforeEnterObserver
{

    @Autowired public NotesModel notesModel;

    //must be the same as the data model fields
    public final TextField ticker;
    public final NumberField iPrice;
    public final TextField description;
    public final NumberField units;
    public final ComboBox<String> action;
    public final ComboBox<String> triggerType;
    public final TextField alert;
    public final TextArea notes;

    public final NotesAddEditControlsHL controlsHL;

//    private Boolean inPrice = false;

    public NotesAddEditFormAbstractVL()
    {
        this.setClassName("notesAddEditFormAbstractVL");
        this.setMaxWidth("100vw");
        this.setMaxHeight("100vh");

        this.ticker = new TextField();
        this.ticker.setRequiredIndicatorVisible(true);
        this.ticker.setAutofocus(true);
        this.ticker.setAutoselect(true);

        this.iPrice = new NumberField();
        this.iPrice.setRequiredIndicatorVisible(true);

        this.description = new TextField();

        this.units = new NumberField();
        this.units.setRequiredIndicatorVisible(true);

        this.alert = new TextField();
        this.notes = new TextArea();

        this.action = new ComboBox<>("", "Buy", "Sell", "Hold",
            "Watch", "Hedge", "Other");

        this.triggerType = new ComboBox<>("", "Date", "Price", "Other");

        this.controlsHL = new NotesAddEditControlsHL();
    }

    private Boolean checkRequired()
    {
        return true;
//        if (!this.ticker.getValue().isEmpty()
//            && !this.notesModel.getQuote().getSymbol().isEmpty()
//            && !this.notesModel.getQuote().getDescription().isEmpty())
//        {
//            if (this.notesModel.getQuote() instanceof ForexQuote)
//            {
//                if (((ForexQuote) (this.notesModel.getQuote())).getLastPriceInDouble().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof FutureOptionQuote)
//            {
//                if (((FutureOptionQuote) (this.notesModel.getQuote())).getLastPriceInDouble().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof FutureQuote)
//            {
//                if (((FutureQuote) (this.notesModel.getQuote())).getLastPriceInDouble().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof OptionQuote)
//            {
//                if (((OptionQuote) (this.notesModel.getQuote())).getLastPrice().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof EquityQuote)
//            {
//                if (((EquityQuote) (this.notesModel.getQuote())).getLastPrice().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof EtfQuote)
//            {
//                if (((EtfQuote) (this.notesModel.getQuote())).getLastPrice().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//
//            if (this.notesModel.getQuote() instanceof MutualFundQuote)
//            {
//                if (((MutualFundQuote) (this.notesModel.getQuote())).getClosePrice().doubleValue() > 0.0)
//                {
//                    return true;
//                }
//            }
//        }
//
//        return false;
    }

    public void doLayout()
    {
        HorizontalLayout h1, h3, h4, h5;

        this.ticker.setPlaceholder("Ticker*");
        this.ticker.setMaxWidth("180px");
        this.ticker.setValueChangeMode(ValueChangeMode.ON_CHANGE);

        this.action.setMaxWidth("100px");
        this.notesModel.getBinder()
            .forField(this.action)
            .bind(NoteModel::getAction, NoteModel::setAction);

        this.triggerType.setMaxWidth("100px");
        this.notesModel.getBinder()
            .forField(this.triggerType)
            .bind(NoteModel::getTriggerType, NoteModel::setTriggerType);

        this.alert.setPlaceholder("Alert");
        this.alert.setMaxWidth("180px");
        this.notesModel.getBinder()
            .forField(this.alert)
            .bind(NoteModel::getTrigger, NoteModel::setTrigger);

        this.description.setPlaceholder("Description*");
        this.description.setMinWidth("297px");
        this.notesModel.getBinder()
            .forField(this.description)
//            .withValidator(e ->
//            {
//                if (this.description.getValue().isEmpty())
//                {
//                    this.controlsHL.getButtonAddSave().setEnabled(false);
//                    return false;
//                } else
//                {
//                    if (this.checkRequired())
//                    {
//                        this.controlsHL.getButtonAddSave().setEnabled(true);
//                        return true;
//                    }
//                }
//                return false;
//            }, "Invalid", ErrorLevel.ERROR)
            .bind(NoteModel::getDescription, NoteModel::setDescription);

        this.notes.setPlaceholder("Enter notes");
        this.notes.setMinHeight("90px");
        this.notes.setMaxHeight("250px");
        this.notes.setVisible(true);
        this.notes.setMinWidth("297px");
        this.notesModel.getBinder()
            .forField(this.notes)
            .bind(NoteModel::getNotes, NoteModel::setNotes);

        this.units.setPlaceholder("Units*");
        this.units.setMaxWidth("140px");
        this.notesModel.getBinder()
            .forField(this.units)
//            .withValidator(e ->
//            {
//                if (this.units.getValue() == null || this.units.getValue() < 1.0)
//                {
//                    this.controlsHL.getButtonAddSave().setEnabled(false);
//                    return false;
//                } else
//                {
//                    if (this.checkRequired())
//                    {
//                        this.controlsHL.getButtonAddSave().setEnabled(true);
//                        return true;
//                    }
//                }
//
//                return false;
//            }, "Invalid", ErrorLevel.ERROR)
            .bind(NoteModel::getUnits, NoteModel::setUnits);

        this.iPrice.setPlaceholder("Price*");
        this.iPrice.setMaxWidth("140px");
        this.notesModel.getBinder()
            .forField(this.iPrice)
            //            .asRequired()
            //            .withNullRepresentation(0.0)
//            .withValidator(e ->
//            {
//                if (this.iPrice.getValue() == null || this.iPrice.getValue() < 1.0)
//                {
//                    this.controlsHL.getButtonAddSave().setEnabled(false);
//                    return false;
//                } else
//                {
//                    if (this.checkRequired())
//                    {
//                        this.controlsHL.getButtonAddSave().setEnabled(true);
//                        return true;
//                    }
//                }
//
//                return false;
//            }, "Invalid", ErrorLevel.ERROR)
            .bind(NoteModel::getIPrice, NoteModel::setIPrice);

        //title
//        this.add(new NotesAddFormTitleVL("Add a note ..."));
        //first row
        h1 = new HorizontalLayout(this.ticker, this.action);
        //second row
//        h2 = new HorizontalLayout(this.notes);
        //third row
        h3 = new HorizontalLayout(this.units, this.iPrice);
        //fourth row
        h4 = new HorizontalLayout(this.triggerType, this.alert);
        //fifth row
        h5 = new HorizontalLayout(this.description);

        this.add(h1, this.notes, h3, h4, h5);

        this.add(this.controlsHL);
    }

    public void buildForm(String formTitle)
    {
        //title
        this.add(new NotesAddEditFormTitleVL(formTitle));

        this.doLayout();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //initial button settings upon entry
////        this.controlsHL.getButtonAddSave().setVisible(true);
//        this.controlsHL.getButtonAddSave().setEnabled(false);
//
////        this.controlsHL.getButtonAddCancel().setVisible(true);
//        this.controlsHL.getButtonAddCancel().setEnabled(true);
//        this.controlsHL.getButtonAddCancel().setText("Close");
//
////        this.controlsHL.getButtonAddArchive().setVisible(true);
//        this.controlsHL.getButtonAddArchive().setEnabled(true);
    }
}
