package com.hpi.tpc.ui.views.setup.sectors;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.editor.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.*;
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
@Route(value = SETUP_SECTORS_VIEW, layout = MainLayout.class)
@PermitAll
//@CssImport("./styles/setupSectors.css")
public class SetupSectorsView
    extends VerticalLayout
    implements BeforeEnterObserver
{

    @Getter @Autowired private SetupSectorTableVL setupSectorTableVL;

    private final ArrayList<Registration> listeners;

    private final NativeLabel sectorTitle;

    private HorizontalLayout setupControlsHL;
    @Getter private Button newButton;
    @Getter private Button cancelButton;
    @Getter private Button saveButton;

    @Getter private HorizontalLayout statusMsgHL;

    @Getter private final Binder<ClientSectorModel> binder
        = new Binder<>(ClientSectorModel.class);
    @Getter private Editor<ClientSectorModel> editor;

    public SetupSectorsView()
    {
        this.addClassName("setupSectorsContentSectors");

        this.listeners = new ArrayList<>();

        this.sectorTitle = new NativeLabel();

        this.setSizeFull();
    }

    @PostConstruct
    private void construct()
    {
        this.setupSectorTableVL.setSetupSectorsMVCView(this);
        //force width to ensure full display of table
        //todo: should this be in PlanMVCView? No as it should not know how wide this needs to be
        this.setMaxWidth("600px");
//        this.setMinWidth("300px");

        //title
        this.doTitle();
        this.add(this.sectorTitle);

        //controls
        this.doControls();
        this.add(this.setupControlsHL);

        //status message
        this.doStatusMsg();
        this.add(this.statusMsgHL);

        //sectors table
        this.add(this.setupSectorTableVL);

        //table editor; must wait until data is set in table
        //this.doEditor();
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
        this.sectorTitle.setText("Sector Preferences");
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
        this.setupControlsHL = new HorizontalLayout();
        this.setupControlsHL.setClassName("setupSectorsContentControls");
        this.setupControlsHL.setWidth("100%");
//        controlsHL.setHeight("55px");

        //controls buttons
        HorizontalLayout controlsBtnsHL = new HorizontalLayout();
        controlsBtnsHL.setClassName("setupSectorsContentControlsBtns");
        controlsBtnsHL.setWidth("60%");
        controlsBtnsHL.setAlignItems(Alignment.CENTER);
        controlsBtnsHL.setAlignItems(Alignment.START);
        controlsBtnsHL.setJustifyContentMode(JustifyContentMode.START);
        this.newButton = new Button("New");
        this.cancelButton = new Button("Cancel");
//        this.cancelButton.setWidth("250px");
        this.saveButton = new Button("Save");
        controlsBtnsHL.add(newButton, cancelButton, saveButton);
        this.setupControlsHL.add(controlsBtnsHL);

        this.saveButton.setEnabled(false);
        this.cancelButton.setEnabled(false);
        this.newButton.setEnabled(true);
    }

    private void doStatusMsg()
    {
        this.statusMsgHL = new HorizontalLayout();
        this.statusMsgHL.setClassName("setupSectorsContentStatus");
        this.statusMsgHL.setHeight("20px");
    }

    public void doEditor()
    {
        this.editor = this.setupSectorTableVL.getSetupSectorsGrid().getEditor();
        this.editor.setBinder(binder);

        //cSecShort edit; fails on the bind for the column
        TextField shortSector = new TextField();
        shortSector.getElement()
            //can also use cancelEditor() for other keys
            .addEventListener("keydown", event -> editor.closeEditor())
            .setFilter("event.key === 'Tab' || event.key === 'Escape' "
                + "|| event.key === 'Esc' || event.key === 'Enter'");
        this.binder.forField(shortSector)
            //.withValidator(vldtr)
            .bind("CSecShort");
        this.setupSectorTableVL.getSetupSectorsGrid().getColumnByKey("cSecShort").setEditorComponent(shortSector);

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
        this.setupSectorTableVL.getSetupSectorsGrid().getColumnByKey("comment").setEditorComponent(comment);

        //binder change update
        Registration binderChangeListener = binder.addValueChangeListener(event ->
        {
            editor.refresh();
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
//        if (this.prefsController.getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
//        {
//            this.appController.setDrawerOpened(false);
//        }

    }
}
