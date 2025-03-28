package com.shop.ecommerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartProductDTO {
    int id;
    String prod_name;
    double price;
    int quantity;
    String description;
    String image;

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

    public int getQuantitiy() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImageURL(String image) {
        this.image = image;
    }

    public CartProductDTO() {
    }
}
