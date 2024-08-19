package com.hpi.tpc.ui.views.setup.accounts;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;
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
@Route(value = ROUTE_SETUP_ACCOUNTS, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_SETUP_ACCOUNTS)
@org.springframework.stereotype.Component
@NoArgsConstructor
public class AccountsController
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Autowired private AccountsModel accountsModel;
    @Autowired private AccountsView accountsView;
    @Autowired @Getter private AccountsEditView accountsEditView;

    @PostConstruct
    private void construct()
    {
        this.setClassName("setupAccountsController");

        //get any preferences
        this.accountsModel.getPrefs("Accounts");

        //set comboBox placeholders
        this.accountsView.getClientFICombobox().setPlaceholder("Financial Institution");
        this.accountsView.getAllAccountsCombobox().setPlaceholder("Account Name");

        this.addClientFIListener();
        this.addClientAcctsListener();
        this.addAcctsActiveCheckboxListener();
        this.addButtonAcctsEditListener();
        this.addButtonDeleteListener();
        this.addButtonAcctsAddListener();
        this.addButtonAcctsCancelListener();
        this.addButtonAcctsCancelListener();
        this.addButtonAcctsSaveListener();
    }

    /**
     * client institution change
     */
    private void addClientFIListener()
    {
        
            this.accountsView.getClientFICombobox().addValueChangeListener(iEvent ->
            {
                //hold on to selected client institution
                this.accountsModel.setSelectedClientInstitutionModel((OfxInstitutionModel) iEvent.getValue());

                if (iEvent.getValue() != null)
                {
                    // have an FI, filter accounts to selected financial institution
                    this.accountsModel.getAllClientAccountsDataProvider()
                        .setFilterByValue(EditAccountModel::getDmOfxFId,
                            this.accountsModel.getSelectedClientInstitutionModel().getFId());

                    //fill the accounts combobox
                    this.accountsView.getAllAccountsCombobox().setItems(this.accountsModel
                        .getAllClientAccountsDataProvider());

                    //org data
                    this.accountsView.getOrgOrganization().setText(this.accountsModel
                        .getSelectedClientInstitutionModel().getOrg());
                    this.accountsView.getOrgInstitutionId().setText(this.accountsModel
                        .getSelectedClientInstitutionModel().getFId());
                    this.accountsView.getOrgBrokerId().setText(this.accountsModel
                        .getSelectedClientInstitutionModel().getBrokerId());
                    this.accountsView.getOrgInstitutionUrl().setText(this.accountsModel
                        .getSelectedClientInstitutionModel().getUrl());
                } else
                {
                    //no institution selected, should be no account selected or available in the list
                    this.accountsModel.setSelectedAccountModel(null);

                    //refresh client accounts
//                    this.accountsModel.retrieveAllClientAccountsDataProvider();
                    this.accountsView.getAllAccountsCombobox().clear();

                    //clear any data org data
                    this.accountsView.getOrgOrganization().setText("");
                    this.accountsView.getOrgInstitutionId().setText("");
                    this.accountsView.getOrgBrokerId().setText("");
                    this.accountsView.getOrgInstitutionUrl().setText("");
                }

                this.setCtrlsEnabled();
            }
        );
    }

    /**
     * client accounts change
     */
    private void addClientAcctsListener()
    {
        
            this.accountsView.getAllAccountsCombobox().addValueChangeListener(aEvent ->
            {
                //hold on to selected client account
                this.accountsModel.setSelectedAccountModel((EditAccountModel) aEvent.getValue());

                if (aEvent.getValue() != null)
                {
                    //set active checkbox
                    this.accountsView.getAcctActiveCheckbox().setValue(this.accountsModel
                        .getSelectedAccountModel().getBActive());

                    //account data
                    this.accountsView.getAcctNumber()
                        .setText((((EditAccountModel) aEvent.getValue()).getInvAcctIdFi()));
                } else
                {
                    this.accountsView.getAcctActiveCheckbox().setValue(false);

                    this.accountsView.getAcctNumber().setText("");
                }

                this.setCtrlsEnabled();
            }
        );
    }

    private void addAcctsActiveCheckboxListener()
    {
        
            this.accountsView.getAcctActiveCheckbox().addClickListener(checkboxAcctActiveEvent ->
            {
                ((EditAccountModel) this.accountsView.getAllAccountsCombobox().getValue())
                    .setActive(checkboxAcctActiveEvent
                        .getSource().getValue() ? "Yes" : "No");

                ((EditAccountModel) this.accountsView.getAllAccountsCombobox().getValue())
                    .setBActive(checkboxAcctActiveEvent
                        .getSource().getValue());

                this.accountsModel.setBAccountsDataProviderIsDirty(true);

                this.setCtrlsEnabled();
            }
        );
    }

    private void addButtonAcctsEditListener()
    {
        
            this.accountsView.getButtonEdit().addClickListener(buttonEditEvent ->
            {
                this.buttonAccountsEdit();

                this.setCtrlsEnabled();
            }
        );
    }

    private void addButtonDeleteListener()
    {
        
            this.accountsView
                .getButtonDelete().addClickListener(buttonDeleteEvent ->
                {
                    this.buttonAccountsDelete();

                    this.setCtrlsEnabled();
                }
        );
    }

    private void addButtonAcctsAddListener()
    {
        
            this.accountsView
                .getButtonAdd().addClickListener(buttonAddEvent ->
                {
                    this.buttonAccountsAdd();
                }
        );
    }

    private void addButtonAcctsSaveListener()
    {
        
            this.accountsView
                .getButtonSave().addClickListener(buttonSaveEvent ->
                {
                    this.buttonAccountsSave();
                }
        );
    }

    private void addButtonAcctsCancelListener()
    {
        
            this.accountsView
                .getButtonCancel().addClickListener(buttonCancelEvent
                    ->
                {
                    this.buttonAccountsCancel();
                }
        );
    }

    /**
     * enable/disable controls based on state
     */
    private void setCtrlsEnabled()
    {
        //start with all disabled
        this.accountsView.getClientFICombobox().setEnabled(false);
        this.accountsView.getClientFIActiveCheckbox().setEnabled(false);
        this.accountsView.getAllAccountsCombobox().setEnabled(false);
        this.accountsView.getAcctActiveCheckbox().setEnabled(false);
        this.accountsView.getButtonEdit().setEnabled(false);
        this.accountsView.getButtonDelete().setEnabled(false);
        this.accountsView.getButtonAdd().setEnabled(false);
        this.accountsView.getButtonCancel().setEnabled(false);
        this.accountsView.getButtonSave().setEnabled(false);

        if (this.accountsModel.getClientFIDataProvider() != null
            && !this.accountsModel.getClientFIDataProvider().getItems().isEmpty()){
            // there are client institutions
            
            this.accountsView.getClientFICombobox().setEnabled(true);
            this.accountsView.getButtonAdd().setEnabled(true);
            
            if (this.accountsModel.getSelectedClientInstitutionModel() != null){
                // a client institution is selected
                this.accountsView.getClientFIActiveCheckbox().setEnabled(true);
            }
        }
        else
        {
            // no client institutions
            this.accountsView.getButtonAdd().setEnabled(true);
        }
        
//        if (!this.accountsModel.getClientFIDataProvider().getItems().isEmpty()
//            && !(this.accountsModel.getSelectedClientInstitutionModel() == null))
//        {
//            // there are client institutions and one is selected
//            this.accountsView.getClientFICombobox().setEnabled(true);
//
//            this.accountsView.getClientFIActiveCheckbox().setEnabled(true);
//            this.accountsView.getButtonAdd().setEnabled(true);
//        } else
//        {
//            //there are no client institutions
//            //need add whether we have anything or not
//            //this.accountsView.getClientFIActiveCheckbox().setEnabled(true);
//            this.accountsView.getButtonAdd().setEnabled(true);
//        }

        //accounts available in selected financial institution?
        if (this.accountsModel.getAllClientAccountsDataProvider() != null
            && this.accountsModel.getAllClientAccountsDataProvider()
                .size(new Query<>(this.accountsModel.getAllClientAccountsDataProvider().getFilter())) != 0)
        {
            //enable accounts combobox
            this.accountsView.getAllAccountsCombobox().setEnabled(true);

            if (this.accountsModel.getSelectedAccountModel() != null)
            {
                //selected account is not null; enable active checkbox and edit button
                this.accountsView.getAcctActiveCheckbox().setEnabled(true);
                this.accountsView.getButtonEdit().setEnabled(true);
            }
        }

        //dirty data?
        if (this.accountsModel.getBAccountsDataProviderIsDirty()
            || this.accountsModel.getBClientInstitutionsDataProviderIsDirty())
        {
            this.accountsView.getButtonSave().setEnabled(true);
            this.accountsView.getButtonCancel().setEnabled(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        super.beforeEnter(bee);

        //refresh: retrieve latest data for all financial institutions
        this.accountsModel.retrieveAllFIDataProvider();

        //refresh: retrieve latest data for client financial institutions
        this.accountsModel.retrieveClientFIDataProvider();

        //refresh: retrieve latest data for all client accounts
        this.accountsModel.retrieveAllClientAccountsDataProvider();

        //refresh: retrieve latest data for all client accounts
        //this.accountsView.getClientFICombobox().setDataProvider(this.accountsModel.getClientFIDataProvider());
        this.accountsView.getClientFICombobox().setItems(this.accountsModel.getClientFIDataProvider());

        this.setCtrlsEnabled();

        //send to default view
        bee.forwardTo(AccountsView.class);
    }

    /**
     * disallow duplicate account numbers
     *
     * @return
     */
    private Boolean accountEditCheck()
    {
        this.accountsEditView.getAcctNumberError().setVisible(false);
        this.accountsEditView.getAcctNameError().setVisible(false);

        if (this.accountsModel.getBAccountsDataProviderIsDirty())
        {
            //something changed in the data
            for (EditAccountModel am : this.accountsModel.getAllClientAccountsDataProvider().getItems())
            {
                if (this.accountsEditView.getBAdd())
                {
                    //adding an account, check account number against existing
                    if (am.getInvAcctIdFi().equalsIgnoreCase(
                        this.accountsEditView.getAcctNumber().getValue()))
                    {
                        //have a duplicate, error and out
                        this.accountsEditView.getAcctNumberError().setVisible(true);
                        return false;
                    }
                } else
                {
                    //editing, can change account name, unique required     
                    //check for unique account name
                    if (am.getClientAcctName().equalsIgnoreCase(
                        this.accountsEditView.getAcctName().getValue()))
                    {
                        //have duplicate, error and out
                        this.accountsEditView.getAcctNameError().setVisible(true);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param bAdd true when adding an account else false
     */
    private void addEditDlgListeners(Boolean bAdd)
    {
        this.accountsEditView.getInstComboBox().addValueChangeListener(icbEvent ->
            {
                this.accountsEditView.getButtonOk().setEnabled(false);
                if (this.accountsEditView.getInstComboBox().getValue() != null
                    && !this.accountsEditView.getAcctNumber().getValue()
                        .equalsIgnoreCase("")
                    && !this.accountsEditView.getAcctName().getValue()
                        .equalsIgnoreCase(""))
                {
                    this.accountsEditView.getButtonOk().setEnabled(true);
                } else
                {
                    this.accountsEditView.getButtonOk().setEnabled(false);
                }
            });

        this.accountsEditView.getAcctNumber().addValueChangeListener(aNoEvent ->
            {
                if (this.accountsEditView.getInstComboBox().getValue() != null
                    && !this.accountsEditView.getAcctNumber().getValue()
                        .equalsIgnoreCase("")
                    && !this.accountsEditView.getAcctName().getValue()
                        .equalsIgnoreCase(""))
                {
                    this.accountsEditView.getButtonOk().setEnabled(true);
                } else
                {
                    this.accountsEditView.getButtonOk().setEnabled(false);
                }
            });

        this.accountsEditView.getAcctName().addValueChangeListener(aNaEvent ->
            {
                //error check
                if (!this.accountEditCheck())
                {
                    return;
                }

                if (this.accountsEditView.getInstComboBox().getValue() != null
                    && !this.accountsEditView.getAcctNumber().getValue()
                        .equalsIgnoreCase("")
                    && !this.accountsEditView.getAcctName().getValue()
                        .equalsIgnoreCase(""))
                {
                    this.accountsEditView.getButtonOk().setEnabled(true);
                } else
                {
                    this.accountsEditView.getButtonOk().setEnabled(false);
                }
//                this.accountsModel.setBAccountsDataProviderIsDirty(true);
            });

        this.accountsEditView.getUserId().addValueChangeListener(icbEvent ->
            {
                if (this.accountsEditView.getInstComboBox().getValue() != null
                    && !this.accountsEditView.getAcctNumber().getValue()
                        .equalsIgnoreCase("")
                    && !this.accountsEditView.getAcctName().getValue()
                        .equalsIgnoreCase(""))
                {
                    this.accountsEditView.getButtonOk().setEnabled(true);
                } else
                {
                    this.accountsEditView.getButtonOk().setEnabled(false);
                }
                this.accountsModel.setBAccountsDataProviderIsDirty(true);
            });

        this.accountsEditView.getPassword().addValueChangeListener(pEvent ->
            {
                if (this.accountsEditView.getInstComboBox().getValue() != null
                    && !this.accountsEditView.getAcctNumber().getValue()
                        .equalsIgnoreCase("")
                    && !this.accountsEditView.getAcctName().getValue()
                        .equalsIgnoreCase(""))
                {
                    this.accountsEditView.getButtonOk().setEnabled(true);
                } else
                {
                    this.accountsEditView.getButtonOk().setEnabled(false);
                }
                this.accountsModel.setBAccountsDataProviderIsDirty(true);
            });

        this.accountsEditView.getButtonCancel().addClickListener(bCEvent ->
            {
                this.buttonAccountEditCancel();
                this.closeAccountsEditDialog();
            });

        this.accountsEditView.getButtonOk().addClickListener(bOEvent ->
            {
                //update arrays
                this.buttonAccountEditOk();

                //arrays adjusted, enable save/cancel
                if (this.accountsModel.getBAccountsDataProviderIsDirty()
                    || this.accountsModel
                        .getBClientInstitutionsDataProviderIsDirty())
                {
                    this.accountsView.getButtonCancel().setEnabled(true);
                    this.accountsView.getButtonSave().setEnabled(true);
                }
                this.closeAccountsEditDialog();
            });
    }

    /**
     * Add an account to the selected institution.
     * <p>
     */
    private void buttonAccountsAdd()
    {
        //create the dialog
        this.accountsEditView
            .doDialog((OfxInstitutionModel) (this.accountsView.getClientFICombobox().getValue()), null);

        //add listeners
        this.addEditDlgListeners(true);

        this.accountsEditView.open();
    }

    /**
     * todo: not implemented
     * Delete the selected account. If there are no accounts, delete the
     * selected Institution. Actually marks Account or Institution as inactive.
     */
    private void buttonAccountsDelete()
    {
        //account or institution

        //confirmation dialog
        //do the work
    }

    /**
     * Edit the selected account.
     */
    private void buttonAccountsEdit()
    {
        //create the dialog
        this.accountsEditView.doDialog(
            (OfxInstitutionModel) (this.accountsView.getClientFICombobox().getValue()),
            (EditAccountModel) (this.accountsView.getAllAccountsCombobox().getValue()));

        //add listeners
        this.addEditDlgListeners(false);

        this.accountsEditView.open();
    }

    /**
     * Edit dialog cancel actions.
     */
    private void buttonAccountEditCancel()
    {
        //do nothing
    }

    /*
     * Called from edit/add dialog on OK button click
     * adjusts the clientInstitutions and accounts arrays
     * to include data from the edit/add
     */
    private void buttonAccountEditOk()
    {
        if (this.accountsEditView.getBAdd())
        {
            //using add functionality of dialog; add the institution
            this.accountsModel.addInstitution(this.accountsEditView.getInstComboBox().getValue());
        }

        //whether add/edit, add the account: adds if necessary
        this.accountsModel.AddAccount(this.accountsEditView
            .getAcctName().getValue(),
            this.accountsEditView.getInstComboBox().getValue(),
            this.accountsEditView.getAcctNumber().getValue(),
            this.accountsEditView.getUserId().getValue(),
            this.accountsEditView.getPassword().getValue());

        //arrays up to date, refresh the view
        if (this.accountsModel.getBClientInstitutionsDataProviderIsDirty())
        {
            //institution list changed, update and set to empty
            this.accountsView.getClientFICombobox()
                .setItems(this.accountsModel.getClientFIDataProvider());
            this.accountsView.getClientFICombobox().setValue(null);
            this.accountsView.getAllAccountsCombobox().setValue(null);
        }

        if (this.accountsModel.getBAccountsDataProviderIsDirty())
        {
            //clientAccounts changed
            this.accountsView.getAllAccountsCombobox()
                .setItems(this.accountsModel.getAllClientAccountsDataProvider());

            this.accountsView.getAllAccountsCombobox().setValue(null);
        }
    }

    /**
     * Save any edits
     */
    private void buttonAccountsSave()
    {
        //number of accounts and changes is small; 
        //  just upsert everything for simplicity
        if (this.accountsModel.upsertAccounts())
        {
            //refresh data
            this.accountsModel.retrieveAllFIDataProvider();

            //refresh comboboxes
            this.accountsView.getClientFICombobox()
                .setItems(this.accountsModel.getClientFIDataProvider());
            this.accountsView.getClientFICombobox().setValue(null);

            this.accountsView.getAllAccountsCombobox()
                .setItems(this.accountsModel.getAllClientAccountsDataProvider());
            this.accountsView.getAllAccountsCombobox().setValue(null);

            this.accountsModel.setBClientInstitutionsDataProviderIsDirty(false);
            this.accountsModel.setBAccountsDataProviderIsDirty(false);

            this.setCtrlsEnabled();
        }
    }

    /**
     * Cancel any edits
     */
    private void buttonAccountsCancel()
    {
        //return to arrays from database
        //just do a page reload?
        int i = 0;
    }

    private void closeAccountsEditDialog()
    {
        this.accountsEditView.close();
    }

    @Override
    public void addMenuBarTabs()
    {
        //none
    }
}
