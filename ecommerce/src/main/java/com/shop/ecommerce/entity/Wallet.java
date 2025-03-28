package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Entity
public class Wallet extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double balance;

    @OneToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<WalletAudit> walletAuditsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wallet(double balance, User user) {
        this.balance = balance;
        this.user = user;
    }

    public Wallet() {
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", user=" + user +
                ", walletAuditsList=" + walletAuditsList +
                '}';
    }

    public List<WalletAudit> getWalletAuditsList() {
        return walletAuditsList;
    }

    public void setWalletAuditsList(List<WalletAudit> walletAuditsList) {
        this.walletAuditsList = walletAuditsList;
    }
}
