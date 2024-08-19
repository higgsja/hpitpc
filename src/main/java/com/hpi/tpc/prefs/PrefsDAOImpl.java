package com.hpi.tpc.prefs;

import com.hpi.tpc.data.entities.PrefsModel;
import com.hpi.tpc.data.entities.PrefsMapper;
import com.hpi.tpc.app.security.*;
import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

/**
 * Provides implementation methods for managing Preferences in the database
 */
//@NoArgsConstructor
@Repository
public class PrefsDAOImpl
    implements PrefsDAOInterface {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Getter private final HashMap<String, String> prefsModelsSet;

    public PrefsDAOImpl() {
        this.prefsModelsSet = new HashMap<>();
    }

    @Override
    public synchronized void getByPrefix(String joomlaId, String prefix) {
        String sql;
        List<PrefsModel> tempPrefsModels;

        sql = String.format(PrefsModel.SQL_GET_LIKE_STRING,
            joomlaId,
            prefix + "%");

        tempPrefsModels = jdbcTemplate.query(sql, new PrefsMapper());
        if (tempPrefsModels.isEmpty()) return;

        for (PrefsModel pm : tempPrefsModels) {
            if (joomlaId.equalsIgnoreCase("0")) {
                prefsModelsSet.putIfAbsent(pm.getKeyId(), pm.getKeyValue());
            }
            else {
                prefsModelsSet.replace(pm.getKeyId(), pm.getKeyValue());
            }
        }
    }

    @Override
    public synchronized void updateModelSet(String keyId, String keyValue) {
        this.prefsModelsSet.replace(keyId, keyValue);
    }

    private synchronized Boolean insertDB(String keyId, String keyValue) {
        String sql;

        sql = String.format(PrefsModel.SQL_INSERT_KEY_STRING,
            SecurityUtils.getUserId().toString(),
            keyId, keyValue);

        // returns 0(fail) or 1(success)
        return jdbcTemplate.update(sql) == 1;
    }

    private synchronized Boolean updateDB(String keyId, String keyValue) {
        String sql;

        sql = String.format(PrefsModel.SQL_UPDATE_KEY_STRING,
            keyValue,
            SecurityUtils.getUserId().toString(),
            keyId);

        // returns 0(fail) or 1(success)
        return jdbcTemplate.update(sql) == 1;
    }

    @Override
    public synchronized Boolean upsertDB(String keyId, String keyValue) {
        if (!insertDB(keyId, keyValue)) {
            return updateDB(keyId, keyValue);
        }
        
        return true;
    }
}
