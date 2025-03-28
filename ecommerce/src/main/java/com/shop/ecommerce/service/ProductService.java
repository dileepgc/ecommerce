package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.AddProductDTO;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    public ResponseEntity newProduct(int id, AddProductDTO addProductDTO, String authorizationHeader);
    public ResponseEntity deleteProduct(int id,String authorizationHeader);
    public ResponseEntity getAllProducts();
    public ResponseEntity updatePrice(int id,double price, String authorizationHeader);
    public ResponseEntity adNewStock(int newstock, int id, String authorizationHeader);
    ResponseEntity getProdDetails(int id);
    ResponseEntity getProdByCate(int cateId);

//    ResponseEntity getProdByName(String name, String authorizationHeader);
}
