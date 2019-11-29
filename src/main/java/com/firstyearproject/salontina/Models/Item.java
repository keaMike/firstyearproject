package com.firstyearproject.salontina.Models;

public class Item extends Product {

    private int itemQuantity;

    public Item() {
    }

    public Item(int productId, String productName, String productDescription, double productPrice, boolean productActive, int itemQuantity) {
        super(productId, productName, productDescription, productPrice, productActive);
        this.itemQuantity = itemQuantity;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
