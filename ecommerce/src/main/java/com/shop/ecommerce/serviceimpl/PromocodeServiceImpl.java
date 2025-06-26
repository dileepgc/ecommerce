package com.shop.ecommerce.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.shop.ecommerce.DTO.FetchProductPromocodeDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.PromocodeDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.entity.Promocode;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.ProductRepo;
import com.shop.ecommerce.repo.PromocodeRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.PromocodeService;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PromocodeServiceImpl implements PromocodeService {
    public static final Logger logger = LoggerFactory.getLogger(PromocodeServiceImpl.class);
    @Autowired
    PromocodeRepo promocodeRepo;
    @Autowired
    JWTService jwtService;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    GlobalMethod globalMethod;

    /**
     * This method is scheduled to run every 30 minutes and updates all active promocodes
     * by setting their expiredStatus to true. This simulates expired promocodes,
     * marking them as no longer valid.
     */
    @Scheduled(cron = "*/30 * * * * *")
    // This runs every minute
    public void changePromoStatus() {
        List<Promocode> activePromocodes = promocodeRepo.findAll();
        for (Promocode promocode : activePromocodes) {
            if(promocode.getEndTime().isBefore(LocalDateTime.now()))
            {
                promocode.setExpiredStatus(true);
                promocode.setStatus("Expired");
                promocodeRepo.save(promocode);
            }
            else if(promocode.getStartTime().isBefore(LocalDateTime.now()) && promocode.getEndTime().isAfter(LocalDateTime.now()))
            {
                promocode.setStatus("Active");
                promocodeRepo.save(promocode);
            }


        }
        logger.info("Promocode status updated successfully");

    }

    @Transactional
    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            if(!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);
            System.out.println();
            List<Promocode> existingPromocode=promocodeRepo.findAll();
            
            Promocode promocode=new Promocode();
            promocode.setCode(promocodeDTO.getCode());
            promocode.setDiscount(promocodeDTO.getDiscount());
            Product product = productRepo.findById(promocodeDTO.getProductId()).orElse(null);
            if(product==null)
            {
                return new ResponseEntity<>("Product not found",HttpStatus.OK);
            }
            promocode.setProduct(product);
            promocode.setStartTime(promocodeDTO.getStartTime());
            promocode.setEndTime(promocodeDTO.getEndTime());
            promocode.setStatus("Coming Soon");
            promocodeRepo.save(promocode);
            logger.info("Promocode created successfully");      

            return new ResponseEntity<>("Promocode created successfully",HttpStatus.OK);
        }
                catch (GlobalException e) {
            logger.error("Promocode not created successfully");
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
    public ResponseEntity getProductPromocode(String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            List<Promocode> promocodes = promocodeRepo.findAllActivePromocode();
            List<FetchProductPromocodeDTO> fetchProductPromocodeDTOs = new ArrayList<>();
            for (Promocode promocode : promocodes) {
                FetchProductPromocodeDTO fetchProductPromocodeDTO = new FetchProductPromocodeDTO();
                fetchProductPromocodeDTO.setId(promocode.getId());
                fetchProductPromocodeDTO.setCode(promocode.getCode());
                fetchProductPromocodeDTO.setDiscount(promocode.getDiscount());
                fetchProductPromocodeDTO.setStatus(promocode.getStatus());
                fetchProductPromocodeDTO.setProductName(promocode.getProduct().getName());
                fetchProductPromocodeDTOs.add(fetchProductPromocodeDTO);
            }
            logger.info("Product promocode fetched successfully");      
            return new ResponseEntity<>(fetchProductPromocodeDTOs, HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Product promocode not fetched successfully");
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
