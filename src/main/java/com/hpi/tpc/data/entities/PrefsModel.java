package com.hpi.tpc.data.entities;

import lombok.*;

@Getter
@Builder
public class PrefsModel {
    public static final String SQL_GET_LIKE_STRING;
    public static final String SQL_UPDATE_KEY_STRING;
    public static final String SQL_INSERT_KEY_STRING;
    
    private final String keyId;
    private final String keyValue;

    static {
        SQL_GET_LIKE_STRING =
            "select * from hlhtxc5_dmOfx.TPCPreferences where JoomlaId = '%s' and KeyId like '%s';";
        
        SQL_UPDATE_KEY_STRING = "update hlhtxc5_dmOfx.TPCPreferences set KeyValue = '%s' where JoomlaId = '%s' and KeyId = '%s';";
        
        SQL_INSERT_KEY_STRING = "insert ignore into hlhtxc5_dmOfx.TPCPreferences (JoomlaId, KeyId, KeyValue) values ('%s', '%s', '%s');";
    }
}
