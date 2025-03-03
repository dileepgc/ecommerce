package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.CartItem;
import com.shop.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
    public CartItem findByProduct(Product product);


    List<CartItem> findByCartId(int cartId);





    List<CartItem> findAllByCartId(int cartId);
}
