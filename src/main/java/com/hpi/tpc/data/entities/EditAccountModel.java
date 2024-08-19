package com.hpi.tpc.data.entities;

/*
 * last: Jul 2020
 */
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * All account elements for account in setup
 */
public class EditAccountModel {

    public static final String SQL_ALL_ACCOUNTS;

    //dmOfx.BrokerId same as dbOfx.BrokerId
    private Integer brokerId;
    //dmOfx.AcctId auto
    private Integer dmAcctId;
    //dbOfx.AcctId auto
    private Integer dbAcctId;
    //dmOfx.Accounts.dmOfxFId dmOfx.OfxInstitutions.FIdfldProv_mProvBankId
    private String dmOfxFId;
    //dmOfx.Accounts.InvAcctIdFi dbOfx.Accounts.InvAcctIdFi 5440-8621
    private String invAcctIdFi;
    //dmOfx.Accounts.UserId
    private String userId;
    //dmOfx.Accounts.Pwd
    private String pwd;
    //dmOfx.ClientAccts.ClientAcctName
    private String clientAcctName;
    //dmOfx.ClientAccts.ClientAcctName Yes/No
    private String active;
    private Boolean bActive;

    static {
        SQL_ALL_ACCOUNTS =
            "select mAc.BrokerId, mAc.DMAcctId, mAc.DBAcctId, mAc.dmOfxFId, mAc.InvAcctIdFi, mAc.UserId, decode(mAc.Pwd, 'cpt/moc.iph') as Pwd, mAc.ClientAcctName, mAc.Active from hlhtxc5_dbOfx.Accounts as bAc, hlhtxc5_dbOfx.Brokers2 as bBr, hlhtxc5_dmOfx.Accounts2 as mAc where bAc.BrokerId = bBr.BrokerId and bAc.BrokerId = mAc.BrokerId and bAc.AcctId = mAc.DBAcctId and bAc.JoomlaId = mAc.JoomlaId and bAc.JoomlaId = mAc.JoomlaId and bAc.JoomlaId = '%s';";
    }

    @Override
    public String toString() {
        return this.clientAcctName;
    }
}
