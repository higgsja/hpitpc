package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class TradeTacticMapper implements RowMapper<TradeTacticModel> {
    @Override
    public TradeTacticModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        TradeTacticModel model;

        model = TradeTacticModel.builder()
            .tacticId(rs.getInt("TacticId"))
            .tacticName(rs.getString("TacticName"))
            .tacticDescr(rs.getString("TacticDescr"))
            .tacticEquityType(rs.getString("TacticEquityType"))
            .active(rs.getInt("Active"))
            .build();

        return model;
    }
}
