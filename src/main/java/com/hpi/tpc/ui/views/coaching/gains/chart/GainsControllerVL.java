package com.hpi.tpc.ui.views.coaching.gains.chart;

import com.github.appreciated.apexcharts.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.coaching.gains.*;
import com.hpi.tpc.ui.views.coaching.gains.positions.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

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
//@Route(value = ROUTE_COACHING_GAINS_CONTROLLER, layout = MainLayout.class)
//@PermitAll
@Component
public class GainsControllerVL
    extends ViewControllerBaseVL
    implements BeforeEnterObserver
{

    @Autowired private GainsVLModel gainsVLModel;
    @Autowired private GainsControlsHL1 gainsControlsHL1;
    @Autowired private GainsControlsHL2 gainsControlsHL2;
    @Autowired private GainsChartVL gainsChartVL;
    @Autowired private PositionsControllerVL positionsControllerVL;
    @Autowired private GainsPositionGridVL gainsPositionsGridVL;

    private GainsTitleVL gainsTitleVL;

    public GainsControllerVL()
    {
        this.addClassName("gainsControllerVL");

        this.getStyle().set("padding", "0px 0px 16px 5px");
    }

    @PostConstruct
    private void construct()
    {

        //title
        this.gainsTitleVL = new GainsTitleVL("Gains");
        this.add(this.gainsTitleVL);

        //controls
        this.gainsControlsHL1.doLayout();
        this.add(this.gainsControlsHL1);

        this.gainsControlsHL2.doLayout();
        this.add(this.gainsControlsHL2);

        //chart
        this.gainsChartVL.setMinWidth("320px");
        this.gainsChartVL.setMaxWidth("550px");
        this.gainsChartVL.setWidth("550px");

        this.gainsChartVL.setMinHeight("320px");
//        this.gainsChartVL.setMaxHeight("600px");
//        this.gainsChartVL.setHeight("600px");
        this.add(this.gainsChartVL);

        this.doSelectionListenersAdd();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        super.beforeEnter(event);

        //change the preferences route
        super.updateNavBarGear(null);
    }

    private void doSelectionListenersAdd()
    {
        //positions
        this.gainsControlsHL1.getCbPositions().addValueChangeListener(position ->
        {
            //eliminate recursion
            if (this.gainsVLModel.getBInSelections())
            {
                return;
            }

            //reset the selectedTicker
            this.gainsVLModel.setSelectedPositionModel(position.getValue());

            //reset the selections
            this.doResetOnSelections(position.getValue(), null, null, null, null);
        });

        //tactics
        this.gainsControlsHL1.getCbTactics().addValueChangeListener(event ->
        {
            //eliminate recursion
            if (this.gainsVLModel.getBInSelections())
            {
                return;
            }

            //reset the selectedTradeTactic
            this.gainsVLModel.setSelectedTradeTacticModel(event.getValue());

            //reset the selections
            this.doResetOnSelections(null, event.getValue().getTacticId(), null, null, null);
        });

        //timeframe
        this.gainsControlsHL1.getCbTimeframe().addValueChangeListener(event ->
        {
            //eliminate recursion
            if (this.gainsVLModel.getBInSelections())
            {
                return;
            }

            //reset selectedTimeframe
            this.gainsVLModel.setSelectedTimeframeModel(event.getValue());

            //reset the selections
            this.doResetOnSelections(null, null,
                this.gainsVLModel.getSelectedTimeframeModel().getTimeframe(), null, null);
        });

        //equityType
        this.gainsControlsHL2.getCbEquityType().addValueChangeListener(event ->
        {
            //eliminate recursion
            if (this.gainsVLModel.getBInSelections())
            {
                return;
            }

            //reset selectedEquityType
            this.gainsVLModel.setSelectedEquityTypeModel(event.getValue());

            //reset the selections
            this.doResetOnSelections(null, null, null, event.getValue(), null);
        });

        //radio buttons
        this.gainsControlsHL2.getRadioButtonGroup().addValueChangeListener(event ->
        {
            if (event.getValue() == null)
            {
                //should never happen
                int i = 0;
            } else
            {
                this.gainsVLModel.setSelectedChartType(event.getValue());

                //reset the selections
                this.doResetOnSelections(null, null, null, null, event.getValue());
            }
        });
    }

    /**
     * triggered on change to any selection; only one change processed at a time
     * all others will be null
     */
    private void doResetOnSelections(PositionModel positionModel, Integer tactic,
        String timeframe, EquityTypeModel equityType, String aChartType)
    {
        String dataTable;
        ApexCharts aCharts;

        //avoid recursive issues with listeners
        this.gainsVLModel.setBInSelections(true);

        //different data table for open v closed
        if (this.gainsVLModel.getSelectedTimeframeModel().getTimeframe().equalsIgnoreCase("open"))
        {
            //use positionsOpen data table
            dataTable = "PositionsOpen";
        } else
        {
            //use positionsClosed data table
            dataTable = "PositionsClosed";
        }

        if (aChartType != null)
        {
            //chart type change
            //do nothing extra
//            this.setChartType(dataTable, false);
        }

        if (positionModel != null)
        {
            //doing a position change
            this.gainsControlsHL1.setPositions(dataTable, false);

            //adjust the tactics list to match selected position
            this.gainsControlsHL1.setTactics(dataTable, false);

            //adjust timeFrame
            this.gainsControlsHL1.setTimeframe(false);

            //adjust equityTypes
            this.gainsControlsHL2.setEquityTypes(dataTable, false);
        }

        if (tactic != null)
        {
            //doing a tactic change
            this.gainsControlsHL1.setTactics(dataTable, false);

            //adjust positions
            this.gainsControlsHL1.setPositions(dataTable, false);

            //adjust timeFrame
            this.gainsControlsHL1.setTimeframe(false);

            //adjust equityTypes
            this.gainsControlsHL2.setEquityTypes(dataTable, false);
        }

        if (timeframe != null)
        {
            //doing a timeframe change
            this.gainsControlsHL1.setTimeframe(false);

            //adjust positions
            this.gainsControlsHL1.setPositions(dataTable, false);

            //adjust the tactics list to match selected position
            this.gainsControlsHL1.setTactics(dataTable, false);

            //adjust equityTypes
            this.gainsControlsHL2.setEquityTypes(dataTable, false);
        }

        if (equityType != null)
        {
            //doing an equityType change
            this.gainsControlsHL2.setEquityTypes(dataTable, false);

            //adjust positions
            this.gainsControlsHL1.setPositions(dataTable, false);

            //adjust the tactics list to match selected position
            this.gainsControlsHL1.setTactics(dataTable, false);

            //adjust timeFrame
            this.gainsControlsHL1.setTimeframe(false);
        }

        //remove old chart
        this.gainsChartVL.removeAll();

        if ((aCharts = this.gainsChartVL.addChart(dataTable)) != null)
        {
            this.gainsChartVL.add(aCharts);
        } else
        {
            this.gainsChartVL.add(new NativeLabel("*** No Data Available ***"));
        }

        //reset the data selections
        this.gainsPositionsGridVL
            .filterPositions(
                this.gainsVLModel.getSelectedPositionModel().getTicker(),
                this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
                this.gainsVLModel.getSelectedTimeframeModel().getTimeframe(),
                this.gainsVLModel.getSelectedEquityTypeModel().getEquityType());
        //reset recursion control
        this.gainsVLModel.setBInSelections(false);

        //all set to change chart
//        this.gainsChartVL.add(this.gainsChartVL.addChart(dataTable));
    }

    @Override
    public void addMenuBarTabs()
    {
        //none; not changing top tabs
    }
}
