package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Cart;
import com.shop.ecommerce.entity.CartItem;
import com.shop.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {

    public List<CartItem> findByCart(Cart cart);

    CartItem findByProduct(Product product);
}
