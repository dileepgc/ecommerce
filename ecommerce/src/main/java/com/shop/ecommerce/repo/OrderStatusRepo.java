package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderStatusRepo extends JpaRepository<OrderStatus,Integer> {

    OrderStatus findByOrder(Order order);
}
