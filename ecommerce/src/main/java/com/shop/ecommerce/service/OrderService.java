package com.shop.ecommerce.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.DTO.PlaceOrderDTO;

@Component
public interface OrderService {
    ResponseEntity changeStatus(int orderid, String status, String authorizationHeader);
    public ResponseEntity doOrder(PlaceOrderDTO placeOrderDTO, String authorizationHeader );
    public ResponseEntity getOrders(String authorizationHeader);
    public ResponseEntity getAllOrders(String authorizationHeader);
    ResponseEntity cancelOrder(int orderId,String authorizationHeader);
    


}
