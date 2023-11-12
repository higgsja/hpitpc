package com.hpi.tpc.prefs;

public interface PrefsDAOInterface {
    void getByPrefix(String joomlaId, String prefix);
    void updateModelSet(String keyId, String keyValue);
    
//    Boolean insertDB(String keyId, String keyValue);
//    Boolean updateDB(String keyId, String keyValue);
    Boolean upsertDB(String keyId, String keyValue);
}
