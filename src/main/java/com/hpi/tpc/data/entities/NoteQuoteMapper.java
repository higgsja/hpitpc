package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class NoteQuoteMapper implements RowMapper<NoteQuoteModel> {
    @Override
    public NoteQuoteModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        NoteQuoteModel noteQuoteModel;

        noteQuoteModel = NoteQuoteModel.builder()
            .ticker (rs.getString("Ticker"))
            .company (rs.getString("Company"))
            .close (rs.getDouble("Close"))
            .build();

        return noteQuoteModel;
    }
}
