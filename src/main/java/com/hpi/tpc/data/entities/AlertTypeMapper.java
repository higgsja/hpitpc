package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class AlertTypeMapper implements RowMapper<AlertTypeModel> {
    @Override
    public AlertTypeModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        AlertTypeModel alertTypeModel;

        alertTypeModel = AlertTypeModel.builder()
            .kVal(rs.getInt("KVal"))
                .triggerType(rs.getString("TriggerType"))
                .active(rs.getInt("Active"))
                .build();

        return alertTypeModel;
    }
}
