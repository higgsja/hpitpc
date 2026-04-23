package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class TrackingPositionGridVL
    extends PositionGridVLBase
{

    @PostConstruct
    private void init()
    {
        this.setClassName("trackingPositionsGridVL");
        this.setHeight("100%");
    }

    //filter based on client selections
    public void filterEquity(Integer sectorId, String ticker)
    {
        this.getDataProvider().clearFilters();

        //position filter
        if (sectorId != null && sectorId != -1)
        {
            this.getDataProvider().addFilter(position -> Objects
                .equals(position.getClientSectorId(), sectorId));
        }

        if (ticker != null && !ticker.isEmpty())
        {
            this.getDataProvider().addFilter(position -> position.getTicker().equalsIgnoreCase(ticker));
        }
    }
}
