package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

import java.util.ArrayList;
import java.util.List;

public interface ProductService{
    boolean createTreatment(Treatment treatment);
    boolean editTreatment(Treatment treatment);
    boolean createItem(Item item);
    boolean editItem(Item item);
    List<Treatment> createTreatmentArrayList();
    List<Item> createItemArrayList();
    boolean deleteTreatment(int treatmentId);
    boolean deleteItem(int itemId);
    Treatment getTreatment(int treatmentId);
    Item getItem(int itemId);
}
