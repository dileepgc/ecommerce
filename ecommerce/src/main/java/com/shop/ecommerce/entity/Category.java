package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Category extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate_id;

    private String cate_name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore  //It is used to stop fetching products when we are fetching categories
    private List<Product> products;



    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setP(List<Product> products) {
        this.products = this.products;
    }





    public Category(String cate_name) {
        this.cate_name = cate_name;
    }

    public Category() {
    }
    public void removeProduct(Product product) {
        this.products.remove(product);
        product.setCategory(null);
    }

    @Override
    public String toString() {
        return "Category{" +
                "cate_id=" + cate_id +
                ", cate_name='" + cate_name + '\'' +
                ", products=" + products +
                '}';
    }

}
