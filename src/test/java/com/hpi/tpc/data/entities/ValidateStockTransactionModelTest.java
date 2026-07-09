package com.hpi.tpc.data.entities;

import com.hpi.tpc.testutil.TestDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidateStockTransactionModelTest {

    private static int countPlaceholders(String sql) {
        return sql.split("%s", -1).length - 1;
    }

    @Test
    public void sqlStringDoesNotReferenceClientStockTables() {
        String lower = ValidateStockTransactionModel.SQL_STRING.toLowerCase();
        assertFalse(lower.contains("clientclosingstock"),
            "SQL_STRING should no longer union in ClientClosingStock");
        assertFalse(lower.contains("clientopeningstock"),
            "SQL_STRING should no longer union in ClientOpeningStock");
    }

    @Test
    public void sqlStringHasSixPlaceholders() {
        // TPCDAOImpl.getValidateStockTransactionModels() calls String.format with
        // (acctId, ticker, userId) x2 — one group per remaining union branch (BuyStock, SellStock)
        assertEquals(6, countPlaceholders(ValidateStockTransactionModel.SQL_STRING));
    }

    @Test
    public void sqlStringExecutesAgainstDatabase() throws Exception {
        String sql = String.format(ValidateStockTransactionModel.SQL_STRING,
            999999, "TEST", 999999,
            999999, "TEST", 999999);

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }
}
