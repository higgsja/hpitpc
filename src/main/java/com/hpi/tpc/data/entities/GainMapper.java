package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class GainMapper implements RowMapper<GainModel> {
    @Override
    public GainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        GainModel gainsModel;

        gainsModel = GainModel.builder()
            .category(rs.getString("Category"))
            .gain(rs.getDouble("Gain"))
            .gainPct(rs.getDouble("GainPct"))
            .build();

        return gainsModel;
    }
}
