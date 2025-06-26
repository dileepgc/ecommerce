package com.shop.ecommerce.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FetchOrderPromocodeDTO {
    private int id;
    private String code;
    private int discount;
    private double amount;
    private String status;
    
    
    
}
