package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class PositionMapper1 implements RowMapper<PositionModel>
{

    @Override
    public PositionModel mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        PositionModel positionModel;

        positionModel = PositionModel.builder()
            .positionId(rs.getInt("PositionId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .clientSectorId(rs.getInt("ClientSectorId"))
            .ticker(rs.getString("Ticker"))
            .equityId(rs.getString("EquityId"))
            .tacticId(rs.getInt("TacticId"))
            .positionName(rs.getString("PositionName"))
            .units(rs.getDouble("Units"))
            .priceOpen(rs.getDouble("PriceOpen"))
            .price(rs.getDouble("Price"))
            .gain(rs.getDouble("Gain"))
            .gainPct(rs.getDouble("GainPct"))
            .dateOpen(rs.getDate("DateOpen"))
            .dateClose(rs.getDate("DateClose"))
            .days(rs.getInt("Days"))
            .date(rs.getDate("Date"))
            .positionType(rs.getString("PositionType"))
            .equityType(rs.getString("EquityType"))
            .build();

        return positionModel;
    }
}
