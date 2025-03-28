package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.PromocodeDTO;
import com.shop.ecommerce.service.PromocodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromocodeController {
    @Autowired
    PromocodeService promocodeService;

    // Endpoint to create a new product promocode
    @PostMapping("/productpromocode")
    public ResponseEntity productPromo(@RequestBody PromocodeDTO promocodeDTO, @RequestHeader("Authorization") String authorizationHeader)
    {
        return promocodeService.newPromocode(promocodeDTO,authorizationHeader);
    }
}
