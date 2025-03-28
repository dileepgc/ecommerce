package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.OrderPromoDTO;
import com.shop.ecommerce.service.OrderPromocodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPromocodeController {
    @Autowired
    OrderPromocodeService orderPromocodeService;


    // Endpoint to apply a promocode to an order
    @PostMapping("/orderpromocode")
    public ResponseEntity orderPromocode(@RequestBody OrderPromoDTO orderPromoDTO, @RequestHeader("Authorization") String authorizationHeader)
    {
        return orderPromocodeService.orderPromocode(orderPromoDTO,authorizationHeader);
    }
}
