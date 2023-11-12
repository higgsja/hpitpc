package com.hpi.tpc.data.entities;

import lombok.*;

@Getter
@Setter
@Builder
public class ValidateOptionTransactionModel {
    public static final String SQL_STRING;
    public static final String SQL_TICKERS;
    public static final String SQL_UPDATE_INVTRAN;

    private Integer joomlaId;
    private Integer acctId;
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
    private Integer shPerCtrct;
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
        SQL_STRING =
            "(select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvBuy.Units, InvBuy.UnitPrice as TradePrice, InvBuy.Markup as MarkUpDn, InvBuy.Commission, InvBuy.Taxes, InvBuy.Fees, InvBuy.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Bo.FiTId, Bo.OptBuyType as OptTransactionType, Bo.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.BuyOpt as Bo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvBuy, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvTran.AcctId and Bo.FiTId = InvTran.FiTId and Accts.AcctId = InvBuy.AcctId and Bo.FiTId = InvBuy.FiTId and Br.BrokerId = SecInfo.BrokerId and InvBuy.SecId = SecInfo.SecId and Accts.AcctId = '%s' and EquityId like '%s%%' and Accts.JoomlaId = '%s') union (select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, Co.Units, 0.0 as TradePrice, 0.0 as MarkUpDn, 0.0 as Commission, 0.0 as Taxes, 0.0 as Fees, 0.0 as Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Co.FiTId, Co.OptAction as OptTransactionType, Co.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.ClosureOpt as Co, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Co.AcctId and Accts.AcctId = InvTran.AcctId and Co.FiTId = InvTran.FiTId and Br.BrokerId = SecInfo.BrokerId and Co.SecId = SecInfo.SecId and Accts.AcctId = '%s' and EquityId like '%s%%' and Accts.JoomlaId = '%s') union (select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvSell.Units, InvSell.UnitPrice as TradePrice, InvSell.Markdown as MarkUpDn, InvSell.Commission, InvSell.Taxes, InvSell.Fees, InvSell.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Bo.FiTId, Bo.OptSellType as OptTransactionType, Bo.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.SellOpt as Bo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvSell, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvTran.AcctId and Bo.FiTId = InvTran.FiTId and Accts.AcctId = InvSell.AcctId and Bo.FiTId = InvSell.FiTId and Br.BrokerId = SecInfo.BrokerId and InvSell.SecId = SecInfo.SecId and Accts.AcctId = '%s' and EquityId like '%s%%' and Accts.JoomlaId = '%s') union /* ClientClosingOption */ (select cco.JoomlaId, Accts.AcctId, cco.EquityId, cco.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, cco.Units, cco.PriceOpen as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, DateOpen as DtTrade, cco.FiTId, TransactionType as TransactionType, '100', '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.ClientClosingOptions as cco, hlhtxc5_dmOfx.Accounts as Accts where cco.DMAcctId = Accts.DMAcctId and cco.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and EquityId like '%s%%' and Accts.JoomlaId = '%s') union /* ClientOpeningOption */ (select coo.JoomlaId, Accts.AcctId, coo.EquityId, coo.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, coo.Units, coo.PriceOpen as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, DateOpen as DtTrade, coo.FiTId, TransactionType as TransactionType, '100', '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.ClientOpeningOptions as coo, hlhtxc5_dmOfx.Accounts as Accts where coo.DMAcctId = Accts.DMAcctId and coo.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and EquityId like '%s%%' and Accts.JoomlaId = '%s') order by EquityId, FiTId";
//            "(select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvBuy.Units, InvBuy.UnitPrice as TradePrice, InvBuy.Markup as MarkUpDn, InvBuy.Commission, InvBuy.Taxes, InvBuy.Fees, InvBuy.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Bo.FiTId, Bo.OptBuyType as OptTransactionType, Bo.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.BuyOpt as Bo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvBuy, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvTran.AcctId and Bo.FiTId = InvTran.FiTId and Accts.AcctId = InvBuy.AcctId and Bo.FiTId = InvBuy.FiTId and Br.BrokerId = SecInfo.BrokerId and InvBuy.SecId = SecInfo.SecId and Accts.JoomlaId = '%s') union (select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, Co.Units, 0.0 as TradePrice, 0.0 as MarkUpDn, 0.0 as Commission, 0.0 as Taxes, 0.0 as Fees, 0.0 as Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Co.FiTId, Co.OptAction as OptTransactionType, Co.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.ClosureOpt as Co, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Co.AcctId and Accts.AcctId = InvTran.AcctId and Co.FiTId = InvTran.FiTId and Br.BrokerId = SecInfo.BrokerId and Co.SecId = SecInfo.SecId and Accts.JoomlaId = '%s') union (select Accts.JoomlaId, Accts.AcctId, SecInfo.EquityId, SecInfo.Ticker, SecInfo.SecName, SecInfo.UnitPrice as LastPrice, left(SecInfo.DtAsOf, 8) as DtAsOf, InvSell.Units, InvSell.UnitPrice as TradePrice, InvSell.Markdown as MarkUpDn, InvSell.Commission, InvSell.Taxes, InvSell.Fees, InvSell.Total, Br.BrokerId, left(InvTran.DtTrade, 8) as DtTrade, Bo.FiTId, Bo.OptSellType as OptTransactionType, Bo.ShPerCtrct, InvTran.Skip, InvTran.Validated, InvTran.Complete from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.SellOpt as Bo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvSell, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvTran.AcctId and Bo.FiTId = InvTran.FiTId and Accts.AcctId = InvSell.AcctId and Bo.FiTId = InvSell.FiTId and Br.BrokerId = SecInfo.BrokerId and InvSell.SecId = SecInfo.SecId and Accts.JoomlaId = '%s') union /* clientClosingOption */ (select cco.JoomlaId, Accts.AcctId, cco.EquityId, cco.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, cco.Units, cco.UnitPrice as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, GMTDtTrade as DtTrade, cco.FiTId, OptTransType as TransactionType, '100', '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.clientClosingOptions as cco, hlhtxc5_dmOfx.Accounts as Accts where cco.DMAcctId = Accts.DMAcctId and cco.JoomlaId = Accts.JoomlaId and Accts.JoomlaId = '%s') union /* clientOpeningOption */ (select coo.JoomlaId, Accts.AcctId, coo.EquityId, coo.Ticker, '' as SecName, '' as LastPrice, '' as DtAsOf, coo.Units, coo.UnitPrice as TradePrice, '' as MarkUpDn, '' as Commission, '' as Taxes, '' as Fees, '' as Total, '' as BrokerId, GMTDtTrade as DtTrade, coo.FiTId, OptTransType as TransactionType, '100', '0' as Skip, '1' as Validated, '0' as Complete from hlhtxc5_dmOfx.clientOpeningOptions as coo, hlhtxc5_dmOfx.Accounts as Accts where coo.DMAcctId = Accts.DMAcctId and coo.JoomlaId = Accts.JoomlaId and Accts.JoomlaId = '%s') order by EquityId, FiTId";

        SQL_TICKERS = "select distinct SecInfo.Ticker from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.BuyOpt as Bo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvTran.AcctId and Bo.FiTId = InvTran.FiTId and Br.BrokerId = SecInfo.BrokerId and Accts.AcctId = '%s' and Accts.JoomlaId = '%s' union select distinct SecInfo.Ticker from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.ClosureOpt as Co, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Co.AcctId and Br.BrokerId = SecInfo.BrokerId and Co.SecId = SecInfo.SecId and Accts.AcctId = '%s' and Accts.JoomlaId = '%s' union select distinct SecInfo.Ticker from  hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.SellOpt as Bo, hlhtxc5_dbOfx.InvSell, hlhtxc5_dbOfx.SecInfo as SecInfo where Br.BrokerId = Accts.BrokerId and Accts.AcctId = Bo.AcctId and Accts.AcctId = InvSell.AcctId and Bo.FiTId = InvSell.FiTId and Br.BrokerId = SecInfo.BrokerId and InvSell.SecId = SecInfo.SecId and Accts.AcctId = '%s' and Accts.JoomlaId = '%s' union select distinct cco.Ticker from hlhtxc5_dmOfx.ClientClosingOptions as cco, hlhtxc5_dmOfx.Accounts as Accts where cco.DMAcctId = Accts.DMAcctId and cco.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and Accts.JoomlaId = '%s' union select distinct coo.Ticker from hlhtxc5_dmOfx.ClientOpeningOptions as coo, hlhtxc5_dmOfx.Accounts as Accts where coo.DMAcctId = Accts.DMAcctId and coo.JoomlaId = Accts.JoomlaId and Accts.AcctId = '%s' and Accts.JoomlaId = '%s' order by Ticker;";
        
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
