package com.hpi.tpc.data.entities;

import com.hpi.tpc.testutil.TestDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidateOptionTransactionModelTest {

    private static int countPlaceholders(String sql) {
        return sql.split("%s", -1).length - 1;
    }

    @Test
    public void sqlStringDoesNotReferenceClientOptionTables() {
        String lower = ValidateOptionTransactionModel.SQL_STRING.toLowerCase();
        assertFalse(lower.contains("clientclosingoption"),
            "SQL_STRING should no longer union in ClientClosingOptions");
        assertFalse(lower.contains("clientopeningoption"),
            "SQL_STRING should no longer union in ClientOpeningOptions");
    }

    @Test
    public void sqlStringHasNinePlaceholders() {
        // TPCDAOImpl.getValidateOptionTransactionModels() calls String.format with
        // (acctId, ticker, userId) x3 — one group per remaining union branch (BuyOpt, ClosureOpt, SellOpt)
        assertEquals(9, countPlaceholders(ValidateOptionTransactionModel.SQL_STRING));
    }

    @Test
    public void sqlStringExecutesAgainstDatabase() throws Exception {
        String sql = String.format(ValidateOptionTransactionModel.SQL_STRING,
            999999, "TEST", 999999,
            999999, "TEST", 999999,
            999999, "TEST", 999999);

        try (Connection con = TestDbConnection.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {
            // no exception thrown means the query is syntactically valid against the live schema
        }
    }
}
