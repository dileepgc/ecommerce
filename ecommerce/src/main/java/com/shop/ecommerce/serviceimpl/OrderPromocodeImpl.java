package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.OrderPromoDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.OrderPromocode;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.OrderPromocodeRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.OrderPromocodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPromocodeImpl implements OrderPromocodeService {
    @Autowired
    OrderPromocodeRepo orderPromocodeRepo;
    @Autowired
    JWTService jwtService;
    @Autowired
    GlobalMethod globalMethod;

    // This method is scheduled to run every 30 minutes (cron expression "0 0/30 * * * *").
// It marks all order promocodes as expired by updating their status in the database.
    @Scheduled(cron = "0 0/30 * * * *")
    // This runs every 01 minute
    public void changeOrderPromoStatus() {
        List<OrderPromocode> orderPromocodes = orderPromocodeRepo.findAll();
        for (OrderPromocode orderPromocode1 : orderPromocodes) {
            orderPromocode1.setExpireStatus(true);
            orderPromocodeRepo.save(orderPromocode1);
        }
    }

    // This method allows the creation of a new order promocode after verifying if the user has admin access.
// It takes a `OrderPromoDTO` object containing the promocode details, and if the user has admin access,
// the promocode is saved in the database.
    public ResponseEntity orderPromocode(OrderPromoDTO orderPromoDTO, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            System.out.println(globalMethodDTO.isAccess());
            if (!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);
            List<OrderPromocode> existingorderpromocode=orderPromocodeRepo.findAll();
            if(!existingorderpromocode.isEmpty())
            {
                return new ResponseEntity<>("Already Order promo code exists", HttpStatus.UNAUTHORIZED);
            }
            OrderPromocode orderPromocode = new OrderPromocode();
            orderPromocode.setCode(orderPromoDTO.getCode());
            orderPromocode.setDiscount(orderPromoDTO.getDiscount());
            orderPromocodeRepo.save(orderPromocode);
            return new ResponseEntity<>("Ordered Promocode created succesfully", HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
