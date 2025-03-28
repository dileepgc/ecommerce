package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AllCategoriesDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.CategoryRepo;
import com.shop.ecommerce.service.CategoryService;
import com.shop.ecommerce.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    JWTService jwtService;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    GlobalMethod globalMethod;


    // This method creates a new category by first verifying if the user has admin access.
// If the user has admin access, a new category is created and saved to the database.
    @Override
    public ResponseEntity createCategory(String authorizationHeader, AddCategoryDTO addCategoryDTO) {
        try{

            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            if(!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            Category category = new Category(addCategoryDTO.getCate_name());
            categoryRepo.save(category);
            return new ResponseEntity<>("Category has been created",HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    // This method retrieves all categories from the database and returns them as a list of AddCategoryDTO objects.
// Each category in the database is mapped to an AddCategoryDTO and returned as a response.
    public ResponseEntity getAllCategories() {
        try
        {


            List<Category> categories = categoryRepo.findCategories();
            List<AllCategoriesDTO> allCategoriesDTOList=new ArrayList<AllCategoriesDTO>();
            for (Category category:categories)
            {
                AllCategoriesDTO allCategoriesDTO=new AllCategoriesDTO();
                allCategoriesDTO.setName(category.getCate_name());
                allCategoriesDTO.setNumberOfProducts(category.getProducts().size());

                allCategoriesDTOList.add(allCategoriesDTO);

            }
            return new ResponseEntity<>(allCategoriesDTOList,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }
}
