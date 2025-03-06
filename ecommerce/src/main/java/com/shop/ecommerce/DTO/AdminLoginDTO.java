package com.shop.ecommerce.DTO;

import org.springframework.stereotype.Component;

@Component
public class AdminLoginDTO {
    String email;
    String password;



    public AdminLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public AdminLoginDTO() {
    }
}
