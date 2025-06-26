package com.shop.ecommerce.service;

import org.springframework.http.ResponseEntity;

import com.shop.ecommerce.DTO.PromocodeDTO;

public interface PromocodeService {
    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, String authorizationHeader);
    public ResponseEntity getProductPromocode(String authorizationHeader);
}
