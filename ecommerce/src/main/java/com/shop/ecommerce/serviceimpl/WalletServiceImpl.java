package com.shop.ecommerce.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.entity.Wallet;
import com.shop.ecommerce.entity.WalletAudit;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.UserRepo;
import com.shop.ecommerce.repo.WalletAuditRepo;
import com.shop.ecommerce.repo.WalletRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.WalletService;

import jakarta.transaction.Transactional;

@Service
public class WalletServiceImpl implements WalletService {
    public static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);
    @Autowired
    JWTService jwtService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    Wallet wallet;
    @Autowired
    WalletAuditRepo walletAuditRepo;
    @Autowired
    GlobalMethod globalMethod;
    // This method allows an admin to top-up a user's wallet by a specified amount.
// It first validates the admin's access using the authorization header, checks if the user exists by ID,
// and then updates the wallet balance. A corresponding wallet audit entry is also created to track the change.
    @Transactional
    public ResponseEntity topupWallet(String authorizationHeader, int id, double amount) {
        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            System.out.println(globalMethodDTO.getRole());
            if (!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);

            User user = userRepo.findById(id).orElse(null);
            System.out.println(user);
            Wallet wallet = walletRepo.findByUser(user);
            if (user == null) {
                throw new GlobalException("User does not exist to this id");
            }
            if(amount<0)
            {
                throw new GlobalException("Amount cannot be negative");
            }
            wallet.setBalance(wallet.getBalance() + amount);
            WalletAudit walletAudit1 = new WalletAudit();
            walletAudit1.setWallet(wallet);
            walletAudit1.setDiffamount("+" + amount);
            walletAuditRepo.save(walletAudit1);
            walletRepo.save(wallet);
            logger.info("Wallet updated successfully");     
            return new ResponseEntity<>("Wallet Updated Successfully", HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Wallet not updated successfully");
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    // This method retrieves the wallet audit history for a specific user identified by their userId.
// It checks if the user's wallet exists and fetches all audit records associated with that wallet.
// If no audits are found, it returns a message indicating that the wallet has no audit history.
    public ResponseEntity getWalletAudit(String authorizationHeader) {
        try {
            // Move the wallet retrieval logic below after the role check
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            String username=globalMethodDTO.getUsername();
            
            User user=userRepo.findByUsername(username);
            Wallet wallet = walletRepo.findByUser(user);
            System.out.println(wallet.getWalletAuditsList());
            if (wallet == null) {
                return new ResponseEntity<>("wallet does not exist", HttpStatus.NOT_FOUND);
            }

            List<WalletAudit> walletAudits = walletAuditRepo.findAllByWallet(wallet);
            if (walletAudits.isEmpty()) {
                return new ResponseEntity<>("Wallet does not have any audit", HttpStatus.NOT_FOUND);
            }
//            List<WalletAudit> walletAuditList = new ArrayList<String>();
//            for (WalletAudit walletAudit : walletAudits) {
//                walletAuditList.add(walletAudit.getDiffamount());
//            }
            logger.info("Wallet audit retrieved successfully");
            return new ResponseEntity<>(walletAudits, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Wallet audit not retrieved successfully");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // This method allows a user to view their wallet balance. It validates the user's access through the authorization header
// and retrieves the wallet balance associated with the user's username. It returns the balance as a response.
    public ResponseEntity viewwallet(String authorizationHeader) {

        try {
            GlobalMethodDTO globalMethodDTO = globalMethod.adminAccess(authorizationHeader);
            String username = globalMethodDTO.getUsername();
            if (globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied", HttpStatus.UNAUTHORIZED);
            User user = userRepo.findByUsername(username);
            Wallet wallet = walletRepo.findByUserId(user.getId());
            logger.info("Wallet retrieved successfully");
            return new ResponseEntity(" wallet balance is " + wallet.getBalance(), HttpStatus.OK);
        } catch (GlobalException e) {
            logger.error("Wallet not retrieved successfully");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
