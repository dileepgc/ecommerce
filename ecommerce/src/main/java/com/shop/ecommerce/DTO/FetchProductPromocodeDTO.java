package com.shop.ecommerce.DTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FetchProductPromocodeDTO {
    private int id;
    private String code;
    private int discount;
    
    private String status;
    private String productName;
}