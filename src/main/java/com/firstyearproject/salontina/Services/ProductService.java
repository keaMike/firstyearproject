package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

import java.util.ArrayList;

public interface ProductService{
    boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList);
}
