package com.hpi.tpc.data.entities;

import lombok.*;

@Getter @Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode
public class EquityTypeModel
{

    public static final String SQL_DISTINCT_EQUITY_TYPES;
//    public static final String YTD;

    private String equityType;

    static
    {
//        YTD = " and po.DateClose >= '" 
//            + java.sql.Date.valueOf(LocalDate.now().with(firstDayOfYear())).toString() 
//            + "' and po.DateClose <= '" 
//            + java.sql.Date.valueOf(LocalDate.now().toString()) + "' ";
//        SQL_DISTINCT_EQUITY_TYPES
//            = "select distinct pc.EquityType from hlhtxc5_dmOfx.%s pc where if ('%s' = '--All--', pc.Ticker like '%', pc.Ticker = '%s') and if (%s = -1, pc.TacticId like '%', pc.TacticId = %s) and if ('%s' = 'year', pc.DateClose >= concat(year(current_date), '-01-01'), pc.DateClose > '2001-01-01') and if ('%s' = 'year', pc.DateClose <= concat(year(current_date), '-', month(current_date), '-', day(current_date)), pc.DateClose > '2001-01-01') and JoomlaId = %s order by pc.EquityType";

        SQL_DISTINCT_EQUITY_TYPES = "select distinct po.EquityType from hlhtxc5_dmOfx.%s as po where if ('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if (%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and JoomlaId = '%s' order by po.EquityType";
    }

    @Override
    public String toString()
    {
        return this.equityType;
    }
}
