package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.Category;

@Component
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    @Query("select c from Category c where c.is_deleted=false")
    List<Category> findCategories();
    @Query("select c from Category c where c.is_deleted=false and c.id=?1")
    Category findById(int id);
}
