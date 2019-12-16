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

    @Autowired
    ProductRepoImpl productRepoImplManager;

    /**
     * Asbjørn
     */
    @Override
    public boolean createTreatment(Treatment treatment){
        log.info("createTreatment method started...");
        return productRepoImplManager.createTreatment(treatment);
    }

    /**
     * Mike
     */
    @Override
    public boolean deleteTreatment(int treatmentId){
        log.info("deleteTreatment method started...");
        return productRepoImplManager.deleteTreatment(treatmentId);
    }

    /**
     * Asbjørn
     */
    @Override
    public boolean editTreatment(Treatment treatment){
        log.info("editTreatment method started...");
        return productRepoImplManager.editTreatment(treatment);
    }

    /**
     * Asbjørn
     */
    @Override
    public boolean createItem(Item item){
        log.info("createItem method started...");
        return productRepoImplManager.createItem(item);
    }

    /**
     * Mike
     */
    @Override
    public boolean deleteItem(int itemId){
        log.info("deleteItem method started...");
        return productRepoImplManager.deleteItem(itemId);
    }

    /**
     * Asbjørn
     */
    @Override
    public boolean editItem(Item item){
        log.info("editItem method started...");
        return productRepoImplManager.editItem(item);
    }

    /**
     * Asbjørn
     */
    @Override
    public List<Treatment> createTreatmentArrayList(){
        log.info("createTreatmentArrayList method started...");
        return productRepoImplManager.createTreatmentArrayList();
    }

    /**
     * Asbjørn
     */
    @Override
    public List<Item> createItemArrayList(){
        log.info("createItemArrayList method started...");
        return productRepoImplManager.createItemArrayList();
    }

    /**
     * Luca
     */
    @Override
    public Treatment getTreatment(int treatmentId){
        log.info("getTreatment method started...");
        return productRepoImplManager.getTreatment(treatmentId);
    }

    /**
     * Luca
     */
    @Override
    public Item getItem(int itemId){
        log.info("getItem method started...");
        return productRepoImplManager.getItem(itemId);
    }

}
