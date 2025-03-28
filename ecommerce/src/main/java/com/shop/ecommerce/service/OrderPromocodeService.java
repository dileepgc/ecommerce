package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.OrderPromoDTO;
import org.springframework.http.ResponseEntity;

public interface OrderPromocodeService {
    ResponseEntity orderPromocode(OrderPromoDTO orderPromoDTO, String  authorizationHeader);
}
