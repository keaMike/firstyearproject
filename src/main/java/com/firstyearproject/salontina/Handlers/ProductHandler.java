package com.firstyearproject.salontina.Handlers;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductHandler {

    @Autowired
    ProductRepo productRepo;

    public boolean addTreatment(Treatment treatment){
        return false;
    }

    public boolean deleteTreatment(int treatmentId){
        return false;
    }

    public boolean editTreatment(Treatment treatment){
        return false;
    }

    public boolean addItem(Item item){
        return false;
    }

    public boolean deleteItem(int itemId){
        return false;
    }

    public boolean editItem(Item item){
        return false;
    }

    public List getTreatments() {
        return productRepo.findAllTreatments();
    }

}
