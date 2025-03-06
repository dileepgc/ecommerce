package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaymentRepo extends JpaRepository<Payment,Integer> {
}
