package com.hpi.tpc.ui.views.tools.hedge;

import com.hpi.tpc.ui.views.main.MainLayout;
import com.hpi.tpc.services.TPCDAOImpl;
import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.prefs.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
//@CssImport("./styles/hedgePortfolio.css")
@Route(value = ROUTE_TOOLS_HEDGE_PORTFOLIO, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_HEDGE_PORTFOLIO)
@Component
@PermitAll
public class HedgePortfolioView
    extends FlexLayout
    implements BeforeEnterObserver, BeforeLeaveObserver
{

    @Autowired private MainLayout appController;
    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private HedgePortfolioInputs inputs;
    @Autowired private HedgePortfolioOutputs outputs;
    @Autowired private PrefsController prefsController;

//    private FlexLayout topRow;
    private VerticalLayout topLeft;
    private VerticalLayout topRight;

    private final ArrayList<Registration> listeners;

    public HedgePortfolioView()
    {
        this.listeners = new ArrayList<>();
    }

    @PostConstruct
    public void init()
    {
        this.doMainContent();

        this.doTopLeft();
        this.topLeft.add(this.inputs.getInputsLayout());

        this.doTopRight();
        this.topRight.add(this.outputs.getOutputsLayout());

        this.add(this.topLeft, this.topRight);

        this.addListeners();
    }

    private void doMainContent()
    {
        this.addClassName("hedgeContent");
        this.setFlexDirection(FlexDirection.ROW);
        this.setFlexWrap(FlexWrap.WRAP);
        this.setAlignContent(ContentAlignment.START);
        this.setAlignItems(Alignment.START);

        this.setSizeFull();
    }

//    private void doTopRow()
//    {
//        this.topRow = new FlexLayout();
//        this.topRow.addClassName("hedgeTopRow");
//        this.topRow.setAlignItems(Alignment.START);
//        this.topRow.setWidth("100%");
//        this.topRow.setHeight("100%");
//        this.topRow.setFlexGrow(1, topRight);
//        this.topRow.setWrapMode(WrapMode.WRAP);
//
//        this.topRow.add(this.topLeft, this.topRight);
//    }

    private void doTopLeft()
    {
        this.topLeft = new VerticalLayout();
        this.topLeft.addClassName("inputsVL");
        this.topLeft.setAlignItems(Alignment.START);

        this.topLeft.setWidth("320px");
        this.topLeft.setMinWidth("320px");
        this.topLeft.setMaxWidth("490px");

        this.topLeft.setHeight("100%");
        this.topLeft.setMinHeight("600px");
    }

    private void doTopRight()
    {
        this.topRight = new VerticalLayout();
        this.topRight.addClassName("outputsVL");
        this.topRight.setAlignItems(Alignment.START);

        this.topRight.setMinWidth("570px");
        this.topRight.setWidth("570px");
        this.topRight.setMaxWidth("570px");

        this.topRight.setMinHeight("600px");
        this.topRight.setHeight("100%");
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event)
    {
        inputs.writeHedgeInputsPrefs();
    }

    @PreDestroy
    public void doDestroy()
    {
        //remove all listeners
        this.removeListeners();
    }

    private void addListeners()
    {
        this.listeners.add(this.inputs.getAmtInput().addValueChangeListener(
            amtInput ->
        {
            this.outputs.doHedgeTable();
        }));

        this.listeners.add(this.inputs.getExpirationCB()
            .addValueChangeListener(expCB ->
            {
                this.outputs.doHedgeTable();
            }));

        this.listeners.add(this.inputs.getTypeCB().addValueChangeListener(
            typeCB ->
        {
            this.outputs.doHedgeTable();
        }));

        this.listeners.add(this.inputs.getRealizationInput()
            .addValueChangeListener(realizationInput ->
            {
                this.outputs.doHedgeTable();
            }));

        this.listeners.add(this.inputs.getMinStrikeInput()
            .addValueChangeListener(minStrikeInput ->
            {
                this.outputs.doHedgeTable();
            }));

        this.listeners.add(this.inputs.getContractCostInput()
            .addValueChangeListener(contractCostInput ->
            {
                this.outputs.doHedgeTable();
            }));

        this.listeners.add(this.inputs.getMaxStrikeInput()
            .addValueChangeListener(maxStrikeInput ->
            {
                this.outputs.doHedgeTable();
            }));

        this.listeners.add(this.inputs.getTicketCostInput()
            .addValueChangeListener(ticketCostInput ->
            {
                this.outputs.doHedgeTable();
            }));
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

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        this.serviceTPC.AppTracking("TPC:" + TITLE_PAGE_HEDGE_PORTFOLIO);

        if (this.prefsController.getPref("TPCDrawerClose").
            equalsIgnoreCase("yes"))
        {
            this.appController.setDrawerOpened(false);
        }

        this.outputs.doHedgeTable();
    }    
}
