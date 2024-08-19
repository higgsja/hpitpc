package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class EditAccountMapper implements RowMapper<EditAccountModel> {
    @Override
    public EditAccountModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        EditAccountModel accountModel;

        accountModel = EditAccountModel.builder()
            .brokerId(rs.getInt("BrokerId"))
            .dmAcctId(rs.getInt("DMAcctId"))
            .dbAcctId(rs.getInt("DBAcctId"))
            .dmOfxFId(rs.getString("dmOfxFId"))
            .invAcctIdFi(rs.getString("InvAcctIdFi"))
            .userId(rs.getString("UserId"))
            .pwd(rs.getString("Pwd"))
            .clientAcctName(rs.getString("ClientAcctName"))
            .active(rs.getString("Active"))
            .bActive(rs.getString("Active").equalsIgnoreCase("yes"))
            .build();

        return accountModel;
    }
}
