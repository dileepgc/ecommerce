package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.username=?1")
    User findByUsername(String username);


    @Query("select u from User u where u.is_deleted=false and u.role='user'")
    public List<User> findByRole();

    User findByEmail(String email);
}
