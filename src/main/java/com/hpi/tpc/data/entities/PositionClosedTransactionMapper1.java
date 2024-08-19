package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class PositionClosedTransactionMapper1 implements RowMapper<PositionClosedTransactionModel1>
{

    @Override
    public PositionClosedTransactionModel1 mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        PositionClosedTransactionModel1 positionModel;

        positionModel = PositionClosedTransactionModel1.builder()
            .dmAcctId(rs.getInt("DMAcctId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .positionId(rs.getInt("PositionId"))
            .fiTId(rs.getString("FiTId"))
            .equityId(rs.getString("EquityId"))
            .transactionName(rs.getString("TransactionName"))
            .ticker(rs.getString("Ticker"))
            .dateOpen(rs.getDate("DateOpen"))
            .dateClose(rs.getDate("DateClose"))
            .units(rs.getDouble("Units"))
            .priceOpen(rs.getDouble("PriceOpen"))
            .priceClose(rs.getDouble("PriceClose"))
            .dateExpire(rs.getDate("DateExpire"))
            .days(rs.getInt("Days"))
            .positionType(rs.getString("PositionType"))
            .totalOpen(rs.getDouble("TotalOpen"))
            .totalClose(rs.getDouble("TotalClose"))
            .equityType(rs.getString("EquityType"))
            .gain(rs.getDouble("Gain"))
            .gainPct(rs.getDouble("GainPct"))
            .transactionType(rs.getString("TransactionType"))
            .clientAcctName(rs.getString("ClientAcctName"))
            .build();

        return positionModel;
    }
}
