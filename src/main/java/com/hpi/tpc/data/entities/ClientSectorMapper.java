package com.hpi.tpc.data.entities;

import java.sql.*;
import org.springframework.jdbc.core.*;

public class ClientSectorMapper implements RowMapper<ClientSectorModel> {
    @Override
    public ClientSectorModel mapRow(ResultSet rs, int rowNum) throws
        SQLException {
        ClientSectorModel clientSectorModel;

        clientSectorModel = ClientSectorModel.builder()
            .joomlaId(rs.getInt("JoomlaId"))
            .clientSectorId(rs.getInt("ClientSectorId"))
            .clientSector(rs.getString("ClientSector"))
            .cSecShort(rs.getString("CSecShort"))
            .active(rs.getString("Active"))
            .bActive(rs.getString("Active").equalsIgnoreCase("yes"))
            .tgtPct(rs.getDouble("TgtPct"))
            .adjPct(0.0)
            .comment(rs.getString("Comment"))
            .tgtLocked(rs.getString("TgtLocked"))
            .bTgtLocked(rs.getString("TgtLocked").equalsIgnoreCase("yes"))
            .actPct(rs.getDouble("ActPct"))
            .mktVal(rs.getDouble("MktVal"))
            .lMktVal(rs.getDouble("LMktVal"))
            .customSector(rs.getInt("CustomSector"))
            .changed(ClientSectorModel.CHANGE_NONE)
            .build();

        return clientSectorModel;
    }
}
