package com.hpi.tpc.data.entities;

import com.hpi.tpc.testutil.TestDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

public class ClientSectorModelTest {

    @Test
    public void sqlSectorIdFromTkrExecutesAgainstDatabase() throws Exception {
        String sql = String.format(ClientSectorModel.SQL_SECTORID_FROM_TKR, "TEST", 999999);

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }
}
