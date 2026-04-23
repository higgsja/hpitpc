package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@Getter @Setter
@EqualsAndHashCode @ToString
@Builder
public class HedgeOptionVerticalModel
{
    private final ArrayList<HedgeOptionVerticalModel> 
        verticalModels = new ArrayList<>();
    @Getter private static final String SQL;
    private String equityId;
    private String ticker;
    private String putCall;
    private String buyStrike;
    private String sellStrike;
    private String spreadCost;
    private String calcMultiple;
    private String realizedMultiple;
    private String contracts;
    private String breakEven;
    private String cost;

    static
    {
        SQL =
            "select EquityId, Symbol, AskPrice, LastPrice, PutCall, StrikePrice, OpenInterest, DataDate from OptionHistory where Symbol = '%s' and DataDate = (select max(DataDate) as DataDate from OptionHistory where Symbol = '%s') and ExpirationDate = '%s' and PutCall = '%s' and StrikePrice >= '%s' and StrikePrice <= '%s' order by StrikePrice;";
    }

    public HedgeOptionVerticalModel(String equityId, String ticker,
        String putCall, String buyStrike,
        String sellStrike, String spreadCost, String calcMultiple,
        String realizedMultiple, String contracts, String breakEven,
        String cost)
    {
        this.equityId = equityId;
        this.ticker = ticker;
        this.putCall = putCall;
        this.buyStrike = buyStrike;
        this.sellStrike = sellStrike;
        this.spreadCost = spreadCost;
        this.calcMultiple = calcMultiple;
        this.realizedMultiple = realizedMultiple;
        this.contracts = contracts;
        this.breakEven = breakEven;
        this.cost = cost;
    }
}
