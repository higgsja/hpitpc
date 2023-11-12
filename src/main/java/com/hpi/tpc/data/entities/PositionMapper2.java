package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class PositionMapper2 implements RowMapper<PositionModel>
{

    @Override
    public PositionModel mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        PositionModel positionModel;

        positionModel = PositionModel.builder()
            .ticker(rs.getString("Ticker"))
            .build();

        return positionModel;
    }
}
