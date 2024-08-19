package com.hpi.tpc.data.datamart.entities;

/*
 * last: Aug 2020
 */
import java.sql.*;
import org.springframework.jdbc.core.*;

public class DmAccountMapper implements RowMapper<DmAccountModel> {
    @Override
    public DmAccountModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        DmAccountModel accountModel;

        accountModel = DmAccountModel.builder()
            .dmAcctId(rs.getInt("DMAcctId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .brokerId(rs.getInt("BrokerId"))
            .org(rs.getString("Org"))
            .dmOfxFId(rs.getString("dmOfxFId"))
            .brokerIdFi(rs.getString("BrokerIdFi"))
            .invAcctIdFi(rs.getString("InvAcctIdFi"))
            .userId(rs.getString("UserId"))
            .pwd(rs.getString("Pwd"))
            .build();

        return accountModel;
    }
}
