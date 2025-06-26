package com.shop.ecommerce.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.service.CartService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    @Autowired
    CartService cartService;


    // Endpoint to add a product to the cart

    /**
     * Adds a product to the user's shopping cart.
     * This method processes a request to add a product to the user's cart. The product details are provided in the request body,
     * and the authorization token is passed in the request header.
     * @param pdetails The details of the product to be added to the cart, provided as a {@link ProdToCart} object in the request body.
     * @param authorizationHeader The authorization token passed in the request header for user authentication and validation.
     * @return A {@link ResponseEntity} indicating the result of the operation, including status and any necessary response data.
     */

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
    @DeleteMapping("/deleteprod/{productId}/{quantity}")
public ResponseEntity deleteProdFromCart(@PathVariable("productId") int id, 
                                          @PathVariable("quantity") int quantity, 
                                          @RequestHeader("Authorization") String authorizationHeader) {
    return cartService.deleteProdFromCart(id, quantity, authorizationHeader);
}

}
