package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class PositionOpenTransactionMapper1 implements RowMapper<PositionOpenTransactionModel1>
{

    @Override
    public PositionOpenTransactionModel1 mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        PositionOpenTransactionModel1 positionModel;

        positionModel = PositionOpenTransactionModel1.builder()
            .dmAcctId(rs.getInt("DMAcctId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .positionId(rs.getInt("PositionId"))
            .fiTId(rs.getString("FiTId"))
            .transactionName(rs.getString("TransactionName"))
            .ticker(rs.getString("Ticker"))
            .dateOpen(rs.getDate("DateOpen"))
            .units(rs.getDouble("Units"))
            .priceOpen(rs.getDouble("PriceOpen"))
            .dateExpire(rs.getDate("DateExpire"))
            .days(rs.getInt("Days"))
            .positionType(rs.getString("PositionType"))
            .totalOpen(rs.getDouble("TotalOpen"))
            .equityType(rs.getString("EquityType"))
            .gain(rs.getDouble("Gain"))
            .gainPct(rs.getDouble("GainPct"))
            .transactionType(rs.getString("TransactionType"))
            .complete(rs.getInt("Complete"))
            .mktVal(rs.getDouble("MktVal"))
            .lMktVal(rs.getDouble("LMktVal"))
            .clientAcctName(rs.getString("ClientAcctName"))
            .build();

        return positionModel;
    }
}
