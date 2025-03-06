package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
public class OrderStatus extends Tracking {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   @OneToOne
    private Order order;
   private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderStatus(int id, Order order, String status) {
        this.id = id;
        this.order = order;
        this.status = status;
    }

    public OrderStatus() {
    }
}
