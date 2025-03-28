package com.shop.ecommerce.DTO;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
@Getter
@Setter
public class GlobalMethodDTO {

    private String username;
    private String role;
    private boolean access;


}
