package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

import java.util.ArrayList;

public interface ProductService{
    boolean createTreatment(Treatment treatment);
    boolean editTreatment(Treatment treatment);
    boolean createItem(Item item);
    boolean editItem(Item item);
    boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList);
}
