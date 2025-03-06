package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Transaction transaction;
    @OneToOne
    private Order order;
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Payment(int id, Transaction transaction, Order order) {
        this.id = id;
        this.transaction = transaction;
        this.order = order;
    }

    public Payment() {
    }
}
