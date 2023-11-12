package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class ActionMapper implements RowMapper<ActionModel>
{

    @Override
    public ActionModel mapRow(ResultSet rs, int rowNum)
        throws SQLException
    {
        ActionModel actionModel;

        actionModel = ActionModel.builder()
            .kVal(rs.getInt("KVal"))
            .action(rs.getString("Action"))
            .active(rs.getInt("Active"))
            .build();

        return actionModel;
    }
}
