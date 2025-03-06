package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    @Query("select c from Category c where c.is_deleted=false")
    List<Category> findCategories();
}
