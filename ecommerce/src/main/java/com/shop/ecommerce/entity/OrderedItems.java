package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
public class OrderedItems extends Tracking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private String name;
    private double price ;
    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;
    @ManyToOne
    private Product product;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderedItems(int id, int quantity, Order order ) {
        this.id = id;
        this.quantity = quantity;
        this.order = order;
    }

    public OrderedItems(int id, int quantity, String name, double price, Order order ) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.order = order;

    }

    public OrderedItems() {
    }
}
