package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.Promocode;

@Component
public interface PromocodeRepo extends JpaRepository<Promocode,Integer> {
    Promocode findByCode(String prodPromo);
    @Query("SELECT p FROM Promocode p WHERE p.expiredStatus = false")
    List<Promocode> findAllActivePromocode();
}
