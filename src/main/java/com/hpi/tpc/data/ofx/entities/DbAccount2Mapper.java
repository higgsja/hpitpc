package com.hpi.tpc.data.ofx.entities;

/*
 * last: Aug 2020
 */
import java.sql.*;
import org.springframework.jdbc.core.*;

public class DbAccount2Mapper implements RowMapper<DbAccount2Model> {
    @Override
    public DbAccount2Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        DbAccount2Model accountModel;

        accountModel = DbAccount2Model.builder()
            .dbAcctId(rs.getInt("AcctId"))
            .dbBrokerId(rs.getInt("BrokerId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .invAcctIdFi(rs.getString("InvAcctIdFi"))
            .build();

        return accountModel;
    }
}
