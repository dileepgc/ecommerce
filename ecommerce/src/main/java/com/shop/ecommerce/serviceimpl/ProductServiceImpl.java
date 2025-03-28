package com.shop.ecommerce.serviceimpl;

import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.GlobalMethodDTO;
import com.shop.ecommerce.DTO.ProductDTO;
import com.shop.ecommerce.Exception.GlobalException;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.helperclasses.GlobalMethod;
import com.shop.ecommerce.repo.CategoryRepo;
import com.shop.ecommerce.repo.ProductRepo;
import com.shop.ecommerce.service.JWTService;
import com.shop.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    JWTService jwtService;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    GlobalMethod globalMethod;


    // This method allows an admin to add a new product to a specified category.
// It checks if the category exists and is not deleted, then creates and saves the new product.
// The product is then added to the category's list of products.
    @Transactional
    public ResponseEntity newProduct(int id, AddProductDTO addProductDTO, String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String role = jwtService.extractRole(token);
            if (!role.equalsIgnoreCase("admin"))
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            Category category = categoryRepo.findById(id).orElse(null);
            if (category == null || category.isIs_deleted()) {
                throw new GlobalException("Category does not exist or deleted");
            }

            Product product = new Product();
            product.setStock(addProductDTO.getStock());
            product.setName(addProductDTO.getProd_name());
            product.setPrice(addProductDTO.getPrice());
            product.setImage(addProductDTO.getImageURL());
            product.setDescription(addProductDTO.getDescription());
            product.setCategory(category); // Set category for the product

            productRepo.save(product);

            // Add the new product to the category's list of products
            List<Product> productList = category.getProducts();
            if (productList == null) {
                productList = new ArrayList<>();
            }
            productList.add(product);
            category.setP(productList);

            System.out.println(product.getCategory().getCate_name());
            return new ResponseEntity<>("Product has been added successfully", HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // This method allows an admin to delete a product by marking it as deleted in the database.
// It checks if the product exists before marking it as deleted.
    @Override
    public ResponseEntity deleteProduct(int id, String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String role = jwtService.extractRole(token);
            if (!role.equalsIgnoreCase("admin"))
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            Product product = productRepo.findById(id).orElse(null);
            if (product == null) {
                throw new GlobalException("product does not exist");
            }
            product.setIs_deleted(true);
            productRepo.save(product);
            return new ResponseEntity("Product Deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("" + e.getMessage(), HttpStatus.OK);
        }
    }

    // This method retrieves all products in the database and returns them as a list of `AddProductDTO` objects.
    // This is useful for fetching all products for the frontend display.
    public ResponseEntity getAllProducts() {
        try
        {
            List<Product> products = productRepo.findProducts();
            List<ProductDTO> allProductDTOS=new ArrayList<ProductDTO>();
            for(Product product:products)
            {
                ProductDTO productDTO=new ProductDTO();
                productDTO.setId(product.getId());
                productDTO.setProd_name(product.getName());
                productDTO.setPrice(product.getPrice());
                productDTO.setDescription(product.getDescription());
                productDTO.setImageURL(product.getImage());
                productDTO.setStock(product.getStock());
                allProductDTOS.add(productDTO);
            }
            return new ResponseEntity<>(allProductDTOS,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }


    // This method allows an admin to update the price of an existing product.
// It checks if the product exists before updating its price in the database.
    public ResponseEntity updatePrice(int id, double price,  String authorizationHeader) {
        try
        {
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            if(!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            Product product = productRepo.findById(id).orElse(null);
            if (product == null) {
                throw new GlobalException("Product does not exist") ;
            }
            product.setPrice(price);
            productRepo.save(product);
            return new ResponseEntity<>("Price Updated successfully",HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    // This method allows an admin to add new stock to an existing product.
// It checks if the product exists before updating its stock quantity in the database.
    public ResponseEntity adNewStock(int newstock, int id, String authorizationHeader) {
        try{
            GlobalMethodDTO globalMethodDTO=globalMethod.adminAccess(authorizationHeader);
            if(!globalMethodDTO.isAccess())
                return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);

            Product product = productRepo.findById(id).orElse(null);
            if (product == null) {
                return new ResponseEntity<>("product does not exist",HttpStatus.OK);
            }
            product.setStock( newstock);
            productRepo.save(product);
            return new ResponseEntity<>("Stock updated successfully",HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(""+e.getMessage(),HttpStatus.OK);
        }
    }

    // This method retrieves detailed information about a specific product by its ID.
    // If the product does not exist, an error message is returned.
    public ResponseEntity getProdDetails(int id) {
        try
        {
            Product product=productRepo.findById(id).orElse(null);
            if(product==null)
            {
                throw new GlobalException("product not found");
            }
            Map productMap=new LinkedHashMap();
            productMap.put("id",product.getId());
            productMap.put("name",product.getName());
            productMap.put("category",product.getCategory().getCate_name());
            productMap.put("price",product.getPrice());
            productMap.put("stock",product.getStock());
            productMap.put("description",product.getDescription());
            productMap.put("imageURL",product.getImage());
            return new ResponseEntity(productMap,HttpStatus.OK);
        } catch (GlobalException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    // This method retrieves all products in a specific category based on the category ID.
    // It checks if the category exists and if products exist in that category before returning the list.
    public ResponseEntity getProdByCate(int cateId)
    {
        try
        {
            Category category=categoryRepo.findById(cateId).orElse(null);
            if(category==null)
            {
                throw new GlobalException("category does not exist");
            }
            List<Product> products=productRepo.findByCategoryId(cateId);
            if(products.isEmpty())
            {
                throw new GlobalException("There are no products in this category");
            }
            return new ResponseEntity(products,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
//   public ResponseEntity getProdByName(String name, String authorizationHeader)
//    {
//        try
//        {
//            Product product=productRepo.findByName();
//            if(product==null)
//            {
//                throw new GlobalException("product does not exist");
//            }
//
//
//            return new ResponseEntity(product,HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
//        }
//    }
}
