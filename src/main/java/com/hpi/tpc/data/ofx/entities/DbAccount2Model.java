package com.hpi.tpc.data.ofx.entities;

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
public class DbAccount2Model {

    public static final String SQL_ALL_ACCOUNTS;
    public static final String SQL_DBOFX_ACCOUNTS_EXISTS;
    public static final String SQL_DBOFX_ACCOUNTS_EXISTS2;
    public static final String SQL_DBOFX_ACCOUNTS_INSERT;
    public static final String SQL_DBOFX_ACCOUNTS_UPDATE;

    private Integer dbAcctId;
    private Integer dbBrokerId;
    private Integer joomlaId;
    private String invAcctIdFi;

    static {
        SQL_ALL_ACCOUNTS =
            "select AcctId, BrokerId, JoomlaId, InvAcctIdFi from hlhtxc5_dbOfx.Accounts where JoomlaId = '%s';";
    SQL_DBOFX_ACCOUNTS_EXISTS = "select AcctId, BrokerId, JoomlaId, InvAcctIdFi from hlhtxc5_dbOfx.Accounts where AcctId = '%s' and BrokerId = '%s' and InvAcctIdFi = '%s' and JoomlaId = '%s';";
    SQL_DBOFX_ACCOUNTS_EXISTS2 = "select AcctId, BrokerId, JoomlaId, InvAcctIdFi from hlhtxc5_dbOfx.Accounts where BrokerId = '%s' and InvAcctIdFi = '%s' and JoomlaId = '%s';";
    SQL_DBOFX_ACCOUNTS_INSERT = "insert into hlhtxc5_dbOfx.Accounts (BrokerId, JoomlaId, InvAcctIdFi) values ('%s', '%s', '%s');";
    SQL_DBOFX_ACCOUNTS_UPDATE = "update hlhtxc5_dbOfx.Accounts set InvAcctIdFi = '%s' where AcctId = '%s';";
    }

    @Override
    public String toString() {
        return this.invAcctIdFi;
    }
}
