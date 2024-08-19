package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class OHLCVMapper implements RowMapper<OHLCVModel> {
    @Override
    public OHLCVModel mapRow(ResultSet rs, int rowNum) 
        throws SQLException {
        OHLCVModel ohlcvm;

        ohlcvm = OHLCVModel.builder()
            .ticker(rs.getString("Ticker"))
            .date(rs.getDate("Date"))
            .open(rs.getDouble("Open"))
            .high(rs.getDouble("High"))
            .low(rs.getDouble("Low"))
            .close(rs.getDouble("Close"))
            .volume(rs.getDouble("Volume"))
                .build();

        return ohlcvm;
    }

    
    
}
