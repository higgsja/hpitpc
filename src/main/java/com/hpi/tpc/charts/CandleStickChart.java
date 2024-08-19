package com.hpi.tpc.charts;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.*;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Coordinate;
import com.github.appreciated.apexcharts.helper.Series;

public class CandleStickChart extends ApexChartsBuilder {
    public CandleStickChart(Series<Coordinate> series, Annotations annotations) {
        withChart(ChartBuilder.get()
            .withType(Type.CANDLESTICK)
            .build())
            .withSeries(series)
            .withAnnotations(annotations)
            .withXaxis(XAxisBuilder.get()
                .withType(XAxisType.DATETIME)
                .build())
            .withYaxis(YAxisBuilder.get()
                .withTooltip(TooltipBuilder.get()
                    .withEnabled(true)
                    .build())
                .build());
    }
}