package com.firstyearproject.salontina.Repositories;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;

public interface ProductRepo {
    boolean createItem(Item item);
    boolean createTreatment(Treatment treatment);
}
