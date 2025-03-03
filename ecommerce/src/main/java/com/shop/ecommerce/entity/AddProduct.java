package com.shop.ecommerce.entity;

public class AddProduct extends Tracking {

    private int productId;
    private int quantity;




    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AddProduct(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


}
