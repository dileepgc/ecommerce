package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.ProdToCart;
import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.DTO.UserLoginDTO;
import com.shop.ecommerce.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public String signUp(SignUpDTO signUpDTO);
    public String updateUser(User user,int id);
    public String loginUser(UserLoginDTO userLoginDTO, HttpSession session);
    public String userLogout(HttpSession session);
    public boolean isEmailPresent(String email);
    public String viewwallet( HttpSession session);
    public Object addproductToCart( ProdToCart pdetails, HttpSession session);
    public Object doOrder(String address, HttpSession session);

    public ResponseEntity myProfile(HttpSession session);

    ResponseEntity getProdinCart(HttpSession session);


    public ResponseEntity getOrders(HttpSession session);


    ResponseEntity cancelOrder(int orderId,HttpSession session);
}
