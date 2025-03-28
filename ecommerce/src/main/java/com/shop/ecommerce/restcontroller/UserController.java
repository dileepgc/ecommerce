package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.PlaceOrderDTO;
import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.DTO.UserLoginDTO;
import com.shop.ecommerce.entity.User;

import com.shop.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;

    // Endpoint to register a new user into the database, ensuring that email and username are unique.
    @PostMapping("/register")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO) {
            return userService.signUp(signUpDTO);
    }


    // Endpoint to retrieve the full profile of the currently authenticated user

    @GetMapping("/myprofile")
    public ResponseEntity myProfile(@RequestHeader("Authorization") String authorizationHeader)
    {
            return userService.myProfile(authorizationHeader);
    }
    @GetMapping("/allusers")
    public ResponseEntity allusers(@RequestHeader("Authorization") String authorizationHeader)
    {
        return userService.allusers(authorizationHeader);
    }
    // Endpoint to update user details
    @PatchMapping("/updateuser")
    public ResponseEntity updateUser(@RequestBody User user, @RequestHeader("Authorization") String authorizationHeader) {
        return userService.updateUser(user, authorizationHeader);
    }

    // Endpoint to log in a user
    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody UserLoginDTO userLoginDTO) {

        return userService.loginUser(userLoginDTO);
    }
}
