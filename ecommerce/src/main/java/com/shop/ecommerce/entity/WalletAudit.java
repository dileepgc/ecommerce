package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
public class WalletAudit extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String diffamount;

    @ManyToOne
    @JsonIgnore
    private Wallet wallet;


    public int getId() {
        return id;
    }

    public void setDiffamount(String diffamount) {
        this.diffamount = diffamount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiffamount() {
        return diffamount;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }


    public WalletAudit(String diffamount, Wallet wallet) {
        this.diffamount = diffamount;
        this.wallet = wallet;
    }

    public WalletAudit(Wallet wallet) {
        this.wallet = wallet;
    }

    public WalletAudit() {
    }
}
