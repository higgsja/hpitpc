package com.hpi.tpc.data.entities;

import com.github.appreciated.apexcharts.helper.*;
import java.util.*;
import lombok.*;

@Getter
@Builder
public class TimeseriesModel {

    public static final String SQL_GAINPCT;
    public static final String SQL_ACCT_TOTALS;

private final String ticker;
private final Date date;
private final Double data;


    static {
        SQL_GAINPCT = "select Ticker, `Date`, `Close`, round(100 * (`Close` - (select `Close` as FirstClose from hlhtxc5_dmOfx.EquityHistory where `Date` = (SELECT min(`Date`) FROM hlhtxc5_dmOfx.EquityHistory WHERE year(`Date`) = year(current_date) and Ticker = '%s') and Ticker = '%s')) / (select `Close` as FirstClose from hlhtxc5_dmOfx.EquityHistory where `Date` = (SELECT min(`Date`) FROM hlhtxc5_dmOfx.EquityHistory WHERE year(`Date`) = year(current_date) and Ticker = '%s') and Ticker = '%s') , 1) as PctGain from hlhtxc5_dmOfx.EquityHistory where `Date` >= (SELECT min(`Date`) FROM hlhtxc5_dmOfx.EquityHistory WHERE year(`Date`) = year(current_date) and Ticker = '%s') and Ticker = '%s' order by `Date` asc;";
        
        SQL_ACCT_TOTALS = "select 'Portfolio' as Ticker, StmtDt as `Date`, sum(MktValue) as MktValue, round(100 * (sum(MktValue) - (select sum(MktValue) as MktValue from hlhtxc5_dmOfx.AccountTotals where StmtDt = (select min(StmtDt) from hlhtxc5_dmOfx.AccountTotals where year(StmtDt) = year(current_date) and JoomlaId = '%s') and JoomlaId = '%s' group by StmtDt)) / (select sum(MktValue) as MktValue from hlhtxc5_dmOfx.AccountTotals where StmtDt = (select min(StmtDt) from hlhtxc5_dmOfx.AccountTotals where year(StmtDt) = year(current_date) and JoomlaId = '%s') and JoomlaId = '%s' group by StmtDt), 1) as PctGain from hlhtxc5_dmOfx.AccountTotals where JoomlaId = '%s' and StmtDt >= makedate(year(now()), 1) group by StmtDt";
    }

    public static synchronized Series<Coordinate> getCoordSeries(
        String ticker, List<TimeseriesModel> timeseriesModels){
        Series seriesCoordinate;
        List<Coordinate> coordinates;
        
        coordinates = new ArrayList<>();
        
        for (TimeseriesModel tm : timeseriesModels){
            coordinates.add(new Coordinate(tm.date, tm.data));
        }
        
        Coordinate[] data = new Coordinate[coordinates.size()];
        
        for (int i = 0; i < coordinates.size(); i++){
            data[i] = coordinates.get(i);
        }
        
        seriesCoordinate = new Series();
        seriesCoordinate.setName(ticker);
        seriesCoordinate.setData(data);
        
        return seriesCoordinate;
    }
}
