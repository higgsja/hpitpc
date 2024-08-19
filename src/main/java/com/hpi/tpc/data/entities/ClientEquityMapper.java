package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class ClientEquityMapper implements RowMapper<ClientEquityModel> {
    @Override
    public ClientEquityModel mapRow(ResultSet rs, int rowNum) throws
        SQLException {
        ClientEquityModel clientEquityModel;

        clientEquityModel = ClientEquityModel.builder()
            .joomlaId(rs.getInt("JoomlaId"))
            .ticker(rs.getString("Ticker"))
            .tickerIEX(rs.getString("TickerIEX"))
            .company(rs.getString("Company"))
            .active(rs.getString("Active"))
            .bActive(rs.getString("Active").equalsIgnoreCase("yes"))
            .clientSectorId(rs.getInt("ClientSectorId"))
            .cSecShort(rs.getString("cSecShort"))
            .tgtPct(rs.getDouble("TgtPct"))
            .adjPct(0.0)
            .analystTgt(rs.getDouble("AnalystTgt"))
            .stkPrice(rs.getDouble("StkPrice"))
            .comment(rs.getString("Comment"))
            .tgtLocked(rs.getString("TgtLocked"))
            .bTgtLocked(rs.getString("TgtLocked").equalsIgnoreCase("yes"))
            .actPct(rs.getDouble("ActPct"))
            .price(0.0)
            .changed(ClientEquityModel.CHANGE_NONE)
            .build();

        return clientEquityModel;
    }
}
