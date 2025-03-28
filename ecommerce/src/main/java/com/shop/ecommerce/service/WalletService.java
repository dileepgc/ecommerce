package com.shop.ecommerce.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface WalletService {
    public ResponseEntity topupWallet(String authorizationHeader, int id, double amount);
    public ResponseEntity getWalletAudit(int userId);
    public ResponseEntity viewwallet( String authorizationHeader );
}
