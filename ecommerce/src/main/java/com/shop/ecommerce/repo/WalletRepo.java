package com.shop.ecommerce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.entity.Wallet;



@Component
public interface WalletRepo extends JpaRepository<Wallet,Integer> {
    Wallet findByUserId(int userId);

    public Wallet findByUser(User user);
}
