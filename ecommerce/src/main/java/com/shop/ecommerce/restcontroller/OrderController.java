package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.PlaceOrderDTO;
import com.shop.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    // Endpoint to change the status of an order based on its ID
    @PostMapping("/orderstatus/{id}")
    public ResponseEntity changeStatus(@PathVariable int id   /*orderid*/   , @RequestBody String status, @RequestHeader("Authorization") String authorizationHeader)
    {
        return orderService.changeStatus(id,status, authorizationHeader);
    }
    // Endpoint to place a new order
    @PostMapping("/do_order")
    public ResponseEntity doOrder(@RequestBody PlaceOrderDTO placeOrderDTO, @RequestHeader("Authorization") String authorizationHeader)
    {
        return orderService.doOrder(placeOrderDTO,authorizationHeader);
    }

    // Endpoint to fetch all orders for the authenticated user
    @GetMapping("/fetchorders")
    public ResponseEntity getOrders(@RequestHeader("Authorization") String authorizationHeader)
    {
        return  orderService.getOrders(authorizationHeader);
    }

    // Endpoint to cancel an order by its ID
    @PostMapping("/cancelorder/{id}")
    public ResponseEntity cancelOrder(@PathVariable int id,@RequestHeader("Authorization") String authorizationHeader)
    {
        return orderService.cancelOrder(id,authorizationHeader);
    }
}
