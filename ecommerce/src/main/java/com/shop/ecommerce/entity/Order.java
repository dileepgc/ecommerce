package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Entity
@Table(name = "orders")
public class Order extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    private double totalAmount;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderedItems> orderedItems;

    private String orderedStatus;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<Payment> payment;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderedAudit> orderedAudit;



    public double getTotalAmount() {
        return totalAmount;
    }

    public int getId() {
        return id;
    }

    public String getOrderedStatus() {
        return orderedStatus;
    }

    public void setOrderedStatus(String orderedStatus) {
        this.orderedStatus = orderedStatus;
    }

    public List<OrderedAudit> getOrderedAudit() {
        return orderedAudit;
    }

    public void setOrderedAudit(List<OrderedAudit> orderedAudit) {
        this.orderedAudit = orderedAudit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }



    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    public Order() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
