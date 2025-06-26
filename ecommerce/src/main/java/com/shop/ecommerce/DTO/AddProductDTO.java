package com.shop.ecommerce.DTO;

import org.springframework.stereotype.Service;

@Service
public class AddProductDTO {
    int prod_id;
    String prod_name;
    double price;
    int stock;
    String description;
    String imageURL;

    public String getImageURL() {
        return imageURL;
    }
    public int getProd_id() {
        return prod_id;
    }
    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
