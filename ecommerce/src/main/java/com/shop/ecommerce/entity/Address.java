package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Entity
@Component
public class Address extends  Tracking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String address;
    @OneToOne
    @JsonIgnore
    private Order order;
    @ManyToOne
    @JsonIgnore
    private User user;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address() {
    }
}
