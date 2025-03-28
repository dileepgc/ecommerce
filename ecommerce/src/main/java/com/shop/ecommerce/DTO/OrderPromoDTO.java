package com.shop.ecommerce.DTO;

import org.springframework.stereotype.Component;

@Component
public class OrderPromoDTO {
    String code;
    int discount;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }



}
