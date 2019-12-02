package com.firstyearproject.salontina.Services;

import com.firstyearproject.salontina.Models.Item;
import com.firstyearproject.salontina.Models.Treatment;
import com.firstyearproject.salontina.Repositories.ProductRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService{

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean pHandlerTaskResult = false;

    @Autowired
    ProductRepoImpl productRepoImplManager;

    //Asbjørn
    public boolean createTreatment(Treatment treatment){
        pHandlerTaskResult = productRepoImplManager.createTreatment(treatment);
        log.info(String.valueOf(pHandlerTaskResult));
        return pHandlerTaskResult;
    }

    public boolean deleteTreatment(int treatmentId){
        return false;
    }

    public boolean editTreatment(Treatment treatment){
        return false;
    }

    //Asbjørn
    public boolean createItem(Item item){
        pHandlerTaskResult = productRepoImplManager.createItem(item);
        log.info(String.valueOf(pHandlerTaskResult));
        return pHandlerTaskResult;
    }

    public boolean deleteItem(int itemId){
        return false;
    }

    public boolean editItem(Item item){
        return false;
    }

    //Asbjørn
    @Override
    public boolean createProductArrayLists(ArrayList<Item> itemArrayList, ArrayList<Treatment> treatmentArrayList) {

        pHandlerTaskResult = productRepoImplManager.createProductArrayLists(itemArrayList, treatmentArrayList);

        return pHandlerTaskResult;
    }
}
