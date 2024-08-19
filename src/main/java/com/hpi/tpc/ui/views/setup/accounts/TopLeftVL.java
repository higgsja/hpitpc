package com.hpi.tpc.ui.views.setup.accounts;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.*;

/**
 * Accounts management form
 */
@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
public class TopLeftVL
    extends VerticalLayout
{

    @Autowired private AccountsModel accountsModel;

    @Getter private final ComboBox<OfxInstitutionModel> clientFICombobox = new ComboBox<>();
    @Getter private final Checkbox clientFIActiveCheckbox = new Checkbox("Active", false);
    private final HorizontalLayout instHL = new HorizontalLayout(
        this.clientFICombobox, this.clientFIActiveCheckbox);

    @Getter private final ComboBox<EditAccountModel> allAccountsCombobox = new ComboBox<>();
    @Getter private final Checkbox acctActiveCheckbox = new Checkbox("Active", false);
    private final HorizontalLayout acctNameHL = new HorizontalLayout(
        this.allAccountsCombobox, this.acctActiveCheckbox);

    @Getter private final Button buttonEdit = new Button("Edit");
    @Getter private final Button buttonDelete = new Button("Delete");
    @Getter private final Button buttonAdd = new Button("Add");
    private final HorizontalLayout acctNameBtnsHL = new HorizontalLayout(
        this.buttonEdit, this.buttonDelete, this.buttonAdd);

    private final NativeLabel acctNumberTitle = new NativeLabel("Account: ");
    @Getter NativeLabel acctNumber = new NativeLabel();
    private final HorizontalLayout acctHL = new HorizontalLayout(this.acctNumberTitle, this.acctNumber);

    private final NativeLabel orgOrganizationTitle = new NativeLabel("Organization:");
    @Getter private final NativeLabel orgOrganization = new NativeLabel();
    private final HorizontalLayout orgOrganizationHL = new HorizontalLayout(orgOrganizationTitle, orgOrganization);

    private final NativeLabel orgInstitutionIdTitle = new NativeLabel("Institution ID:");
    @Getter private final NativeLabel orgInstitutionId = new NativeLabel();
    private final HorizontalLayout orgInstitutionIdHL = new HorizontalLayout(
        orgInstitutionIdTitle, orgInstitutionId);

    private final NativeLabel orgBrokerIdTitle = new NativeLabel("Broker ID:");
    @Getter private final NativeLabel orgBrokerId = new NativeLabel();
    private final HorizontalLayout orgBrokerIdHL = new HorizontalLayout(orgBrokerIdTitle, orgBrokerId);

    private final NativeLabel orgInstitutionUrlTitle = new NativeLabel("Institution URL:");
    @Getter private final NativeLabel orgInstitutionUrl = new NativeLabel();
    private final HorizontalLayout orgInstitutionUrlHL = new HorizontalLayout(
        orgInstitutionUrlTitle, orgInstitutionUrl);

    @Getter private final VerticalLayout orgDataVL = new VerticalLayout(this.acctHL, this.orgOrganizationHL,
        this.orgInstitutionIdHL, this.orgBrokerIdHL, this.orgInstitutionUrlHL);

    @Getter private final Button buttonCancel = new Button("Cancel");
    @Getter private final Button buttonSave = new Button("Save");
    private final HorizontalLayout bottomCtrlPanel = new HorizontalLayout(this.buttonCancel, this.buttonSave);

    @PostConstruct
    private void construct()
    {
        //add to combobox
        this.setClassName("topLeftVL");
//        if (this.clientInstComboBox.getListDataView().getItemCount() == 0)
//        {
//            this.clientInstComboBox.setPlaceholder("Financial Institution");
//        }
//
//        if (this.accountsComboBox.getListDataView().getItemCount() == 0)
//        {
//            this.accountsComboBox.setPlaceholder("Account Name");
//        }

//        this.doInst();
//        this.doAcctName();
//        this.doAcctNameBtns();
//        this.doOrgData();
//        this.doBottomLeftCtrlsPanel();

        this.add(this.instHL, this.acctNameHL, this.acctNameBtnsHL,
            this.orgDataVL, this.bottomCtrlPanel);
    }

    @PreDestroy
    private void destruct()
    {
    }

    private void doInst()
    {
        this.instHL.setClassName("instHL");
        this.clientFICombobox.setClassName("instComboBox");
        this.instHL.setWidth("100%");
        this.configText(this.clientFIActiveCheckbox);
        
//        this.clientInstComboBox.setPlaceholder("Financial Institution");
        this.clientFICombobox.setItems(this.accountsModel.getClientFIDataProvider());

        this.clientFIActiveCheckbox.getElement().getStyle().set("padding", "10px 0 0 0");

        //currently not using
        this.clientFIActiveCheckbox.setVisible(false);
    }

    private void doAcctName()
    {
        this.acctNameHL.setClassName("acctNameHL");
        this.allAccountsCombobox.setClassName("acctsComboBox");
        this.acctNameHL.setWidth("100%");
        this.configText(this.acctActiveCheckbox);
        
//        this.accountsComboBox.setPlaceholder("Account Name");
        this.allAccountsCombobox.setItems(this.accountsModel.getAllClientAccountsDataProvider());

        this.acctActiveCheckbox.getElement().getStyle().set("padding", "10px 0 0 0");
    }

    private void doAcctNameBtns()
    {
        this.acctNameBtnsHL.setClassName("acctNameBtnsHL");
        this.acctNameBtnsHL.setWidth("100%");

        this.buttonEdit.addThemeVariants(ButtonVariant.LUMO_SMALL);
        this.buttonDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);
        this.buttonAdd.addThemeVariants(ButtonVariant.LUMO_SMALL);

        //not using now
        this.buttonDelete.setVisible(false);
    }

    private void doOrgData()
    {
        this.orgDataVL.setClassName("orgDataVL");
        this.orgDataVL.setWidth("100%");

        this.acctHL.setClassName("acctHL");
        this.orgOrganizationHL.setClassName("orgOrganizationHL");
        this.orgInstitutionIdHL.setClassName("orgInstitutionIdHL");
        this.orgBrokerIdHL.setClassName("orgBrokerIdHL");
        this.orgInstitutionUrlHL.setClassName("orgInstitutionUrlHL");

        this.configText(this.acctHL);
        this.configText(this.orgOrganization);
        this.configText(this.orgOrganizationTitle);
        this.configText(this.orgBrokerId);
        this.configText(this.orgBrokerIdTitle);
        this.configText(this.orgInstitutionId);
        this.configText(this.orgInstitutionIdTitle);
        this.configText(this.orgInstitutionUrl);
        this.configText(this.orgInstitutionUrlTitle);
    }

    private void doBottomLeftCtrlsPanel()
    {
        this.bottomCtrlPanel.setClassName("topLeftCtrlPanel");
        this.bottomCtrlPanel.setWidth("100%");

        this.buttonCancel.addThemeVariants(ButtonVariant.LUMO_SMALL);
        this.buttonSave.addThemeVariants(ButtonVariant.LUMO_SMALL);
    }

    private void configText(com.vaadin.flow.component.Component component)
    {
        component.getElement().getStyle().set("font-size", "14px");
        component.getElement().getStyle().set("font-family", "Arial");
//        component.getElement().getStyle().set("color", "black");
        component.getElement().getStyle().set("margin-top", "0px");
        component.getElement().getStyle().set("margin-bottom", "0px");
        component.getElement().getStyle().set("margin-block-start",
            "0px");
        component.getElement().getStyle().set("margin-block-end",
            "0px");
        component.getElement().getStyle().set("line-height", "1em");
    }
}
