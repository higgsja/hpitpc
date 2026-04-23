package com.hpi.tpc.ui.views.coaching.gains.chart;

import com.github.appreciated.apexcharts.*;
import com.hpi.tpc.charts.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.coaching.gains.*;
import com.vaadin.flow.spring.annotation.*;
import java.text.*;
import jakarta.annotation.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
public class GainsChartVL
    extends ViewBaseVL
{

    @Autowired private GainsVLModel gainsVLModel;
    @Autowired private GainsControlsHL2 gainsControlsHL2;

    public GainsChartVL()
    {
        this.addClassName("coachingGainsChartVL");
    }

    @PostConstruct
    private void construct()
    {
        this.getStyle().set("padding", "0px 0px 16px 5px");

        this.add(this.addChart("PositionsClosed"));
    }

    public ApexCharts addChart(String dataTable)
    {
        ApexCharts chart;
        TornadoChart tornadoChart;

        this.gainsVLModel.getChartData(dataTable);

        this.gainsControlsHL2.getTotal().setText("Total: "
            + NumberFormat.getCurrencyInstance().format(this.gainsVLModel.getGainsTotal()));

        if (!this.gainsVLModel.getGainsModels().isEmpty())
        {
            if (this.gainsVLModel.getSelectedChartType().equalsIgnoreCase("%"))
            {
                tornadoChart = new TornadoChart(this.gainsVLModel.getChartPctMin(),
                    this.gainsVLModel.getChartPctMax(),
                    GainModel.getCoordSeries(this.gainsVLModel.getGainsModels(),
                        this.gainsVLModel.getSelectedChartType()));

//                builder = new TornadoChart(this.gainsVLModel.getChartPctMin(),
//                    this.gainsVLModel.getChartPctMax(),
//                    GainModel.getCoordSeries(this.gainsVLModel.getGainsModels(),
//                        this.gainsVLModel.getSelectedChartType()));
            } else
            {
                tornadoChart = new TornadoChart(this.gainsVLModel.getChartMin(), this.gainsVLModel.getChartMax(),
                    GainModel.getCoordSeries(this.gainsVLModel.getGainsModels(),
                        this.gainsVLModel.getSelectedChartType()));
//                builder = new TornadoChart(this.gainsVLModel.getChartMin(), this.gainsVLModel.getChartMax(),
//                    GainModel.getCoordSeries(this.gainsVLModel.getGainsModels(),
//                        this.gainsVLModel.getSelectedChartType()));
            }

//            this.gainsVLModel.setChart(builder.build());
//            this.gainsVLModel.getChart().addClassName("coachingGainsChart");
//            this.gainsVLModel.getChart().setHeight("100%");
            //remove the old chart
//            this.removeAll();
//            return this.gainsVLModel.getChart();
            chart = tornadoChart.build();
            chart.setHeight("100%");
//            chart.addClassName("coachingGainsChart");
            return chart;
        } else
        {
            //remove the old chart
            this.removeAll();

//            this.add(new Label("*** No Data Available ***"));
            return null;
        }
    }
}
