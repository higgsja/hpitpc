package com.hpi.tpc.data.datamart.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class DmAccount2Mapper implements RowMapper<DmAccount2Model> {
    @Override
    public DmAccount2Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        DmAccount2Model dmAccountModel;

        dmAccountModel = DmAccount2Model.builder()
            .dmAcctId(rs.getInt("DMAcctId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .brokerId(rs.getInt("BrokerId"))
            .dbAcctId(rs.getInt("DBAcctId"))
            .invAcctIdFi(rs.getString("InvAcctIdFi"))
            .userId(rs.getString("UserId"))
            .pwd(rs.getString("Pwd"))
            .dmOfxFId(rs.getString("DmOfxFId"))
            .clientAcctName(rs.getString("ClientAcctName"))
            .active(rs.getString("Active"))
            .bActive(rs.getString("Active").equalsIgnoreCase("yes"))
            .build();

        return dmAccountModel;
    }
}
