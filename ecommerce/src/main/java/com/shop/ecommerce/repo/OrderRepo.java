package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderRepo extends JpaRepository<Order,Integer> {
}
