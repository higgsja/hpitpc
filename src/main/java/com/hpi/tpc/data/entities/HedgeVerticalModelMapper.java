package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class HedgeVerticalModelMapper implements RowMapper<HedgeOptionVerticalModel> {
    @Override
    public HedgeOptionVerticalModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        HedgeOptionVerticalModel verticalModel;

        verticalModel = HedgeOptionVerticalModel.builder()
            .equityId("EquityId")
            .ticker("Symbol")
            .putCall(rs.getString("PutCall"))
            .buyStrike(((Double) rs.getDouble("StrikePrice")).toString())
            .sellStrike("")
            .spreadCost("")
            .calcMultiple("")
            .realizedMultiple("")
            .contracts("")
            .breakEven("")
            .cost(((Double) rs.getDouble("AskPrice")).toString())
            .build();

        return verticalModel;
    }
}
