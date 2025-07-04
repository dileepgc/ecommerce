package com.shop.ecommerce.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.shop.ecommerce.entity.Role;
import com.shop.ecommerce.entity.User;


@Component
@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {

    @Query("Select r.user from Role r where r.rolename=?1")
    User findByRoleName(String roleName);



}
