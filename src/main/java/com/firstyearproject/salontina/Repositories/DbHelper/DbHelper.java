package com.firstyearproject.salontina.Repositories.DbHelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Component fortæller spring boot at der skal laves en singleton
 */
@Component
public class DbHelper {

    private Connection connection;

    @Value("${spring.datasource.url}")
    private String springDatasourceUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public Connection createConnection() {
        try {

            connection = DriverManager.getConnection(springDatasourceUrl, dbUsername, dbPassword);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

