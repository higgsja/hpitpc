package com.hpi.tpc.ui.views.setup.sectors;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.stereotype.Component;

/*
 * Accounts edit dialog
 */
@UIScope
@VaadinSessionScope
@Component
@PermitAll
//@CssImport(value = "./styles/dlgTemplate.css")
//@CssImport(value = "./styles/setupSectorsAddEdit.css")
public class SetupSectorEditView
    extends Dialog {

    @Getter private ClientSectorModel clientSectorModel;

    @Getter private Boolean bAdd;

    @Getter private final Button buttonCancel;
    @Getter private final Button buttonOk;
    private final HorizontalLayout ctrlsPanelHL;

    @Getter private final HorizontalLayout statusMsgHL;

    @Getter private final TextField sectorName;
    @Getter private final NativeLabel sectorNameError;
    private final HorizontalLayout sectorNameHL;

    @Getter private final TextField sectorShortName;
    @Getter private final NativeLabel sectorShortNameError;
    private final HorizontalLayout sectorShortNameHL;

    @Getter private final TextField sectorComment;
    private final HorizontalLayout sectorCommentHL;

    private final VerticalLayout content;

    private final Header header;

    public SetupSectorEditView() {
        this.buttonCancel = new Button("Cancel");
        this.buttonOk = new Button("Ok");
        this.ctrlsPanelHL = new HorizontalLayout(buttonCancel, buttonOk);

        this.statusMsgHL = new HorizontalLayout();

        this.sectorName = new TextField();
        this.sectorNameError = new NativeLabel("");
        this.sectorNameHL = new HorizontalLayout(sectorName, sectorNameError);

        this.sectorShortName = new TextField();
        this.sectorShortNameError = new NativeLabel("");
        this.sectorShortNameHL = new HorizontalLayout(sectorShortName, sectorShortNameError);

        this.sectorComment = new TextField();
        this.sectorCommentHL = new HorizontalLayout(sectorComment);

        this.content = new VerticalLayout(
            this.ctrlsPanelHL, this.statusMsgHL, this.sectorNameHL, this.sectorShortNameHL, this.sectorCommentHL);

        this.header = new Header();

        this.clientSectorModel = null;
    }

    @PostConstruct
    private void construct() {
        this.statusMsgHL.setHeight("20px");

        //cancel
        this.buttonCancel.addClickListener(cancel -> {
            this.close();
        });

        //OK
        this.buttonOk.addClickListener(ok -> {
            //at this point have this.clientSectorModel as either new or edited
            //todo: validation?
            //nothing else to do here; handling in setupSectorsMVCController
            this.clientSectorModel.setClientSector(sectorName.getValue());
            this.clientSectorModel.setCSecShort(sectorShortName.getValue());
            this.clientSectorModel.setComment(sectorComment.getValue());
        });

        //sector name
        this.sectorName.addValueChangeListener(sectorNameVCL -> {
            this.checkOK();
        });

        //sector short name
        this.sectorShortName.addValueChangeListener(sectorShortNameVCL -> {
            this.checkOK();
        });

        //sector comment
        this.sectorComment.addValueChangeListener(sectorCommentVCL -> {
            this.buttonOk.setEnabled(true);
        });

        //dialog theme
        this.getElement().getThemeList().add("setupSectorAddEditDlg");

        //accessibility
        this.getElement().setAttribute("aria-labelledby", "dialog-title");
    }

    @PreDestroy
    private void destruct() {
        //not hit on close(); using this as @Autowired (singleton) so that makes sense.
        int i = 0;
    }

    private void checkOK() {
        if (this.sectorName.isEmpty() || this.sectorShortName.isEmpty()) {
            //disable ok
            this.buttonOk.setEnabled(false);
        }
        else {
            //enable ok
            this.buttonOk.setEnabled(true);
        }
    }

    /**
     * Provides dialog for add/edit
     *
     * @param csm:  can be a full ClientSectorModel from the database
     *              or a default ClientSectorModel with empty ClientSector, CSecShort and Comment; and null ClientSectorId
     * @param bAdd: true if adding a new sector; false if editing and existing one
     *
     * To enable cancel while editing an existing ClientSectorModel need to clone it to this.clientSectorModel
     */
    public void doDialog(ClientSectorModel csm, Boolean bAdd) {
        this.bAdd = bAdd;
        this.add(this.content);
        this.statusMsgHL.removeAll();
        
        //at this point, this.clientSectorModel has a model, either default or one to edit
        //this.bAdd is set
        if (!bAdd) {
            //editing existing
            //clone it
            this.clientSectorModel = new ClientSectorModel(csm);
            this.clientSectorModel.setChanged(ClientSectorModel.CHANGE_EDIT);
        }else
        {
            //adding new
            this.clientSectorModel = new ClientSectorModel(csm);
            this.clientSectorModel.setChanged(ClientSectorModel.CHANGE_NEW);
        }

        //fill the form
        this.doSectorName();
        this.doSectorShortName();
        this.doSectorComment();
        this.doCtrlsPanel();

        //set controls and focus
        this.buttonOk.setEnabled(false);
        
        if(this.clientSectorModel.getCustomSector().equals(ClientSectorModel.STD_SECTOR)){
            this.sectorShortName.setEnabled(false);
        }else{
            this.sectorShortName.setEnabled(true);
        }
        this.sectorName.focus();
    }

    private void doSectorName() {
        //whether edit or add allow edit for non-standard
        this.sectorNameHL.setClassName("sectorNameHL");
        this.sectorName.setClassName("sectorName");
        this.sectorName.setWidth("100%");

        this.sectorName.setPlaceholder("Sector name");

        this.sectorName.setValue(this.clientSectorModel == null ? "" : this.clientSectorModel.getClientSector());

        this.configText(this.sectorName);

        if (!bAdd) {
            //editing
            if (this.clientSectorModel.getCustomSector().equals(0)) {
                //standard sector, allow edit of name
            }
        }
    }

    private void doSectorShortName() {
        this.sectorShortNameHL.setClassName("sectorShortNameHL");
        this.sectorShortName.setClassName("sectorShortName");
        this.sectorShortName.setWidth("100%");

        this.sectorShortName.setPlaceholder("Sector short name");

        this.sectorShortName.setValue(this.clientSectorModel == null ? "" : this.clientSectorModel.getCSecShort());

        this.configText(this.sectorShortName);

        if (!bAdd) {
            if (this.clientSectorModel.getCustomSector().equals(0)) {
                //standard sector, disallow edit of short name
                this.sectorShortName.setEnabled(false);
            }
        }
    }

    private void doSectorComment() {
        this.sectorCommentHL.setClassName("sectorCommentHL");
        this.sectorComment.setClassName("sectorComment");
        this.sectorComment.setWidth("100%");

        this.sectorComment.setPlaceholder("Comment");

        this.sectorComment.setValue(this.clientSectorModel == null ? "" : this.clientSectorModel.getComment());

        this.configText(this.sectorComment);
    }

    private void doCtrlsPanel() {
        this.ctrlsPanelHL.setClassName("setupSectorsEditCtrlsPanelHL");

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
