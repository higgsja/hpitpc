package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class FinVizMapper implements RowMapper<FinVizEquityInfoModel> {
    @Override
    public FinVizEquityInfoModel mapRow(ResultSet rs, int rowNum)
        throws SQLException {
        FinVizEquityInfoModel finVizModel;

        finVizModel = FinVizEquityInfoModel.builder()
            .Ticker(rs.getString("Ticker"))
            .Company(rs.getString("Company"))
            .Sector(rs.getString("Sector"))
            .Industry(rs.getString("Industry"))
            .Country(rs.getString("Country"))
            .MktCap(rs.getString("MktCap(B)").equalsIgnoreCase("-999.999") ? "n/a"
                        : rs.getString("MktCap(B)").length() > 7 ? rs.getString("MktCap(B)").substring(0, 7)
                          : rs.getString("MktCap(B)"))
            .PE(rs.getString("PE").equalsIgnoreCase("-999.999") ? "n/a"
                    : rs.getString("PE"))
            .FwdPE(rs.getString("FwdPE").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("FwdPE"))
            .PEG(rs.getString("PEG").equalsIgnoreCase("-999.999") ? "n/a"
                     : rs.getString("PEG"))
            .Div(rs.getString("Div").equalsIgnoreCase("-999.999") ? "n/a"
                     : rs.getString("Div"))
            .PayoutRatio(rs.getString("PayoutRatio").equalsIgnoreCase("-999.999") ? "n/a"
                             : rs.getString("PayoutRatio"))
            .EPS(rs.getString("EPS").equalsIgnoreCase("-999.999") ? "n/a"
                     : rs.getString("EPS"))
            .EPSCY(rs.getString("EPS/CY").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("EPS/CY"))
            .EPSNY(rs.getString("EPS/NY").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("EPS/NY"))
            .EPSP5Y(rs.getString("EPS/P5Y").equalsIgnoreCase("-999.999") ? "n/a"
                        : rs.getString("EPS/P5Y"))
            .EPSN5Y(rs.getString("EPS/N5Y").equalsIgnoreCase("-999.999") ? "n/a"
                        : rs.getString("EPS/N5Y"))
            .Beta(rs.getString("Beta").equalsIgnoreCase("-999.999") ? "n/a"
                      : rs.getString("Beta"))
            .ATR(rs.getString("ATR").equalsIgnoreCase("-999.999") ? "n/a"
                     : rs.getString("ATR"))
            .SMA20(rs.getString("SMA20").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("SMA20"))
            .SMA50(rs.getString("SMA50").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("SMA50"))
            .SMA200(rs.getString("SMA200").equalsIgnoreCase("-999.999") ? "n/a"
                        : rs.getString("SMA200"))
            .Hi50d(rs.getString("50dHi").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("50dHi"))
            .Lo50d(rs.getString("50dLo").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("50dLo"))
            .Hi52w(rs.getString("52wHi").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("52wHi"))
            .Lo52w(rs.getString("52wLo").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("52wHi"))
            .RSI(rs.getString("RSI").equalsIgnoreCase("-999.999") ? "n/a"
                     : rs.getString("RSI"))
            .AnRec(rs.getString("AnRec").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("AnRec"))
            .Price(rs.getString("Price").equalsIgnoreCase("-999.999") ? "n/a"
                       : rs.getString("Price"))
            .Volume(rs.getString("Volume").equalsIgnoreCase("-999.999") ? "n/a"
                        : rs.getString("Volume"))
            .EarnDate(rs.getString("EarnDate").equalsIgnoreCase("-999.999") ? "n/a"
                          : rs.getString("EarnDate"))
            .TgtPrice(rs.getString("TgtPrice").equalsIgnoreCase("-999.999") ? "n/a"
                          : rs.getString("TgtPrice"))
            .date(rs.getString("Date").equalsIgnoreCase("-999.999") ? "n/a"
                      : rs.getString("Date"))
            .Beta(rs.getString("Beta").equalsIgnoreCase("-999.999") ? "n/a"
                      : rs.getString("Beta"))
            .build();

        return finVizModel;
    }
}
