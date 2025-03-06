package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

}
