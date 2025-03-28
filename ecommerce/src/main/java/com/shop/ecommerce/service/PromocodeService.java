package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.PromocodeDTO;
import org.springframework.http.ResponseEntity;

public interface PromocodeService {
    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, String authorizationHeader);
}
