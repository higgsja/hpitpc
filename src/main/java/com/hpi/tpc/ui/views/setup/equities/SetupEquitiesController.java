package com.hpi.tpc.ui.views.setup.equities;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;

import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;

/*
 * Interface between Model and View to process business logic and incoming requests:
 * manipulate data using the Model
 * interact with the Views to render output
 * respond to user input and performance actions on data model objects.
 * receives input, optionally validates it and passes it to the model
 * Also establishes navBar tabs
 */
@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class SetupEquitiesController
    extends VerticalLayout {
    @Autowired private MainLayout mainLayout;
    @Autowired private SetupEquitiesEditView setupEquitiesEditView;
    @Autowired private SetupEquitiesModel setupEquitiesModel;
    @Autowired private SetupEquitiesView setupEquitiesMVCView;

    private final ArrayList<Registration> listeners;
    private final ArrayList<Registration> editListeners;
    private Registration gridDataProviderListener;

    public SetupEquitiesController() {
        this.listeners = new ArrayList<>();
        this.editListeners = new ArrayList<>();
    }

    @PostConstruct
    private void construct() {
        //get initial data
        this.getData();

        //setup listeners
        this.doListeners();
    }

    @PreDestroy
    private void destruct() {
        this.removeListeners();
        this.removeEditListeners();
    }

    /**
     * Create ListDataProvider for grid
     * todo: Also used in Tracking [true?]
     */
    public void getData() {
        //refresh from the database
        this.mainLayout.getClientEquityAttributesModels();

        //put the data in the grid
        this.setupEquitiesMVCView.getSetupEquitiesTableVL().getSetupEquitiesGrid()
            .setItems(this.mainLayout.getClientEquityAttributesModels());
    }

    /**
     * These listeners are those that are local to this mvc
     */
    private void doListeners() {
        //cancel
        this.addCancelClickListener();
        //save
        this.addSaveClickListener();
        //new
        this.addNewClickListener();
        //edit
        this.addEditDoubleClickListener();
    }

    private void addEditTickerChangeListener() {
        //ticker change
        this.editListeners.add(this.setupEquitiesEditView.getTicker().addValueChangeListener(tkrValue -> {
            FinVizEquityInfoModel fvm;
            NativeLabel statusMsg;
            Iterator itemsIterator;
            ClientEquityModel cem;
            Integer sectorId;
            
            //ensure status message is empty
            this.setupEquitiesEditView.getStatusMsgHL().removeAll();

            //upper case
            final String ticker = this.setupEquitiesEditView.getTicker().getValue().toUpperCase();
            this.setupEquitiesEditView.getTicker().setValue(ticker);
            
            //validate ticker
            fvm = FinVizEquityInfoModel.FIN_VIZ_HASH_TABLE.get(ticker);

            //check ticker
            if (fvm == null) {
                //invalid ticker
                statusMsg = new NativeLabel("Invalid ticker");
                statusMsg.getElement().getStyle().set("color", "red");
                this.setupEquitiesEditView.getStatusMsgHL().add(statusMsg);
                this.setupEquitiesEditView.getTicker().focus();
                return;
            }

            //check if already have ticker; duplicates disallowed
            //look in the view as may have added there without save
            itemsIterator = ((ListDataProvider) this.setupEquitiesMVCView.getSetupEquitiesTableVL()
                .getSetupEquitiesGrid().getDataProvider()).getItems().iterator();

            while (itemsIterator.hasNext()) {
                cem = (ClientEquityModel) itemsIterator.next();

                if (cem.getTicker().equalsIgnoreCase(ticker)) {
                    //already have this ticker
                    statusMsg = new NativeLabel("Ticker is already in the list");
                    statusMsg.getElement().getStyle().set("color", "red");
                    this.setupEquitiesEditView.getStatusMsgHL().add(statusMsg);
                    this.setupEquitiesEditView.getTicker().focus();
                    return;
                }
            }

            //ok, passed validation
            this.setupEquitiesEditView.getButtonOk().setEnabled(true);

            //set the sector
            sectorId = this.setupEquitiesModel.serviceTPC.getClientSectorIdFromTicker(this.setupEquitiesEditView
                .getTicker().getValue());

            //set data in model
            this.setupEquitiesEditView.getEditingClientEquityModel().setTickerIEX(ticker);
            this.setupEquitiesEditView.getEditingClientEquityModel().setClientSectorId(sectorId);
            this.setupEquitiesEditView.getEditingClientEquityModel().setComment(this.setupEquitiesEditView
                .getEquityComment().getValue());

            //set sector in the combobox
            for (ClientSectorModel csm : this.mainLayout.getClientAllSectorListModels()) {
                if (csm.getClientSectorId().equals(sectorId)) {
                    this.setupEquitiesEditView.getCbClientSectorModel().setValue(csm);
                    break;
                }
            }
        }));
    }

    private void addEditCommentChangeListener() {
        this.editListeners.add(this.setupEquitiesEditView.getEquityComment().addValueChangeListener(comment -> {
            //set data in editView model
            this.setupEquitiesEditView.getEditingClientEquityModel().setComment(this.setupEquitiesEditView
                .getEquityComment().getValue());

            this.setupEquitiesEditView.getButtonOk().setEnabled(true);
        }));
    }

    private void addEditSectorChangeListener() {
        this.editListeners.add(this.setupEquitiesEditView.getCbClientSectorModel().addValueChangeListener(sector -> {
            //set data in editView model
            this.setupEquitiesEditView.getEditingClientEquityModel().setClientSectorId(this.setupEquitiesEditView
                .getCbClientSectorModel().getValue().getClientSectorId());

            this.setupEquitiesEditView.getEditingClientEquityModel().setCSecShort(
                this.mainLayout.getClientAllSectorHashMap().get(this.setupEquitiesEditView
                    .getCbClientSectorModel().getValue().getClientSectorId()));

            this.setupEquitiesEditView.getButtonOk().setEnabled(true);
        }));
    }

    private void addEditOkClickListener() {
        this.editListeners.add(this.setupEquitiesEditView.getButtonOk().addClickListener(bOk -> {
            Collection<ClientEquityModel> collection;
            Boolean bRemove;

            //ensure status message is empty
            this.setupEquitiesEditView.getStatusMsgHL().removeAll();

            //massage collection; appears no direct way to do this
            collection = ((ListDataProvider) (this.setupEquitiesMVCView.getSetupEquitiesTableVL()
                .getSetupEquitiesGrid().getDataProvider())).getItems();

            if (this.setupEquitiesEditView.getBAdd()) {
                //adding
                collection.add(this.setupEquitiesEditView.getEditingClientEquityModel());
            }
            else {
                //editing; remove
                bRemove = collection.remove(this.setupEquitiesModel.getEditClientEquityModel());

                //editing: add
                collection.add(this.setupEquitiesEditView.getEditingClientEquityModel());
            }

            //re-sort
            ArrayList<ClientEquityModel> al = new ArrayList<>(collection);
            Collections.sort(al);
            
            //update grid dataProvider (actually creates a new one)
            this.setupEquitiesMVCView.getSetupEquitiesTableVL().getSetupEquitiesGrid().setItems(al);

            //refresh (not sure necessary)
            this.setupEquitiesMVCView.getSetupEquitiesTableVL().getSetupEquitiesGrid().getDataProvider().refreshAll();

            //enable save, cancel button
            this.setupEquitiesMVCView.getSaveButton().setEnabled(true);
            this.setupEquitiesMVCView.getCancelButton().setEnabled(true);

            //close dialog
            this.closeEditDialog();
        }));
    }

    private void addEditCancelClickListener() {
        this.editListeners.add(this.setupEquitiesEditView.getButtonCancel().addClickListener(cancel -> {
            this.closeEditDialog();
        }));
    }

    private void closeEditDialog() {
        //need to remove listeners or they compound
        this.removeEditListeners();
        this.setupEquitiesEditView.close();
    }

    private void addNewClickListener() {
        this.listeners.add(this.setupEquitiesMVCView.getNewButton().addClickListener(event -> {
            //create a default new equity and allow edit; hold in setupEquitiesMVCModel
            this.setupEquitiesModel.createDefaultClientEquityModel();

            //adjust the changed property
            this.setupEquitiesModel.getEditClientEquityModel().setChanged(ClientEquityModel.CHANGE_NEW);

            //take it to the dialog for further editing
            this.setupEquitiesEditView.doDialog(this.setupEquitiesModel.getEditClientEquityModel(), true);

            //add any listeners
            //edit ticker change
            this.addEditTickerChangeListener();

            //edit comment change
            this.addEditCommentChangeListener();

            //edit sector change
            this.addEditSectorChangeListener();

            //edit ok
            this.addEditOkClickListener();

            //edit cancel
            this.addEditCancelClickListener();

            //open the dialog
            this.setupEquitiesEditView.open();
        }));
    }

    private void addEditDoubleClickListener() {
        this.listeners.add(this.setupEquitiesMVCView.getSetupEquitiesTableVL()
            .getSetupEquitiesGrid().addItemDoubleClickListener(editItem -> {
                //case: add a new one; edit before save. This results in delete.
                //only adjust changed if CHANGE_NONE; allows multiple changes to a NEW before save
                if (editItem.getItem().getChanged().equals(ClientEquityModel.CHANGE_NONE)) {
                    editItem.getItem().setChanged(ClientEquityModel.CHANGE_EDIT);
                }

                //hold copy of original clientEquityModel in setupEquitiesMVCModel
                this.setupEquitiesModel.setEditClientEquityModel(new ClientEquityModel(editItem.getItem()));

                //take it to the dialog for editing
                this.setupEquitiesEditView.doDialog(this.setupEquitiesModel.getEditClientEquityModel(), false);

                /*
                 * add any listeners
                 *
                 * ticker change: not allowed on edit
                 * this.addTickerChangeListener();
                 *
                 * edit comment change
                 */
                this.addEditCommentChangeListener();

                //edit sector change
                this.addEditSectorChangeListener();

                //edit ok
                this.addEditOkClickListener();

                //edit cancel
                this.addEditCancelClickListener();

                //open dialog
                this.setupEquitiesEditView.open();
            }));
    }

    private void addCancelClickListener() {
        this.listeners.add(this.setupEquitiesMVCView.getCancelButton().addClickListener(event -> {
            /*
             * cancel from list means return to what the database has now
             */
            this.setupEquitiesMVCView.getSaveButton().setEnabled(false);
            this.setupEquitiesMVCView.getCancelButton().setEnabled(false);

            //refresh data from database
            this.getData();
        }));
    }

    private void addSaveClickListener() {
        this.listeners.add(this.setupEquitiesMVCView.getSaveButton().addClickListener(event -> {
            //save pending changes which are in the dataProvider of the grid
            this.setupEquitiesModel.doSave(this.setupEquitiesMVCView
                .getSetupEquitiesTableVL().getSetupEquitiesGrid());

            //refresh from database after save to ensure sync with real database
            this.mainLayout.getClientEquityAttributesList();

            //reset dataProvider
            this.getData();

            //and refresh dataProvider
            this.setupEquitiesMVCView.getSetupEquitiesTableVL().getSetupEquitiesGrid().getDataProvider().refreshAll();

            this.setupEquitiesMVCView.getSaveButton().setEnabled(false);
            this.setupEquitiesMVCView.getCancelButton().setEnabled(false);
        }));
    }

    private void removeListeners() {
        for (Registration r : this.listeners) {
            if (r != null) {
                r.remove();
            }
        }
        
        this.listeners.clear();
    }

    private void removeEditListeners() {
        for (Registration r : this.editListeners) {
            if (r != null) {
                r.remove();
            }
        }

        this.editListeners.clear();
    }
}
