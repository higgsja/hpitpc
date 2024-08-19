package com.hpi.tpc.data.entities;

import com.github.appreciated.apexcharts.helper.*;
import java.util.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GainModel
{

    public static final String SQL_GAINS_BY_TACTIC = "select A.Category, A.Gain, 100 * (A.Gain / abs(A.TotalOpen)) as GainPct from (select tt.TacticName as Category, sum(po.TotalOpen) as TotalOpen, sum(po.TotalClose) as TotalClose, sum(po.Gain) as Gain from hlhtxc5_dmOfx.%s as po, hlhtxc5_dmOfx.TradeTactics tt where po.TacticId = tt.TacticId and if ('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if (%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and if('%s' = '--All--', po.EquityType like '%%', po.EquityType = '%s') and JoomlaId = %s group by tt.TacticName) as A order by if('%s' = '%%', GainPct, Gain) desc";
    
    public static final String SQL_GAINS_BY_POSITION = "select A.Category, A.Gain, 100 * (A.Gain / abs(A.TotalOpen)) as GainPct from (select po.Ticker as Category, sum(po.TotalOpen) as TotalOpen, sum(po.TotalClose) as TotalClose, sum(po.Gain) as Gain from hlhtxc5_dmOfx.%s as po, hlhtxc5_dmOfx.TradeTactics tt where po.TacticId = tt.TacticId and if ('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if (%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and if('%s' = '--All--', po.EquityType like '%%', po.EquityType = '%s') and JoomlaId = %s group by po.Ticker) as A order by if('%s' = '%%', GainPct, Gain) desc";
    
    public static final String YEAR = " and po.DateClose >= makedate(year(curdate()), 1) ";

    private String category;  //sector or ticker or ...
    private Double gain;
    private Double gainPct;

    public static synchronized Series<Coordinate> getCoordSeries(List<GainModel> gainsModels, String chartType)
    {
        Series seriesCoordinate;
        List<Coordinate> coordinates;

        coordinates = new ArrayList<>();

        if (chartType.equalsIgnoreCase("%"))
        {
            for (GainModel gm : gainsModels)
            {
                coordinates.add(new Coordinate(gm.category, gm.gainPct));
            }
        } else
        {
            for (GainModel gm : gainsModels)
            {
                coordinates.add(new Coordinate(gm.category, gm.gain));
            }
        }

        Coordinate[] data = new Coordinate[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++)
        {
            data[i] = coordinates.get(i);
        }

        seriesCoordinate = new Series();
        seriesCoordinate.setName("");
        seriesCoordinate.setData(data);

        return seriesCoordinate;
    }
}
