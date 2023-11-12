package com.hpi.tpc.data.entities;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter @Setter
public class FIFOOpenTransactionModel1
{

    public static final String GET_STOCK_BY_TICKER_DATEOPEN_JOOMLAID
        = "select fot.DmAcctId, fot.JoomlaId, fot.Ticker, fot.EquityId, fot.TransactionName, fot.DateOpen, fot.DateClose, abs(fot.Units) as Units, fot.PriceOpen, fot.PriceClose, fot.Days, fot.MktVal + fot.TotalOpen as Gain, (100 * (fot.MktVal + fot.TotalOpen) / abs(fot.TotalOpen)) as GainPct, EquityType, PositionType, TransactionType, a2.ClientAcctName from hlhtxc5_dmOfx.FIFOOpenTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'stock' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.EquityId <> 'cash' and fot.Ticker = '%s' and fot.JoomlaId = '%s' order by fot.DateOpen desc, fot.DmAcctId;";
    
public static final String GET_OPTION_BY_EQUITYID_DATEOPEN_JOOMLAID
        = "select fot.DmAcctId, fot.JoomlaId, fot.Ticker, fot.EquityId, fot.TransactionName, fot.DateOpen, fot.DateClose, abs(fot.Units) as Units, fot.PriceOpen, fot.PriceClose, fot.Days, fot.MktVal + fot.TotalOpen as Gain, (100 * (fot.MktVal + fot.TotalOpen) / abs(fot.TotalOpen)) as GainPct, EquityType, PositionType, TransactionType, a2.ClientAcctName from hlhtxc5_dmOfx.FIFOOpenTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'option' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and EquityId = '%s' and fot.JoomlaId = '%s' order by fot.DateOpen desc, fot.DmAcctId;";

    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTId;
    private final String ticker;
    private final String equityId;
    private final String transactionName;
    private final java.sql.Date dateOpen;
    private final java.sql.Date dateClose;
    private final java.sql.Date dateExpire;
    private final Double units;
    private final Double priceOpen;
    private final Double priceClose;
    private final Double totalOpen;
    private final Double totalClose;
    private final Double gain;
    private final Double gainPct;
    private final String equityType;
    private final String positionType;
    private final String transactionType;
    private final Integer complete;
    private final String optionType;
    private final Double strikePrice;
    private final Integer shPerCtrct;
    private final Integer days;
    private final Integer clientSectorId;
    private final Double mktVal;
    private final Double lMktVal;
    private final Double actPct;
    private final Boolean bComplete;
    private final String clientAcctName;
}
