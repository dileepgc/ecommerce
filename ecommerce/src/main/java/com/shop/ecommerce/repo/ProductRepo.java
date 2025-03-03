package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepo extends JpaRepository<Product,Integer> {

}
