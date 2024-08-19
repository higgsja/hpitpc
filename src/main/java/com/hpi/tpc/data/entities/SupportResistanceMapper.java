package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class SupportResistanceMapper implements
    RowMapper<SupportResistanceModel> {
    @Override
    public SupportResistanceModel mapRow(ResultSet rs, int rowNum) throws
        SQLException {
        SupportResistanceModel supportResistanceModel;
        
        supportResistanceModel = SupportResistanceModel.builder()
            .joomlaId(rs.getInt("JoomlaId"))
            .equityId(rs.getString("EquityId"))
            .srLevel(rs.getDouble("S/Rday"))
            .weight(rs.getInt("Weight"))
            .build();
        
        return supportResistanceModel;
    }
}
