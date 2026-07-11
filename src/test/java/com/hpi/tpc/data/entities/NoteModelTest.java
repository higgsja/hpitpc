package com.hpi.tpc.data.entities;

import com.hpi.tpc.testutil.TestDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

public class NoteModelTest {

    @Test
    public void sqlGetStringExecutesAgainstDatabase() throws Exception {
        String sql = NoteModel.SQL_GET_STRING
            + String.format(NoteModel.SQL_GET_WHERE_STRING, 999999, "1");

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }
}
