package com.hpi.tpc.testutil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Direct JDBC connection to the production dataMart, bypassing Spring's Hikari-pooled
 * DataSource (which requires a full Spring context). Read-only integration tests
 * only — hlhtxc5_dmOfx is production, there is no _dev database.
 *
 * Schema/user/password are read from application.properties, same as the app.
 * The host is hardcoded to zeus:3306 because application.properties points
 * spring.datasource.url at 127.0.0.1:3306 — a local tunnel only available when
 * the app itself is running, not from a standalone test.
 */
public class TestDbConnection {

    private static final String HOST = "zeus:3306";
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = TestDbConnection.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        String schema = props.getProperty("app.db.schema");
        URL = "jdbc:mariadb://" + HOST + "/" + schema;
        USER = props.getProperty("spring.datasource.username");
        PASSWORD = props.getProperty("spring.datasource.password");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
