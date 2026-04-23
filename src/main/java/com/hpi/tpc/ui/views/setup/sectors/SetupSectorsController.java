package com.hpi.tpc.ui.views.setup.sectors;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
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
public class SetupSectorsController
    extends VerticalLayout {
    @Autowired private MainLayout mainLayout;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private SetupSectorEditView setupSectorEditView;

    //not used there but sets up MVC
    @Autowired private SetupSectorsModel setupSectorsMVC_M;
    //doing view before data is available
    @Autowired private SetupSectorsView setupSectorsMVCView;

    private final ArrayList<Registration> listeners;
    //must keep separate to enable removal on close of dialog
    private final ArrayList<Registration> editDlgListeners;
    private Registration gridDataProviderListener;

    public SetupSectorsController() {
        this.listeners = new ArrayList<>();
        this.editDlgListeners = new ArrayList<>();
    }

    @PostConstruct
    private void construct() {
        this.getData();

        //has to be after data is set in the table
        this.setupSectorsMVCView.doEditor();

        this.doListeners();
    }
    
    /**
     * Create ListDataProvider for grid
     * Also used in Tracking
     */
    public void getData() {
        //put the data in the grid
        this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid()
            .setItems(this.mainLayout.getClientAllSectorListModels());
        
        //set the dataProviderListener
        this.doDataProviderListener();
    }

    @PreDestroy
    private void destruct() {
        //not hit on close() of dialog
        this.removeListeners();
        this.removeDataProviderListener();
        this.removeEditDlgListeners();
    }

    /**
     * These listeners are those that are local to this mvc
     */
    private void doListeners() {
        this.doNewListener();
        this.doCancelListener();
        this.doSaveListener();
        this.doEditListener();
    }

    private void doNewListener() {
        this.listeners.add(this.setupSectorsMVCView.getNewButton().addClickListener(event -> {
            //create a new sector and allow edit
            //create default ClientSectorModel
            this.setupSectorsMVC_M.createDefaultClientSectorModel();

            //take it to the dialog for further editing
            this.setupSectorEditView.doDialog(this.setupSectorsMVC_M.getEditClientSectorModel(), true);

            //add any listeners for dialog
            //todo: this likely should go with the rest for consistency
            this.doEditOkListener();
            this.doEditCancelListener();

            //open the dialog
            this.setupSectorEditView.open();
        }));
    }

    private void doCancelListener() {
        //cancel all pending changes to any sectors and reset view to what is in the database
        this.listeners.add(this.setupSectorsMVCView.getCancelButton().addClickListener(event -> {
            
            //remove dataProvider listener
            this.removeDataProviderListener();
            
            //refresh data from database
            this.mainLayout.getClientSectorLists();
            
            //reset the dataProvider
            this.getData();
            
            //and refresh the view
            this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid().getDataProvider().refreshAll();
            
            this.setupSectorsMVCView.getSaveButton().setEnabled(false);
            this.setupSectorsMVCView.getCancelButton().setEnabled(false);
        }));
    }

    private void doSaveListener() {
        //save all pending changes to any sectors then set view to what is now in the database
        this.listeners.add(this.setupSectorsMVCView.getSaveButton().addClickListener(event -> {
            //save
            this.setupSectorsMVC_M.doSave(this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid());
            
            //remove dataProvider listener
            this.removeDataProviderListener();

            //after save, refresh data from the database
            this.mainLayout.getClientSectorLists();
            
            //reset the dataProvider
            this.getData();
            
            //and refresh
            this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid().getDataProvider().refreshAll();
            
            this.setupSectorsMVCView.getSaveButton().setEnabled(false);
            this.setupSectorsMVCView.getCancelButton().setEnabled(false);
        }));
    }

    private void doEditListener() {
        //respond to doubleClick on a sector in the list
        this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid()
            .addItemDoubleClickListener(event -> {
                //create clientSectorModel to edit
                //set the grid item to edited
                event.getItem().setChanged(ClientSectorModel.CHANGE_EDIT);
                
                //hold copy of original in setupSectorsMVCModel
                this.setupSectorsMVC_M.setEditClientSectorModel(new ClientSectorModel(event.getItem()));

                //take it to the dialog for further editing
                this.setupSectorEditView.doDialog(this.setupSectorsMVC_M.getEditClientSectorModel(), false);

                //add listeners for dialog
                this.doEditOkListener();
                this.doEditCancelListener();

                //open the dialog
                this.setupSectorEditView.open();
            });
    }

    private void doEditOkListener() {
        //respond to click on OK in add/edit dialog
        this.editDlgListeners.add(this.setupSectorEditView.getButtonOk().addClickListener(bOk -> {
            Collection<ClientSectorModel> collection;
            Boolean bRemove;

            //ensure status message is empty
            this.setupSectorEditView.getStatusMsgHL().removeAll();

            //validation required
            if (!sectorValidated()) {
                return;
            }

            //remove old listener
            this.removeDataProviderListener();

            //massage collection; appears no direct way to do this
            collection = ((ListDataProvider) (this.setupSectorsMVCView.getSetupSectorTableVL()
                .getSetupSectorsGrid().getDataProvider())).getItems();

            if (this.setupSectorEditView.getBAdd()) {
                //add the new one
                collection.add(this.setupSectorEditView.getClientSectorModel());
            }
            else {
                //editing an existing one: replace it
                //remove
                bRemove = collection.remove(this.setupSectorsMVC_M.getEditClientSectorModel());

                //add the edited one
                collection.add(this.setupSectorEditView.getClientSectorModel());
            }

            //re-sort
            ArrayList<ClientSectorModel> al = new ArrayList<>(collection);
            Collections.sort(al);

            //update grid dataProvider (actually creates a new one)
            this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid().setItems(al);

            //refresh (not sure necessary)
            this.setupSectorsMVCView.getSetupSectorTableVL().getSetupSectorsGrid().getDataProvider().refreshAll();

            //add new listener
            this.doDataProviderListener();

            //enable save, cancel button
            this.setupSectorsMVCView.getSaveButton().setEnabled(true);
            this.setupSectorsMVCView.getCancelButton().setEnabled(true);

            //close dialog
            //listeners do not go away; must do here
            //destruct in dialog not called
            this.removeEditDlgListeners();
            this.setupSectorEditView.close();
        }));
    }

    private Boolean sectorValidated() {
        Collection<ClientSectorModel> collection;
        Iterator iterator;
        ClientSectorModel csm;
        NativeLabel statusLabel;

        collection = ((ListDataProvider) (this.setupSectorsMVCView.getSetupSectorTableVL()
            .getSetupSectorsGrid().getDataProvider())).getItems();

        iterator = collection.iterator();

        // did not remove the old one; on an edit either of the checks could succeed
        //clientSectorModel in setupSectorsMVCModel is the original: check against it
        while (iterator.hasNext()) {
            csm = (ClientSectorModel) iterator.next();

            if (this.setupSectorEditView.getBAdd()) {
                //adding: unnecessary to check against setupSectorsMVCModel clientSectorModel
                //no duplicate sector names
                //no duplicate short sector names
                if (csm.getClientSector().equalsIgnoreCase(this.setupSectorEditView
                    .getClientSectorModel().getClientSector()) || csm.getCSecShort().equalsIgnoreCase(
                        this.setupSectorEditView.getClientSectorModel().getCSecShort())) {
                    statusLabel = new NativeLabel("Sector names and/or short names are not unique");
                    statusLabel.getElement().getStyle().set("color", "red");
                    this.setupSectorEditView.getStatusMsgHL().add(statusLabel);
                    this.setupSectorEditView.getSectorName().focus();

                    this.setupSectorEditView.getButtonOk().setEnabled(false);
                    return false;
                }
            }
            else {
                //editing: need to ignore the one we are editing in the list
                if (csm.equals(this.setupSectorsMVC_M.getEditClientSectorModel())) {
                    //this is the one we are editing so ignore it
                }
                else {
                    //no duplicate sector names
                    //no duplicate short sector names
                    if (csm.getClientSector().equalsIgnoreCase(this.setupSectorEditView
                        .getClientSectorModel().getClientSector()) || csm.getCSecShort().equalsIgnoreCase(
                            this.setupSectorEditView.getClientSectorModel().getCSecShort())) {
                        statusLabel = new NativeLabel("Sector names and/or short names are not unique");
                        statusLabel.getElement().getStyle().set("color", "red");
                        this.setupSectorEditView.getStatusMsgHL().add(statusLabel);
                        this.setupSectorEditView.getSectorName().focus();

                        this.setupSectorEditView.getButtonOk().setEnabled(false);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void doEditCancelListener() {
        this.editDlgListeners.add(this.setupSectorEditView.getButtonCancel().addClickListener(cancel -> {
            this.removeEditDlgListeners();
            this.setupSectorEditView.close();
        }));
    }

    /**
     * called from preDestroy
     */
    private void removeListeners() {
        for (Registration r : this.listeners) {
            if (r != null) {
                r.remove();
            }
        }
        
        this.listeners.clear();
        
        this.removeDataProviderListener();
        this.removeEditDlgListeners();
    }
    
    private void removeEditDlgListeners() {
        for (Registration r : this.editDlgListeners) {
            if (r != null) {
                r.remove();
            }
        }
        
        this.editDlgListeners.clear();
    }

    private void doDataProviderListener() {
        this.removeDataProviderListener();
        //change to table dataProvider
        this.gridDataProviderListener = this.setupSectorsMVCView.getSetupSectorTableVL()
            .getSetupSectorsGrid().getDataProvider()
            .addDataProviderListener(data -> {
                //todo: does nothing
            });
    }

    private void removeDataProviderListener() {
        if (this.gridDataProviderListener != null) {
            this.gridDataProviderListener.remove();
        }
    }
}
