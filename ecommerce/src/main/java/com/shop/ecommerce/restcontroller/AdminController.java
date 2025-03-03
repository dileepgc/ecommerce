package com.shop.ecommerce.restcontroller;

import com.shop.ecommerce.DTO.AddCategoryDTO;
import com.shop.ecommerce.DTO.AddProductDTO;
import com.shop.ecommerce.DTO.AdminLoginDTO;
import com.shop.ecommerce.entity.Category;
import com.shop.ecommerce.entity.Product;
import com.shop.ecommerce.serviceimpl.AdminServiceimpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminServiceimpl as;
//    @PostMapping("/signup")
//    public ResponseEntity<Admin> signUp(@RequestParam("name")String name,@RequestParam("email")String email,@RequestParam("password")String password)
//    {
//        Admin a=new Admin();
//        a.setName(name);
//        a.setEmail(email);
//        a.setPassword(password);
//
//        Admin a1=as.signUpAdmin(a);
//        return new ResponseEntity<Admin>(a1, HttpStatus.CREATED);
//    }

//    public ResponseEntity<Admin> signUp(@RequestBody Admin a)
//    {
//        return ResponseEntity.status(HttpStatus.CREATED).body(as.signUpAdmin(a));
//    }


        @PostMapping("/login")
        public ResponseEntity login (@RequestBody AdminLoginDTO a, HttpSession session)
        {
            boolean b = as.login(a, session);
            if (b) {
                return ResponseEntity.status(HttpStatus.OK).body("Login Succesfull");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or already login Credentials");
            }
        }

        @PostMapping("/logout")
        public ResponseEntity adminLogout (HttpSession session)
        {
            return new ResponseEntity(as.adminLogout(session), HttpStatus.OK);
        }
        @PostMapping("/top-up")
        public ResponseEntity topupWallet ( @RequestParam("id") int id,
        @RequestParam("amount") double amount, HttpSession session)
        {
            return new ResponseEntity(as.topupWallet(id, amount, session), HttpStatus.OK);
        }
        @PostMapping("/newcategory")
        public ResponseEntity createCategory (@RequestBody AddCategoryDTO addCategoryDTO, HttpSession session)
        {
            return new ResponseEntity(as.createCategory(addCategoryDTO, session), HttpStatus.CREATED);
        }
        @PostMapping("/removecategory/{id}")
        public ResponseEntity removeCategroy ( @PathVariable int id, HttpSession session)
        {
            return new ResponseEntity((as.removeCategory(id, session)), HttpStatus.OK);
        }

        //Adding new Product
        @PostMapping("/newproduct/{cate_id}")
        public ResponseEntity newProduct ( @PathVariable int cate_id,
        @RequestBody AddProductDTO addProductDTO, HttpSession session)
        {
            return new ResponseEntity(as.newProduct(cate_id, addProductDTO, session), HttpStatus.OK);
        }

        //delete product
        @PostMapping("/deleteproduct/{id}")
        public ResponseEntity deleteProduct ( @PathVariable int id, HttpSession session)
        {
            return new ResponseEntity(as.deleteProduct(id, session), HttpStatus.OK);
        }

        //fetching all categoties
        @GetMapping("/allcategories")
        public ResponseEntity getAllCategories ()
        {
            return new ResponseEntity(as.getAllCategories(), HttpStatus.OK);

        }
        @GetMapping("/allproducts")
        public ResponseEntity allProducts ()
        {
            return new ResponseEntity(as.getAllProducts(), HttpStatus.OK);
        }
        @PostMapping("/updateprice/{id}")
        public ResponseEntity updateProduct ( @PathVariable int id, @RequestBody Double price, HttpSession session)
        {

            return new ResponseEntity(as.updatePrice(id, price, session), HttpStatus.OK);
        }
        @PostMapping("/getwalletaudit/{id}")
        public ResponseEntity getWalletAudit ( @PathVariable int id, HttpSession session)
        {
            return new ResponseEntity(as.getWalletAudit(id, session), HttpStatus.OK);
        }
        @PostMapping("/addstock{id}")
        public ResponseEntity addStock ( @RequestBody int newstock, @PathVariable int id, HttpSession session)
        {
            return new ResponseEntity(as.adNewStock(newstock, id, session), HttpStatus.OK);
        }






}



