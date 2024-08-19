package com.hpi.tpc.ui.views.portfolio.plan.charts;

import com.github.appreciated.apexcharts.*;
import com.github.appreciated.apexcharts.config.*;
import com.hpi.tpc.charts.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Provides the charts view of the portfolio
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
//@Route(value = MENU_PORTFOLIO_PLAN_CHARTS_VIEW, layout = MainLayout.class)
//@PageTitle(TITLE_PORTFOLIO + ": " + TITLE_PORTFOLIO_PLAN)
//@CssImport("./styles/portfolioPlanning.css")
public class PlanChartsMVC_V
    extends VerticalLayout{

    public PlanChartsMVC_V() {
        this.addClassName("planningContentCharts");
        this.setMaxWidth("350px");
    }

    @PostConstruct
    private void construct() {
        //hit
    }

    @PreDestroy
    private void destruct() {
    }
    
    public void doCharts(Double[] doublesTargetValues, String[]stringsTargetLabels,
        Double[] doublesActualValues, String[]stringsActualLabels){
        TitleSubtitle titleTarget;
        TitleSubtitle titleActual;
        String[] colors;
        ApexChartsBuilder targetBuilder;
        ApexChartsBuilder actualBuilder;
        ApexCharts chartTarget;
        ApexCharts chartActual;
        VerticalLayout chartTargetLayout;
        VerticalLayout chartActualLayout;
        
        this.removeAll();

        //todo: there is a fix to make this work
        colors = new String[]{
            "#00ff00", "#ff0000", "#0000ff",
            "#ffff00", "#ffc800", "#ff7f00",
            "#00ffff", "#00ffc8", "#00ff7f",
            "#c8c840", "#c840c8", "#c8407f",
            "#7f407f", "#40407f", "#404040",
            "#407f7f", "#40107f", "#404010",
            "#9c0000", "#9c4000", "#9c4040",
            "#d3d7cf", "#d3d77f", "#d37f7f"};

        titleTarget = new TitleSubtitle();
        titleTarget.setText("Target");

        titleActual = new TitleSubtitle();
        titleActual.setText("Actual");

        //target
        targetBuilder = new PieChart(doublesTargetValues, titleTarget,
            stringsTargetLabels, colors);
        chartTarget = targetBuilder.build();
        chartTarget.setClassName("planningChart");
        chartTarget.setWidth("300px");

        //actual
        actualBuilder = new PieChart(doublesActualValues, titleActual,
            stringsActualLabels, colors);
        chartActual = actualBuilder.build();
        chartActual.setClassName("actualChart");
        chartActual.setWidth("300px");

        this.add(chartTarget, chartActual);
    }
}