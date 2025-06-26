package com.shop.ecommerce.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.service.ProductService;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    // Endpoint to add a new product to a specific category
    @PostMapping("/newproduct/{cate_id}")
    public ResponseEntity newProduct (@PathVariable int cate_id,
                                      @RequestBody AddProductDTO addProductDTO, @RequestHeader("Authorization") String authorizationHeader)
    {
        return productService.newProduct(cate_id, addProductDTO, authorizationHeader);
    }

    // Endpoint to delete a product by its ID
    @DeleteMapping ("/delete_product/{id}")
    public ResponseEntity deleteProduct ( @PathVariable int id,@RequestHeader("Authorization") String authorizationHeader)
    {
        return productService.deleteProduct(id,authorizationHeader);
    }

    // Endpoint to fetch all products
    @GetMapping("/allproducts")
    public ResponseEntity allProducts ()
    {
        return productService.getAllProducts();
    }

    // Endpoint to update the price of a product by its ID
    @PostMapping("/updateprice/{id}")
    public ResponseEntity updateProduct ( @PathVariable int id, @RequestBody Double price,@RequestHeader("Authorization") String authorizationHeader)
    {

        return productService.updatePrice(id, price, authorizationHeader);
    }
    // Endpoint to add new stock to an existing product by its ID
    @PostMapping("/addstock/{id}")
    public ResponseEntity addStock ( @RequestBody int newstock, @PathVariable int id,@RequestHeader("Authorization") String authorizationHeader)
    {
        return productService.adNewStock(newstock, id, authorizationHeader);
    }

    // Endpoint to fetch the details of a specific product by its ID
    @GetMapping("/productdetails/{id}")
    public ResponseEntity getProdDetails(@PathVariable int id)
    {
        return productService.getProdDetails(id);
    }

    // Endpoint to fetch products by category ID
    @GetMapping("/prodbycategory/{cate_id}")
    public ResponseEntity prodByCategory(@PathVariable("cate_id") int cate_id)
    {
        return productService.getProdByCate(cate_id);
    }

   @GetMapping("/prodbyname/{name}")
   public ResponseEntity prodByCategory(@PathVariable String name, @RequestHeader("Authorization") String authorizationHeader)
   {
       return productService.getProdByName(name,authorizationHeader);
   }
}
