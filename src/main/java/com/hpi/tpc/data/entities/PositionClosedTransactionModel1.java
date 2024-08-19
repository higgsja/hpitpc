package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/*
 * Holds a transaction as one leg of a position
 */
public class PositionClosedTransactionModel1 {
     public static final String GET_BY_POSITIONID_JOOMLAID = "select fot.DmAcctId, fot.JoomlaId, fot.PositionId, fot.FiTId, fot.EquityId, fot.TransactionName, fot.Ticker, fot.DateOpen, fot.DateClose, abs(fot.Units) as Units, abs(fot.PriceOpen) as PriceOpen, abs(fot.PriceClose) as PriceClose, fot.DateExpire, fot.Days, fot.PositionType, fot.TotalOpen, fot.TotalClose, fot.EquityType, fot.Gain, fot.GainPct, fot.TransactionType, fot.Complete, a2.ClientAcctName from hlhtxc5_dmOfx.PositionsClosedTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.PositionId = %s and fot.JoomlaId = %s order by fot.DateOpen desc, fot.DmAcctId;";
     
//     public static final String GET_OPTION_BY_EQUITYID_DATECLOSE_JOOMLAID = "select fot.DmAcctId, fot.JoomlaId, fot.PositionId, fot.FiTId, fot.EquityId, fot.TransactionName, fot.Ticker, fot.DateOpen, fot.DateClose, abs(fot.Units) as Units, abs(fot.PriceOpen), abs(fot.PriceClose), fot.DateExpire, fot.Days, fot.PositionType, fot.TotalOpen, fot.TotalClose, fot.EquityType, fot.Gain, fot.GainPct, fot.TransactionType, fot.Complete, a2.ClientAcctName from hlhtxc5_dmOfx.PositionsClosedTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'option' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.PositionId = %s and fot.JoomlaId = %s order by fot.DateOpen desc, fot.DmAcctId;";
     

//    public static final String GET_OPTION_BY_EQUITYID_DATECLOSE_JOOMLAID = "select fot.DmAcctId, fot.JoomlaId, fot.PositionId, fot.FiTId, fot.EquityId, fot.TransactionName, fot.Ticker, fot.DateOpen, fot.DateClose, abs(fot.Units) as Units, abs(fot.PriceOpen), abs(fot.PriceClose), fot.DateExpire, fot.Days, PositionType, fot.TotalOpen, fot.TotalClose, fot.EquityType, fot.Gain, fot.GainPct, fot.TransactionType, fot.Complete, a2.ClientAcctName from hlhtxc5_dmOfx.PositionsClosedTransactions as fot, hlhtxc5_dmOfx.Accounts2 as a2 where fot.EquityType = 'option' and fot.DMAcctId = a2.DMAcctId and fot.JoomlaId = a2.JoomlaId and fot.PositionId = %s and fot.JoomlaId = %s order by fot.DateOpen desc, fot.DmAcctId;";
    


    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer positionId;
    private String fiTId;
    private String equityId;
    private String transactionName;
    private String ticker;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;  //use the latest date in cases where multiple close dates
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private Double totalClose;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;
    private String clientAcctName;

    //one or more transactions make up the transaction model (lots)
    @Builder.Default @Getter private final 
        ArrayList<FIFOClosedTransactionModel> fifoClosedTransactionModels = new ArrayList<>();
    
    public void setBComplete(Boolean complete) {
        this.bComplete = complete;

        if (this.bComplete) {
            this.complete = 1;
        }
        else {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete) {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }
}
