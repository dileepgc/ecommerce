package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<Product> findByCategory(Category category);

    @Query("select p from Product p where p.is_deleted=false")
    List<Product> findProducts();
    @Query("select p from Product p where p.category.id=?1 ")
    public List<Product> findByCategoryId(int cateId);

//    Product findByName();
}
