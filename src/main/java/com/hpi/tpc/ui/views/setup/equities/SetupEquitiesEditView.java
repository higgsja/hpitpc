package com.hpi.tpc.ui.views.setup.equities;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.main.*;
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
 * Equities edit dialog
 */
@UIScope
@VaadinSessionScope
@Component
@PermitAll
//@CssImport(value = "./styles/dlgTemplate.css")
//@CssImport(value = "./styles/setupEquitiesAddEdit.css")
public class SetupEquitiesEditView
    extends Dialog {
    @Autowired private MainLayout mainLayout;

    //model being edited or added
    @Getter @Setter private ClientEquityModel editingClientEquityModel;

    @Getter private Boolean bAdd;
    @Getter private final Button buttonCancel;
    @Getter private final Button buttonOk;
    private final HorizontalLayout ctrlsPanelHL;

    @Getter private final HorizontalLayout statusMsgHL;

    //todo: add equity name
    @Getter private final TextField equityName;
    @Getter private final NativeLabel equityNameError;
    private final HorizontalLayout equityNameHL;

    @Getter private final TextField ticker;
    @Getter private final NativeLabel tickerErrorUnique;
    @Getter private final NativeLabel tickerErrorInvalid;
    private final HorizontalLayout tickerHL;

    @Getter private final TextField equityComment;
    private final HorizontalLayout equityCommentHL;

    @Getter private final ComboBox<ClientSectorModel> cbClientSectorModel;
    private final HorizontalLayout cbClientSectorModelHL;

    private final Span equitySectorChangeWarning;
    private final HorizontalLayout warningHL;

    private final VerticalLayout content;

    private final Header header;

    public SetupEquitiesEditView() {
        this.buttonCancel = new Button("Cancel");
        this.buttonOk = new Button("Ok");
        this.ctrlsPanelHL = new HorizontalLayout(this.buttonCancel, this.buttonOk);

        this.statusMsgHL = new HorizontalLayout();
        this.equityName = new TextField();
        this.equityNameError = new NativeLabel("");
        this.equityNameHL = new HorizontalLayout(equityName, equityNameError);

        this.ticker = new TextField();
        this.tickerErrorUnique = new NativeLabel("");
        this.tickerErrorInvalid = new NativeLabel("Ticker is invalid");
        this.tickerHL = new HorizontalLayout(ticker, tickerErrorUnique);

        this.equityComment = new TextField();
        this.equityCommentHL = new HorizontalLayout(equityComment);

        this.cbClientSectorModel = new ComboBox<>();
        this.cbClientSectorModelHL = new HorizontalLayout(cbClientSectorModel);

        this.equitySectorChangeWarning = new Span();
        this.equitySectorChangeWarning.getElement().setProperty("innerHTML",
            "Adding a new Ticker or changing a Sector<br> requires refresh of broker download");
        this.equitySectorChangeWarning.getElement().getStyle().set("font-size", "11px");
        this.equitySectorChangeWarning.getElement().getStyle().set("font-family", "Arial");
        this.equitySectorChangeWarning.getElement().getStyle().set("color", "#169FF3");

        this.warningHL = new HorizontalLayout(this.equitySectorChangeWarning);

        this.header = new Header();

        this.content = new VerticalLayout(this.ctrlsPanelHL, this.statusMsgHL,
            //        this.equityNameHL,
            this.tickerHL, this.equityCommentHL, this.cbClientSectorModelHL,
            this.warningHL);

        this.editingClientEquityModel = null;
    }

    @PostConstruct
    private void construct() {
        // status 
        this.statusMsgHL.setHeight("20px");

        //sectors
//        this.cbClientSectorModel.setItems(this.mainLayout.getClientAllSectorListModels().stream());
        this.cbClientSectorModel.setItems(this.mainLayout.getClientAllSectorListModels());

        //cancel
        this.buttonCancel.addClickListener(cancel -> {
            this.close();
        });

        //OK
        this.buttonOk.addClickListener(ok -> {
            //at this point have this.editingClientEquityModel as either new or edited
            //nothing else to do here; handling in setupSectorsMVCController
            this.editingClientEquityModel.setTicker(ticker.getValue());

            for (ClientSectorModel csm : this.mainLayout.getClientAllSectorListModels()) {
                if (csm.getClientSectorId().equals(this.editingClientEquityModel.getClientSectorId())) {
                    this.cbClientSectorModel.setValue(csm);
                    break;
                }
            }

            this.editingClientEquityModel.setComment(equityComment.getValue());
        });

        //status message
        //equity name
//        this.ticker.addValueChangeListener(sectorNameVCL -> {
//            this.checkOK();
//        });
        //ticker
        this.equityName.addValueChangeListener(tickerVCL -> {
            this.checkOK();
        });

        //comment
        this.equityComment.addValueChangeListener(commentVCL -> {
            //nothing to validate
        });

        //dialog theme
        this.getElement().getThemeList().add("setupEquityAddEditDlg");

        //accessibility
        this.getElement().setAttribute("aria-labelledby", "dialog-title");
    }

    @PreDestroy
    private void destruct() {
    }

    private void checkOK() {
        if (this.ticker.isEmpty()) {
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
     * @param cem:  can be a full ClientSectorModel from the database
     *              or a default ClientSectorModel with empty ClientSector,
     *              CSecShort and Comment; and null ClientSectorId
     * @param bAdd: true if adding, false if editing existing
     */
    public void doDialog(ClientEquityModel cem, Boolean bAdd) {
        //hold a separate instance to edit
        this.editingClientEquityModel = new ClientEquityModel(cem);
        this.bAdd = bAdd;

        this.add(this.content);

        this.statusMsgHL.removeAll();

        //at this point, this.editingClientEquityModel has a model, either default or one to edit
        //this.bAdd is set
//        if (!bAdd) {
        //this is existing
        //disallow ticker edit
//            this.ticker.setEnabled(false);
        //set fields
//            this.ticker.setValue(cem.getTicker());
//            this.equityComment.setValue(cem.getComment() == null ? "" : cem.getComment());
        //set sector in the combobox
//            for (ClientSectorModel csm : this.mainLayout.getClientSectorListModels()) {
//                if (csm.getClientSectorId().equals(cem.getClientSectorId())) {
//                    this.cbClientSectorModel.setValue(csm);
//                    break;
//                }
//            }
//        }
//        else {
        //this is an add
//            this.editingClientEquityModel.setChanged(ClientEquityModel.CHANGE_NEW);
        //allow edit of ticker
////            this.ticker.setEnabled(true);
//        }
        //fill the form
        this.doTicker();
        this.doEquityComment();
        this.doSector();
        this.doCtrlsPanel();

        //set controls and focus
        this.buttonOk.setEnabled(false);
        this.equityName.setEnabled(false);
        this.ticker.focus();
    }

    private void doTicker() {
        this.tickerHL.setClassName("equityTickerNameHL");
        this.ticker.setClassName("equityTicker");
        this.ticker.setWidth("100%");

        this.ticker.setPlaceholder("Ticker");

        this.ticker.setValue(this.editingClientEquityModel.getTicker() == null ? "" : this.editingClientEquityModel.getTicker());

        this.configText(this.ticker);

        if (!this.bAdd) {
            //disallow editing ticker unless adding a new one
            this.ticker.setEnabled(false);
        }
        else {
            this.ticker.setEnabled(true);
        }
    }

    private void doEquityComment() {
        this.equityCommentHL.setClassName("equityCommentHL");
        this.equityComment.setClassName("equityComment");
        this.equityComment.setWidth("100%");

        this.equityComment.setPlaceholder("Comment");

        this.equityComment.setValue(this.editingClientEquityModel.getComment() == null ? "" :
                                    this.editingClientEquityModel.getComment());

        this.configText(this.equityComment);
    }

    private void doSector() {
        for (ClientSectorModel csm : this.mainLayout.getClientAllSectorListModels()) {
            if (csm.getClientSectorId().equals(this.editingClientEquityModel.getClientSectorId())) {
                this.cbClientSectorModel.setValue(csm);
                break;
            }
        }
    }

    private void doCtrlsPanel() {
        this.ctrlsPanelHL.setClassName("setupEquityEditCtrlsPanelHL");

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
