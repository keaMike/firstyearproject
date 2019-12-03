package com.firstyearproject.salontina.Repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Luca
@Component
public class MySQLConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static MySQLConnector singleInstance = null;

    private Connection con;

    private boolean connectionOpened = false;

    @Value("${spring.datasource.url}")
    private String mySQLURL;

    @Value("${spring.datasource.username}")
    private String mySQLUsername;

    @Value("${spring.datasource.password}")
    private String mySQLPassword;

    static MySQLConnector getInstance(){
        if(singleInstance == null){
            singleInstance = new MySQLConnector();
        }
        return singleInstance;
    }

    Connection openConnection(){
        //Enter URL, username and password for Database in the getConnection call below

        if(connectionOpened){
            return con;
        }

        try {
            con = DriverManager.getConnection
                    (
                            mySQLURL,
                            mySQLUsername,
                            mySQLPassword
                    );

            log.info("connected to MySQL server: " + mySQLURL + "...");

            connectionOpened = true;

            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void closeConnection(){
        if(connectionOpened){
            log.info("closing MySQL connection...");

            try {
                con.close();

                log.info("MySQL connection closed...");

                connectionOpened = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
