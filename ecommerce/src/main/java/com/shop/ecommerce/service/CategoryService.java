package com.shop.ecommerce.service;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import org.springframework.http.ResponseEntity;




public interface CategoryService {
    public ResponseEntity createCategory(String authorizationHeader, AddCategoryDTO addCategoryDTO);
    public ResponseEntity getAllCategories();
}
