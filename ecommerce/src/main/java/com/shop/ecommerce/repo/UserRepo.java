package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepo extends JpaRepository<User,Integer> {
     public User findByEmail(String email);
}
