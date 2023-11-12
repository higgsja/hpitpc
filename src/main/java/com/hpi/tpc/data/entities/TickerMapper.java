package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class TickerMapper implements RowMapper<TickerModel> {
    @Override
    public TickerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        TickerModel model;

        model = TickerModel.builder()
            .ticker(rs.getString("Ticker"))
            .build();

        return model;
    }
}
