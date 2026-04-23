package com.hpi.tpc.charts;

import com.github.appreciated.apexcharts.*;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.*;
import com.github.appreciated.apexcharts.config.chart.builder.*;
import com.github.appreciated.apexcharts.config.chart.zoom.*;
import com.github.appreciated.apexcharts.config.plotoptions.builder.*;
import com.github.appreciated.apexcharts.config.theme.*;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Coordinate;
import com.github.appreciated.apexcharts.helper.Series;
import static java.lang.Math.*;

public class TornadoChart
    extends ApexChartsBuilder
{

    public TornadoChart(Double chartMin, Double chartMax, Series<Coordinate>... series)
    {
        Double range;
        Double rangeTic;
        Integer count;
        Double tmpChartMin, tmpChartMax;

        tmpChartMin = tmpChartMax = 0.0;

        range = abs(chartMin) + abs(chartMax);
        rangeTic = range / 6.0;
        count = 0;

        while (rangeTic > 10.0)
        {
            rangeTic = rangeTic / 10.0;
            count++;
        }

        rangeTic = ((Long) round(rangeTic + 0.5)).doubleValue();

        if (count > 0)
        {
            for (int i = 0; i < count - 1; i++)
            {
                rangeTic = rangeTic * 10.0;
            }
        }

        //rangeTick would be a nice number here
        //want xMin and xMax to be equal in absolute terms
        if (chartMin < 0.0)
        {
            if (abs(chartMin) < abs(chartMax))
            {
                chartMin = -chartMax;
            } else
            {
                chartMax = -chartMin;
            }

            while (tmpChartMin >= chartMin)
            {
                tmpChartMin -= rangeTic;
            }
        } else
        {
            //for now leaving 0.0 as a fixed lower bound
            chartMin = 0.0;
        }

        if (chartMax >= 0.0)
        {
            while (tmpChartMax <= chartMax)
            {
                tmpChartMax += rangeTic;
            }
        } else
        {
        }

        this.withChart(ChartBuilder.get()
            .withType(Type.BAR)
            .withStackType(StackType.FULL)
            //            .withForeColor("#16E2F3")
            .withZoom(ZoomBuilder.get()
                .withEnabled(true)
                .withType(ZoomType.X)
                .build())
            .build())
            .withSeries(series)
            //          .withColors("#73D216", "#EF2929")
            .withXaxis(XAxisBuilder.get()
                .withType(XAxisType.CATEGORIES)
                .withMin(tmpChartMin)
                .withMax(tmpChartMax)
                .build())
            .withPlotOptions(PlotOptionsBuilder.get()
                .withBar(BarBuilder.get()
                    .withHorizontal(true)
                    .build())
                .build())
            .withTheme(ThemeBuilder.get()
                .withMode(Mode.DARK)
                .build())
            .withDataLabels(DataLabelsBuilder.get()
                .withEnabled(false)
                .build())
            .build();

//        this.withChart(ChartBuilder.get().withHeight("100%").build());
    }
}
