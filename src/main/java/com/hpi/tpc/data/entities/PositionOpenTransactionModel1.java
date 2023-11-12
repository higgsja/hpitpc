package com.hpi.tpc.data.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * transaction as part of a position
 */
public class PositionOpenTransactionModel1 {
    
    public static final String GET_OPTION_BY_POSITIONID_JOOMLAID = "select fot.DmAcctId, fot.JoomlaId, fot.PositionId, fot.FiTId, fot.TransactionName, fot.Ticker, fot.DateOpen, abs(fot.Units) as Units, abs(fot.PriceOpen) as PriceOpen, fot.DateExpire, fot.Days, fot.Positiontype, PositionType, fot.TotalOpen, fot.EquityType, fot.Gain, fot.GainPct, fot.TransactionType, fot.Complete, fot.MktVal, fot.LMktVal, a2.ClientAcctName from hlhtxc5_dmOfx.PositionsOpenTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'option' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.PositionId = %s and fot.JoomlaId = %s order by fot.DateOpen desc, fot.DmAcctId;";
    
     public static final String GET_STOCK_BY_POSITIONID_JOOMLAID = "select fot.DmAcctId, fot.JoomlaId, fot.PositionId, fot.FiTId, fot.TransactionName, fot.Ticker, fot.DateOpen, abs(fot.Units) as Units, abs(fot.PriceOpen) as PriceOpen, fot.DateExpire, fot.Days, fot.Positiontype, PositionType, fot.TotalOpen, fot.EquityType, fot.Gain, fot.GainPct, fot.TransactionType, fot.Complete, fot.MktVal, fot.LMktVal, a2.ClientAcctName from hlhtxc5_dmOfx.PositionsOpenTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'stock' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.PositionId = %s and fot.JoomlaId = %s order by fot.DateOpen desc, fot.DmAcctId;";
    
    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer positionId;
    private String fiTId;
//    private String equityId;  cannot have for options do drop for all
    private String transactionName;
    private String ticker;
    private java.sql.Date dateOpen;
    private Double units;
    private Double priceOpen;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;
    private Double mktVal;
    private Double lMktVal;
    private String clientAcctName;
//    private Double actPct;    not used for positions    
}
