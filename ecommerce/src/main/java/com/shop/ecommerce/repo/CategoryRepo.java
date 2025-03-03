package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CategoryRepo extends JpaRepository<Category,Integer> {
}
