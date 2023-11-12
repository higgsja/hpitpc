package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class OfxInstitutionMapper implements RowMapper<OfxInstitutionModel> {
    @Override
    public OfxInstitutionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        OfxInstitutionModel instModel;

        instModel = OfxInstitutionModel.builder()
            .name(rs.getString("Name"))
            .fId(rs.getString("FId"))
            .org(rs.getString("Org"))
            .brokerId(rs.getString("BrokerId"))
            .url(rs.getString("Url"))
            .ofxFail(rs.getString("OfxFail"))
            .sslFail(rs.getString("SslFail"))
            .lastOfxValidation(rs.getString("LastOfxValidation"))
            .lastSslValidation(rs.getString("LastSslValidation"))
            .profile(rs.getString("Profile"))
            .build();

        return instModel;
    }
}
