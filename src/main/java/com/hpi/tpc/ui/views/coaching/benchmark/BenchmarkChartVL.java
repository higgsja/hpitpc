package com.hpi.tpc.ui.views.coaching.benchmark;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;

@UIScope
@VaadinSessionScope
public class BenchmarkChartVL
    extends ViewBaseVL
{

    public BenchmarkChartVL()
    {
        this.addClassName("benchmarkChartVL");
    }

    @PostConstruct
    private void construct()
    {
        //configure VL
        this.doLayout();        
    }

    private void doLayout()
    {
        this.setMinWidth("320px");
        this.setWidth("600px");
        this.setMaxWidth("600px");
        this.setMinHeight("320px");
        this.setMaxHeight("600px");
        this.setHeight("600px");
    }
}
