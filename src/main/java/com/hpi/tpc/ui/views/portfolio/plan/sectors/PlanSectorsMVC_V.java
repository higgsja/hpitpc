package com.hpi.tpc.ui.views.portfolio.plan.sectors;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.prefs.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.grid.editor.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Provides the sector holdings view of the portfolio
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
@CssImport("./styles/portfolioPlanning.css")
@PermitAll
public class PlanSectorsMVC_V
    extends VerticalLayout
    implements BeforeEnterObserver, BeforeLeaveObserver
{

    @Autowired private MainLayout mainLayout;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private PrefsController prefsController;

    @Getter @Autowired private PlanSectorTableVL planTableVL;

    private final ArrayList<Registration> listeners;

    private final NativeLabel sectorTitle;

    private HorizontalLayout controlsHL;
    @Getter private Button saveButton;
    @Getter private Button adjustButton;

    @Getter private Checkbox cbOpenOnly;
    @Getter private Checkbox cbActiveOnly;

    @Getter private HorizontalLayout statusMsgHL;

    @Getter private final Binder<ClientSectorModel> binder
        = new Binder<>(ClientSectorModel.class);
    @Getter private Editor<ClientSectorModel> editor;

    public PlanSectorsMVC_V()
    {
        this.addClassName("planningContentSectors");

        this.listeners = new ArrayList<>();

        this.sectorTitle = new NativeLabel();
    }

    @PostConstruct
    private void construct()
    {
        this.planTableVL.setPlanSectorsMVCView(this);
        //force width to ensure full display of table
        //todo: should this be in PlanMVCView? No as it should not know how wide this needs to be
        this.setMaxWidth("900px");
        this.setMinWidth("900px");
        this.setHeight("100%");

        //title
        this.doTitle();
        this.add(this.sectorTitle);

        //controls
        this.doControls();
        this.add(this.controlsHL);

        //status message
        this.doStatusMsg();
        this.add(this.statusMsgHL);

        //sectors table
        this.add(this.planTableVL);

        //table editor
        this.doEditor();
    }

    @PreDestroy
    private void destruct()
    {
        this.removeListeners();
    }

    /**
     * called from preDestroy
     */
    private void removeListeners()
    {
        for (Registration r : this.listeners)
        {
            if (r != null)
            {
                r.remove();
            }
        }

        this.listeners.clear();
    }

    private void doTitle()
    {
        this.sectorTitle.setText("Sector Holdings");
        this.sectorTitle.getElement().getStyle().set("font-size", "14px");
        this.sectorTitle.getElement().getStyle().set("font-family", "Arial");
        this.sectorTitle.getElement().getStyle().set("color", "#169FF3");
        this.sectorTitle.getElement().getStyle().set("margin-top", "0px");
        this.sectorTitle.getElement().getStyle().set("margin-bottom", "0px");
        this.sectorTitle.getElement().getStyle().set("margin-block-start", "0px");
        this.sectorTitle.getElement().getStyle().set("margin-block-end", "0px");
        this.sectorTitle.getElement().getStyle().set("line-height", "1em");
    }

    private void doControls()
    {
        //controls
        this.controlsHL = new HorizontalLayout();
        this.controlsHL.setClassName("planningContentControls");
        this.controlsHL.setWidth("100%");

        //controls buttons
        HorizontalLayout controlsBtnsHL = new HorizontalLayout();
        controlsBtnsHL.setClassName("planningContentControlsBtns");
        controlsBtnsHL.setWidth("30%");
        controlsBtnsHL.setAlignItems(Alignment.CENTER);
        controlsBtnsHL.setAlignItems(Alignment.START);
        controlsBtnsHL.setJustifyContentMode(JustifyContentMode.START);
        this.saveButton = new Button("Save");
        this.adjustButton = new Button("Adjust");
        controlsBtnsHL.add(saveButton, adjustButton);
        this.controlsHL.add(controlsBtnsHL);

        this.saveButton.setEnabled(false);
        this.adjustButton.setEnabled(false);

        //controls check boxes
        HorizontalLayout controlsChkBoxesHL = new HorizontalLayout();
        controlsChkBoxesHL.setClassName("planningContentControlsChkBoxes");
        controlsChkBoxesHL.setWidth("70%");
        controlsChkBoxesHL.setAlignItems(Alignment.START);
        controlsChkBoxesHL.setJustifyContentMode(JustifyContentMode.START);
        cbOpenOnly = new Checkbox("Open Only");
        cbActiveOnly = new Checkbox("Active Only");
        controlsChkBoxesHL.add(this.cbOpenOnly, this.cbActiveOnly);

        controlsHL.add(controlsChkBoxesHL);
    }

    private void doStatusMsg()
    {
        this.statusMsgHL = new HorizontalLayout();
        this.statusMsgHL.setClassName("planningContentSectorsStatus");
        this.statusMsgHL.setHeight("20px");
    }

    private void doEditor()
    {
        this.editor = this.planTableVL.getPlanningGrid().getEditor();
        this.editor.setBinder(binder);

        //tgtPct edit
        //these listeners remain here as they are part of the GUI, not business logic
        TextField tgtPct = new TextField();
        tgtPct.getElement()
            .addEventListener("keydown", event -> editor.closeEditor())
            .setFilter("event.key === 'Tab' || event.key === 'Escape' "
                + "|| event.key === 'Esc' || event.key === 'Enter'");
        this.binder.forField(tgtPct)
            //.withvalidator
            .withConverter(new StringToDoubleConverter("Double required"))
            .bind("tgtPct");
        this.planTableVL.getPlanningGrid().getColumnByKey("tgtPct").setEditorComponent(tgtPct);

        //comment edit
        TextField comment = new TextField();
        comment.getElement()
            //can also use cancelEditor() for other keys
            .addEventListener("keydown", event -> editor.closeEditor())
            .setFilter("event.key === 'Tab' || event.key === 'Escape' "
                + "|| event.key === 'Esc' || event.key === 'Enter'");
        this.binder.forField(comment)
            //.withValidator(vldtr)
            .bind("comment");
        this.planTableVL.getPlanningGrid().getColumnByKey("comment").setEditorComponent(comment);

        //listener for command to edit the row
//        Registration gridDblClkListener = this.planTableVL.getPlanningGrid()
//            .addItemDoubleClickListener(event -> {
//                editor.editItem(event.getItem());
//                tgtPct.focus();
//            });
        //binder change update
        Registration binderChangeListener = binder.addValueChangeListener(event ->
        {
            editor.refresh();
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.serviceTPC.AppTracking("TPC:Portfolio:Tracking");

        if (this.prefsController.getPref("TPCDrawerClose").
            equalsIgnoreCase("yes"))
        {
            this.mainLayout.setDrawerOpened(false);
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event)
    {
    }
}
