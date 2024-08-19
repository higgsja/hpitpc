package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class TimeseriesMapper implements RowMapper<TimeseriesModel> {
    @Override
    public TimeseriesModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeseriesModel timeseriesModel;

        timeseriesModel = TimeseriesModel.builder()
            .ticker(rs.getString("Ticker"))
            .date(rs.getDate("Date"))
            .data(rs.getDouble("PctGain"))
            .build();

        return timeseriesModel;
    }
}
