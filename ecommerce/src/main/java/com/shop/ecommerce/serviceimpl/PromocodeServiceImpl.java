package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.PromocodeDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.entity.Promocode;
import com.shop.ecommerce.repo.ProductRepo;
import com.shop.ecommerce.repo.PromocodeRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.PromocodeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromocodeServiceImpl implements PromocodeService {
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
    @Scheduled(cron = "0 0/30 * * * *")
    // This runs every minute
    public void changePromoStatus() {
        List<Promocode> activePromocodes = promocodeRepo.findAll();
        for (Promocode promocode : activePromocodes) {
            promocode.setExpiredStatus(true);
            promocodeRepo.save(promocode);
        }
    }

    @Transactional
    public ResponseEntity newPromocode(PromocodeDTO promocodeDTO, String authorizationHeader) {
        try {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            if(!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);
            System.out.println();
            List<Promocode> existingPromocode=promocodeRepo.findAll();
            if(!existingPromocode.isEmpty())
            {
                return new ResponseEntity("Product Promo code Already Exists",HttpStatus.UNAUTHORIZED);
            }
            Promocode promocode=new Promocode();
            promocode.setCode(promocodeDTO.getCode());
            promocode.setDiscount(promocodeDTO.getDiscount());
            Product product = productRepo.findById(promocodeDTO.getProductId()).orElse(null);
            if(product==null)
            {
                return new ResponseEntity<>("Product not found",HttpStatus.OK);
            }
            promocode.setProduct(product);
            promocodeRepo.save(promocode);


            return new ResponseEntity<>("Promocode created successfully",HttpStatus.OK);
        }
        catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
