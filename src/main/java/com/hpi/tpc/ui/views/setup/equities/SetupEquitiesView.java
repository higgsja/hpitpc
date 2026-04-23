package com.hpi.tpc.ui.views.setup.equities;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
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
 * Provides view
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
@Route(value = SETUP_EQUITIES_VIEW, layout = MainLayout.class)
@PermitAll
//@CssImport("./styles/setupEquities.css")
public class SetupEquitiesView
    extends ViewControllerBaseFL
    implements BeforeEnterObserver
{

    @Getter @Autowired private SetupEquitiesTableVL setupEquitiesTableVL;

    private final ArrayList<Registration> listeners;

    private final NativeLabel equityTitle;

    private HorizontalLayout setupControlsHL;
    @Getter private Button newButton;
    @Getter private Button cancelButton;
    @Getter private Button saveButton;

    @Getter private HorizontalLayout statusMsgHL;

    public SetupEquitiesView()
    {
        this.addClassName("setupEquitiesContent");

        this.listeners = new ArrayList<>();

        this.equityTitle = new NativeLabel();

        this.setSizeFull();
    }

    @PostConstruct
    private void construct()
    {
        this.setupEquitiesTableVL.setSetupEquitiesMVCView(this);
        //force width to ensure full display of table
        this.setMaxWidth("600px");

        //title
        this.doTitle();
        this.add(this.equityTitle);

        //controls
        this.doControls();
        this.add(this.setupControlsHL);

        //status message
        this.doStatusMsg();
        this.add(this.statusMsgHL);

        //equities table
        this.add(this.setupEquitiesTableVL);

        //table editor; must wait until data is set in table
        //actually, not allowing edit here, only in the plan/tracking panels
        //this.doEditor();
    }

    @PreDestroy
    private void destruct()
    {
        this.removeListeners();
    }

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
        this.equityTitle.setText("Equities Preferences");
        this.equityTitle.getElement().getStyle().set("font-size", "14px");
        this.equityTitle.getElement().getStyle().set("font-family", "Arial");
        this.equityTitle.getElement().getStyle().set("color", "#169FF3");
        this.equityTitle.getElement().getStyle().set("margin-top", "0px");
        this.equityTitle.getElement().getStyle().set("margin-bottom", "0px");
        this.equityTitle.getElement().getStyle().set("margin-block-start", "0px");
        this.equityTitle.getElement().getStyle().set("margin-block-end", "0px");
        this.equityTitle.getElement().getStyle().set("line-height", "1em");
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

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
//        if (this.prefsController.getPref("TPCDrawerClose").equalsIgnoreCase("yes"))
//        {
//            this.appController.setDrawerOpened(false);
//        }
    }

    @Override
    public void addMenuBarTabs()
    {
        //none
    }
}
