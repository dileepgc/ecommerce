package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
public class WalletController {
    @Autowired
    WalletService walletService;

    // Endpoint to top-up a user's wallet with a specific amount and new amount gets added into old amount
    @PostMapping("/top_up/{userid}")
    public ResponseEntity topupWallet (@RequestHeader("Authorization") String authorizationHeader, @PathVariable("userid") int id,
                                       @RequestBody double amount)
    {
        return walletService.topupWallet(authorizationHeader,id, amount);
    }

    // Endpoint to get the wallet audit for a specific user
    @GetMapping("/getwalletaudit/{userid}")
    public ResponseEntity getWalletAudit ( @PathVariable("userid") int id /*user_id*/ ,@RequestHeader("Authorization") String authorizationHeader )
    {
        return walletService.getWalletAudit(id);
    }

    // Endpoint to view the current balance of the authenticated user's wallet
    @GetMapping("/viewbalance")
    public ResponseEntity viewBalance( @RequestHeader("Authorization") String authorizationHeader)
    {
        return walletService.viewwallet(authorizationHeader) ;
    }
}
