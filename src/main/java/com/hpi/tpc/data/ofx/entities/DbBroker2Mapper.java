package com.hpi.tpc.data.ofx.entities;

/**
 * last Aug 2020
 */

import java.sql.*;
import org.springframework.jdbc.core.*;

public class DbBroker2Mapper implements RowMapper<DbBroker2Model> {
    @Override
    public DbBroker2Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        DbBroker2Model dBBrokerModel;

        dBBrokerModel = DbBroker2Model.builder()
            .dbBrokerId(rs.getInt("BrokerId"))
            .name(rs.getString("Name"))
            .url(rs.getString("Url"))
            .ofxInstFId(rs.getString("OfxInstFId"))
            .build();

        return dBBrokerModel;
    }
}
