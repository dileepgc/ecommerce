package com.shop.ecommerce.DTO;

public class ProdToCart {
    int prod_id;
    int quantity;

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProdToCart(int prod_id, int quantity) {
        this.prod_id = prod_id;
        this.quantity = quantity;
    }


    public ProdToCart() {
    }
}
