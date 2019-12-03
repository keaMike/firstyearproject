package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface ProductRepo {
    boolean createTreatment (Treatment treatment);
    boolean createItem (Item item);
    boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList);
    void insertIntoItemArrayList (ArrayList<Item> itemArrayList, ResultSet rs);
    void insertIntoTreatmentArrayList (ArrayList<Treatment> treatmentArrayList, ResultSet rs);
    boolean editItem(Item item);
    boolean editTreatment(Treatment treatment);
}
