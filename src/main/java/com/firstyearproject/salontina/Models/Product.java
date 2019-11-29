package com.firstyearproject.salontina.Models;

public class Product {

    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private boolean productActive;

    public Product() {
    }

    public Product(int productId, String productName, String productDescription, double productPrice, boolean productActive) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productActive = productActive;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isProductActive() {
        return productActive;
    }

    public void setProductActive(boolean productActive) {
        this.productActive = productActive;
    }
}
