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
public class DmAccount2Model {

    public static final String SQL_DMOFX_ACCOUNTS_ALL;
    public static final String SQL_DMOFX_ACCOUNTS_EXISTS;
    public static final String SQL_DMOFX_ACCOUNTS_INSERT;
    public static final String SQL_DMOFX_ACCOUNTS_UPDATE;

    //dmOfx.AcctId auto
    private Integer dmAcctId;
    private Integer joomlaId;
    //dmOfx.BrokerId dbOfx.Brokers.BrokerId=dmOfx.Brokers.BrokerId
    private Integer brokerId;
    //DBAcctId
    private Integer dbAcctId;
    //dmOfx.Accounts.InvAcctIdFi dbOfx.Accounts.InvAcctIdFi 5440-8621
    private String invAcctIdFi;
    //dmOfx.Accounts.UserId
    private String userId;
    //dmOfx.Accounts.Pwd
    private String pwd;
    //dmOfx.Accounts.dmOfxFId dmOfx.OfxInstitutions.FIdfldProv_mProvBankId unique
    private String dmOfxFId;
    //dmOfx.ClientAccts.ClientAcctName
    private String clientAcctName;
    //dmOfx.ClientAccts.Active Yes/No
    private String active;
    private Boolean bActive;

    static {
        SQL_DMOFX_ACCOUNTS_ALL =
            "select DMAcctId, JoomlaId, BrokerId, AcctId, Org, FId, BrokerIdFi, InvAcctIdFi, UserId, Pwd, DmOfxFId from hlhtxc5_dmOfx.Accounts2 where JoomlaId = '%s';";
    SQL_DMOFX_ACCOUNTS_EXISTS = "select DMAcctId, JoomlaId, BrokerId, DBAcctId, InvAcctIdFi, UserId, Pwd, DmOfxFId, ClientAcctName, Active from hlhtxc5_dmOfx.Accounts2 where DMAcctId = '%s' and BrokerId = '%s' and JoomlaId = '%s';";
    SQL_DMOFX_ACCOUNTS_INSERT = "insert into hlhtxc5_dmOfx.Accounts2 (JoomlaId, BrokerId, DBAcctId, InvAcctIdFi, UserId, Pwd, DmOfxFId, ClientAcctName, Active) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
    SQL_DMOFX_ACCOUNTS_UPDATE = "update hlhtxc5_dmOfx.Accounts2 set InvAcctIdFi = '%s', UserId = '%s', Pwd = encode('%s', 'cpt/moc.iph'), ClientAcctname = '%s', Active = '%s' where DMAcctId = '%s';";
    }

    @Override
    public String toString() {
        return this.clientAcctName;
    }
}
