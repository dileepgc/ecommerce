package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.PlaceOrderDTO;
import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.DTO.UserLoginDTO;
import com.shop.ecommerce.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity signUp(SignUpDTO signUpDTO);
    public ResponseEntity updateUser(User user, String authorizationHeader);
    public ResponseEntity loginUser(UserLoginDTO userLoginDTO);
    public ResponseEntity myProfile(String authorizationHeader);

    public ResponseEntity allusers(String authorizationHeader);
}
