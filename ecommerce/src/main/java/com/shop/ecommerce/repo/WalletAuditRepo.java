package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Wallet;
import com.shop.ecommerce.entity.WalletAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface WalletAuditRepo extends JpaRepository<WalletAudit,Integer> {
    public WalletAudit findByWallet(Wallet wallet);
}
