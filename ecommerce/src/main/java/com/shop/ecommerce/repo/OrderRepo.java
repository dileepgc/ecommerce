package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.User;

import jakarta.persistence.Index;
import jakarta.persistence.Table;
@Table(indexes = {
   @Index(name = "idx_order_user", columnList = "user_id")
})
@Component
public interface OrderRepo extends JpaRepository<Order,Integer> {
   public List<Order> findByUser(User user);
}

