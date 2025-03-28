package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.ProdToCart;
import org.springframework.http.ResponseEntity;

public interface CartService {
    public ResponseEntity addproductToCart(ProdToCart pdetails, String authorizationHeader );
    ResponseEntity getProdinCart(String authorizationHeader);

    ResponseEntity deleteProdFromCart(int id,int quantity, String authorizationHeader);
}
