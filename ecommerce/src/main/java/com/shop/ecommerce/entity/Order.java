package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Entity
@Table(name = "orders")
public class Order extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    @ManyToOne
    private User user;

    private String name;

    private double totalAmount;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderedItems> orderedItems;

    private String orderedStatus;
    private String address;


    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderedAudit> orderedAudit;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<Transaction> transaction;
    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


    public Payment getPayment() {
        return payment;
    }

    public Order() {
    }



    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }
}
