package com.hpi.tpc.data.entities;

import lombok.*;

@Getter
@Builder
public class SupportResistanceModel {
    public static final String SQL_STRING;

    private final Integer joomlaId;
    private final String equityId;
    private final Double srLevel;
    private final Integer weight;

    static {
        SQL_STRING = "select * from hlhtxc5_dmOfx.SupResDay where JoomlaId = '%s' and EquityId = '%s';";
    }

    @Override
    public String toString() {
        return this.equityId;
    }   
}
