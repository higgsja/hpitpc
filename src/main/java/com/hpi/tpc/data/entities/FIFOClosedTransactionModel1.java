package com.hpi.tpc.data.entities;

import lombok.*;

/**
 * Data model for table fifoClosedTransactionModel joined with clientAccts
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class FIFOClosedTransactionModel1
{
    public static final String COLUMNS = "DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, transactionName, DateOpen, DateClose, DateExpire, Units, ProceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, TransactionType, Days, ClientAcctName";

    public static final String STOCKS_BY_EQUITYID
        ="select fct.EquityId, ca.ClientAcctName, fct.TransactionName, fct.Ticker, fct.DateOpen, fct.DateClose, fct.DateExpire, abs(fct.Units) as Units, fct.PriceOpen, fct.PriceClose, fct.TotalOpen, fct.TotalClose, fct.Gain, fct.GainPct, fct.EquityType, fct.PositionType, fct.TransactionType, fct.Days, ClientAcctName from hlhtxc5_dmOfx.FIFOClosedTransactions as fct, hlhtxc5_dmOfx.ClientAccts as ca where fct.JoomlaId = ca.JoomlaId and fct.DMAcctId = ca.DMAcctId and fct.EquityId = '%s' and fct.DateClose = '%s' and fct.JoomlaId = '%s' order by fct.DateClose desc, fct.EquityId asc;";
    
    public static final String OPTIONS_BY_EQUITYID
    = "select pct.EquityId, ca.ClientAcctName, fct.TransactionName, fct.Ticker, fct.DateOpen, fct.DateClose, fct.DateExpire, abs(fct.Units) as Units, fct.PriceOpen, fct.PriceClose, fct.TotalOpen, fct.TotalClose, fct.Gain, fct.GainPct, fct.EquityType, fct.PositionType, fct.TransactionType, fct.Days, ClientAcctName from hlhtxc5_dmOfx.PositionsClosedTransactions as pct, hlhtxc5_dmOfx.FIFOClosedTransactions as fct, hlhtxc5_dmOfx.ClientAccts as ca where fct.EquityId = pct.EquityId and fct.JoomlaId = ca.JoomlaId and fct.JoomlaId = pct.JoomlaId and fct.EquityId = pct.EquityId and fct.DMAcctId = ca.DMAcctId and pct.PositionId = '%s' and fct.JoomlaId = '%s' order by fct.DateClose desc, pct.EquityId asc";
    //= "select fot.Ticker, pot.EquityId, fot.TransactionName, fot.DateOpen, fot.DateClose, fot.DateExpire, fot.Units, fot.PriceOpen, fot.PriceClose, fot.TotalOpen, fot.TotalClose, fot.MktVal + fot.TotalOpen as Gain, (100 * (fot.MktVal + fot.TotalOpen) / abs(fot.TotalOpen)) as GainPct, fot.EquityType, fot.PositionType, fot.TransactionType, fot.OptionType, fot.StrikePrice, fot.ShPerCtrct, fot.Days, fot.ClientSectorId, fot.MktVal, fot.LMktVal, ClientAcctName from hlhtxc5_dmOfx.PositionsClosedTransactions as pot, hlhtxc5_dmOfx.FIFOClosedTransactions as fot, hlhtxc5_dmOfx.ClientAccts as ca where fot.DMAcctId = ca.DMAcctId and fot.DMAcctId = pot.DMAcctId and fot.JoomlaId = ca.JoomlaId and fot.JoomlaId = pot.JoomlaId and fot.Ticker = pot.Ticker and fot.EquityId = pot.EquityId and pot.PositionId = '%s' and fot.JoomlaId ='%s' order by fot.DateOpen desc, pot.EquityId asc;";

    //do not store clientSectorId here as it can change; get it from ClientEquityAttributes as required
    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private Integer transactionGrp;
    private String ticker;
    private String equityId;
    private String transactionName;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private Double totalOpen;
    private Double totalClose;
    private Double gain;
    private Double gainPct;
    private String equityType;
    private String positionType;
    private String transactionType;
    private Integer days;
    private String clientAcctName;

}
