package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // Endpoint to create a new category
    @PostMapping("/newcategory")
    public ResponseEntity createCategory (@RequestHeader("Authorization") String authorizationHeader, @RequestBody AddCategoryDTO addCategoryDTO)
    {
        return categoryService.createCategory(authorizationHeader,addCategoryDTO);
    }
    // Endpoint to retrieve all categories
    @GetMapping("/allcategories")
    public ResponseEntity getAllCategories ()
    {
        return categoryService.getAllCategories();
    }

}
