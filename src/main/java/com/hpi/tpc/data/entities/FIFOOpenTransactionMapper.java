package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class FIFOOpenTransactionMapper implements RowMapper<FIFOOpenTransactionModel>
{

    @Override
    public FIFOOpenTransactionModel mapRow(ResultSet rs,
        int rowNum)
        throws SQLException
    {
        FIFOOpenTransactionModel fifoPosition;

        fifoPosition = FIFOOpenTransactionModel.builder()
            .dmAcctId(rs.getInt("DMAcctId"))
            .joomlaId(rs.getInt("JoomlaId"))
            .fiTId(rs.getString("FiTId"))
            .ticker(rs.getString("Ticker"))
            .equityId(rs.getString("EquityId"))
            .transactionName(rs.getString("TransactionName"))
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
            .complete(rs.getInt("Complete"))
            .optionType(rs.getString("OptionType"))
            .strikePrice(rs.getDouble("StrikePrice"))
            .shPerCtrct(rs.getInt("ShPerCtrct"))
            .days(rs.getInt("Days"))
            .clientSectorId(rs.getInt("ClientSectorId"))
            .mktVal(rs.getDouble("MktVal"))
            .lMktVal(rs.getDouble("LMktVal"))
            .actPct(rs.getDouble("ActPct"))
            .bComplete(rs.getInt("Complete") == 1)
            .build();

        return fifoPosition;
    }
}
