package com.hpi.tpc.data.entities;

import lombok.*;

@Getter
@Setter
@Builder
public class ValidateStockTransactionModel {
    public static final String SQL_STRING;
//    public static final String SQL_TICKERS;
    public static final String SQL_UPDATE_INVTRAN;

    private Integer joomlaId;
    private Integer dbAcctId;
    private String equityId;
    private String ticker;
    private String secName;
    private Double lastPrice;
    private String dtAsOf;
    private Double units;
    private Double tradePrice;
    private Double markUpDn;
    private Double commission;
    private Double taxes;
    private Double fees;
    private Double total;
    private Integer brokerId;
    private String dtTrade;
    private String fiTId;
    private String transactionType;
    private Integer skip;
    private Boolean bSkip;
    //client manually looked at and validated the entry
    private Integer validated;
    private Boolean bValidated;
    //entire transaction is complete in the dataMart
    //  case where buy 100, sell 50 leaves 50: not complete
    //  we want to continue to work this transaction until complete
    private Integer complete;
    private Boolean bComplete;

    static {
        //parameters: joomlaId, acctiId, joomlaId, acctId, joomlaId, acctId
        //todo: this also has the option tables in it ...
        SQL_STRING =
            "(select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvBuy.Units, InvBuy.UnitPrice as TradePrice, InvBuy.Markup as MarkUpDn, InvBuy.Commission, InvBuy.Taxes, InvBuy.Fees, InvBuy.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, bs.FiTId, bs.BuyType as TransactionType, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.BuyStock as bs, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvBuy, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = bs.AcctId and Accts.AcctId = InvTran.AcctId and bs.FiTId = InvTran.FiTId and Accts.AcctId = InvBuy.AcctId and bs.FiTId = InvBuy.FiTId and Br.BrokerId = SecInfo.BrokerId and InvBuy.SecId = SecInfo.SecId and Accts.AcctId = '%s' and EquityId = '%s' and Accts.JoomlaId = '%s') union (select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvSell.Units, InvSell.UnitPrice as TradePrice, InvSell.Markdown as MarkUpDn, InvSell.Commission, InvSell.Taxes, InvSell.Fees, InvSell.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, ss.FiTId, ss.SellType as TransactionType, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.SellStock as ss, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvSell, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = ss.AcctId and Accts.AcctId = InvTran.AcctId and ss.FiTId = InvTran.FiTId and Accts.AcctId = InvSell.AcctId and ss.FiTId = InvSell.FiTId and Br.BrokerId = SecInfo.BrokerId and InvSell.SecId = SecInfo.SecId and Accts.AcctId = '%s' and EquityId = '%s' and Accts.JoomlaId = '%s') union /* ClientClosingStock */ (select ccs.JoomlaId, Accts.AcctId, ccs.EquityId, ccs.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, ccs.Units, ccs.PriceOpen as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, DateOpen as DtTrade, ccs.FiTId, TransactionType as TransactionType, '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.ClientClosingStock as ccs, hlhtxc5_dmOfx.Accounts as Accts where ccs.DMAcctId = Accts.DMAcctId and ccs.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and EquityId = '%s' and Accts.JoomlaId = '%s') union /* ClientOpeningStock */ (select cos.JoomlaId, Accts.AcctId, cos.EquityId, cos.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, cos.Units, cos.PriceOpen as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, DateOpen as DtTrade, cos.FiTId, TransactionType as TransactionType, '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.ClientOpeningStock as cos, hlhtxc5_dmOfx.Accounts as Accts where cos.DMAcctId = Accts.DMAcctId and cos.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and EquityId = '%s' and Accts.JoomlaId = '%s') order by EquityId, FiTId;";

//        SQL_TICKERS = "select distinct A.Ticker from (" +
//                      SQL_STRING +
//                      ") as A order by Ticker;";
        
        SQL_UPDATE_INVTRAN = "update hlhtxc5_dbOfx.InvTran set Skip = '%s', Validated = '%s' where AcctId = '%s' and FiTId = '%s'";
    }

    @Override
    public String toString() {
        return this.equityId;
    }

    public void setBSkip(Boolean skip) {
        this.bSkip = skip;

        if (bSkip) {
            this.skip = 1;
        }
        else {
            this.skip = 0;
        }
    }

    public void setBValidated(Boolean skip) {
        this.bValidated = skip;

        if (bValidated) {
            this.validated = 1;
        }
        else {
            this.validated = 0;
        }
    }

    public void setBComplete(Boolean skip) {
        this.bComplete = skip;

        if (bComplete) {
            this.complete = 1;
        }
        else {
            this.complete = 0;
        }
    }
}
