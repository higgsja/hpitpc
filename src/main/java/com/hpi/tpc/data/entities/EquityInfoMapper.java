package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class EquityInfoMapper implements RowMapper<EquityInfoModel> {
    @Override
    public EquityInfoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        EquityInfoModel equityInfoModel;

        equityInfoModel = EquityInfoModel.builder()
            .ticker(rs.getString("Ticker"))
            .company(rs.getString("Company"))
            .sector(rs.getString("Sector"))
            .industry(rs.getString("Industry"))
            .country(rs.getString("Country"))
            .mktCap(rs.getDouble("MktCap"))
            .pe(rs.getDouble("PE"))
            .fwdPE(rs.getDouble("FwdPE"))
            .peg(rs.getDouble("PEG"))
            .div(rs.getDouble("Div"))
            .payOutRatio(rs.getDouble("PayOutRatio"))
            .eps(rs.getDouble("EPS"))
            .epsCY(rs.getDouble("EPS/CY"))
            .epsNY(rs.getDouble("EPS/NY"))
            .epsP5Y(rs.getDouble("EPS/P5Y"))
            .epsN5Y(rs.getDouble("EPS/N5Y"))
            .atr(rs.getDouble("ATR"))
            .sma20(rs.getDouble("SMA20"))
            .sma50(rs.getDouble("SMA50"))
            .sma200(rs.getDouble("SMA200"))
            .d50Hi(rs.getDouble("50dHi"))
            .d50Lo(rs.getDouble("50dLo"))
            .w52Hi(rs.getDouble("52wHi"))
            .w52Lo(rs.getDouble("52wLo"))
            .rsi(rs.getDouble("RSI"))
            .anRec(rs.getDouble("AnRec"))
            .price(rs.getDouble("Price"))
            .volume(rs.getInt("Volume"))
            .tgtPrice(rs.getDouble("TgtPrice"))
            .earnDate(rs.getString("EarnDate"))
//            .date(rs.getDouble("Date"))   //sqlDate of data
            .beta(rs.getDouble("Beta"))
            .build();

        return equityInfoModel;
    }
}
