package com.hpi.tpc.data.entities;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class TimeframeModel
{

    //only need to know there is one record
    public static final String SQL_TIMEFRAMES = "select * from hlhtxc5_dmOfx.%s as po where if ('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if (%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and JoomlaId = %s limit 1";;
    
    public static final String LAST_YEAR = "Last Year";
    public static final String LAST_QUARTER = "Last Quarter";
    public static final String LAST_MONTH = "Last Month";
    public static final String LAST_WEEK = "Last Week";
    public static final String WEEK = "Week";
    public static final String MONTH = "Month";
    public static final String QUARTER = "Quarter";
    public static final String YTD = "YTD";
    public static final String OPEN = "Open";
    

    private String timeframe;

    @Override
    public String toString()
    {
        return this.timeframe;
    }
}
