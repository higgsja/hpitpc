package com.hpi.tpc.data.entities;

import java.time.*;
import static java.time.temporal.TemporalAdjusters.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class TradeTacticModel
{

    private final Integer tacticId;
    private final String tacticName;
    private final String tacticDescr;
    private final String tacticEquityType;
    private final Integer active;

    @Override
    public String toString()
    {
        return this.tacticName;
    }

    public static final String TACTICS
        ="select * from hlhtxc5_dmOfx.TradeTactics tt where tt.TacticId in (select distinct po.TacticId from hlhtxc5_dmOfx.%s as po where if ('%s' = '--All--', po.Ticker like '%%', po.Ticker = '%s') and if (%s = -1, po.TacticId like '%%', po.TacticId = %s) %s and if('%s' = '--All--', po.EquityType like '%%', po.EquityType = '%s') and JoomlaId = %s) order by tt.TacticName;";
}
