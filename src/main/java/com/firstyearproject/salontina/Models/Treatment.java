package com.firstyearproject.salontina.Models;

public class Treatment extends Product {

    private int treatmentDuration;

    public Treatment() {
    }

    public Treatment(int productId, String productName, String productDescription, double productPrice, boolean productActive, int treatmentDuration) {
        super(productId, productName, productDescription, productPrice, productActive);
        this.treatmentDuration = treatmentDuration;
    }

    public int getTreatmentDuration() {
        return treatmentDuration;
    }

    public void setTreatmentDuration(int treatmentDuration) {
        this.treatmentDuration = treatmentDuration;
    }
}
