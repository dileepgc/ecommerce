package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.OrderPromocode;

@Component
public interface OrderPromocodeRepo extends JpaRepository<OrderPromocode,Integer> {
    OrderPromocode findByCode(String orderPromo);
    @Query("SELECT p FROM OrderPromocode p WHERE p.expireStatus = false")
    List<OrderPromocode> findAllActivePromocode();
}
