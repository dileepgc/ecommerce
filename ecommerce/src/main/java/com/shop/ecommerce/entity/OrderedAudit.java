package com.shop.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

@Component
@Entity
public class OrderedAudit extends Tracking{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String previous_audit;
    private String present_status;


    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

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

    public String getPrevious_audit() {
        return previous_audit;
    }

    public void setPrevious_audit(String previous_audit) {
        this.previous_audit = previous_audit;
    }

    public String getPresent_status() {
        return present_status;
    }

    public void setPresent_status(String present_status) {
        this.present_status = present_status;
    }

    public OrderedAudit(int id, Order order) {
        this.id = id;

        this.order = order;
    }

    public OrderedAudit(int id, String previous_audit, String present_status, Order order) {
        this.id = id;
        this.previous_audit = previous_audit;
        this.present_status = present_status;
        this.order = order;
    }

    public OrderedAudit() {
    }
}
