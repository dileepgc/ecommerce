package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Component
public class Promocode extends Tracking{
    /// this is for product promocode
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private int discount;
    @CreationTimestamp
    private String startTime;

    private boolean expiredStatus;
    @ManyToOne
    private Product product;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isExpiredStatus() {
        return expiredStatus;
    }

    public void setExpiredStatus(boolean expiredStatus) {
        this.expiredStatus = expiredStatus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
