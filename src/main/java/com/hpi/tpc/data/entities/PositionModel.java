package com.hpi.tpc.data.entities;

import java.time.*;
import java.time.temporal.*;
import static java.time.temporal.TemporalAdjusters.*;
import java.util.*;
import lombok.*;

@AllArgsConstructor @Getter @Setter @Builder
/* combines positionsOpen and positionsClosed for complete view of current and historical positions
 */
public class PositionModel
{

    @Override
    public String toString()
    {
        return this.ticker;
    }

    //combined open and closed positions
    public static final String SQL_1YR_ALL_OPEN_CLOSE_POSITIONS
        = "select * from (select 1 as Rank, po.PositionId, po.JoomlaId, cea.ClientSectorId, po.Ticker, po.EquityId, po.PositionName, po.TacticId, Units, abs(po.PriceOpen) as PriceOpen, abs(po.Price) as Price, po.GainPct, DateOpen, DateClose, DateOpen as `Date`, Days, po.Gain, PositionType, EquityType from hlhtxc5_dmOfx.PositionsOpen as po, hlhtxc5_dmOfx.ClientEquityAttributes as cea where po.JoomlaId = cea.JoomlaId and po.Ticker = cea.Ticker and po.JoomlaId = '%s') as a union all select * from (select 2 as Rank, pc.PositionId, pc.JoomlaId, cea.ClientSectorId, pc.Ticker, pc.EquityId, PositionName, TacticId, abs(Units) as Units, abs(pc.PriceOpen) as PriceOpen, abs(pc.Price) as Price, GainPct, DateOpen, DateClose, DateClose as `Date`, Days, Gain, PositionType, EquityType from hlhtxc5_dmOfx.PositionsClosed as pc, hlhtxc5_dmOfx.ClientEquityAttributes as cea where pc.JoomlaId = cea.JoomlaId and pc.Ticker = cea.Ticker and DateClose >= date_sub(current_date(), interval 1 year) and pc.JoomlaId = '%s') as b order by Rank, `Date` desc, PositionName asc;";

    /**
     * return tickers based on selections of position, tactic, timeframe, equityType selections
     */
    public static final String POSITION_TACTIC_TIMEFRAME_EQUITYTYPE = "select distinct po.Ticker from hlhtxc5_dmOfx.%s as po, hlhtxc5_dmOfx.TradeTactics tt where po.TacticId = tt.TacticId and if('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if(%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and if('%s' = '--All--', po.EquityType like '%%', po.EquityType = '%s') and po.JoomlaId = %s order by po.Ticker;";

    //SQL fragments to handle differing timeframes for positions
    public static final String SQL_LAST_YEAR = " and po.DateClose >= '"
        + (Year.now(ZoneId.of("America/Chicago")).minus(Period.ofYears(1))).toString() + "-01-01'"
        + " and po.DateClose <= '"
        + (Year.now(ZoneId.of("America/Chicago")).minus(Period.ofYears(1))).toString() + "-12-31'";
    
        //if going back crosses a year, this retains current year instead
    public static final String SQL_LAST_QUARTER = " and po.DateClose >= '"
        + ((LocalDate.now(ZoneId.of("America/Chicago"))).minusMonths(3)
                    .with(TemporalAdjusters.firstDayOfMonth()))
        + "' and po.DateClose <= '"
        + ((LocalDate.now(ZoneId.of("America/Chicago"))).minusMonths(3).plusMonths(2)
                    .with(TemporalAdjusters.lastDayOfMonth()))
        + "' ";

    public static final String SQL_LAST_MONTH = " and po.DateClose >= '"
        + ((LocalDate.now(ZoneId.of("America/Chicago"))).minusMonths(1)).with(TemporalAdjusters.firstDayOfMonth())
        + "' and po.DateClose <= '"
        + ((LocalDate.now(ZoneId.of("America/Chicago"))).minusMonths(1)).with(TemporalAdjusters.lastDayOfMonth())
        + "' ";

    //closed from latest Sunday to Sunday prior
    public static String SQL_LAST_WEEK = " and po.DateClose >= '"
        + (LocalDate.now(ZoneId.of("America/Chicago")).with(TemporalAdjusters
            .previous(DayOfWeek.SUNDAY))).minusDays(7)
        + "' and po.DateClose <= '"
        + LocalDate.now(ZoneId.of("America/Chicago")).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        + "' ";

    public static final String SQL_OPEN = "";

    public static String SQL_WEEK = " and po.DateClose >= '"
        + (LocalDate.now(ZoneId.of("America/Chicago")).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)))
        + "' ";

    public static final String SQL_MONTH = " and po.DateClose >= '"
        + (LocalDate.now(ZoneId.of("America/Chicago")).with(TemporalAdjusters.firstDayOfMonth()))
        + "' ";

    public static final String SQL_QUARTER = " and po.DateClose >= '"
        + (LocalDate.now(ZoneId.of("America/Chicago"))).with((LocalDate.now())
            .getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
        + "' and po.DateClose <= '"
        + ((LocalDate.now(ZoneId.of("America/Chicago"))).with((LocalDate.now()).getMonth().firstMonthOfQuarter()
            .plus(2)).with(TemporalAdjusters.lastDayOfMonth()))
        + "' ";

    public static final String SQL_YTD = " and po.DateClose >= '"
        + java.sql.Date.valueOf(LocalDate.now(ZoneId.of("America/Chicago")).with(firstDayOfYear())).toString()
        + "' and po.DateClose <= '"
        + java.sql.Date.valueOf(LocalDate.now(ZoneId.of("America/Chicago")).toString()) + "' ";

    private Integer positionId;
    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer clientSectorId;
    private String ticker;
    private String equityId;
    private Integer tacticId;
    private String positionName;
//    private String clientAcctName;
    private Double units;
    private Double priceOpen;
    private Double price;   //will be current price for open; closed price for closed
    private Double gain;
    private Double gainPct;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private Integer days;
    private Date date;      //will be dateOpen for open positions; dateClose for closed
    private String positionType;
    private String equityType;
//    private Double mktVal;
//    private Double lMktVal;
//    private Double actPct;
}
