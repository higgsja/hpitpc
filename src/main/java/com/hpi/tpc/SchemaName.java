package com.hpi.tpc;

import java.io.*;
import java.util.*;

/**
 * Provides the active database schema name, loaded from application.properties
 * at class-init time (before Spring context starts).
 *
 * To switch databases, change app.db.schema in application.properties and restart.
 *
 * Usage in SQL constants:
 *   public static final String SQL = SchemaName.sql("select * from hlhtxc5_dmOfx.MyTable ...");
 */
public class SchemaName
{
    /** The active schema name (e.g. "hlhtxc5_dmOfx" or "hlhtxc5_dmOfx1"). */
    public static final String DB;

    /** The default/template schema name embedded in SQL string literals. */
    private static final String TEMPLATE = "hlhtxc5_dmOfx";

    static {
        String schema = TEMPLATE; // fallback
        try (InputStream in = SchemaName.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                String val = props.getProperty("app.db.schema");
                if (val != null && !val.isBlank()) {
                    schema = val.trim();
                }
            }
        } catch (Exception ignored) {
        }
        DB = schema;
    }

    /**
     * Replaces every occurrence of the template schema name with the
     * active schema name in the supplied SQL string.
     *
     * No-op if app.db.schema matches the template value.
     */
    public static String sql(String sql) {
        if (DB.equals(TEMPLATE)) {
            return sql;
        }
        return sql.replace(TEMPLATE + ".", DB + ".");
    }

    private SchemaName() {}
}
