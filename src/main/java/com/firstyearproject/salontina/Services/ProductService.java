package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

public interface ProductService{

    boolean createTreatment(Treatment treatment);
    boolean deleteTreatment(int treatmentId);
    boolean editTreatment(Treatment treatment);
    boolean createItem(Item item);
    boolean deleteItem(int itemId);
    boolean editItem(Item item);
}
