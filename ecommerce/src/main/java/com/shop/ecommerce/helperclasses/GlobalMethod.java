package com.shop.ecommerce.helperclasses;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.service.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GlobalMethod {
//    @Autowired
//    GlobalMethodDTO globalMethodDTO;
    @Autowired
    JWTService jwtService;

    // This method checks if the user has admin access by extracting role information from the JWT token
     public GlobalMethodDTO adminAccess(String authorizationHeader)
     {
         GlobalMethodDTO globalMethodDTO=new GlobalMethodDTO();
//         System.out.println(authorizationHeader);
         String token=authorizationHeader.replace("Bearer ","");
         String role=jwtService.extractRole(token);
//         System.out.println(globalMethodDTO.getUsername());
//         System.out.println(globalMethodDTO.getRole());
//         System.out.println(globalMethodDTO.isAccess());
//         System.out.println("+++++++++");
         globalMethodDTO.setRole(role);
         globalMethodDTO.setUsername(jwtService.extractUserName(token));
         if(role.equalsIgnoreCase("admin"))
         {
             globalMethodDTO.setAccess(true);
         }
//         System.out.println(globalMethodDTO.getUsername());
//         System.out.println(globalMethodDTO.getRole());
//         System.out.println(globalMethodDTO.isAccess());
//         System.out.println("+++++++++++");
         return globalMethodDTO;
     }


}
