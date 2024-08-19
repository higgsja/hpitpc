package com.hpi.tpc.charts;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.*;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import lombok.*;

@NoArgsConstructor
public class PieChart extends ApexChartsBuilder {
    public PieChart(Double[] series,
        TitleSubtitle title, String[] labels,
        String[] colors) {
        withChart(ChartBuilder.get()
            .withType(Type.PIE)
            .build())
            .withSeries(series)
            .withTitle(title)
            .withLabels(labels)
            .withColors(colors)
            .withLegend(LegendBuilder.get()
                .withShow(false)
                .build())
            .withDataLabels(DataLabelsBuilder.get()
                .withEnabled(Boolean.FALSE)
                .build())
            .build();
    }
}