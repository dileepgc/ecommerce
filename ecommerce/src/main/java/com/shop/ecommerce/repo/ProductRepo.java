package com.shop.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;

import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Component
@Table(indexes = {
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_category", columnList = "category_id"),
    @Index(name = "idx_product_deleted", columnList = "is_deleted")
})
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query("select p from Product p where p.category.id=?1 and p.is_deleted=false")
    List<Product> findByCategory(Category category);

    @Query("select p from Product p where p.is_deleted=false")
    List<Product> findProducts();
    @Query("select p from Product p where p.category.id=?1 and p.is_deleted=false")
    public List<Product> findByCategoryId(int cateId);
    @Query("select p from Product p where p.name like %?1% and p.is_deleted=false ")
    Product findByName(String name);
}
