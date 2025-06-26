package com.shop.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.shop.ecommerce.DTO.OrderPromoDTO;

public interface OrderPromocodeService {
    ResponseEntity orderPromocode(OrderPromoDTO orderPromoDTO, String  authorizationHeader);
    ResponseEntity getOrderPromocode(String authorizationHeader);
}
