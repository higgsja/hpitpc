package com.hpi.tpc.ui.views.menus.data.equities.stocks;

import lombok.*;

//@Component
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class FinVizEquityInfoSort
{
    public static final String TICKER = "ticker";
    public static final String SECTOR = "sector";
    public static final String INDUSTRY = "industry";
    
    private String propertyName;
    private boolean descending;
}
