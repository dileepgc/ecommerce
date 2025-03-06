package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Promocode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PromocodeRepo extends JpaRepository<Promocode,Integer> {
}
