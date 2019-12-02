package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.mysql.cj.protocol.Resultset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Repository
public class ProductRepoImpl implements ProductRepo{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PreparedStatement pstmt;
    private Connection connection;
    private boolean repoTaskResult = false;
    private Statement stmt;

    @Autowired
    MySQLConnector mySQLConnector;

    //Asbjørn
    public boolean createItem (Item item) {
        try {
            Connection connection = mySQLConnector.openConnection();
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

    //Asbjørn
    public boolean createTreatment (Treatment treatment) {
        try {
            Connection connection = mySQLConnector.openConnection();
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

    //Asbjørn
    @Override
    public boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList) {
        stmt = null;
        String itemQuery = "SELECT items_id, items_name, items_price, items_description, items_quantity, items_active " +
                "FROM salon_tina_database.items";

        String treatmentQuery = "SELECT treatments_id, treatments_name, treatments_price, treatments_description, " +
                "treatments_duration, treatments_active " +
                "FROM salon_tina_database.treatments";
        try{
            Connection connection = mySQLConnector.openConnection();

            ResultSet rsItems = stmt.executeQuery(itemQuery);
            insertIntoItemArrayList(itemArrayList, rsItems);
            log.info(String.valueOf(rsItems));

            ResultSet rsTreatments = stmt.executeQuery(treatmentQuery);
            insertIntoTreatmentArrayList(treatmentArrayList, rsTreatments);
            log.info(String.valueOf(rsTreatments));

            repoTaskResult = true;
        } catch (SQLException e) {
            e.printStackTrace();
            repoTaskResult = false;
        }
        return repoTaskResult;
    }

    //Asbjørn
    @Override
    public void insertIntoItemArrayList(ArrayList<Item> itemArrayList, ResultSet rsItems) {
        try {
            itemArrayList.clear();
            while (rsItems.next()){
                int productID = rsItems.getInt("items_id");
                String productName = rsItems.getString("items_name");
                String productDescription = rsItems.getString("items_description");
                double productPrice  = rsItems.getDouble("items_price");
                boolean productActive = rsItems.getBoolean("items_active");
                int itemQuantity = rsItems.getInt("items_quantity");
                itemArrayList.add(new Item(productID, productName, productDescription, productPrice, productActive, itemQuantity));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertIntoTreatmentArrayList(ArrayList<Treatment> treatmentArrayList, ResultSet rsTreatments) {
        try {
            treatmentArrayList.clear();
            while (rsTreatments.next()){
                int productID = rsTreatments.getInt("treatments_id");
                String productName = rsTreatments.getString("treatments_name");
                String productDescription = rsTreatments.getString("treatments_description");
                double productPrice  = rsTreatments.getDouble("treatments_price");
                boolean productActive = rsTreatments.getBoolean("treatments_active");
                int treatmentDuration = rsTreatments.getInt("treatments_duration");
                treatmentArrayList.add(new Treatment(productID, productName, productDescription, productPrice, productActive, treatmentDuration));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
