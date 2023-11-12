package com.hpi.tpc.ui.views.setup.accounts;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
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
@Route(value = ROUTE_SETUP_ACCOUNTS_VIEW, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_SETUP_ACCOUNTS)
@org.springframework.stereotype.Component
@PermitAll
public class AccountsView
    extends MVCView1WideBase
    implements BeforeEnterObserver, MVCView1WideInterface
{

    @Autowired private MainLayout appController;
    @Autowired private AccountsModel accountsModel;
    @Autowired @Getter private TopLeftVL topLeftVL;

    private NativeLabel title;

    @PostConstruct
    public void construct()
    {
        this.setClassName("setupAccountsView");
//        this.setWidth("320px");

        this.accountsModel.getPrefs("Accounts");
        
        this.add(topLeftVL);
        
        //do the layout without data
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.accountsModel.serviceTPC.AppTracking("TPC:Setup:Accounts");

        if (this.accountsModel.prefsController.getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
        {
            this.appController.setDrawerOpened(false);
        }
        
        //refresh data
    }

    @Override
    public void setupVL(String VLTitle)
    {
        this.add(this.topLeftVL);

    }

    protected ComboBox getClientFICombobox()
    {
        return this.topLeftVL.getClientFICombobox();
    }

    protected ComboBox getAllAccountsCombobox()
    {
        return this.topLeftVL.getAllAccountsCombobox();
    }

    protected Button getButtonEdit()
    {
        return this.topLeftVL.getButtonEdit();
    }

    protected Button getButtonDelete()
    {
        return this.topLeftVL.getButtonDelete();
    }

    protected Button getButtonAdd()
    {
        return this.topLeftVL.getButtonAdd();
    }

    protected NativeLabel getAcctNumber()
    {
        return this.topLeftVL.getAcctNumber();
    }

    protected NativeLabel getOrgOrganization()
    {
        return this.topLeftVL.getOrgOrganization();
    }

    protected NativeLabel getOrgInstitutionId()
    {
        return this.topLeftVL.getOrgInstitutionId();
    }

    protected NativeLabel getOrgBrokerId()
    {
        return this.topLeftVL.getOrgBrokerId();
    }

    protected NativeLabel getOrgInstitutionUrl()
    {
        return this.topLeftVL.getOrgInstitutionUrl();
    }

    protected Button getButtonCancel()
    {
        return this.topLeftVL.getButtonCancel();
    }

    protected Button getButtonSave()
    {
        return this.topLeftVL.getButtonSave();
    }

    protected Checkbox getClientFIActiveCheckbox()
    {
        return this.topLeftVL.getClientFIActiveCheckbox();
    }

    protected Checkbox getAcctActiveCheckbox()
    {
        return this.topLeftVL.getAcctActiveCheckbox();
    }
    
    protected void setAcctActiveCheckbox(Boolean active)
    {
        this.topLeftVL.getAcctActiveCheckbox().setValue(active);
    }

    @Override
    public void addMenuBarTabs()
    {
        //none
    }
}
