package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@Getter
@Builder
public class TransactionModel {

    private final String equityId;
    private final Date transactionDate;
    private final Date openDate;
    private final Date closeDate;
    private final Double units;
    private final Integer daysToExpiry;
    private final String account;
    private final Double gainPct;
    private final String description;
    private final Integer clientSectorId;
    private final String ticker;
    private final Double price;
    private final Double priceOpen;
    private final Double priceClose;
    private final String transType;
    private final String active;
    private final Double actPct;

//    public static final String SQL_ALL_TRANSACTIONS;

//    static {
//        SQL_ALL_TRANSACTIONS =
//            "select po.Ticker, po.EquityId, Units, Days, null as TransType, GainPct, ClientAcctName as Acct, null as `Comment`, GMTDtTrade as `OpenDate`, null as `CloseDate`, po.ClientSectorId , IPrice as UnitPriceOpen, null as UnitPriceClose, Price as Price, GMTDtTrade as `Date`, cea.Active, po.GainPct from hlhtxc5_dmOfx.PositionsOpen as po, hlhtxc5_dmOfx.ClientAccts as ca, hlhtxc5_dmOfx.ClientEquityAttributes as cea where po.JoomlaId = ca.JoomlaId and po.JoomlaId = cea.JoomlaId and po.Ticker = cea.Ticker and po.DMAcctId = ca.DMAcctId and po.JoomlaId = '%s' union select cea.Ticker, EquityId, Units, Days, Transtype, round(GainPct, 1) as GainPct, ClientAcctName as Acct, null as `Comment`, null as `OpenDate`, GMTDTTradeClose as `CloseDate`, cea.ClientSectorId, UnitPriceOpen, UnitPriceClose, UnitPriceClose as Price, GMTDtTradeClose as `Date`, cea.Active, round(ActPct, 1) as ActPct from hlhtxc5_dmOfx.FIFOClosed as fc, hlhtxc5_dmOfx.ClientAccts as ca, hlhtxc5_dmOfx.ClientEquityAttributes as cea where fc.JoomlaId = '%s' and fc.JoomlaId = ca.JoomlaId and fc.DMAcctId = ca.DMAcctId and fc.Ticker = cea.Ticker and fc.ClientSectorId = cea.ClientSectorId and GMTDtTradeClose > date_sub(curdate(), interval %s day) order by OpenDate desc, CloseDate desc, EquityId;";
            
            
            
//            "select hlhtxc5_dmOfx.FIFOTransactions.Ticker, EquityId, Units, Days, null as TransType, GainPct, ClientAcctName as Acct, null as `Comment`, GMTDtTrade as `OpenDate`, null as `CloseDate`, hlhtxc5_dmOfx.FIFOTransactions.ClientSectorId , UnitPrice as UnitPriceOpen, null as UnitPriceClose, UnitPrice as Price, GMTDtTrade as `Date`, hlhtxc5_dmOfx.ClientEquityAttributes.Active, hlhtxc5_dmOfx.FIFOTransactions.ActPct from hlhtxc5_dmOfx.FIFOTransactions, hlhtxc5_dmOfx.ClientAccts, hlhtxc5_dmOfx.ClientEquityAttributes where FIFOPositions.JoomlaId = ClientAccts.JoomlaId and FIFOPositions.JoomlaId = ClientEquityAttributes.JoomlaId and FIFOPositions.Ticker = ClientEquityAttributes.Ticker and FIFOPositions.DMAcctId = ClientAccts.DMAcctId and FIFOPositions.JoomlaId = '%s' union select ClientEquityAttributes.Ticker, EquityId, Units, Days, Transtype, round(GainPct, 1) as GainPct, ClientAcctName as Acct, null as `Comment`, null as `OpenDate`, GMTDTTradeClose as `CloseDate`, ClientEquityAttributes.ClientSectorId, UnitPriceOpen, UnitPriceClose, UnitPriceClose as Price, GMTDtTradeClose as `Date`, ClientEquityAttributes.Active, round(ActPct, 1) as ActPct from hlhtxc5_dmOfx.FIFOClosed, hlhtxc5_dmOfx.ClientAccts, hlhtxc5_dmOfx.ClientEquityAttributes where FIFOClosed.JoomlaId = '%s' and FIFOClosed.JoomlaId = ClientAccts.JoomlaId and FIFOClosed.DMAcctId = ClientAccts.DMAcctId and FIFOClosed.Ticker = ClientEquityAttributes.Ticker and FIFOClosed.ClientSectorId = ClientEquityAttributes.ClientSectorId and GMTDtTradeClose > date_sub(curdate(), interval %s day) order by OpenDate desc, CloseDate desc, EquityId;";
//    }
}
