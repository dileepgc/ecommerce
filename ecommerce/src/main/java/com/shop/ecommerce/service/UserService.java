package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.SignUpDTO;
import com.shop.ecommerce.entity.AddProduct;
import com.shop.ecommerce.entity.User;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    public String signUp(SignUpDTO signUpDTO);
    public String updateUser(User user,int id);
    public String loginUser(User u, HttpSession session);
    public String userLogout(HttpSession session);
    public boolean isEmailPresent(String email);

    public Object addproduct(int userid, AddProduct pdetails, HttpSession session);
    public Object doOrder(int id, HttpSession session);

}
