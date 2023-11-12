package com.hpi.tpc.prefs;

import com.hpi.tpc.app.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * Read, write and manage preferences
 */
@Component
/*
 * dmOfx.TPCPreferences table
 *  JoomlaId 0 has defaults
 *  Defaults set first to ensure new prefs get added to existing clients
 *  Then preferences read overwrite defaults
 */
public class PrefsController
{

    @Autowired PrefsDAOImpl servicePrefsDAO;

    public PrefsController()
    {
    }

    public void readPrefsByPrefix(String prefix)
    {
        this.setDefaults(prefix);

        if (SecurityUtils.getUserId() == null)
        {
            return;
        }
        servicePrefsDAO.getByPrefix(
            SecurityUtils.getUserId().toString(), prefix);
    }

    public void setDefaults(String prefix)
    {
        servicePrefsDAO.getByPrefix("0", prefix);
    }

    public void writePrefsByPrefix(String Prefix)
    {
        for (String keyId : servicePrefsDAO.getPrefsModelsSet().keySet())
        {
            if (keyId.length() >= Prefix.length()
                && keyId.substring(0, Prefix.length()).equalsIgnoreCase(Prefix))
            {
                servicePrefsDAO.upsertDB(keyId, servicePrefsDAO.getPrefsModelsSet().get(keyId));
            }
        }
    }

    public String getPref(String keyId)
    {
        return servicePrefsDAO.getPrefsModelsSet().get(keyId);
    }

    public void setPref(String keyId, String keyValue)
    {
        this.servicePrefsDAO.getPrefsModelsSet().put(keyId, keyValue);
    }
}
