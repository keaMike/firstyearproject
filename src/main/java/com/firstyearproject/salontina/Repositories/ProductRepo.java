package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ProductRepo {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement pstmt;
    private Connection connection;
    public boolean repoTaskResult = false;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String login;
    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnnection (){
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean createItem (Item item) {
        try {
            getConnnection();
            pstmt = null;
            pstmt = connection.prepareStatement
                    ("INSERT INTO salon_tina_database.items " +
                    "(items_name, items_price, items_description, items_active, items_quantity) " +
                    "VALUES (?, ?, ?, TRUE, ?)");
            pstmt.setString(1, item.getProductName());
            pstmt.setDouble(2, item.getProductPrice());
            pstmt.setString(3, item.getProductDescription());
            pstmt.setInt(4, item.getItemQuantity());

            log.info(String.valueOf(pstmt));

            pstmt.executeUpdate();
            pstmt.close();
            repoTaskResult = true;

        } catch (SQLException e) {
            e.printStackTrace();
            repoTaskResult = false;
        }
        return repoTaskResult;
    }

    public boolean createTreatment (Treatment treatment) {
        try {
            getConnnection();
            pstmt = null;
            pstmt = connection.prepareStatement
                    ("INSERT INTO salon_tina_database.treatments " +
                    "(treatments_name, treatments_price, treatments_description, treatments_duration, treatments_active) " +
                    "VALUES (?, ?, ?, ?, TRUE)");
            pstmt.setString(1, treatment.getProductName());
            pstmt.setDouble(2, treatment.getProductPrice());
            pstmt.setString(3, treatment.getProductDescription());
            pstmt.setInt(4, treatment.getTreatmentDuration());

            log.info(String.valueOf(pstmt));

            pstmt.executeUpdate();
            pstmt.close();
            repoTaskResult = true;

        } catch (SQLException e) {
            e.printStackTrace();
            repoTaskResult = false;
        }
        return repoTaskResult;
    }
}
