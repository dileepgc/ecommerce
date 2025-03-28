package com.shop.ecommerce.DTO;

import org.springframework.stereotype.Component;

@Component
public class PlaceOrderDTO {
    private String address;
    private String prodPromo;
    private String orderPromo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProdPromo() {
        return prodPromo;
    }

    public void setProdPromo(String prodPromo) {
        this.prodPromo = prodPromo;
    }

    public String getOrderPromo() {
        return orderPromo;
    }

    public void setOrderPromo(String orderPromo) {
        this.orderPromo = orderPromo;
    }
}
