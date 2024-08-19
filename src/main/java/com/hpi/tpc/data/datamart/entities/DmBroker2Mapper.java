package com.hpi.tpc.data.datamart.entities;

/**
 * last: Aug 2020
 */
import java.sql.*;
import org.springframework.jdbc.core.*;

public class DmBroker2Mapper implements RowMapper<DmBroker2Model> {
    @Override
    public DmBroker2Model mapRow(ResultSet rs, int rowNum) throws SQLException {
        DmBroker2Model dmBrokerModel;

        dmBrokerModel = DmBroker2Model.builder()
            .dmBrokerId(rs.getInt("BrokerId"))
            .name(rs.getString("Name"))
            .url(rs.getString("Url"))
            .ofxInstFId(rs.getString("OfxInstFId"))
            .build();

        return dmBrokerModel;
    }
}
