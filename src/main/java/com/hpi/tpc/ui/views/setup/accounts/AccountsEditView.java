package com.hpi.tpc.ui.views.setup.accounts;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/*
 * Accounts edit dialog
 */
@UIScope
@VaadinSessionScope
@Component
@PermitAll
@NoArgsConstructor
//@CssImport(value = "./styles/dlgTemplate.css")
public class AccountsEditView
    extends Dialog {

    @Autowired private AccountsModel accountsModel;

//    @Getter private Dialog dlg;
    @Getter private Boolean bAdd;

//    private final H2 title1 = new H2("Trader Performance Coach:");
//    private final H4 title2 = new H4();
//    private final H4 title3 = new H4(" Account");
    @Getter private final ComboBox<OfxInstitutionModel> instComboBox =
        new ComboBox<>();
    private final HorizontalLayout instHL = new HorizontalLayout(instComboBox);
    @Getter private final TextField acctNumber = new TextField();
    @Getter private final NativeLabel acctNumberError = new NativeLabel("Must be unique");
    private final HorizontalLayout acctNumberHL = new HorizontalLayout(
        acctNumber, acctNumberError);
    
    @Getter private final TextField acctName = new TextField();
    @Getter private final NativeLabel acctNameError = new NativeLabel("Must be unique");
    private final HorizontalLayout acctNameHL = new HorizontalLayout(
        acctName, acctNameError);
    
    @Getter private final TextField userId = new TextField();
    @Getter private final NativeLabel userIdOptional = new NativeLabel("Optional");
    private final HorizontalLayout userIdHL = new HorizontalLayout(userId, userIdOptional);
    
    @Getter private final PasswordField password = new PasswordField();
    @Getter private final NativeLabel passwordOptional = new NativeLabel("Optional");
    private final HorizontalLayout passwordHL = new HorizontalLayout(password, passwordOptional);

    @Getter private final Button buttonCancel = new Button("Cancel");
    @Getter private final Button buttonOk = new Button("Ok");
    private final HorizontalLayout ctrlsPanelHL = new HorizontalLayout(buttonCancel, buttonOk);

    private final VerticalLayout content = new VerticalLayout(
        this.instHL, this.acctNumberHL, this.acctNameHL,
        this.userIdHL, this.passwordHL, this.ctrlsPanelHL);

//    private final Header header = new Header();

    @SuppressWarnings("all")
    @PostConstruct
    private void construct() {
//        this.title1.addClassName("dialog-title");
//        this.title2.addClassName("dialog-title");
//        this.title3.addClassName("dialog-title");

        //dialog theme
        this.getElement().getThemeList().add("setupAcctAddEditDlg");
//        this.title1.getElement().getThemeList().add(Lumo.DARK);
//        this.title2.getElement().getThemeList().add(Lumo.DARK);
//        this.title3.getElement().getThemeList().add(Lumo.DARK);

        //accessibility
        this.getElement().setAttribute("aria-labelledby", "dialog-title");
    }

    @PreDestroy
    private void destruct() {

    }

    /**
     * Provides dialog for add/edit of an account
     *
     * @param ofxInstitutionModel
     * @param accountModel
     */
    public void doDialog(OfxInstitutionModel ofxInstitutionModel,
        EditAccountModel accountModel) {
        this.add(content);
        this.bAdd = accountModel == null;
//        this.title2.setText(this.bAdd ? "Add" : "Edit");

        this.doInst(ofxInstitutionModel);
        this.doAcctNumber(accountModel);
        this.doAcctName(accountModel);
        this.doUserId(accountModel);
        this.doPassword(accountModel);
        this.doCtrlsPanel();

        this.buttonOk.setEnabled(false);
    }

    private void doInst(OfxInstitutionModel ofxInstitutionModel) {
        this.instHL.setClassName("instHL");
        this.instComboBox.setClassName("instComboBox");
        this.instHL.setWidth("100%");

        this.instComboBox.setPlaceholder("Financial Institution");

        this.instComboBox.setItems(this.accountsModel.getAllInstitutionsDataProvider());

        this.instComboBox.setValue(ofxInstitutionModel);

        this.instComboBox.setEnabled(this.bAdd);
    }

    private void doAcctNumber(EditAccountModel accountModel) {
        this.acctNumberHL.setClassName("acctNumberHL");
        this.acctNumber.setClassName("acctNumber");
        this.acctNumber.setWidth("60%");

        this.acctNumber.setPlaceholder("Account Number");

        this.acctNumber.setValue(accountModel == null ? "" : accountModel.getInvAcctIdFi());

        this.acctNumber.setEnabled(this.bAdd);
        
        this.configText(this.acctNumberError);
        this.acctNumberError.setVisible(false);
    }

    private void doAcctName(EditAccountModel accountModel) {
        this.acctNameHL.setClassName("acctNameHL");
        this.acctName.setClassName("acctName");
        this.acctName.setWidth("60%");

        this.acctName.setPlaceholder("Account Name");

        this.acctName.setValue(accountModel == null ? "" : accountModel.getClientAcctName());
        
        this.configText(this.acctNameError);
        this.acctNameError.setVisible(false);
    }

    private void doUserId(EditAccountModel accountModel) {
        this.userIdHL.setClassName("userIdHL");
        this.userId.setClassName("userId");
        this.userId.setWidth("60%");

        this.userId.setPlaceholder("User ID");

        this.userId.setValue(accountModel == null ? "" : accountModel.getUserId());
        
        this.configText(this.userIdOptional);
    }

    private void doPassword(EditAccountModel accountModel) {
        this.passwordHL.setClassName("passwordHL");
        this.password.setClassName("password");
        this.password.setWidth("60%");

        this.password.setPlaceholder("Password");

        this.password.setValue(accountModel == null ? "" : accountModel.getPwd());
        
        this.configText(this.passwordOptional);
    }

    private void doCtrlsPanel() {
        this.ctrlsPanelHL.setClassName("ctrlsPanelHL");
        this.ctrlsPanelHL.setClassName("ctrlsPanelHL");
        this.ctrlsPanelHL.setWidth("100%");
    }
    
    private void configText(com.vaadin.flow.component.Component component) {
        component.getElement().getStyle().set("font-size", "14px");
        component.getElement().getStyle().set("font-family", "Arial");
        component.getElement().getStyle().set("color", "black");
        component.getElement().getStyle().set("margin-top", "0px");
        component.getElement().getStyle().set("margin-bottom", "0px");
        component.getElement().getStyle().set("margin-block-start",
            "0px");
        component.getElement().getStyle().set("margin-block-end",
            "0px");
        component.getElement().getStyle().set("line-height", "1em");
    }
}
