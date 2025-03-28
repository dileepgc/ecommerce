package com.shop.ecommerce.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class ProductDTO {
    int id;
    String prod_name;
    double price;
    int stock;
    String description;
    String imageURL;
}
