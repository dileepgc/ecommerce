package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Cart;
import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CartRepo extends JpaRepository<Cart,Integer> {
    public Cart findByU(User user);


}
