package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.PlaceOrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface OrderService {
    ResponseEntity changeStatus(int orderid, String status, String authorizationHeader);
    public ResponseEntity doOrder(PlaceOrderDTO placeOrderDTO, String authorizationHeader );
    public ResponseEntity getOrders(String authorizationHeader);
    ResponseEntity cancelOrder(int orderId,String authorizationHeader);


}
