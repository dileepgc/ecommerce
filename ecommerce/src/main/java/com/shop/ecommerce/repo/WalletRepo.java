package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.entity.Wallet;
import com.shop.ecommerce.entity.WalletAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public interface WalletRepo extends JpaRepository<Wallet,Integer> {
    Wallet findByUserId(int userId);

    public Wallet findByUser(User user);
}
