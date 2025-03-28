package com.shop.ecommerce.DTO;

import com.shop.ecommerce.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchOrderDTO {
    int id;
     String name;
     double totalAmount;
     double amountBeforeDiscount;
     String orderedStatus;
//     String address;
}
