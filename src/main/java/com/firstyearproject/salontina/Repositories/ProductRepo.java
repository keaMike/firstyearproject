package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.DbHelper.DbHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepo {

    @Autowired
    DbHelper dbHelper;

    Connection con = null;
    PreparedStatement pstm = null;

    public List findAllTreatments() {
        try {
            con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM treatments");
            ResultSet rs = pstm.executeQuery();
            ArrayList treatments = new ArrayList();
            while(rs.next()) {
                Treatment t = new Treatment();
                t.setProductId(rs.getInt(1));
                t.setProductName(rs.getString(2));
                t.setProductPrice(rs.getDouble(3));
                t.setProductDescription(rs.getString(4));
                t.setTreatmentDuration(rs.getInt(5));
                t.setProductActive(rs.getBoolean(6));
                treatments.add(t);
            }
            return treatments;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }
}
