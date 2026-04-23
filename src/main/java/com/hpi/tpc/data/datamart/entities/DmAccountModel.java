package com.hpi.tpc.data.datamart.entities;

/*
 * last: Aug 2020
 */
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * All account elements for setup
 */
public class DmAccountModel {

//    public static final String SQL_DMOFX_ACCOUNTS_ALL;
    public static final String SQL_DMOFX_ACCOUNTS_EXISTS;
    public static final String SQL_DMOFX_ACCOUNTS_INSERT;
    public static final String SQL_DMOFX_ACCOUNTS_UPDATE;

    //dmOfx.AcctId auto
    private Integer dmAcctId;
    private Integer joomlaId;
    //dmOfx.BrokerId dbOfx.Brokers.BrokerId=dmOfx.Brokers.BrokerId
    private Integer brokerId;
    //dmOfx.Brokers.Org dmOfxAccounts.Org etrade.com
    private String org;
    //dmOfx.Accounts.dmOfxFId dmOfx.OfxInstitutions.FIdfldProv_mProvBankId unique
    private String dmOfxFId;
    //dmOfx.Accounts.BrokerIdFi dbOfx.Brokers.BrokerIdFi etrade.com
    private String brokerIdFi;
    //dmOfx.Accounts.InvAcctIdFi dbOfx.Accounts.InvAcctIdFi 5440-8621
    private String invAcctIdFi;
    //dmOfx.Accounts.UserId
    private String userId;
    //dmOfx.Accounts.Pwd
    private String pwd;

    static {
//        SQL_DMOFX_ACCOUNTS_ALL =
//            "select mAc.BrokerId, mAc.DMAcctId, mAc.AcctId, mAc.Org, mAc.dmOfxFId, mAc.BrokerIdFi, mAc.InvAcctIdFi, mAc.UserId, decode(mAc.Pwd, 'cpt/moc.iph') as Pwd, mCa.ClientAcctName, mCa.Active from hlhtxc5_dbOfx.Accounts as bAc, hlhtxc5_dbOfx.Brokers as bBr, hlhtxc5_dmOfx.Accounts as mAc, hlhtxc5_dmOfx.ClientAccts as mCa where bAc.BrokerId = bBr.BrokerId and bAc.BrokerId = mAc.BrokerId and bAc.AcctId = mAc.AcctId and bAc.JoomlaId = mAc.JoomlaId and mAc.DMAcctId = mCa.DMAcctId and bAc.JoomlaId = mCa.JoomlaId and bAc.JoomlaId = '%s'";
//            "select bAc.BrokerId, bAc.AcctId, bBr.Org, mCa.ClientAcctName, mCa.Active from hlhtxc5_dbOfx.Accounts as bAc, hlhtxc5_dbOfx.Brokers as bBr, hlhtxc5_dmOfx.Accounts as mAc, hlhtxc5_dmOfx.ClientAccts as mCa where bAc.BrokerId = bBr.BrokerId and bAc.BrokerId = mAc.BrokerId and bAc.AcctId = mAc.AcctId and bAc.JoomlaId = mAc.JoomlaId and mAc.DMAcctId = mCa.DMAcctId and bAc.JoomlaId = mCa.JoomlaId and bAc.JoomlaId = '%s'";

    SQL_DMOFX_ACCOUNTS_EXISTS = "";
    SQL_DMOFX_ACCOUNTS_INSERT = "";
    SQL_DMOFX_ACCOUNTS_UPDATE = "";
    }

    @Override
    public String toString() {
        return this.invAcctIdFi;
    }
}
