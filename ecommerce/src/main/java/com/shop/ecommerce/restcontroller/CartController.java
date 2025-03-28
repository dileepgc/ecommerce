package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    @Autowired
    CartService cartService;


    // Endpoint to add a product to the cart
    @PostMapping("/addproducttocart")
    public ResponseEntity addproduct(@RequestBody ProdToCart pdetails, @RequestHeader("Authorization") String authorizationHeader)
    {
        return cartService.addproductToCart(pdetails,authorizationHeader);
    }

    // Endpoint to retrieve all products in the user's cart
    @GetMapping("/prodincart")
    public  ResponseEntity getProdInCart(@RequestHeader("Authorization") String authorizationHeader)
    {
        return cartService.getProdinCart(authorizationHeader);
    }

    // Endpoint to delete a product from the cart
    @DeleteMapping("/deleteprod/{productId}")
    public ResponseEntity deleteProdFromCart(@PathVariable("productId") int id,@RequestBody int quantity,@RequestHeader("Authorization") String authorizationHeader)
    {
        return cartService.deleteProdFromCart(id,quantity,authorizationHeader);
    }
}
