package com.hpi.tpc.data.entities;

import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.*;

public class NoteMapper implements RowMapper<NoteModel>
{

    @Override
    public NoteModel mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        NoteModel note;
        Long longTime;

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        longTime = rs.getTimestamp("TStamp").getTime();

        note = NoteModel.builder()
            .joomlaId(((Integer) rs.getInt("JoomlaId")).toString())
            .tStamp(longTime)
            .ticker(rs.getString("Ticker"))
            //            .iPrice(((Double) rs.getDouble("IPrice")).toString())
            .iPrice(rs.getDouble("IPrice"))
            .description(rs.getString("Description"))
            .action(rs.getString("Action"))
            .triggerType(rs.getString("TriggerType"))
            .trigger(rs.getString("Trigger"))
            .active(((Integer) rs.getInt("Active")).toString())
            .dateEntered(rs.getString("DateEntered"))
            .notes(rs.getString("Notes"))
            .units(rs.getDouble("Units"))
            //            .high((((Double) rs.getDouble("High")).toString()))
            .high(rs.getDouble("High"))
            //            .low((((Double) rs.getDouble("Low")).toString()))
            .low(rs.getDouble("Low"))
            //            .close((((Double) rs.getDouble("Close")).toString()))
            .close(rs.getDouble("Close"))
            //            .priceChange((((Double) rs.getDouble("PriceChange")).toString()))
            .priceChange(rs.getDouble("PriceChange"))
            .priceChangePct((((Double) rs.getDouble("PriceChangePct")).toString()))
            //.gain((((Double) rs.getDouble("Gain")).toString()))
            .gain(rs.getDouble("Gain"))
            .gainPct((((Double) rs.getDouble("GainPct")).toString()))
            //            .atr((((Double) rs.getDouble("ATR")).toString()))
            .atr(rs.getDouble("ATR"))
            .earnDate(rs.getString("EarnDate"))
            .build();

        return note;
    }
}
