package com.hpi.tpc.data.entities;

import com.hpi.tpc.testutil.TestDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

public class FinVizEquityInfoModelTest {

    @Test
    public void sqlGetLatestDateExecutesAgainstDatabase() throws Exception {
        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(FinVizEquityInfoModel.SQL_GET_LATEST_DATE);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }

    @Test
    public void sqlGetLatestFilteredExecutesAgainstDatabase() throws Exception {
        String sql = String.format(FinVizEquityInfoModel.SQL_GET_LATEST_FILTERED,
            "", "", "", "", "", "", 0, 50);

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }

    @Test
    public void sqlGetLatestFilteredCountExecutesAgainstDatabase() throws Exception {
        String sql = String.format(FinVizEquityInfoModel.SQL_GET_LATEST_FILTERED_COUNT,
            "", "", "", "", "", "");

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }
}
