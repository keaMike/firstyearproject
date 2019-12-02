package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.mysql.cj.protocol.Resultset;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface ProductRepo {
    boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList);
    void insertIntoItemArrayList (ArrayList<Item> itemArrayList, ResultSet rs);
    void insertIntoTreatmentArrayList (ArrayList<Treatment> treatmentArrayList, ResultSet rs);

}
