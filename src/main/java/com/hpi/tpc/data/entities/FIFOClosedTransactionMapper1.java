package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class FIFOClosedTransactionMapper1 implements RowMapper<FIFOClosedTransactionModel1>
{

    @Override
    public FIFOClosedTransactionModel1 mapRow(ResultSet rs,
        int rowNum)
        throws SQLException
    {
        FIFOClosedTransactionModel1 fifoPosition1;

        fifoPosition1 = FIFOClosedTransactionModel1.builder()
//            .dmAcctId(rs.getInt("DMAcctId"))
//            .joomlaId(rs.getInt("JoomlaId"))
//            .fiTId(rs.getString("FiTId"))
//            .transactionGrp(rs.getInt("TransactionGrp"))
            .equityId(rs.getString("EquityId"))
            .transactionName(rs.getString("TransactionName"))
            .ticker(rs.getString("Ticker"))
            .dateOpen(rs.getDate("DateOpen"))
            .dateClose(rs.getDate("DateClose"))
            .dateExpire(rs.getDate("DateExpire"))
            .units(rs.getDouble("Units"))
            .priceOpen(rs.getDouble("PriceOpen"))
            .priceClose(rs.getDouble("PriceClose"))
            .totalOpen(rs.getDouble("TotalOpen"))
            .totalClose(rs.getDouble("TotalClose"))
            .gain(rs.getDouble("Gain"))
            .gainPct(rs.getDouble("GainPct"))
            .equityType(rs.getString("EquityType"))
            .positionType(rs.getString("PositionType"))
            .transactionType(rs.getString("TransactionType"))
//            .complete(rs.getInt("Complete"))
            .days(rs.getInt("Days"))
            .clientAcctName(rs.getString("ClientAcctName"))
            .build();

        return fifoPosition1;
    }
}
