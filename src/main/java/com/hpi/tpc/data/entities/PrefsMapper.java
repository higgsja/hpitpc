package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class PrefsMapper implements RowMapper<PrefsModel> {
    @Override
    public PrefsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrefsModel prefsModelModel;

        prefsModelModel = PrefsModel.builder()
            .keyId(rs.getString("KeyId"))
                .keyValue(rs.getString("KeyValue"))
                .build();

        return prefsModelModel;
    }
}
