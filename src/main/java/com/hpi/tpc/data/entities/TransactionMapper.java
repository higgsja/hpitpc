package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class TransactionMapper implements RowMapper<TransactionModel> {
    @Override
    public TransactionModel mapRow(ResultSet rs, int rowNum) throws
        SQLException {
        TransactionModel transactionModel;

        transactionModel = TransactionModel.builder()
            .equityId(rs.getString("EquityId"))
            .transactionDate(rs.getDate("Date"))
            .openDate(rs.getDate("OpenDate"))
            .closeDate(rs.getDate("CloseDate"))
            .units(rs.getDouble("Units"))
            .daysToExpiry(rs.getInt("Days"))
            .account(rs.getString("Acct"))
            .gainPct(Math.round(10.0 * rs.getDouble("GainPct")) / 10.0)
            .description(rs.getString("Comment"))
            .clientSectorId(rs.getInt("ClientSectorId"))
            .ticker(rs.getString("Ticker"))
            .price(rs.getDouble("Price"))
            .priceOpen(rs.getDouble("UnitPriceOpen"))
            .priceClose(rs.getDouble("UnitPriceClose"))
            .transType(rs.getString("TransType"))
            .active(rs.getString("Active"))
            .actPct(rs.getDouble("ActPct"))
            .build();

        return transactionModel;
    }
}