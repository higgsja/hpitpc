#TPCDAOImpl.java getTickerModels
#overly cautious, all tickers should be in InvTran only. See GettickersDataValidationNew.sql

select distinct SecInfo.Ticker 
from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.BuyOpt as Bo, 
	hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo 
where Br.BrokerId = Accts.BrokerId 
	and Accts.AcctId = Bo.AcctId 
	and Accts.AcctId = InvTran.AcctId 
	and Bo.FiTId = InvTran.FiTId 
	and Br.BrokerId = SecInfo.BrokerId 
	and Accts.AcctId = '1' 
	and Accts.JoomlaId = '816' 

union 
select distinct SecInfo.Ticker 
from hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.ClosureOpt as Co, 
	hlhtxc5_dbOfx.SecInfo as SecInfo 
where Br.BrokerId = Accts.BrokerId 
	and Accts.AcctId = Co.AcctId 
	and Br.BrokerId = SecInfo.BrokerId 
	and Co.SecId = SecInfo.SecId 
	and Accts.AcctId = '1' 
	and Accts.JoomlaId = '816' 

union 
select distinct SecInfo.Ticker 
from  hlhtxc5_dbOfx.Brokers as Br, hlhtxc5_dbOfx.Accounts as Accts, hlhtxc5_dbOfx.SellOpt as Bo, 
	hlhtxc5_dbOfx.InvSell, hlhtxc5_dbOfx.SecInfo as SecInfo 
where Br.BrokerId = Accts.BrokerId 
	and Accts.AcctId = Bo.AcctId 
	and Accts.AcctId = InvSell.AcctId 
	and Bo.FiTId = InvSell.FiTId 
	and Br.BrokerId = SecInfo.BrokerId 
	and InvSell.SecId = SecInfo.SecId 
	and Accts.AcctId = '1' 
	and Accts.JoomlaId = '816' 

union 
select distinct cco.Ticker 
from hlhtxc5_dmOfx.ClientClosingOptions as cco, hlhtxc5_dmOfx.Accounts as Accts 
where cco.DMAcctId = Accts.DMAcctId 
	and cco.JoomlaId = Accts.JoomlaId 
	and Accts.AcctId = '1' 
	and Accts.JoomlaId = '816' 

union 
select distinct coo.Ticker 
from hlhtxc5_dmOfx.ClientOpeningOptions as coo, hlhtxc5_dmOfx.Accounts as Accts 
where coo.DMAcctId = Accts.DMAcctId 
	and coo.JoomlaId = Accts.JoomlaId 
	and Accts.AcctId = '1' 
	and Accts.JoomlaId = '816' 

order by Ticker;