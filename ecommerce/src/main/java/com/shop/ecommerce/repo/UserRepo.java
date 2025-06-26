package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.User;
@Component
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.username=?1")
    User findByUsername(String username);


    @Query("select u from User u where u.is_deleted=false and u.role='user'")
    public List<User> findByRole();

    @Query("select u from User u where u.is_deleted=false and u.role='user'")
    public List<User> findByRoleUser();

    User findByEmail(String email);
}
