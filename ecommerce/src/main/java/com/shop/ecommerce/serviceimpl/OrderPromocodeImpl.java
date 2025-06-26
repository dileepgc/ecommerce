package com.shop.ecommerce.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.shop.ecommerce.DTO.FetchOrderPromocodeDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.OrderPromoDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.OrderPromocode;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.OrderPromocodeRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.OrderPromocodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderPromocodeImpl implements OrderPromocodeService {
    public static final Logger logger = LoggerFactory.getLogger(OrderPromocodeImpl.class);
    @Autowired
    OrderPromocodeRepo orderPromocodeRepo;
    @Autowired
    JWTService jwtService;
    @Autowired
    GlobalMethod globalMethod;

    // This method is scheduled to run every 30 minutes (cron expression "0 0/30 * * * *").
// It marks all order promocodes as expired by updating their status in the database.
@Scheduled(cron = "*/30 * * * * *")
    // This runs every 01 minute
    public void changeOrderPromoStatus() {
        List<OrderPromocode> orderPromocodes = orderPromocodeRepo.findAll();
        for (OrderPromocode orderPromocode1 : orderPromocodes) {
            if(orderPromocode1.getEndTime().isBefore(LocalDateTime.now()))
            {
                orderPromocode1.setExpireStatus(true);
                orderPromocode1.setStatus("Expired");
                orderPromocodeRepo.save(orderPromocode1);
            }
            else if(orderPromocode1.getStartTime().isBefore(LocalDateTime.now()) && orderPromocode1.getEndTime().isAfter(LocalDateTime.now()))
            {
                orderPromocode1.setStatus("Active");
                orderPromocodeRepo.save(orderPromocode1);
            }
            
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
            
            OrderPromocode orderPromocode = new OrderPromocode();
            orderPromocode.setAmount(orderPromoDTO.getAmount());
            orderPromocode.setCode(orderPromoDTO.getCode());
            orderPromocode.setDiscount(orderPromoDTO.getDiscount());
            orderPromocode.setStartTime(orderPromoDTO.getStartTime());
            orderPromocode.setEndTime(orderPromoDTO.getEndTime());
            orderPromocode.setStatus("Coming Soon");
            orderPromocodeRepo.save(orderPromocode);
            logger.info("Ordered Promocode created succesfully");
            return new ResponseEntity<>("Ordered Promocode created succesfully", HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    public ResponseEntity getOrderPromocode(String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            
            List<OrderPromocode> orderPromocodes = orderPromocodeRepo.findAllActivePromocode();
            List<FetchOrderPromocodeDTO> fetchOrderPromocodeDTOs = new ArrayList<>();
            for (OrderPromocode orderPromocode : orderPromocodes) {
                FetchOrderPromocodeDTO fetchOrderPromocodeDTO = new FetchOrderPromocodeDTO();
                fetchOrderPromocodeDTO.setId(orderPromocode.getId());
                fetchOrderPromocodeDTO.setCode(orderPromocode.getCode());
                fetchOrderPromocodeDTO.setDiscount(orderPromocode.getDiscount());
                fetchOrderPromocodeDTO.setAmount(orderPromocode.getAmount());
                fetchOrderPromocodeDTO.setStatus(orderPromocode.getStatus());
                fetchOrderPromocodeDTOs.add(fetchOrderPromocodeDTO);

            }
            logger.info("Order Promocode fetched successfully");
            return new ResponseEntity<>(fetchOrderPromocodeDTOs, HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
