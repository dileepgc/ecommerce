package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class OrderedAudit extends Tracking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String action;
    @CreationTimestamp
    private String created_at;

    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderedAudit(int id, String action, String created_at, Order order) {
        this.id = id;
        this.action = action;
        this.created_at = created_at;
        this.order = order;
    }

    public OrderedAudit() {
    }
}
