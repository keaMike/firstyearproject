package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.ProductRepo;
import com.firstyearproject.salontina.Repositories.ProductRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean pHandlerTaskResult = false;

    @Autowired
    ProductRepoImpl productRepoImplManager;

    //Asbjørn
    @Override
    public boolean createTreatment(Treatment treatment){
        pHandlerTaskResult = productRepoImplManager.createTreatment(treatment);
        log.info(String.valueOf(pHandlerTaskResult));
        return pHandlerTaskResult;
    }

    //Mike
    public boolean deleteTreatment(int treatmentId){
        return productRepoImplManager.deleteTreatment(treatmentId);
    }

    //Asbjørn
    @Override
    public boolean editTreatment(Treatment treatment){

        pHandlerTaskResult = productRepoImplManager.editTreatment(treatment);

        return pHandlerTaskResult;
    }

    //Asbjørn
    @Override
    public boolean createItem(Item item){
        pHandlerTaskResult = productRepoImplManager.createItem(item);
        log.info(String.valueOf(pHandlerTaskResult));
        return pHandlerTaskResult;
    }

    //Mike
    public boolean deleteItem(int itemId){
        return productRepoImplManager.deleteItem(itemId);
    }

    //Asbjørn
    @Override
    public boolean editItem(Item item){

        pHandlerTaskResult = productRepoImplManager.editItem(item);

        return pHandlerTaskResult;
    }

    public List<Treatment> createTreatmentArrayList(){
        return productRepoImplManager.createTreatmentArrayList();
    }

    public List<Item> createItemArrayList(){
        return productRepoImplManager.createItemArrayList();
    }

    //Luca
    public Treatment getTreatment(int treatmentId){
        return productRepoImplManager.getTreatment(treatmentId);
    }

    //Luca
    public Item getItem(int itemId){
        return productRepoImplManager.getItem(itemId);
    }

}
