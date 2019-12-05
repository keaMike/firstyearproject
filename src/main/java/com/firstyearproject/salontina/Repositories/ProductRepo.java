package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public interface ProductRepo {
    boolean createTreatment (Treatment treatment);
    boolean createItem (Item item);
    List<Treatment> createTreatmentArrayList();
    List<Item> createItemArrayList();
    void insertIntoItemArrayList (List<Item> itemArrayList, ResultSet rs);
    void insertIntoTreatmentArrayList (List<Treatment> treatmentArrayList, ResultSet rs);
    boolean editItem(Item item);
    boolean editTreatment(Treatment treatment);
    Treatment getTreatment(int treatmentId);
    Item getItem(int itemId);
}
