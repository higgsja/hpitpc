package com.hpi.tpc.data.entities;

import java.sql.*;
import lombok.*;
import org.springframework.jdbc.core.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquityTypeMapper implements RowMapper<EquityTypeModel>
{
    private String equityType;

    @Override
    public EquityTypeModel mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        EquityTypeModel equityTypeModel;

        equityTypeModel = EquityTypeModel.builder()
            .equityType(rs.getString("EquityType"))
            .build();

        return equityTypeModel;
    }
}
