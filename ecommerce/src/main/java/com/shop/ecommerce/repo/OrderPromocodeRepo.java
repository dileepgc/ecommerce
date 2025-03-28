package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.OrderPromocode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderPromocodeRepo extends JpaRepository<OrderPromocode,Integer> {
    OrderPromocode findByCode(String orderPromo);
}
